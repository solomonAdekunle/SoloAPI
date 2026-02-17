#!/bin/bash

set -e
set -u
set -o pipefail

source utils.sh

printHWInfo() {
    echo ""
    echo -e "${BOLD}****************************${NORMAL}"
    echo -e "${BOLD}Display hardware information${NORMAL}"
    echo -e "${BOLD}****************************${NORMAL}"
    getHWInfo

    echo ""
    echo -e "${BOLD}Memory information${NORMAL}"
    getMemoryInfo

    echo ""
    echo -e "${BOLD}Disk Space information${NORMAL}"
    getDiskSpaceInfo

    echo ""
    echo -e "${BOLD}***********************************************************************${NORMAL}"
    echo -e "${BOLD}Available threads (from all online CPUs/Cores): $(getAvailableThreads) ${NORMAL}"
    echo -e "${BOLD}***********************************************************************${NORMAL}"
}

printRuntimeEnvInfo() {
    dockerContainer=""
    if [[ -f "/.dockerenv" ]]; then
        dockerContainer="inside a docker container "
    fi

    if [[ -z "$(getVMInfo)" ]]; then
        machine="bare-metal (native)"
    else
        machine="VM or VM-like"
    fi

    echo ""
    echo -e "${BOLD}********************************************************************************${NORMAL}"
    echo -e "${BOLD}* Processes are running ${dockerContainer}on a ${machine} environment ${NORMAL}"
    echo -e "${BOLD}********************************************************************************${NORMAL}"

    getProcessesInfo
}

printOSInfo() {
    echo ""
    echo -e "${BOLD}**********************${NORMAL}"
    echo -e "${BOLD}Display OS information${NORMAL}"
    echo -e "${BOLD}**********************${NORMAL}"
    uname -a

    echo ""
    getOSInfo
}

printHWInfo
printOSInfo
printRuntimeEnvInfo

echo -e "${BOLD}****************************************************************${NORMAL}"