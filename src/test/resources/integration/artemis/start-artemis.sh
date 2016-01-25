#!/usr/bin/env bash
docker run -d  --name some-artemis \
 -e 'ACTIVEMQ_MIN_MEMORY=1512M' -e 'ACTIVEMQ_MAX_MEMORY=3048M' \
 -p 61653:61613 -p 61656:61616 -p 5445:5445 -p 8162:8161 \
 vertx-stomp/artemis-stomp