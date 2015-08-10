#!/usr/bin/env bash

docker build -t vertx-stomp/activemq-stomp - < Dockerfile-ActiveMQ
docker build -t vertx-stomp/rabbitmq-stomp - < Dockerfile-RabbitMQ


