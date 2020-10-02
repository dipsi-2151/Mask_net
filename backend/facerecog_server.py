from fastapi import FastAPI, File, UploadFile, Form
from Db_models.mongo_setup import global_init
from Db_models.models.masked import Masked
from Db_models.models.suspicious import Suspicious
from Db_models.models.user import UserModel
from Db_models.models.penalty import PenaltyModel
import numpy as np
import cv2
import os
import base64
import globals
import face_recognition
import pickle



def _get_embedding(face_image):
    face_image = face_recognition.load_image_file(face_image)
    face_locations = face_recognition.face_locations(face_image, model='cnn')
    face_encoding = face_recognition.face_encodings(face_image, face_locations, model="large")[0]
    return face_encoding


def _face_recognition(face_image):
    embeddings = []
    for dic in globals.embeddings:
        print(dic["name"])
        embeddings.append(dic["encoding"])
    uname = None
    """if face detection occurs than try else except"""
    try:
        face_encoding = _get_embedding(face_image)
        face_distances = face_recognition.face_distance(embeddings, face_encoding)
        for i, face_distance in enumerate(face_distances):
            if face_distance < 0.6:
                user_dic = globals.embeddings[i]
                uname = user_dic["name"]
                return uname
    except IndexError:
            return uname
        
def _save(file):
    file_name = file.filename
    with open(file_name, 'wb') as f:
        f.write(file.file.read())
    return file_name




app = FastAPI()
global_init()
for user in UserModel.objects:
    globals.add_to_embeddings(username=user.user_name, encoding=pickle.loads(user.encoding))

@app.post("/register/")
def register(file: UploadFile = File(...), user_name: str = Form(...)):
    try:
        returning_user_object= UserModel.objects.get(user_name=user_name)
        return False
    except UserModel.DoesNotExist:
        """If user_name not in db than error will be handled here """
        user_model_obj = UserModel()
        file_name = _save(file)
        try:
            face_encoding = _get_embedding(file_name)
            binary_encoding = pickle.dumps(face_encoding)
            user_model_obj.user_name = user_name
            user_model_obj.encoding = binary_encoding
            """saving data in db through model ob of user"""
            with open(file_name, 'rb') as fd:
                user_model_obj.image.put(fd)
            os.remove(file_name)
            user_model_obj.save()
            return True
        except IndexError:
            return False



@app.post("/recognize")
def signin(file: UploadFile = File(...)):
    file_name = _save(file)
    uname = _face_recognition(file_name)
    if uname is None:
        """if unknown person detected than return none"""
        os.remove(file_name)
        return False
    else:
        return uname


