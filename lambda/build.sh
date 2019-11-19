#!/usr/bin/env bash

export WORKDIR=$(pwd)
export LAYERSDIR="layer-requests"

# init packages directory
mkdir -p packages/

# build python-requests layer
cd ${WORKDIR}/${LAYERSDIR}/
${WORKDIR}/${LAYERSDIR}/build_layer.sh

export PKGDIR="python"
rm -rf ${PKGDIR} && mkdir -p ${PKGDIR}
pip3 install -r requirements.txt --no-deps -t ${PKGDIR}

zip -r ${WORKDIR}/packages/python3-requests.zip .
rm -rf ${WORKDIR}/${LAYERSDIR}/python/
