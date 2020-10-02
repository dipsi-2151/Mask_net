FROM tensorflow/tensorflow
RUN apt-get update

RUN apt-get install -y build-essential cmake


RUN apt-get install -y libopenblas-dev liblapack-dev


RUN apt-get install -y libx11-dev libgtk-3-dev

RUN pip install dlib

ADD requirements.txt .

RUN pip install -r requirements.txt

RUN DEBIAN_FRONTEND="noninteractive" apt-get -y install tzdata

RUN pip install opencv-python

ADD . .
EXPOSE 8000
CMD uvicorn main:app --reload --host 0.0.0.0