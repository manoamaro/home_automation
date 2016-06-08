FROM node:slim

RUN mkdir app
WORKDIR app

ADD . /app/

RUN npm install

EXPOSE 1883

CMD node app.js
