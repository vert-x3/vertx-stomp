#!/usr/bin/env bash

docker run -d --name some-rabbitmq -p 15672:15672  -p 61643:61613 vertx-stomp/rabbitmq-stomp