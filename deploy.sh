#!/usr/bin/env bash
#export AWS_SHARED_CREDENTIALS_FILE=${AWS_SHARED_CREDENTIALS_FILE:-/tmp/.aws}
#unset AWS_PROFILE
#unset AWS_ACCESS_KEY_ID
#unset AWS_SECRET_ACCESS_KEY
#unset AWS_ROLE_ARN
#unset AWS_ROLE_SESSION_NAME
export AWS_PROFILE=${AWS_PROFILE:-dev}
#export AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY}
#export AWS_SECRET_ACCESS_KEY=${AWS_SECRET_KEY}
#export AWS_ROLE_ARN=${AWS_CROSS_ACCOUNT_ROLE_ARN}
#export AWS_ROLE_SESSION_NAME=dev
export AWS_SHARED_CREDENTIALS_FILE=/tmp/.aws

# test 
aws --profile dev sts get-caller-identity
aws --profile dev s3 ls /
pushd lambda
./build.sh
popd

aws --profile dev s3 ls /
rm -rf ~/.terraform
AWS_PROFILE=dev TF_LOG=DEBUG terraform init -upgrade
terraform apply -auto-approve
