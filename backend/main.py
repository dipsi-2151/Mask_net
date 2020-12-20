from fastapi import FastAPI, File, UploadFile, Form
from Db_models.mongo_setup import global_init
from Db_models.models.masked import Masked
from Db_models.models.suspicious import Suspicious
from Db_models.models.user import UserModel
from tensorflow.keras.applications.mobilenet_v2 import preprocess_input
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.python.keras.backend import set_session
import tensorflow as tf
from tensorflow.python.keras.models import load_model
import numpy as np
import cv2
import os
import base64
import globals
import pickle
from Db_models.models.penalty import PenaltyModel
from face_recog_server.face_recog_service import FaceRecog

face_recog_obj = FaceRecog()

def _save(file):
    file_name = file.filename
    with open(file_name, 'wb') as f:
        f.write(file.file.read())
    return file_name


# load our serialized face detector model from disk
print("[INFO] loading face detector model...")
prototxtPath =  "deploy.prototxt"
weightsPath = "res10_300x300_ssd_iter_140000.caffemodel"
net = cv2.dnn.readNet(prototxtPath, weightsPath)

config = tf.ConfigProto()
config.gpu_options.allow_growth=True
sess = tf.Session(config=config)
graph = tf.get_default_graph()
set_session(sess)

# load the face mask detector model from disk
print("[INFO] loading face mask detector model...")
model = load_model("mask_detector.model")

app = FastAPI()
global_init()
for user in UserModel.objects:
    globals.add_to_embeddings(username=user.user_name, encoding=pickle.loads(user.encoding))


@app.post("/signup/")
def register(file: UploadFile = File(...), user_name: str = Form(...)):
    try:
        UserModel.objects.get(user_name=user_name)
        return False
    except UserModel.DoesNotExist:
        """If user_name not in db than error will be handled here """
        user_model_obj = UserModel()
        file_name = _save(file)
        face_encoding = face_recog_obj.get_embedding(face_image=file_name)
        if face_encoding == "No-Face":
            return False
        else:
            binary_encoding = pickle.dumps(face_encoding)
            user_model_obj.user_name = user_name
            user_model_obj.encoding = binary_encoding
            """saving data in db through model ob of user"""
            with open(file_name, 'rb') as fd:
                user_model_obj.image.put(fd)
            os.remove(file_name)
            user_model_obj.save()
            return True



@app.post("/predict/")
def predict(file: UploadFile = File(...), location: str = Form(...)):
    file_name = _save(file)
    # load the input image from disk, clone it, and grab the image spatial
    # dimensions
    image = cv2.imread(file_name)
    (h, w) = image.shape[:2]
    # construct a blob from the image
    blob = cv2.dnn.blobFromImage(image, 1.0, (300, 300),
                                 (104.0, 177.0, 123.0))
    # pass the blob through the network and obtain the face detections
    print("[INFO] computing face detections...")
    net.setInput(blob)
    detections = net.forward()

    # loop over the detections
    for i in range(0, detections.shape[2]):
        # extract the confidence (i.e., probability) associated with
        # the detection
        confidence = detections[0, 0, i, 2]

        # filter out weak detections by ensuring the confidence is
        # greater than the minimum confidence
        if confidence > 0.5:
            # compute the (x, y)-coordinates of the bounding box for
            # the object
            box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
            (startX, startY, endX, endY) = box.astype("int")

            # ensure the bounding boxes fall within the dimensions of
            # the frame
            (startX, startY) = (max(0, startX), max(0, startY))
            (endX, endY) = (min(w - 1, endX), min(h - 1, endY))

            # extract the face ROI, convert it from BGR to RGB channel
            # ordering, resize it to 224x224, and preprocess it
            face = image[startY:endY, startX:endX]
            face = cv2.cvtColor(face, cv2.COLOR_BGR2RGB)
            face = cv2.resize(face, (224, 224))
            face = img_to_array(face)
            face = preprocess_input(face)
            face = np.expand_dims(face, axis=0)

            # pass the face through the model to determine if the face
            # has a mask or not
            global sess
            global graph
            with graph.as_default():
                set_session(sess)
                (mask, withoutMask) = model.predict(face)[0]

            # determine the class label and color we'll use to draw
            # the bounding box and text
            label = "Mask" if mask > withoutMask else "No Mask"
            color = (0, 255, 0) if label == "Mask" else (0, 0, 255)
            result = label
            # include the probability in the label
            label = "{}: {:.2f}%".format(label, max(mask, withoutMask) * 100)


            cv2.putText(image, label, (startX, startY - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 2.0, color, 2)
            cv2.rectangle(image, (startX, startY), (endX, endY), color, 2)
            break
        else:
            result = "suspicious"
            break

    if result == "Mask":
        cv2.imwrite(file_name, image)
        mask_model_obj = Masked()
        with open(file_name, 'rb') as fd:
            mask_model_obj.image= fd.read()
        os.remove(file_name)
        mask_model_obj.location = location
        mask_model_obj.save()
        return result


    elif result == "No Mask": 
        uname = face_recog_obj.face_recognition(file_name)
        if uname is None:
            """if unknown person detected than return none"""
            os.remove(file_name)
        elif uname is False:
            """if unknown person detected than return none"""
            os.remove(file_name)
        else:
            print("in else")
            print("*************")
            print(uname)
            print("#############")
            cv2.imwrite(file_name, image)
            penalty_model_obj = PenaltyModel()
            penalty_model_obj.user_name = uname
            penalty_model_obj.location = location
            with open(file_name, 'rb') as fd:
                penalty_model_obj.image = fd.read()
            penalty_model_obj.save()
            os.remove(file_name)

        return result
        
        
    else:
        suspicious_model_obj = Suspicious()
        with open(file_name, 'rb') as fd:
            suspicious_model_obj.image = fd.read()
        os.remove(file_name)
        suspicious_model_obj.location = location
        suspicious_model_obj.save()
        return result


@app.get("/masked")
def fetch_masked(skip: int = 0):
    """pagination query param"""
    skip = skip * 10
    limit = skip + 10
    masked = []
    for mask_model_obj in Masked.objects[skip:limit].order_by('-date'):
        """limiting results to fetch from db """
        mask_dict = dict()
        mask_dict["date"] = mask_model_obj.date
        mask_dict["location"] = mask_model_obj.location
        mask_dict["img"] = base64.b64encode(mask_model_obj.image)
        masked.append(mask_dict)
    return masked


@app.get("/penalty")
def fetch_penalty(skip: int = 0):
    """pagination query param"""
    skip = skip * 10
    limit = skip + 10
    penalty_list = []
    for penalty_model_obj1 in PenaltyModel.objects[skip:limit].order_by('-date'):      
        """limiting results to fetch from db """
        penalty_dict = dict()
        penalty_dict["user"] = penalty_model_obj1.user_name
        penalty_dict["date"] = penalty_model_obj1.date
        penalty_dict["location"] = penalty_model_obj1.location
        penalty_dict["img"] = base64.b64encode(penalty_model_obj1.image)
        penalty_list.append(penalty_dict)
    return penalty_list


@app.post("/signin")
def signin(file: UploadFile = File(...)):
    file_name = _save(file)
    uname =face_recog_obj.face_recognition(file_name)
    if uname is None:
        """if unknown person detected than return none"""
        os.remove(file_name)
        return False
    else:
        return uname


@app.get("/penalty_user")
def penalty_user(user_name: str = 0):
    penalties = PenaltyModel.objects(user_name=user_name)
    penalty_list = []
    for penalty in penalties:
        penalty_dict = dict()
        penalty_dict["id"] = str(penalty.id)
        penalty_dict["date"] = penalty.date
        penalty_dict["location"] = penalty.location
        penalty_dict["img"] = base64.b64encode(penalty.image)
        penalty_list.append(penalty_dict)
    return penalty_list


@app.get("/suspicious")
def fetch(skip: int = 0):
    """pagination query param"""
    skip = skip * 10
    limit = skip + 10
    suspicious = []
    for suspicious_model_obj in Suspicious.objects[skip:limit].order_by('-date'):
        """limiting results to fetch from db """
        suspicious_dict = dict()
        suspicious_dict["date"] = suspicious_model_obj.date
        suspicious_dict["location"] = suspicious_model_obj.location
        suspicious_dict["img"] = base64.b64encode(suspicious_model_obj.image)
        suspicious.append(suspicious_dict)
    return suspicious


@app.post("/remove_penalty")
def fetch(id: str):
    try:
        penalty_obj= PenaltyModel.objects.get(id=id)
        penalty_obj.delete()
        return True
    except PenaltyModel.DoesNotExist:
        return False