FROM fiscoorg/wedpr-image:v3.0.0 as  wedpr-jupyter-image

ENV LANG zh_CN.UTF-8
ENV LANGUAGE zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8

RUN mkdir -p /home/share/.jupyter/

COPY depends/jupyter_lab_config.py /home/share/.jupyter/
COPY depends/requirements.txt /root/requirements.txt

RUN chmod -R 777  /home/share/

# install jupyter
RUN pip install --no-cache-dir -i https://pypi.mirrors.ustc.edu.cn/simple/ -r root/requirements.txt

RUN pip install --no-cache-dir -i https://mirrors.aliyun.com/pypi/simple wedpr-authenticator
RUN pip install --no-cache-dir -i https://mirrors.aliyun.com/pypi/simple wedpr-ml-toolkit
