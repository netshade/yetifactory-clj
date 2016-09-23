#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
export AWS_HOST_IDENTIFIER="$(curl $(ip neighbor | head -n 1 | awk '{print $1}'):51678/v1/tasks?dockerid=$(head -n 1 /proc/self/cgroup | awk -F':' '{print $3}' | sed 's/\/docker\///') | grep -oE 'arn:[^\"]+')"
/etc/init.d/codedeploy-agent restart
while [ ! -f /tmp/STOP ]
do
  sleep 30
done
exit 0
