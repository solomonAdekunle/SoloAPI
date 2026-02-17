# SearchAPI Acceptance Tests

This repository contains accepted tests for the SearchAPI project (Cloud). The test cases are written using Cucumber, Selenium running on the JVM.
Both Endeca and GroupBy related acceptance tests are available to be run against the latest version of SearchAPI Cloud.

**Table of contents**
- [History](#history)
- [Prerequisites](#prerequisites)
- [Getting started](#getting-started)
- [Cucumber](#cucumber)
- [Browser support](#browser-support)
- [Usage](#usage)
  - [Local Usage](#local-usage)
  - [IDE Usage](#ide-usage)
  - [Pipeline Usage](#pipeline-usage)
  - [Jenkins](#jenkins)
  - [Test rails](#test-rails)
- [Outputs](#outputs)

## History

This project (or repo) is an outcome of the extraction of the Web folder from the OnPrem [ecommercetesting project](https://ecllajgitpn001.ebs.ecomp.com/root/ecommercetesting).
All the tests relevant to SearchAPI cloud has been retained and the remaining test (or features) were deleted. Details of the original [legacy testing projects](https://rs-components.atlassian.net/wiki/spaces/TH/pages/1441432220/Web+Regression+Pack+-+Running+Tests) can be found [here](https://rs-components.atlassian.net/wiki/spaces/TH/pages/1441432220/Web+Regression+Pack+-+Running+Tests).

As a team we decided to not retain the git history of the older project and hence the initial commits to the project has the author and ownership (as per git) of the person committing this project to the OnPrem Gitlab server.

A page on confluence has been created to track all links and resources related to this project i.e. [Legacy Test Suite](https://rs-components.atlassian.net/wiki/spaces/TH/pages/1441432220/Web+Regression+Pack+-+Running+Tests).

Before creating this project, we were running the [ecommercetesting project](https://ecllajgitpn001.ebs.ecomp.com/root/ecommercetesting) which meant going through a word document and following steps, installing programs, downloading Run configurations, etc... before we were able to get started with running tests via the IDE or CLI.

## Prerequisites

- Java 1.8.x (does not work on another JDK version, would require changes to project, due to JDK changes)
- Maven 3.8.x or higher
- Jenkins (to run pipeline onPrem or local)
- Access to onPrem servers 
  - Gitlab OnPrem - to be able to clone the repo and run the Acceptance tests (Cucumber/Gherkin) on local machine and target local or remote instances of SearchAPI Cloud
  - Jenkins OnPrem and TestRails - to be able to run the Acceptance tests (Cucumber/Gherkin) on onPrem cloud and target a cloud instance of SearchAPI Cloud
- Change the `settings.xml` and `toolchains.xml` inside the `.m2` folder on your local machine, to point to Monolith Nexus (if you are using different `mvn` repositories than the onPrem version).
  - Download this ready-to-use `settings.xml` file from [here](https://electrocomponents.sharepoint.com/sites/RSDigital/_layouts/15/guestaccess.aspx?guestaccesstoken=K3y1QpXbIgqkLhRyqSfYxGwVfKIbQuNn4m8cV9RR2yQ%3d&docid=2_18854bdd4bf504f038ddea6c62cf611d0&rev=1)
  - In case you don't have one present in your `.m2` folder, place it there
    - Please ensure you have added your **RS Employee id** for eg. _Cxxxxxxx_ to the `<username></username>` tag under the `<id>nexus-snapshots</id>` tag section in the `settings.xml` file 
  - Otherwise, merge the necessary configuration from the downloaded `settings.xml` into the existing one in the `.m2` folder
  - In case you are not sure about the above, please ask a team member
- IntelliJ CE (or another equivalent IDE)
  - Cucumber plugin(s) installed (to edit and execute Gherkin/BDD Features and Scenarios):
    - Cucumber JVM
    - Gherkin
- Note that the setting of the environment variable `WEBDRIVER_CHROME_DRIVER_LOCATION` is Optional - find out when to use/set it as you go along the README

## Getting started

The following steps should get you started with using and running the tests suite quickly:
- read and ensure the [Prerequisites](#prerequisites) are fulfilled before proceeding further
- read and follow the steps in the [IDE Usage](#ide-usage) section
- try to run one or all of the tests
  - from the IDE using the Run configuration setup in the previous step, OR,
  - from the CLI using the commands shown in the [Local Usage](#local-usage) section
  - find the reports generated in the `target` folder as a result of one or both of the above steps
- have a quick read of the other sections mentioned in the Table of contents at the start of the README, to familiarise yourself with the other aspects of the test suite i.e. Jenkins, TestRail, etc...

## Cucumber
### Tags

We have defined two new cucumber tags, on top of the existing tags:

- ```@All_Acceptance_Tests```: runs the whole suite of tests in the `src/test/resources/cucumber` folder
- ```@SAPI_Quick_Acceptance_Tests```: runs tests that are marked with this tag in the above folder. For simplicity mostly Natural Search related tests are selected. And other tests that are NOT Product, Category (Root, L1-L3) or Terminal node related. Although a recent action of re-organising the tests means a number of quick tests we added to this category and a handful of slow tests were moved out irrespective of them being Natural Search or other kinds. GroupBy Acceptance tests are also included in this group.
- ```@GroupBy_Acceptance_Tests```: runs tests that are marked with this tag in the above folder. These are GroupBy related tests only and not related to the Endeca ones, although they are covered when either of the above tags are used. 

These tags are used appropriately in the respective Jenkins jobs depending on the usage - regular builds (quick tests) and nightly builds (long running tests).

## Browser support

We need a compatible version of Chrome or Chrominium to be able to run the tests in the test suite.
The _WebDriverManager_ library which manages which version of the ChromeDriver to download depends on the installed version of  Chrome or Chrominium.
Hence it's best to pin the version so the _WebDriverManager_ library does not have to download a newer version each time, also if a version is unavailable this could lead to not been able to run the suite in a given environment.

This can be achieved in a couple of ways:
- [Downgrade your Chrome version (Windows)](https://support.google.com/chrome/a/answer/7125792)
  - [command to downgrade Chrome](https://support.google.com/chrome/a/answer/7125792#zippy=%2Cstep-downgrade-chrome) - if you wish to switch-back to an older version
- [Manage Chrome updates (Windows)](https://support.google.com/chrome/a/answer/6350036)
  - [roll-back Chrome to previous version](https://support.google.com/chrome/a/answer/6350036#rollback&zippy=%2Croll-back-chrome-browser-to-a-previous-version)
  
The first option to Downgrade the Chrome version has worked during instances that need correcting the version of Chrome as it has progressed further away than the _WebDriverManager_ can keep up. To help further the below command worked (so can be tried out):

```bash
msiexec /fvomus GoogleChromeStandaloneEnterprise.msi ALLOWDOWNGRADE=1
```

## Usage

**Note:** _please do NOT use the below cucumber option on CLI, IDE or on CI:_

```cli-args
 -Dcucumber.options="--tags @..."
```

The above causes tests (i.e. Scenarios) to run multiple times, use the `-DparallelTags="..."` instead. Multiple
tags are comma-separated and specified WITHOUT the `@` usually specified when referring to cucumber features.

In the absence of the usage of the above flag, please ensure passing in the below flag as arguments for every run:

```cli-args
-Dskip.cucumber.parallel.tests.report=true
```

Otherwise, the build/test run will fail on local machine or CI, as by default this is set to `false` in the `pom.xml`. 
All examples in this section have been updated to reflect these changes, with the exception that when using the above 
option(s) only a small number of features or scenarios are selected (see how they have been used in the Run/Debug 
configurations files).

### Local Usage

Below are a few maven commands examples to be able to run the tests from your CLI or from within the IDE:

**env: st1, testrail disabled, using your own/manually downloaded WebDriverManager for ChromeDriver**

```bash
export WEBDRIVER_CHROME_DRIVER_LOCATION="/path/to/manually/downloaded/chromedriver"
mvn -f pom.xml clean verify -Dcountry=uk -Denv=st1 -Dheadless=true     \
         -Dversion=cloud -Dlocal=true -Dwebdriver.chrome.driver="${WEBDRIVER_CHROME_DRIVER_LOCATION}" \
         -Dngsp=true -Dendeca=true -DuseNexus=true -Dtestrail.enabled=false           \
         -Dtestrail.rerunSuccessful=true -Dskip.cucumber.parallel.tests.report=false  \
         -DparallelTags=All_Acceptance_Tests -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

**env: st1, local or cloud, testrail disabled, using a shell-script provided in the `scripts` folder, custom Chrome Driver**

When using the shell-script and a custom version of the Chrome Driver, ensure you have downloaded the ChromeDriver
from https://chromedriver.chromium.org/downloads for the respective version of the Chrome browser on your local machine.
We recommend placing this into the following folder for Windows (or Linux or macOS as per the examples below):

```bash
$HOME/.cache/selenium/chromedriver/win64/[version number]/
```

Then set the following environment variable in the `.bashrc` file:

```bash
export WEBDRIVER_CHROME_DRIVER_LOCATION="$HOME/.cache/selenium/chromedriver/win64/[version number]/chromedriver.exe"

# Above is for Windows, while for Linux and macOS the path would differ slightly, for e.g.:

export WEBDRIVER_CHROME_DRIVER_LOCATION="$HOME/.cache/selenium/chromedriver/linux/[version number]/chromedriver"
export WEBDRIVER_CHROME_DRIVER_LOCATION="$HOME/.cache/selenium/chromedriver/macos/[version number]/chromedriver"
```

Run the below in the root folder of the project:

```bash
./scripts/mvn-clean-verify-from-local-machine.sh
```

The above shell-script depicts more or less how we run the tests on the Jenkins pipeline. When environment variables
`ENV` and `VERSION` are not set they assume values 'st1' and local'. So we run the tests from the local machine using
data/settings for 'st1' and running _SearchAPI Cloud_ app on the same local machine.

In order to run, from the local machine but target the 'dev' environment settings and run against the _SearchAPI Cloud_
app on the cloud, the below command would need to be used:

```bash
ENV='dev' VERSION='cloud' ./scripts/mvn-clean-verify-from-local-machine.sh
```

In order to run, the tests but not evict and reload the cache against the _SearchAPI Cloud_
app instance, the below command would need to be used:

```bash
SKIP_CACHE_RELOAD="true" ./scripts/mvn-clean-verify-from-local-machine.sh
```

By default `SKIP_CACHE_RELOAD` is "false", so that we evict and reload the cache and start from a clean start but there are instances when the above is also necessary. This flag can be used in combination with the other flags discussed in the README. 

To only clear the cache, run the below command (it uses the default flags):

```bash
./scripts/evict-and-reload-cache.sh

# Default values for LOCALE="uk" ENV="st1" VERSION="local" are used 
```

OR

where you dan specify the flag, flags can be used in any combination:

```bash
LOCALE="uk" ENV="dev" VERSION="cloud" ./scripts/evict-and-reload-cache.sh
```

`MAVEN_OPTS` and/or `MAVEN_ARGS` can be used to pass in additional JVM and Maven related flags to the running maven process:

```bash
VERSION="cloud" MAVEN_OPTS="-Xmx4096m" MAVEN_ARGS="-X" ./scripts/mvn-clean-verify-from-local-machine.sh
```

The above runs the maven process in debug mode (maven debug mode), see [Configure docs](https://maven.apache.org/configure.html).

**env: st1, testrail disabled**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -Dheadless=true -Dversion=cloud -Dlocal=true -Dtimeout=30 \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```


**env: st1, testrail disabled, specific tag(s)**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -DparallelTags="SAPI_Quick_Acceptance_Tests" \
                 -Dheadless=true -Dversion=cloud -Dlocal=true -Dtimeout=30 \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```


**env: st1, testrail disabled, specific tag(s), multiple tags in parallel (and enabled maven cucumber report generation flag)**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -DparallelTags="Feature_Natural_Search,Feature_Attribute_Filter_Category" \
                 -Dheadless=true -Dversion=cloud -Dlocal=true -Dtimeout=30 \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

_**Note: that these tests can take a long time to run, hence use the appropriate tags or other cucumber options to narrow down the features or scenarios you wish to run._**

**env: st1, testrail disabled, one or more tag(s), using WebDriverManager for ChromeDriver against a local instance of SearchAPI Cloud**

Ensure the **search-api** cloud app is running on your local-machine as per the instructions on https://gitlab.com/rs-devsecops/rs-dev/discovery/searchapi-cloud in a separate CLI terminal.
You may need to apply the Local Redis Cache configuration changes to your master branch (or any other target branch) before being able to run the app, in case this not yet built into the branch.

Once the _SearchAPI Cloud_ app is running you should see something like this in the terminal console:
```shell
.
.
.
{"@timestamp":"2023-09-22T09:26:13.2007277+01:00","@version":"1","message":"Tomcat started on port(s): 8080 (http) with context path ''","logger_name":"org.springframework.boot.web.embedded.tomcat.TomcatWebServer","thread_name":"restartedMain","level":"INFO","level_value":20000,"source":"Search API Cloud","service":"searchapi-cloud"}
{"@timestamp":"2023-09-22T09:26:13.2167114+01:00","@version":"1","message":"Started SearchAPI in 5.484 seconds (process running for 6.424)","logger_name":"com.electrocomponents.searchapi.SearchAPI","thread_name":"restartedMain","level":"INFO","level_value":20000,"source":"Search API Cloud","service":"searchapi-cloud"}
```

Then perform one of the below calls against the local instance from another terminal window:
```bash
curl "http://localhost:8080/search-api/admin/health"
```

OR

```bash
curl "http://localhost:8080/v1/search?categoryIdNamespace=rsId&channelId=desktop&clientId=test&localeId=uk&searchQuery=car" -H "accept: application/json" 
```

AND the ones below are mandatory before kicking off the test suite against the SearchAPI Cloud app:

```bash
### Depending the purpose please change the LOCALE and/or CUSTOMER_FILTER to fit the needs
LOCALE="uk"
CUSTOMER_FILTER=12345
curl "http://localhost:8080/v1/evictCategory?localeId=${LOCALE}"

curl "http://localhost:8080/v1/category/ROOT?localeId=${LOCALE}&clientId=test&subLevels=3&channelId=responsive"
curl "http://localhost:8080/v1/category/ROOT?localeId=${LOCALE}&clientId=test&subLevels=3&channelId=responsive&customerFilter=${CUSTOMER_FILTER}"
```

Once one of the above commands comes back with a positive response i.e. a json response body with some results, proceed with the below command in a new terminal window:

_To run all features run the below:_
```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -DparallelTags="All_Acceptance_Tests" \
                 -Dheadless=true -Dversion=local -Dlocal=true -Dtimeout=30 \
                 -DuseWebDriverManager=true \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

OR

```bash
./scripts/mvn-clean-verify-from-local-machine.sh
```

_To run specific set of features run the below:_
```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -DparallelTags="SearchApi_Experience_Manager_Tests,Feature_Map_Double_Single_Byte_Characters" \
                 -Dheadless=true -Dversion=local -Dlocal=true -Dtimeout=30 \
                 -DuseWebDriverManager=true \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

OR

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -Dcucumber.options="--tags @SearchApi_Experience_Manager_Tests,@Feature_Map_Double_Single_Byte_Characters" \
                 -Dskip.cucumber.parallel.tests.report=true \
                 -Dheadless=true -Dversion=local -Dlocal=true -Dtimeout=30 \
                 -DuseWebDriverManager=true \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

**env: st1, cloud, testrail disable, only GroupBy Test Scenarios feature**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=false \
                 -DparallelTags="GroupBy_Acceptance_Tests" \
                 -Dskip.cucumber.parallel.tests.report=true \
                 -Dheadless=true -Dversion=cloud -Dlocal=true -Dtimeout=30 \
                 -DuseWebDriverManager=true \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```



You can see activities in both the terminal windows, showing the outcomes of the requests and responses respectively.

### IDE Usage

If you are using _Jetbrain's_ **IntelliJ**, then this is an easy process if one has the Run Configurations in place. These can be found in the `run-configurations` folder in the root directory of this project.
They are in template form with most, if not, all of the minimum configurations in place, that can be used to run specific or all tests on your local machine via the IDE.

They are equivalent to running the above _mvn_ commands from the CLI. Here are the steps to follow in order to get them work in your IDE:
- shutdown IntelliJ if it's currently running
- check if you have the `.idea/runConfigurations` folder in the root of your project folder
  - if not, quickly create it with the exact name and capitalisation
- copy the configuration files (`*.xml` files) from the `./intellij-run-configs` folder into the `.idea/runConfigurations` folder
- start IntelliJ to see the configurations in effect under the _Run > Edit Configurations..._ menu option at the top bar

- Under Run/Debug Configuration you should see the below configurations among others (if you had any previously):
  - **Cucumber Java based**:
     - [Cucumber Java config (uk, st1)](.%2Fintellij-run-configs%2FCucumber_Java_config__uk__st1_.xml)
     - [Cucumber Java config (uk, st1, one or more tags)](.%2Fintellij-run-configs%2FCucumber_Java_config__uk__st1__one_or_more_tags_.xml)
     - [All Features in: EndecaSearchAPI (dry run to print list)](.%2Fintellij-run-configs%2FAll_Features_in__EndecaSearchAPI__dry_run_to_print_list_.xml): 
    this Run configuration can be used to extract _Features and Scenarios_ in the project as an HTML file. They will be printed as test results in the Run/Debug panel, and can be exported as HTML file (click on the button with the diagonal arrow pointing to the top-right corner).
    See [docs](https://www.jetbrains.com/help/idea/viewing-and-exploring-test-results.html) to see how to use this feature.
     - Also see [Run Cucumber tests from within the IntelliJ IDE](https://www.jetbrains.com/help/idea/running-cucumber-tests.html)
  - **JUnit based**:
     - [JUnit config (uk, st1)](.%2Fintellij-run-configs%2FJUnit_config__uk__st1_.xml)
     - [JUnit config (uk, st1, one or more tags)](.%2Fintellij-run-configs%2FJUnit_config__uk__st1__one_or_more_tags_.xml)
     - Using the two Run/Debug configuration would require updating the JDK linked to them, please select the one available from the list and that meets the minimum prerequisites (see top section of the README for details).
  - **maven based**:
     - [maven config (uk, st1).xml](.%2Fintellij-run-configs%2Fmaven_config__uk__st1_.xml)
     - [maven config (uk, st1, one or more tags)](.%2Fintellij-run-configs%2Fmaven_config__uk__st1__one_or_more_tags_.xml)

These configuration files are pre-set with the most regularly used parameters but those mentioned in the [Usage](#usage) section above could also be used if necessary.
In these Run/Debug Configuration files you are particularly looking for parameters like `--tags` or `-DparallelTags` in order to pass in one or more cucumber tags to them. 
From the above three groups, the **Cucumber Java config** and **JUnit config** are most handy when trying to debug features or scenarios as they respond to line or method level breakpoints set during debugging.

### Pipeline Usage

Below are a few maven commands examples used when running these tests as part of a pipeline. See [Jenkins](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/) for real world examples.

**env: st1, testrail enabled, specific tag(s), using WebDriverManager for ChromeDriver**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=true \
                 -DplanNumber=12345 -DparallelTags="SAPI_Quick_Acceptance_Tests" \
                 -Dheadless=true -Dversion=cloud -Dlocal=false -Dtimeout=30 \
                 -DuseWebDriverManager=true \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

**env: st1, testrail enabled, specific tag(s), specifying ChromeDriver**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=true \
                 -DplanNumber=12345 -DparallelTags="SAPI_Quick_Acceptance_Tests" \
                 -Dheadless=true -Dversion=cloud -Dlocal=false -Dtimeout=30 \
                 -Dwebdriver.chrome.driver=path_to_chromedriver/chromedriver.exe \
                 -DchromeUserAgent=110.0.5481.77 \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

**env: st1, testrail enabled, multiple tags, specifying ChromeDriver**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=true \
                 -DplanNumber=12345 \
                 -DparallelTags="SearchApi_Experience_Manager_Tests,Feature_Map_Double_Single_Byte_Characters" \
                 -Dheadless=true -Dversion=cloud -Dlocal=false -Dtimeout=30 \
                 -Dwebdriver.chrome.driver=path_to_chromedriver/chromedriver.exe \
                 -DchromeUserAgent=110.0.5481.77 \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

_**Note: We highly recommend using WebDriverManager over specifying your own chromedriver executable._** Multiple 
tags are comma-separated and specified WITHOUT the `@` usually specified when referring to cucumber features.

**env: st1, testrail enabled (Rerunning previously passed tests), specific tag(s), using WebDriverManager for ChromeDriver**

```bash
mvn clean verify -Dcountry=uk -Denv=st1 -Dngsp=true -Dtestrail.enabled=true \
                 -DplanNumber=12345 -Dtestrail.rerunSuccessful=true \
                 -DparallelTags="SearchApi_Experience_Manager_Tests,Feature_Map_Double_Single_Byte_Characters" \
                 -Dheadless=true -Dversion=cloud -Dlocal=false \
                 -DuseWebDriverManager=true \
                 -Dtest=skipMavenTestPhase -DfailIfNoTests=false
```

_**Note: By default the app will not rerun tests which have previously passed within the TestRail test plan._**

### Jenkins

The [Jenkins (onPrem) site](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/) can be found [here](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/). Below are the two jobs that this project has setup for CI purposes:

- [Regular builds (branches, merges)](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/job/Static_1/job/Jobs/job/SAPI%20Acceptance%20Tests/)
- [Nightly builds](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/job/Static_1/job/Jobs/job/SAPI%20Acceptance%20Tests%20(Nightly)/)

And there is a trigger job that triggers the [Nightly builds](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/job/Static_1/job/Jobs/job/SAPI%20Acceptance%20Tests%20(Nightly)/):
- [Nighly builds trigger job](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/job/Static_1/job/Triggers/job/Nightly%20Build/)

The nightly build trigger job has been configured to run at 1:00AM between Monday and Friday (including both days). 
The _Build periodically_ option under **Build Triggers** is set to _H 01 * * 1-5_ to achieve this outcome. 

_Note:_ at the moment we are only covering the **ST1 (stable environment) in the onPrem environment.**

### Test rails

The [Test Rails (onPrem) site](http://testrail.internal.ecomp.com/testrail/index.php) can be found [here](http://testrail.internal.ecomp.com/testrail/index.php). Below are the two Test Rails plans that this project has setup for test tracking purposes:

- [Regular builds (branches, merges)](http://testrail.internal.ecomp.com/testrail/index.php?/plans/view/180366)
- [Nightly builds](http://testrail.internal.ecomp.com/testrail/index.php?/plans/view/180416)

_Note:_ at the moment we are only covering the **ST1 (stable environment) in the onPrem environment.**

## Outputs

The outputs to the above commands when successfully run (even if tests have failed), can be found in the following folders:

- `target/cucumber-reports/cucumber-html-reports/`

The HTML reports in this folder give an overview of the tests that have run based on the parameters passed from the point of view of Cucumber features, tags, scenarios etc.

The raw output files can be found in this folder:

- `target/cucumber/`
- `target/cucumber-parallel/`

There is an overnight job that runs on Jenkins which creates a JUnit report and a Cucumber report, the Cucumber report is archived and can be found against the respective Jenkins jobs:

- [Regular builds (branches, merges)](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/job/Static_1/job/Jobs/job/SAPI%20Acceptance%20Tests/)
- [Nightly builds](http://ecllajtstpn001.ebs.ecomp.com:8080/jenkins/job/SAPI%20Acceptance%20Tests/job/Static_1/job/Jobs/job/SAPI%20Acceptance%20Tests%20(Nightly)/)

If the "Cucumber Reports" link does not appear on the left side panel of the Jenkins interface, it can be found from the last run job (also left side panel).
You will need access to OnPrem Jenkins in order to be able to open these reports.