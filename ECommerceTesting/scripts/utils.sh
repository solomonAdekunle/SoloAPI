#!/bin/bash

set -e
set -u
set -o pipefail

BOLD="\e[1m"
NORMAL="\e[0m"

getAvailableThreads() {
    if [[ -f "/.dockerenv" ]]; then
       result=$(cat /sys/fs/cgroup/cpuset/cpuset.cpus | awk -F'-' '{print $2}')
       result=$((result + 1))
    else
        if [[ "$(uname)" = "Darwin" ]]; then
            result=$(sysctl -n hw.ncpu || true)
        else
            result=$(nproc --all || true)
        fi
    fi
    echo ${result:-4}
}

getAllowedThreads() {
    availableThreads=$(getAvailableThreads)
    thresholdLimit="$(awk "BEGIN {print (${availableThreads} * 1/2)}")"
    if [[ -z "${availableThreads}" ]] || [[  "${availableThreads}" -ge "${thresholdLimit}" ]]; then
        availableThreads="${thresholdLimit}"
    fi
    echo ${availableThreads}
}

getHWInfo() {
    if [[ "$(uname)" = "Darwin" ]]; then
       system_profiler SPHardwareDataType || true
    else
       lscpu || true
    fi
}

getVMInfo() {
    if [[ "$(uname)" = "Darwin" ]]; then
        result=$(ioreg -l | grep -e Manufacturer -e 'Vendor Name')
    else
        result=$(cat /proc/cpuinfo | grep hypervisor || true)
    fi

    echo ${result}
}

getMemoryInfo() {
    if [[ "$(uname)" = "Darwin" ]]; then
        top -l 1 -s 0 | grep PhysMem
        sysctl vm.swapusage
    else
        free -m
        free -m -h
    fi
}

getDiskSpaceInfo() {
    df -H --output=source,size,used,avail
}

getOSInfo() {
    if [[ "$(uname)" = "Darwin" ]]; then
        system_profiler SPSoftwareDataType || true
    else
        cat /etc/lsb-release || true
    fi
}

getProcessesInfo() {
      echo ""
      echo -e "${BOLD}*************************************${NORMAL}"
      echo -e "${BOLD}* List of Java/JVM processes running"
      echo -e "${BOLD}*************************************${NORMAL}"
      jps

      echo ""
      echo -e "${BOLD}*************************************${NORMAL}"
      echo -e "${BOLD}* List of all processes running"
      echo -e "${BOLD}*************************************${NORMAL}"
      ps -aux --forest
}