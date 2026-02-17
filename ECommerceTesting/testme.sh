#!/bin/bash
 
headless=false
while getopts ":t:c:p:r:e:h" opt; do
  case $opt in
    t)
      tag=$OPTARG;;
    c)
      countries=$OPTARG;;
    p)
      testPlanNumber=$OPTARG;;	  
	r)
      testRailEnabled=$OPTARG;;	  	  
    e)
      envir=$OPTARG;;	  	  
    h)
      headless='true';;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done

rm ./output*.log

declare -a OUTPUT

for country in $(echo $countries | sed "s/,/ /g")
do
	echo ">>> Running tag [$tag] for country [$country]"
	(mvn surefire:test -Dtest.skip=false -Dcountry=$country -Denv=$envir -Dtestrail.enabled=$testRailEnabled -Dtestrail.planid=$testPlanNumber -Dchromebooboo=true "-Dcucumber.options=--tags  $tag") > ./output_$country.log &

done

wait

for country in $(echo $countries | sed "s/,/ /g")
do
	result='PASSED'
        grep -q "There are test failures" output_$country.log
	if [ $? -eq 0 ]; then
		result='FAILED'
	fi

	grep -q "\[ERROR\]" output_$country.log
	if [ $? -eq 0 ]; then
		result='FAILED'
	fi

	echo "[$country] -> $result"
done
