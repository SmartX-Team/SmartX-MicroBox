#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#
# Name			: bandwidth-check.sh
# Description	: Script for playground bandwidth checking.
#
# Created by    : MUhammad Ahmad Rathore
# Version       : 0.1
# Last Update	: Sept, 2019

# Configuration Parameter

#from kafka import KafkaProducer
#from kafka.errors import KafkaError
# -*- coding: utf-8 -*-

##LOGDIR="log"
##LOGRES="/home/netcs/active_monitoring/result"

##CLOUD_BOXES=""
##ACCESS_BOXES=""

#import datetime
#load micro-box configuration YAML file.
#	with open("/tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/micro-box-config.yaml", 'r') as stream:
#	data_loaded = yaml.load(stream)


#VCenter
	my_list=`cat  /tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/micro-box-config.yaml`
for item in $my_list;
do
  if [[ $item == *"VCENTER"* ]]; then
   echo "VCENTER is there"
   VCENTER=${item#*=}
   echo $VCENTER 
  fi

#   echo $item
done

FROM_BOX_SITE=$(ip addr | awk '/inet/ && /eno1/{sub(/\/.*$/,"",$2); print $2}')
#"$(getent hosts `hostname` | awk '{print $1}')"
        echo "***Hostname***"
        echo $FROM_BOX_SITE
BOX_HOSTNAME="$(getent hosts `hostname` | awk '{print $2}')"
	echo $BOX_HOSTNAME
BOX_HOSTNAME=${BOX_HOSTNAME:7}

	echo $BOX_HOSTNAME

ping_grid=""

	my_list=`cat  /tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/micro-box-config.yaml`

echo "**********************************************************************"
echo "**********************************************************************"
echo "**********************************************************************"


function micro_box_ping_latency {
	vcenter_check="0"
        TIME=`date +%Y/%m/%d`
        echo -e "\n"
        echo -e "------------------------------------------------------------"
        echo -e "|               Ping+Latency  For SmartX Micro-Boxes       |"
        echo -e "------------------------------------------------------------"

	if ping -c 1 -W 1 "$VCENTER"; then
	 vcenter_check="1"
        else
	 vcenter_check="0"
	fi

#        ping -q -c2 $VCENTER > /dev/null

#        if [ $? -eq 0 ]
#        then
#        echo "ok"
#        vcenter_check="1"
#	echo "vcenter_check:"$vcenter_check
#        fi
	echo "vcenter_check:"$vcenter_check



#	cat  micro-box-config.yaml | while read line
	while read line
        do
#         TO_BOX="cut -d ":" -f 2 <<< $line1
	  if [[ $line == *"smartx-microbox"* ]]; then
           echo -e "smartx-microbox Found"
	   TO_BOX=${line#*=}
          else
           echo -e "smartx-microbox NOT Found"
          fi
	  echo -e "                         ---------------Site: $line---------------"
          echo "TO_BOX="$TO_BOX
	  echo "FROM_BOX="$FROM_BOX_SITE
 	  if [ -z "$TO_BOX" ]
	  then
              echo "\$TO_BOX is empty"
          else
              echo "\$TO_BOX is NOT empty"
          fi
          echo "                          ******************Connecting with $TO_BOX******************"
	  if [ "$TO_BOX" != "$FROM_BOX_SITE" ]&& [ ! -z "$TO_BOX" ]&& [ "$TO_BOX" != "0.0.0.0" ] ; then
                PING_RESULT2=`ping $TO_BOX -c 2 -s 10 -W 5 | grep 'Unreachable\|not reachable'`
                PING_RESULT1=`ping $TO_BOX -c 1 | grep ttl`
                echo -e "PING_RESULT2: $PING_RESULT2"
                echo -e "PING_RESULT1:$PING_RESULT1"


                if [ -z "$PING_RESULT1" ] && [ ! -z "$PING_RESULT2" ] && [ ! -z "$TO_BOX" ]; then
                        echo "Loop: Host $TO_BOX is not reachable from $FROM_BOX_SITE."
                                        ping_grid+=0
                                        ping_grid+=" "
                                        latency_grid+=0
                                        latency_grid+=" "
                else if [ -z "$PING_RESULT1" ] && [  -z "$PING_RESULT2" ] && [ ! -z "$TO_BOX" ]; then
			 echo "Host $TO_BOX is not reachable from $FROM_BOX_SITE."
                                        ping_grid+=0
                                        ping_grid+=" "
                                        latency_grid+=0
                                        latency_grid+=" "

                else if [ ! -z "$PING_RESULT1" ] && [  -z "$PING_RESULT2" ] && [ ! -z "$TO_BOX" ]; then
                        echo "Loop: Connection found."
                        ping_grid+="1 "
                        latency_grid+=`echo $PING_RESULT1 | grep -o -P '(?<=time=).*(?=ms)'`
                else
                        echo "Loop: Host $TO_BOX is not reachable from $FROM_BOX_SITE."
                                        ping_grid+=0
                                        ping_grid+=" "
                                        latency_grid+=0
                                        latency_grid+=" "
                fi
                fi
                fi
          else if [ "$TO_BOX" == "$FROM_BOX_SITE" ]; then
                        ping_grid+="-"
                        ping_grid+=" "
                        latency_grid+="-"
                        latency_grid+=" "
                        echo $Value_grid
	  else if  [ "$TO_BOX"==0.0.0.0 ]&& [ ! -z "$TO_BOX" ]; then
                        ping_grid+="0"
                        ping_grid+=" "
                        latency_grid+="0"
                        latency_grid+=" "
                        echo $Value_grid
          fi
          fi
	  fi
          echo "                           ************Ping_grid(1:UP, 0:Down)***********: $ping_grid"
	  ping=$ping_grid
	  latency=$latency_grid
          echo "                           ************Latency_grid (ms)***********:$latency_grid"
	  echo -e $ping
          echo -e $latency
          echo -e "\n\n--------------------------------------------------------------------------------------------------------------\n\n"
        done < <(cat  /tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/micro-box-config.yaml )

        echo -e $ping
        echo -e $latency
        echo "END of LOOP SEND Daily Ping report"
        echo "smartx-microbox_ping_latency" "$FROM_BOX_SITE" "$ping_grid" "$latency_grid" "$vcenter_check"
	exec python /tmp/SmartX-MicroBox/basic_functionality/Measurement/docker/daily_report.py "smartx-microbox_ping_latency" "$BOX_HOSTNAME" "$ping_grid" "$latency_grid" "$vcenter_check"
}

echo -e "\n"
echo -e "#######################################################"
echo -e "#       Checking Playground Resources Ping+ Latency       #"
echo -e "#######################################################"

micro_box_ping_latency

echo -e "Checking Playground Resources Ping latency is Completed.\n"
echo -e "\n"

