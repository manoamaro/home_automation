FROM node:slim

RUN mkdir app
WORKDIR app

ADD package.json /app/
RUN npm install

ADD . /app/

EXPOSE 1883

CMD node app.js
