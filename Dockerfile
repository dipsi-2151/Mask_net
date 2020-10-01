FROM tensorflow/tensorflow
RUN apt-get update

RUN apt-get install -y build-essential cmake


RUN apt-get install -y libopenblas-dev liblapack-dev


RUN apt-get install -y libx11-dev libgtk-3-dev


ADD . .

RUN pip install -r requirements.txt


CMD uvicorn main:app --reload