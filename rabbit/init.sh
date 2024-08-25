#!/bin/bash
rabbitmq-server &
sleep 10
envsubst < /etc/rabbitmq/definitions.template.json > /etc/rabbitmq/definitions.json
rabbitmqadmin import /etc/rabbitmq/definitions.json --username="${RABBITMQ_DEFAULT_USER}" --password="${RABBITMQ_DEFAULT_PASS}"
wait