#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
/etc/init.d/codedeploy-agent restart
while [ ! -f /tmp/STOP ]
do
  sleep 30
done
exit 0
