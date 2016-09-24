#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
cd /app
nohup lein with-profile production trampoline run -m yetifactory.core > /dev/null 2> /dev/null < /dev/null &
echo "${!}" > application.pid
exit 0
