FROM tensorflow/tensorflow
RUN apt-get update

RUN apt-get install -y build-essential cmake


RUN apt-get install -y libopenblas-dev liblapack-dev


RUN apt-get install -y libx11-dev libgtk-3-dev

RUN pip install dlib

ADD requirements.txt .

RUN pip install -r requirements.txt

ADD . .

CMD uvicorn main:app --reload
