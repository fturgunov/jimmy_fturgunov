#!/bin/sh
../setup-env.sh

docker-compose \
-f jimmy_ft_postgres.dev.yml \
down "${@}"
