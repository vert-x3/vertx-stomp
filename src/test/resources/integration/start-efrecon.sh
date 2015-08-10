#!/usr/bin/env bash

docker run -d --name some-efrecon -p 61613:61613 efrecon/stomp -verbose 5
docker logs -f some-efrecon