#!/usr/bin/env bash
export WORKDIR=$(pwd)
export LAYERSDIR="layer-requests"

# init packages directory
mkdir -p packages/

# build python-requests layer
cd ${WORKDIR}/${LAYERSDIR}/
${WORKDIR}/${LAYERSDIR}/build_layer.sh
zip -r ${WORKDIR}/packages/python3-requests.zip .
rm -rf ${WORKDIR}/${LAYERSDIR}/python/

