#!/bin/bash
#
# Copyright 2015 SmartX Collaboration (GIST NetCS). All rights reserved.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#
# Name          : multi-view-flowcentric-integration.sh
# Description   : Run spark submit jobs.
#
# Updated by    : Muhammad Ahmad Rathore
# Version       : 0.1
# Last Update   : June, 2019
SOURCEFOLDER='/opt/IOVisor-Data/flowsOut'
export PATH=$PATH:$SPARK_HOME
VISIBILITY_CENTER_IP='103.22.221.56'
VISIBILITY_CENTER_USER='netcs'
PASS='fn!xo!ska!'
my_list=`cat  /opt/measurement/micro-box-config.yaml`
for item in $my_list;
do
  if [[ $item == *"VCENTER"* ]]; then
   VCENTER=${item#*=}
  else
   VCENTER=$VISIBILITY_CENTER_IP
  fi
done
TARGETFOLDERDP='/home/netcs/IOVisor-Data/Data-Plane'
TARGETFOLDERCP='/home/netcs/IOVisor-Data/Control-Plane'
TARGETFOLDERDPL='/home/netcs/IOVisor-Data/Data-Plane-Latest'
TARGETFOLDERCPL='/home/netcs/IOVisor-Data/Control-Plane-Latest'
#export JAVA_HOME=/usr/lib/jvm/java-8-oracle
#export SPARK_HOME=/opt/KONE-MultiView/MultiView-Dependencies/spark-2.2.0-bin-hadoop2.7
export SPARK_HOME=/opt/MultiView-Dependencies/spark-2.2.0-bin-hadoop2.7


while : 
do
  minute="$(date +'%-M')"
  remainder=$(( minute % 5 ))

  if [ "$remainder" -eq 0 ]; then
    echo "RUN"
    sudo rm -r /opt/Multi-View-Staged/AggregateControlPlane.csv/
#    sudo $SPARK_HOME/bin/spark-submit --class smartx.multiview.flowcentric.Main --master local[*] --driver-memory 4g /opt/MultiView-Dependencies/multi-view-flowcentric-aggregate_2.11-0.1.jar "smartx-microbox-gist-1"
    sudo $SPARK_HOME/bin/spark-submit --class smartx.multiview.flowcentric.Main --master local[*] --driver-memory 4g /opt/MultiView-Dependencies/multi-view-flowcentric-aggregate_2.11-0.1.jar "smartx-microbox-gist-1"
    sleep 60
#    $SPARK_HOME/bin/spark-submit --class smartx.multiview.flowcentric.Main --master local[*] --driver-memory 4g multi-view-flowcentric-aggregate_2.11-0.1.jar "SmartX-Box-GIST1" 
#    $SPARK_HOME/bin/spark-submit --class smartx.multiview.flowcentric.Main --master local[*] --driver-memory 4g multi-view-flowcentric-integration_2.11-0.1.jar "SmartX-Box-GIST1"
    shopt -s extglob; set -H
    cd $SOURCEFOLDER
    PREVIOUS_FILE1=`ls -t1 | head -n 1 | sed 'N; s/\n/|/'`

     ping -q -c2 $VCENTER > /dev/null

        if [ $? -eq 0 ];
        then
         vcenter_check="1"
         echo "vcenter_check:"$vcenter_check
        fi

    sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCP
    sleep 5
    sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCP
    #sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE2 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERDP
    sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCPL
    sleep 5
    sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCPL
    #sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE2 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERDPL

  else
    sleep 30
  fi
done
