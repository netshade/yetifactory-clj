#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
export TASK_ARN="$(curl $(ip neighbor | head -n 1 | awk '{print $1}'):51678/v1/tasks?dockerid=$(head -n 1 /proc/self/cgroup | awk -F':' '{print $3}' | sed 's/\/docker\///') | grep -oE 'arn:[^\"]+')"
mkdir -p /etc/codedeploy-agent/conf
cat <<EOF > /etc/codedeploy-agent/conf
---
aws_access_key_id: 
aws_secret_access_key: 
iam_user_arn: ${TASK_ARN}
region: us-east-1
EOF
/etc/init.d/codedeploy-agent restart
while [ ! -f /tmp/STOP ]
do
  sleep 30
done
exit 0
