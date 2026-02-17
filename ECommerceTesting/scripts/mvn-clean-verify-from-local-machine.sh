#!/bin/bash

### See below URL for details on the set commands
# https://jasonfleetwoodboldt.com/courses/shell-scripting/shell-scripting-set-euo-pipefail-failsafe/
set -eu
set -o pipefail

SCRIPT_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "JAVA_OPTS: [ ${JAVA_OPTS:-} ]"s
echo "Java version: $(java -version)"
echo "maven version: $(mvn --version)"

mvn dependency:tree

ENV="${ENV:-st1}"
VERSION="${VERSION:-local}"
LOCALE="${LOCALE:-uk}"
echo "---------------------"
echo "ENV=${ENV}"
echo "VERSION=${VERSION}"
echo "LOCALE=${LOCALE}"

WEBDRIVER_RELATED_FLAGS="-DuseWebDriverManager=true"
if [[ -z "${WEBDRIVER_CHROME_DRIVER_LOCATION:-}" || ! -e "${WEBDRIVER_CHROME_DRIVER_LOCATION:-}" ]]; then
  echo ""
  echo "The environment variable 'WEBDRIVER_CHROME_DRIVER_LOCATION' hasn't been defined or not set to the correct location, only set it when not using the '-DuseWebDriverManager=true' parameter."
  echo "Please download the appropriate version of the ChromeDriver for your browser from https://chromedriver.chromium.org/downloads, place it at some suitable location and set the 'WEBDRIVER_CHROME_DRIVER_LOCATION' environment variable to point to that location as full path to the driver file."
  echo ""
  echo "A recommend placed to place the chromedriver.exe (for Windows) would be $HOME/.cache/selenium/chromedriver/win64/[version number]/"
  echo ""
  echo "...switching to using the -DuseWebDriverManager=true flag and not looking for a downloaded Chrome driver."
  echo ""
else
  echo "WEBDRIVER_CHROME_DRIVER_LOCATION=${WEBDRIVER_CHROME_DRIVER_LOCATION}"
  WEBDRIVER_RELATED_FLAGS="-Dwebdriver.chrome.driver=${WEBDRIVER_CHROME_DRIVER_LOCATION}"
fi
echo "---------------------"; echo "";

SKIP_CACHE_RELOAD="${SKIP_CACHE_RELOAD:-false}"
echo "SKIP_CACHE_RELOAD: ${SKIP_CACHE_RELOAD}"; echo "";
if [[ "${SKIP_CACHE_RELOAD}" == "false" ]]; then
  set -x
  LOCALE="${LOCALE}" ENV="${ENV}" VERSION="${VERSION}" \
    bash "${SCRIPT_FOLDER}"/evict-and-reload-cache.sh
  set +x
  echo "---------------------"; echo "";
fi

set -x
time mvn -f pom.xml clean verify -Dcountry=${LOCALE} -Denv="${ENV}" -Dheadless=true   \
         -Dversion="${VERSION}" -Dlocal=true "${WEBDRIVER_RELATED_FLAGS}"             \
         -Dngsp=true -Dendeca=true -DuseNexus=true -Dtestrail.enabled=false           \
         -Dtestrail.rerunSuccessful=true -Dskip.cucumber.parallel.tests.report=false  \
         -DparallelTags="All_Acceptance_Tests" -Dtest=skipMavenTestPhase -DfailIfNoTests=false
set +x