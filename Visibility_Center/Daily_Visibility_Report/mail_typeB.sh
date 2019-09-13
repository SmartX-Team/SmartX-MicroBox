
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
# Name			: dailymail_report.sh
# Description	: Script for Box health check Report Generation.
#
# Created by    : Muhammad Ahmad
# Version       : 0.1
# Last Update	: Sept, 2018
now="$( date +%Y%m%d -d "yesterday")"
file_IOVISOR="/home/netcs/active_monitoring/Daily-report-IOVISOR-data/Daily-report-IOVISOR-mc_$now.csv"
file_vcenter_ping="/home/netcs/active_monitoring/Daily-report-vcenter-data/Daily-report-vcenter-data_$now.csv"
file_total_ping="/home/netcs/active_monitoring/Daily-report-ping-collection/Daily-report-total_ping_$now.csv"
file_ping="/home/netcs/active_monitoring/Daily-report-ping-data/Daily-report-ping-data_$now.csv"
file_latency="/home/netcs/active_monitoring/Daily-report-latency-data/Daily-report-latency-data_$now.csv"
file_tcp="/home/netcs/active_monitoring/Daily-report-tcp-data/Daily-report-tcp-data_$now.csv"


sed -i 's/smartx-microbox-gist-1/smartx-microbox-gist1/g' /home/netcs/active_monitoring/microbox/Daily-report-tcp-data/Daily-report-tcp-data_$now.csv
sed -i 's/'\041'/foo/g' /home/netcs/active_monitoring/microbox/Daily-report-tcp-data/Daily-report-tcp-data_$now.csv
sed -i 's/smartx-microbox-um-1/smartx-microbox-um1/g' /home/netcs/active_monitoring/microbox/Daily-report-tcp-data/Daily-report-tcp-data_$now.csv
sed -i 's/smartx-microbox-um-2/smartx-microbox-um2/g' /home/netcs/active_monitoring/microbox/Daily-report-tcp-data/Daily-report-tcp-data_$now.csv
sed -i 's/smartx-microbox-rub-1/smartx-microbox-rub1/g' /home/netcs/active_monitoring/microbox/Daily-report-tcp-data/Daily-report-tcp-data_$now.csv

file_udp="/home/netcs/active_monitoring/microbox/Daily-report-udp-data/Daily-report-udp-data_$now.csv"
> /home/netcs/active_monitoring/result1.html
################################################Start HTML Generation##########################################################################
TIME=`date +%Y/%m/%d`
echo "<html><head><style>
table {
    width:100%;
}
table, th, td {
    border: 1px solid black;
    border-collapse: collapse;
}
th, td {
    padding: 5px;
    text-align: left;
}
table#t01 tr:nth-child(even) {
    background-color: #eee;
}
table#t01 tr:nth-child(odd) {
   background-color:#fff;
}
table#t01 th	{
    background-color: black;
    color: white;
}
</style>
</head>
<body>
<p style='color:black;'>Dear Playground Operators,</p>
<p style='color:red;'>***      This is an automatically generated email, please do not reply      ***</p>
<br>

<table style='width:50%; border: 1px solid black; border-collapse:collapse;'>
	<tr><th style='font-size:120%; color:blue; text-align: center;' colspan='16'>IOVISOR Daily Collection (Percentage)</th></tr>" >> /home/netcs/active_monitoring/result1.html
bgco="#fff"
FILTER=`date +%Y/%m/%d`
i=0

while IFS=" " read -r f1 f2 f3 f4 f5 f6 f7 f8
do
	if [ "$i" == 0 ];
	then
                echo "<tr style='background-color: $bgco;'><th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Date</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Total Expected Collection</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST1</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-PH</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-HUST</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-CHULA</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-MYREN</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST3</th></tr>" >>/home/netcs/active_monitoring/result1.html
		i=1
	elif [ "$i" == 1 ];
	then
                echo "<tr style='background-color: $bgco;'><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f1</td><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f2</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f3</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f4</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f5</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f6</td><td style='text-align: right; padding:4.8px; border: 1px solid black;'>$f7</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f8</td></tr>" >>/home/netcs/active_monitoring/result1.html

 		if [ "$bgco" == "#fff" ]; then
		bgco="#eee"
		else
 		bgco="#fff"
		fi
	fi
done <"$file_IOVISOR"
echo "</table>" >> /home/netcs/active_monitoring/result1.html



echo "<table style='width:50%; border: 1px solid black; border-collapse:collapse;'>
	<tr><th style='font-size:120%; color:blue; text-align: center;' colspan='16'>Liveliness with Visibility Center(Daily Percentage)</th></tr>" >> /home/netcs/active_monitoring/result1.html
bgco="#fff"
FILTER=`date +%Y/%m/%d`
i=0

while IFS=" " read -r f1 f2 f3 f4 f5 f6 f7 f8
do
	if [ "$i" == 0 ];
	then
                echo "<tr style='background-color: $bgco;'><th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Date</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Total Expected Collection</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST1</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-PH</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-HUST</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-CHULA</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-MYREN</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST3</th></tr>" >>/home/netcs/active_monitoring/result1.html
		i=1
	elif [ "$i" == 1 ];
	then
                echo "<tr style='background-color: $bgco;'><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f1</td><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f2</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f3</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f4</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f5</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f6</td><td style='text-align: right; padding:4.8px; border: 1px solid black;'>$f7</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f8</td></tr>" >>/home/netcs/active_monitoring/result1.html

 		if [ "$bgco" == "#fff" ]; then
		bgco="#eee"
		else
 		bgco="#fff"
		fi
	fi
done <"$file_vcenter_ping"
echo "</table>" >> /home/netcs/active_monitoring/result1.html



echo "<table style='width:50%; border: 1px solid black; border-collapse:collapse;'>
	<tr><th style='font-size:120%; color:blue; text-align: center;' colspan='16'>PING Generated Daily Collection (Count)</th></tr>" >> /home/netcs/active_monitoring/result1.html
bgco="#fff"
FILTER=`date +%Y/%m/%d`
i=0

while IFS=" " read -r f1 f2 f3 f4 f5 f6 f7 f8
do
	if [ "$i" == 0 ];
	then
                echo "<tr style='background-color: $bgco;'><th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Date</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Total Expected Collection</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST1</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-PH</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-HUST</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-CHULA</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-MYREN</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST3</th></tr>" >>/home/netcs/active_monitoring/result1.html
		i=1
	elif [ "$i" == 1 ];
	then
                echo "<tr style='background-color: $bgco;'><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f1</td><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f2</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f3</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f4</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f5</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f6</td><td style='text-align: right; padding:4.8px; border: 1px solid black;'>$f7</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f8</td></tr>" >>/home/netcs/active_monitoring/result1.html

 		if [ "$bgco" == "#fff" ]; then
		bgco="#eee"
		else
 		bgco="#fff"
		fi
	fi
done <"$file_total_ping"
echo "</table>" >> /home/netcs/active_monitoring/result1.html
	

echo "<table style='width:50%; border: 1px solid black; border-collapse:collapse;'>
	<tr><th style='font-size:120%; color:blue; text-align: center;' colspan='16'>Average Daily Uptime(percentage) Site to Site Report based on Ping</th></tr>" >> /home/netcs/active_monitoring/result1.html
bgco="#fff"
FILTER=`date +%Y/%m/%d`
i=0

while IFS=" " read -r f1 f2 f3 f4 f5 f6 f7 f8
do
	if [ "$i" == 0 ];
	then
                echo "<tr style='background-color: $bgco;'><th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Date</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Total Expected Collection</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST1</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-PH</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-HUST</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-CHULA</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-MYREN</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST3</th></tr>" >>/home/netcs/active_monitoring/result1.html
		i=1
	elif [ "$i" == 1 ];
	then
                echo "<tr style='background-color: $bgco;'><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f1</td><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f2</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f3</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f4</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f5</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f6</td><td style='text-align: right; padding:4.8px; border: 1px solid black;'>$f7</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f8</td></tr>" >>/home/netcs/active_monitoring/result1.html

 		if [ "$bgco" == "#fff" ]; then
		bgco="#eee"
		else
 		bgco="#fff"
		fi
	fi
done <"$file_ping"
echo "</table>" >> /home/netcs/active_monitoring/result1.html



echo "<table style='width:50%; border: 1px solid black; border-collapse:collapse;'>
	<tr><th style='font-size:120%; color:blue; text-align: center;' colspan='16'>Average Daily Latency(ms) Site to Site Report</th></tr>" >> /home/netcs/active_monitoring/result1.html
bgco="#fff"
FILTER=`date +%Y/%m/%d`
i=0

while IFS=" " read -r f1 f2 f3 f4 f5 f6 f7 f8
do
	if [ "$i" == 0 ];
	then
                echo "<tr style='background-color: $bgco;'><th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Date</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Total Expected Collection</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST1</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-PH</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-HUST</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-CHULA</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-MYREN</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST3</th></tr>" >>/home/netcs/active_monitoring/result1.html
		i=1
	elif [ "$i" == 1 ];
	then
                echo "<tr style='background-color: $bgco;'><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f1</td><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f2</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f3</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f4</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f5</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f6</td><td style='text-align: right; padding:4.8px; border: 1px solid black;'>$f7</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f8</td></tr>" >>/home/netcs/active_monitoring/result1.html

 		if [ "$bgco" == "#fff" ]; then
		bgco="#eee"
		else
 		bgco="#fff"
		fi
	fi
done <"$file_latency"
echo "</table>" >> /home/netcs/active_monitoring/result1.html


echo "<table style='width:50%; border: 1px solid black; border-collapse:collapse;'>
	<tr><th style='font-size:120%; color:blue; text-align: center;' colspan='16'>Throughput TCP(Mbits/sec) Report</th></tr>" >> /home/netcs/active_monitoring/result1.html
bgco="#fff"
FILTER=`date +%Y/%m/%d`
i=0

while IFS=" " read -r f1 f2 f3 f4 f5 f6 f7 f8
do
	if [ "$i" == 0 ];
	then
                echo "<tr style='background-color: $bgco;'><th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Date</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4px; border: 1px solid black;'>Total Expected Collection</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST1</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: left; padding: 4.8px; border: 1px solid black;'>SmartX-Box-PH</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-HUST</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-CHULA</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-MYREN</th>
		<th style='background-color: black; color:white; border: 1px solid black; text-align: right; padding: 4.8px; border: 1px solid black;'>SmartX-Box-GIST3</th></tr>" >>/home/netcs/active_monitoring/result1.html
		i=1
	elif [ "$i" == 1 ];
	then
                echo "<tr style='background-color: $bgco;'><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f1</td><td style='text-align: left; padding: 4px; border: 1px solid black;'>$f2</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f3</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f4</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f5</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f6</td><td style='text-align: right; padding:4.8px; border: 1px solid black;'>$f7</td><td style='text-align: right; padding: 4.8px; border: 1px solid black;'>$f8</td></tr>" >>/home/netcs/active_monitoring/result1.html

 		if [ "$bgco" == "#fff" ]; then
		bgco="#eee"
		else
 		bgco="#fff"
		fi
	fi
done <"$file_tcp"
echo "</table>" >> /home/netcs/active_monitoring/result1.html




echo "</body></html>" >> /home/netcs/active_monitoring/result1.html
now="$( date +%Y%m%d -d "yesterday")"
mail -a "Content-type: text/html;" -s "[$now] Micro-Box OF@TEIN+ Playground: Daily visibility Report" ahmad@smartx.kr < /home/netcs/active_monitoring/result1.html
