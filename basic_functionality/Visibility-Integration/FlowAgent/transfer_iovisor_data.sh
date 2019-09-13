#!/bin/bash
VISIBILITY_CENTER_IP='103.22.221.56'
VISIBILITY_CENTER_USER='netcs'
PASS='fn!xo!ska!'

TARGETFOLDERDP='/home/netcs/IOVisor-Data/Data-Plane'
TARGETFOLDERCP='/home/netcs/IOVisor-Data/Control-Plane'
TARGETFOLDERDPL='/home/netcs/IOVisor-Data/Data-Plane-Latest'
TARGETFOLDERCPL='/home/netcs/IOVisor-Data/Control-Plane-Latest'
SOURCEFOLDER='/opt/IOVisor-Data'
MOVE_FOLDER='/opt/IOVisor-Data/flows'

Minute="$(date +'%M')"
Hour="$(date +'%H')"
cDate="$(date +'%Y-%m-%d')"

host=`hostname`

echo "$cDate $Hour $Minute" >> transfer.log
#if [ "$Hour" -lt 10 ]
#    then 
#        Hour="0$Hour"
#        echo $Hour
#fi

if [ "$Minute" -eq 00 ]
    then
	if [ "$Hour" -eq 00 ]
        then
                PREVIOUS_HOUR=23
                PREVIOUS_MINUTE=55
                PREVIOUS_DAY="$(date +'%d')"
                PREVIOUS_DAY=$((PREVIOUS_DAY - 1))
                if [ "$PREVIOUS_DAY" -lt 10 ]
                then
                        PREVIOUS_DAY="0$PREVIOUS_DAY"
                fi
        #        echo "In if $PREVIOUS_HOUR $PREVIOUS_MINUTE $PREVIOUS_DAY"
        else
                PREVIOUS_HOUR=$((Hour - 1))
                PREVIOUS_MINUTE=55
                PREVIOUS_DAY="$(date +'%d')"
                if [ "$PREVIOUS_HOUR" -lt 10 ]
                then
                        PREVIOUS_HOUR="0$PREVIOUS_HOUR"
                fi
        #        echo "In else $PREVIOUS_HOUR $PREVIOUS_MINUTE $PREVIOUS_DAY"
        fi
        cDate="$(date +'%Y-%m')"
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$PREVIOUS_DAY-$PREVIOUS_HOUR-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$PREVIOUS_DAY-$PREVIOUS_HOUR-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 5 ]
    then
        PREVIOUS_MINUTE=00
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 10 ]
    then
        PREVIOUS_MINUTE=05
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 15 ]
    then
        PREVIOUS_MINUTE=10
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 20 ]
    then
        PREVIOUS_MINUTE=15
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 25 ]
    then 
        PREVIOUS_MINUTE=20
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE" 
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 30 ]
    then
        PREVIOUS_MINUTE=25
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 35 ]
    then
        PREVIOUS_MINUTE=30
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 40 ]
    then
        PREVIOUS_MINUTE=35
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 45 ]
    then 
        PREVIOUS_MINUTE=40
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 50 ]
    then
        PREVIOUS_MINUTE=45
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
elif [ "$Minute" -eq 55 ]
    then
        PREVIOUS_MINUTE=50
        PREVIOUS_FILE1="/opt/IOVisor-Data/$host-mc-$cDate-$Hour-$PREVIOUS_MINUTE"
        PREVIOUS_FILE2="/opt/IOVisor-Data/$host-data-$cDate-$Hour-$PREVIOUS_MINUTE"
	echo "$host $cDate $Hour $Minute $PREVIOUS_FILE1" >> /opt/FlowAgent/transfer_iovisor_data.log
else
	echo "Missed File:$host $cDate $Hour $Minute " >> /opt/FlowAgent/transfer_iovisor_data.log
fi
echo "0***" $PREVIOUS_FILE1 $PREVIOUS_FILE2 "***"

echo "1***" $PREVIOUS_FILE1 "***" >> /opt/FlowAgent/transfer_iovisor_data.log

echo "2***" $PREVIOUS_FILE1 $MOVE_FOLDER "***" >> /opt/FlowAgent/transfer_iovisor_data.log

if [ ! -z $PREVIOUS_FILE1 ]; then
    sudo cp $PREVIOUS_FILE1 $MOVE_FOLDER  >> /opt/FlowAgent/transfer_iovisor_data.log
    echo "Copying to Local Done" >>  /opt/FlowAgent/transfer_iovisor_data.log
#	sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCP
#	sleep 5
#	sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCP
#	sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCPL
#	sleep 5
#	sshpass -p $PASS scp -o "StrictHostKeyChecking no" $PREVIOUS_FILE1 $VISIBILITY_CENTER_USER@$VISIBILITY_CENTER_IP:$TARGETFOLDERCPL
#	echo "Copying to Server Done" >>  /opt/FlowAgent/transfer_iovisor_data.log

	sleep 20
#/opt/MultiView-Dependencies/multi-view-flowcentric-integration.sh
fi
shopt -s extglob; set -H
cd $SOURCEFOLDER
CURRENT_FILES=`ls -t1 | head -n 2 | sed 'N; s/\n/|/'`
sudo rm -v !($CURRENT_FILES)
echo "Deleted Files Done" >>  /opt/FlowAgent/transfer_iovisor_data.log

cd $MOVE_FOLDER
CURRENT_FILES=`ls -t1 | head -n 2 | sed 'N; s/\n/|/'`
sudo rm -v !($CURRENT_FILES)
echo "Deleted Files from Flows Done" >>  /opt/FlowAgent/transfer_iovisor_data.log
if [ $? -eq 0 ]; then
    echo OK
    echo "Deleted Files Done" >>  /opt/FlowAgent/transfer_iovisor_data.log
else
    echo FAIL
fi
