#!/usr/bin/env bash

export PKGDIR="python"
rm -rf ${PKGDIR} && mkdir -p ${PKGDIR}

docker run --rm -v $(pwd):/foo -w /foo lambci/lambda:build-python3.7 \
    pip3 install -r requirements.txt --no-deps -t ${PKGDIR}

