#!/usr/bin/env bash
export AWS_SHARED_CREDENTIALS_FILE=${AWS_SHARED_CREDENTIALS_FILE:-/tmp/.aws}
export AWS_PROFILE=dev

# test 
aws --profile dev sts get-caller-identity

pushd lambda
./build.sh
popd

rm -rf ~/.terraform
terraform init -upgrade
terraform apply -auto-approve
