#!/bin/bash
nohup lein run 2>&1 >/var/log/application.log &
echo "${!}" > application.pid
exit 0
