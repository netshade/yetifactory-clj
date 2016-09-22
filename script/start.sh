#!/bin/bash
nohup lein with-profile production trampoline run -m yetifactory.core 2>&1 >/var/log/application.log &
echo "${!}" > application.pid
while kill -0 $(<application.pid)
do
  sleep 30
done
exit 0

