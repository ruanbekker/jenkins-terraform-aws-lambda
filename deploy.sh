#!/usr/bin/env bash

pushd lambda
./build.sh
popd

terraform init
terraform apply -auto-approve
