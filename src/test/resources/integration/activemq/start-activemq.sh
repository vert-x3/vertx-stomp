#!/usr/bin/env bash
docker run -d --name some-activemq -p 61622:61612  -p 61623:61613 -p 61626:61616 -p 8161:8161 vertx-stomp/activemq-stomp