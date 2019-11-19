#!/usr/bin/env bash
set -ex
export PKGDIR="python"
rm -rf ${PKGDIR} && mkdir -p ${PKGDIR}

#docker run --rm -v $(pwd):/foo -w /foo lambci/lambda:build-python3.7 \
virtualenv .venv -p /usr/local/bin/python3
source .venv/bin/activate
pip3 install -r requirements.txt --no-deps -t ${PKGDIR}

deactivate .venv
rm -rf .venv
