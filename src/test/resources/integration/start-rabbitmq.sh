#!/usr/bin/env bash

docker run -d --name some-rabbitmq -p 61612:61612  -p 61613:61613 -p 61616:61616 -p 8161:8161 vertx-stomp/rabbitmq-stomp