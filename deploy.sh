#!/usr/bin/env bash

pushd lambda
./build.sh
popd

terraform init -upgrade
terraform apply -auto-approve
