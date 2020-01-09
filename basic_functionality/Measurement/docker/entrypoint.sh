#!/bin/sh
while :
do
  minute="$(date +'%-M')"
  remainder=$(( minute % 10 ))

  if [ "$remainder" -eq 0 ]; then
        python /tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/daily_report_1.py > /tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/measurement_ping_latency.log 2>&1
	echo "Measurement Run"
  fi
  sleep 30
done
