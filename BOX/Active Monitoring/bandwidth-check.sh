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
# Created by    : 
# Version       : 0.1
# Last Update	: MAY, 2018

# Configuration Parameter

#from kafka import KafkaProducer
#from kafka.errors import KafkaError



LOGDIR="log"
LOGRES="/home/tein/active_monitoring/result"
#BoxID_GIST1=1, BoxID_ID=2, BoxID_PH=3, BoxID_PKS=4, BoxID_HUST=5, BoxID_GIST2=6, BoxID_CHULA=7, BoxID_NCKU=8, BoxID_MY=9, BoxID_MYREN=10, BoxID_GIST3=11, BoxID_NUC=12
WAN_BOXES="103.22.221.170 0 202.90.150.4 0 203.191.48.228 0 161.200.25.99 0 0 103.26.47.228 103.22.221.30 103.22.221.62"
#WAN_BOXES="0 0 0 0 0 0 0 0 0 103.26.47.228 0 0"


#WAN_BOXES="103.26.47.228"
#WAN_BOXES="103.26.47.228 161.200.25.99 203.191.48.228 103.22.221.170"
#WAN_BOXES="103.22.221.170 203.191.48.228 161.200.25.99 140.116.214.241 203.80.21.4"
#WAN_BOXES="103.22.221.170"

CLOUD_BOXES=""
ACCESS_BOXES=""
FROM_BOX="103.22.221.170"
FROM_BOX_SITE="SmartX-Box-GIST1"
daily_report=""
A="";

if [ ! -f "$LOGRES/IPERF_TCP_RESULT_WAN_BOX.log" ]
then
	touch $LOGRES/IPERF_TCP_RESULT_WAN_BOX.log
	touch $LOGRES/IPERF_UDP_RESULT_WAN_BOX.log
	echo "$file found."
fi

#if [ ! -f "$LOGRES/IPERF_TCP_RESULT_CLOUD_BOX.log" ]
#then
#	touch $LOGRES/IPERF_TCP_RESULT_CLOUD_BOX.log
#	touch $LOGRES/IPERF_UDP_RESULT_CLOUD_BOX.log
#	echo "$file found."
#fi

#if [ ! -f "$LOGRES/IPERF_TCP_RESULT_ACCESS_BOX.log" ]
#then
#	touch $LOGRES/IPERF_TCP_RESULT_ACCESS_BOX.log
#	touch $LOGRES/IPERF_UDP_RESULT_ACCESS_BOX.log
#	echo "$file found."
#fi


#
# [1] Bandwidth Checking
#
function check_box_bandwidth {
        TIME=`date +%Y/%m/%d`
	echo -e "\n"
        echo -e "------------------------------------------------------------"
        echo -e "|               Start Iperf Test For SD-WAN (Boxes)           |"
        echo -e "------------------------------------------------------------"
        iperf3 -s &

	for TO_BOX in $WAN_BOXES
   	do
          if [ "$TO_BOX" != "$FROM_BOX" ] && [ "$TO_BOX" != 0 ]; then
	  	PING_RESULT2=`ping $TO_BOX -c 2 -s 10 -W 5 | grep Unreachable`
		echo -e "$PING_RESULT2"
		if [ "${PING_RESULT2:-null}" = null ] && [ "$TO_BOX" != 0 ]; then
			echo "*************Destination IP == $TO_BOX****************"
			echo -e " $TO_BOX is UP"
			echo -e " Start Iperf TCP Client at Box $TO_BOX             "










	                RESULT1=`ssh tein@$TO_BOX "iperf3 -c $FROM_BOX  -i 2 -t 6 -l 1M -O 6 -4 -N" | grep -A 1 "sender" | awk '{print $7" "$8}'`
			RESULT1=`echo $RESULT1 | sed 'N;s/\n/ /'`
#			RESULT1=0








# SENDING TCP THROUGH KAFKA
                        python /home/tein/active_monitoring/kafka_send.py  "$TIME $FROM_BOX $TO_BOX TCP $RESULT1" "TCP"
#                       check_daily_report.py "$RESULT1"
####			result_= $RESULT1.split()
###			daily_report= daily_report+result[4]
                        echo  "***DAILY REPORT****"
			RESULT_SPLIT="$(cut -d' ' -f1 <<<$RESULT1)"


			if [ -z "$RESULT_SPLIT" ]; then
		         echo "*****\$RESULT_SPLIT is empty*****"
			 RESULT_SPLIT=0
			 Value_grid+=$RESULT_SPLIT
                    	 Value_grid+=" "
			 echo $Value_grid
  			else
			 Value_grid+=$RESULT_SPLIT
			 Value_grid+=" "
			 echo $Value_grid
                        fi


###                        print(daily_report)
# STORING TCP IN LOG
			echo -e "$TIME $FROM_BOX $TO_BOX TCP $RESULT1" >>  $LOGRES/IPERF_TCP_RESULT_WAN_BOX.log
##################	echo "$TIME $FROM_BOX $TO_BOX TCP $RESULT1" | /opt/KONE-MultiView/MultiView-Dependencies/kafka_2.10-0.10.2.0/bin/kafka-console-producer.sh --broker-list vc.manage.overcloud:9092 --topic koren-s-bandwidth-tcp

#			RESULT_split=$RESULT1.split()
#			dreport= RESULT_split[1]
#                        print (dreport)

			echo -e "Start Iperf UDP Client at Box $TO_BOX"
#			RESULT2=`ssh tein@$TO_BOX "iperf3 -c $FROM_BOX  -u -i 2 -t 10 -b 20M"  | grep "0.00-10.00" | awk '{print $7" "$8" "$9" "$11" "$12}'`
			RESULT3=`ssh tein@$TO_BOX "iperf3 -c $FROM_BOX  -u -i 2 -t 10 -b 20M"  | grep "0.00-10.00" | awk '{print $7" "$8" "$9" "$11" "$12}'`
			echo -e "RESULT3: $RESULT3"
			RESULT4=`echo $RESULT3 | sed 'N;s/\n/ /'`
# SENDING UDP THROUGH KAFKA
			python /home/tein/active_monitoring/kafka_send.py  "$TIME $FROM_BOX $TO_BOX UDP $RESULT3" "UDP"
   			echo  -e "$TIME $FROM_BOX $TO_BOX UDP $RESULT3" >> $LOGRES/IPERF_UDP_RESULT_WAN_BOX.log

			echo -e "Printing UDP RESULT3"
			echo -e "$TIME $FROM_BOX $TO_BOX UDP $RESULT3"
			echo -e "RESULT4:$RESULT4"

			RESULT_SPLIT="$(cut -d' ' -f1 <<<$RESULT3)"
			if [ -z "$RESULT_SPLIT" ]; then
			 Value_grid_udp+=0
			 Value_grid_udp+=" "
			else
			 Value_grid_udp+=$RESULT_SPLIT
  			 Value_grid_udp+=" "
			echo "Value_grid_udp:$Value_grid_udp"

			fi
			echo -e "RESULT_SPLIT:$RESULT_SPLIT"
#			echo "$TIME $FROM_BOX $TO_BOX UDP $RESULT2" | /opt/KONE-MultiView/MultiView-Dependencies/kafka_2.10-0.10.2.0/bin/kafka-console-producer.sh --broker-list vc.manage.overcloud:9092 --topic koren-s-bandwidth-udp
                else if [ "$TO_BOX" == 0 ]; then
                        echo "**********Destination IP == 0************"
                        Value_grid+="-1"
                        Value_grid+=" "
			Value_grid_udp+="-1"
                        Value_grid_udp+=" "
                        echo $Value_grid
                        echo $Value_grid_udp
		else
			echo -e " $TO_BOX is DOWN"
# SENDING TCP THROUGH KAFKA
                        python /home/tein/active_monitoring/kafka_send.py  "$TIME $FROM_BOX $TO_BOX TCP $RESULT1"  "TCP"
##                        MESSAGE="$TIME $FROM_BOX $TO_BOX TCP -2 Mbits/sec -2 Mbits/sec"
##                        producer = KafkaProducer(bootstrap_servers=['vc.manage.overcloud:9092'])
##                        producer.send('active_monitoring_throughput_tcp', key=b'throughput_tcp', value=MESSAGE)
# STORING TCP IN LOG
#			echo -e "$TIME $FROM_BOX $TO_BOX TCP -2 Mbits/sec -2 Mbits/sec" >>  $LOGRES/IPERF_TCP_RESULT_WAN_BOX.log
# SENDING UDP THROUGH KAFKA
# 			 python /home/tein/active_monitoring/kafka_send.py "$TIME $FROM_BOX $TO_BOX UDP -2 Mbits/sec -2 -2" "UDP"
##                        MESSAGE="$TIME $FROM_BOX $TO_BOX UDP -2 Mbits/sec -2 -2" 
##                        producer = KafkaProducer(bootstrap_servers=['vc.manage.overcloud:9092'])
##                        producer.send('active_monitoring_throughput_udp', key=b'throughput_udp', value=MESSAGE)
# STORING UDP IN LOG
#			echo -e "$TIME $FROM_BOX $TO_BOX UDP -2 Mbits/sec -2 -2" >>  $LOGRES/IPERF_UDP_RESULT_WAN_BOX.log

#			echo "$TIME $FROM_BOX $TO_BOX TCP -2 Mbits/sec -2 Mbits/sec" | /opt/KONE-MultiView/MultiView-Dependencies/kafka_2.10-0.10.2.0/bin/kafka-console-producer.sh --broker-list vc.manage.overcloud:9092 --topic koren-s-bandwidth-tcp
#        		echo "$TIME $FROM_BOX $TO_BOX UDP -2 Mbits/sec -2 -2" | /opt/KONE-MultiView/MultiView-Dependencies/kafka_2.10-0.10.2.0/bin/kafka-console-producer.sh --broker-list vc.manage.overcloud:9092 --topic koren-s-bandwidth-udp
		fi
                fi
          else if [ "$TO_BOX" == "$FROM_BOX" ]; then
                        Value_grid+="-"
                        Value_grid+=" "
			Value_grid_udp+="-"
                        Value_grid_udp+=" "
                        echo $Value_grid
			echo $Value_grid_udp
          else if [ "$TO_BOX" == 0 ]; then
                        echo "**********Destination IP == 0************"
                        Value_grid+="-1"
                        Value_grid+=" "
                        Value_grid_udp+="-1"
                        Value_grid_udp+=" "
                        echo $Value_grid
                        echo $Value_grid_udp
          fi
          fi
          fi
 	done
        echo "END of LOOP SEND Daily Report:$Value_grid"
	python /home/tein/active_monitoring/check_daily_report.py "tcp_udp" "$FROM_BOX_SITE" "$Value_grid" "$Value_grid_udp"
        echo " Terminate iperf Server on the $FROM_BOX"
	killall -r -s KILL iperf3
}
#
# Main Script
#

echo -e "\n"
echo -e "#######################################################"
echo -e "#       Checking Playground Resources Bandwidth       #"
echo -e "#######################################################"

check_box_bandwidth

echo -e "Checking Playground Resources Bandwidth is Completed.\n"
echo -e "\n"
