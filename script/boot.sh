#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
sleep 10
export TASK_ARN="$(curl $(ip neighbor | head -n 1 | awk '{print $1}'):51678/v1/tasks?dockerid=$(head -n 1 /proc/self/cgroup | awk -F':' '{print $3}' | sed 's/\/docker\///') | grep -oE 'arn:[^\"]+')"
mkdir -p /etc/codedeploy-agent/conf
cat <<EOF >/etc/codedeploy-agent/conf/codedeploy.onpremises.yml
---
aws_access_key_id: ${AWS_ACCESS_KEY_ID}
aws_secret_access_key: ${AWS_SECRET_ACCESS_KEY}
iam_user_arn: ${TASK_ARN}
region: ${AWS_REGION}
EOF
/etc/init.d/codedeploy-agent restart
while [ ! -f /tmp/STOP ]
do
  sleep 30
done
exit 0
