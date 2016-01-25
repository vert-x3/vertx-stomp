#!/usr/bin/env bash

docker run -d --name some-efrecon -p 61633:61613 vertx-stomp/efrecon-stomp -verbose 5
docker logs -f some-efrecon