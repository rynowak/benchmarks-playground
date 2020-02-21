FROM python:3.6.6-stretch

ADD ./ /app

WORKDIR /app

RUN pip3 install -r requirements.txt

CMD gunicorn app:run -c gunicorn_conf.py