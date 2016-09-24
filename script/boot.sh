#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
DEPLOY_GROUP="deployers"
DEPLOY_TAG="role"
DEPLOY_TAG_VALUE="web"
IAM_USER_NAME="$(hostname)"
INSTANCE_NAME="docker-${IAM_USER_NAME}"
aws iam create-user --user-name ${IAM_USER_NAME}
aws iam create-access-key --user-name ${IAM_USER_NAME} > /tmp/CREDENTIALS
ACCESS_KEY_ID=$(grep AccessKeyId /tmp/CREDENTIALS | awk -F': ' '{print $2}' | sed 's/[\", ]//g')
SECRET_ACCESS_KEY=$(grep SecretAccessKey /tmp/CREDENTIALS | awk -F': ' '{print $2}' | sed 's/[\", ]//g')
ARN=$(aws iam get-user --user-name ${IAM_USER_NAME} --query "User.Arn" --output text)
export AWS_HOST_IDENTIFIER="${ARN}"
aws iam add-user-to-group --user-name ${IAM_USER_NAME} --group-name ${DEPLOY_GROUP}
aws deploy register-on-premises-instance --instance-name ${INSTANCE_NAME} --iam-user-arn ${ARN} --region ${AWS_REGION}
aws deploy add-tags-to-on-premises-instances --instance-names ${INSTANCE_NAME} --tags Key=${DEPLOY_TAG},Value=${DEPLOY_TAG_VALUE} --region ${AWS_REGION}
cat <<EOF >/etc/rc6.d/K99remove_iam_user
aws deploy deregister-on-premises-instance --instance-name ${INSTANCE_NAME} --region ${AWS_REGION}
aws iam remove-user-from-group --user-name ${IAM_USER_NAME} --group-name ${DEPLOY_GROUP}
aws iam delete-access-key --user-name ${IAM_USER_NAME} --access-key-key-id ${ACCESS_KEY_ID}
aws iam delete-user --user-name ${IAM_USER_NAME}
EOF
chmod +x /etc/rc6.d/K99remove_iam_user
mkdir -p /etc/codedeploy-agent/conf
cat <<EOF >/etc/codedeploy-agent/conf/codedeploy.onpremises.yml
---
aws_access_key_id: "${ACCESS_KEY_ID}"
aws_secret_access_key: "${SECRET_ACCESS_KEY}"
iam_user_arn: "${ARN}"
region: "${AWS_REGION}"
EOF
/etc/init.d/codedeploy-agent restart
while [ ! -f /tmp/STOP ]
do
  sleep 30
done
exit 0
