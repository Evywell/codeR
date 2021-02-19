#!/usr/bin/env bash

chmod 777 -R /mnt/log

# run original entrypoint
echo "Run original entrypoint"
docker-entrypoint.sh mysqld
