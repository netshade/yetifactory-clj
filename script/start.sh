#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
nohup lein with-profile production trampoline run -m yetifactory.core 2>&1 >/var/log/application.log &
echo "${!}" > application.pid
exit 0

