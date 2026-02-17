#!/bin/bash

### See below URL for details on the set commands
# https://jasonfleetwoodboldt.com/courses/shell-scripting/shell-scripting-set-euo-pipefail-failsafe/
set -eu
set -o pipefail

ENV="${ENV:-st1}"
LOCALE="${LOCALE:-uk}"
VERSION="${VERSION:-local}"
CUSTOMER_FILTER="${CUSTOMER_FILTER:-12345}"
echo "---------------------"
echo "ENV=${ENV}"
echo "VERSION=${VERSION}"
echo "LOCALE=${LOCALE}"

if [[ "${VERSION}" == "local" ]]; then
  TARGET_URI="http://localhost:8080"
else
  ENV_IN_URI="${ENV}"
  if [[ "${ENV}" == "st1" ]]; then
    ENV_IN_URI="preprod"
  fi
  TARGET_URI="https://searchapi-cloud-${ENV_IN_URI}.services.nonprod.rscomp.systems"
fi

echo "TARGET_URI=${TARGET_URI}"
echo ""
echo "Clearing cache for '${LOCALE}' locale"
time curl "${TARGET_URI}/v1/evictCategory?localeId=${LOCALE}"
echo ""

echo ""
echo "Populating the cache for '${LOCALE}' locale (ROOT category)"
time curl "${TARGET_URI}/v1/category/ROOT?localeId=${LOCALE}&clientId=test&subLevels=3&channelId=responsive"
echo ""

echo ""
echo "Populating the cache for '${LOCALE}' locale with Customer Filter '${CUSTOMER_FILTER}' (ROOT category)"
time curl "${TARGET_URI}/v1/category/ROOT?localeId=${LOCALE}&clientId=test&subLevels=3&channelId=responsive&customerFilter=${CUSTOMER_FILTER}"
echo ""