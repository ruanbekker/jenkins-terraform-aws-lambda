#!/usr/bin/env bash
export AWS_SHARED_CREDENTIALS_FILE=${AWS_SHARED_CREDENTIALS_FILE:-/tmp/.aws}
export AWS_PROFILE=dev

# test 
aws --profile dev sts get-caller-identity

pushd lambda
./build.sh
popd

rm -rf ~/.terraform
AWS_SHARED_CREDENTIALS_FILE=/tmp/.aws AWS_PROFILE=dev TF_LOG=DEBUG terraform init
terraform apply -auto-approve
