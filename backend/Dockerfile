ARG UBUNTU_VERSION=18.04
ARG ARCH=
ARG CUDA=10.0
ARG ARCH
ARG CUDA
ARG CUDNN=7.6.2.24-1
ARG CUDNN_MAJOR_VERSION=7
ARG LIB_DIR_PREFIX=x86_64
# Install face recognition dependencies
FROM nvidia/cuda:10.0-cudnn7-devel
SHELL ["/bin/bash", "-c"]
# Install face recognition dependencies
RUN apt update -y; apt install -y git cmake libsm6 libxext6 libxrender-dev
# Install compilers
RUN apt install -y software-properties-common
RUN apt-get install -y  libopenblas-dev liblapack-dev
# Configure the build for our CUDA configuration.
ENV CI_BUILD_PYTHON python
ENV LD_LIBRARY_PATH /usr/local/cuda/extras/CUPTI/lib64:/usr/local/cuda/lib64:$LD_LIBRARY_PATH
ENV TF_NEED_CUDA 1
ENV TF_NEED_TENSORRT 1
ENV TF_CUDA_COMPUTE_CAPABILITIES=3.5,5.2,6.0,6.1,7.0
ENV TF_CUDA_VERSION=10
ENV TF_CUDNN_VERSION=7
# CACHE_STOP is used to rerun future commands, otherwise cloning tensorflow will be cached and will not pull the most recent version
ARG CACHE_STOP=1
# Check out TensorFlow source code if --build-arg CHECKOUT_TF_SRC=1
ARG CHECKOUT_TF_SRC=0
RUN ln -s /usr/local/cuda/lib64/stubs/libcuda.so /usr/local/cuda/lib64/stubs/libcuda.so.1 \
    && echo "/usr/local/cuda/lib64/stubs" > /etc/ld.so.conf.d/z-cuda-stubs.conf \
    && ldconfig

ARG USE_PYTHON_3_NOT_2
ARG _PY_SUFFIX=${USE_PYTHON_3_NOT_2:+3}
ARG PYTHON=python3
ARG PIP=pip3

# See http://bugs.python.org/issue19846
ENV LANG C.UTF-8

RUN apt-get install -y python3 python3-pip

RUN pip3 --no-cache-dir install --upgrade \
    pip \
    setuptools

# Some TF tools expect a "python" binary
RUN ln -s $(which python3) /usr/local/bin/python 

RUN apt-get install -y \
    build-essential \
    curl \
    git \
    wget \
    python3-dev \
    virtualenv \
    swig

#Install dlib 
RUN git clone https://github.com/jainal09/dlib.git /dlib
RUN mkdir -p dlib/build

RUN cmake -H/dlib -B/dlib/build -DDLIB_USE_CUDA=1 -DUSE_AVX_INSTRUCTIONS=1
RUN cmake --build /dlib/build

RUN cd /dlib; python3 /dlib/setup.py install
RUN pip3 --no-cache-dir install \
    Pillow \
    h5py \
    keras_applications \
    keras_preprocessing \
    matplotlib \
    mock \
    numpy \
    scipy \
    sklearn \
    pandas \
    future \
    portpicker \
    fastapi \
    python-multipart \
    mongoengine \
    python-dotenv \
    face_recognition \
    uvicorn \
    blinker \
    opencv-python\
    scikit-build\
    tensorflow-gpu==1.15.0\
    && test "${USE_PYTHON_3_NOT_2}" -eq 1 && true || ${PIP} --no-cache-dir install \
    enum34
RUN apt-get install -y libx11-dev libgtk-3-dev
RUN DEBIAN_FRONTEND="noninteractive" apt-get -y install tzdata
ADD . .
EXPOSE 8000