#!/usr/bin/env python
import time
import os
import os.path
from kafka import KafkaProducer
from kafka.errors import KafkaError
from datetime import datetime
import sys
import subprocess
import re
import socket
import math
import decimal
#producer=KafkaProducer(bootstrap_servers=['103.22.221.56:9092'],
#connections_max_idle_ms=40100000,
#acks='all',
#retries=2147483647,
#retry_backoff_ms=1000000,
#request_timeout_ms=305000,max_block_ms=2147483647)

hostname=socket.gethostname()
ping_check_microbox=""
hostip=""
latency_check_microbox=""
print "******SENDING THROUGHPUT DAILY REPORT THROUGH KAFKA*********"
i = datetime.now()
date=i.strftime('%Y/%m/%d %H:%M:%S')


def check_ping_vc():
	with open('micro-box-config.yaml') as f:
		content = f.readlines()
# you may also want to remove whitespace characters like `\n` at the end of each line
	content = [x.strip() for x in content]
	for line in content:
	        ip=line.split("=",1)[1]
#	        ip_ps=line.split('=')[0]
#	        print ip
        	if "VCENTER" in line:
	            vc_ip=ip
		    print vc_ip
		    response = os.system("ping -c 1 " + vc_ip)
		    if response == 0:
			vc_status='1'
			return vc_status
		    else:
			vc_status='0'
		        return vc_status


def ping(server=hostip, count=2, wait_sec=2):
    """
    :rtype: dict or None
    """
    cmd =  "ping -c {} -W {} {}".format(count, wait_sec, hostip).split(' ')
#    print cmd
    try:
        output =  subprocess.check_output(cmd).decode().strip()
        print output
        lines = output.split("\n")
        total = lines[-2].split(',')[3].split()[1]
        loss = lines[-2].split(',')[2].split()[0]
        timing = lines[-1].split()[3].split('/')
	print "--------------------------------------"
	print type(timing[1])
	D = decimal.Decimal
        return  round(float(timing[1]),1),float(timing[0]),float(timing[2])
#        return {
#            'type': 'rtt',
#            'min': timing[0],
#            'avg': timing[1],
#            'max': timing[2],
#            'mdev': timing[3],
#            'total': total,
#            'loss': loss,
#        }
    except subprocess.CalledProcessError as e:
#    except Exception as e:
        print(e)
        return 0


def check_ping():
#    hostname = "103.22.221.56"
    response = os.system("ping -c 1 " + hostip)
#    print "response:"+ response
#     and then check the response...
    if response == 0:
        pingstatus = "Network Active"
	#pingcheck=ping()
	#print pingcheck
        return  pingstatus #+ pingcheck[0]
    else:
         pingstatus = "Network Error"
	 return pingstatus


#Reading file micro-box-config.yaml with names of Micro-Boxes

vc_check_microbox=check_ping_vc()
print "Visibility Center Status: "+ vc_check_microbox

with open('micro-box-config.yaml') as f:
    content = f.readlines()
# you may also want to remove whitespace characters like `\n` at the end of each line
content = [x.strip() for x in content] 
for line in content:
        print(line)
        ip=line.split("=",1)[1]
        ip_ps=line.split('=')[0]
        print ip
        print ip_ps
        if "smartx-microbox" in line:
	 if ip_ps == hostname:
            ping_check_microbox= ping_check_microbox + '- '
            latency_check_microbox= latency_check_microbox + '- '
	 else:
            hostip=ip
            pingstatus = check_ping()
            print pingstatus
            if pingstatus == "Network Active":
		pingcheck= ping()
                ping_check_microbox= ping_check_microbox + '1 '
                latency_check_microbox= latency_check_microbox + str(pingcheck[0]) +' '

#         print 'latency_avg:' + pingstatus[0] + ' latency_min:' + pingstatus[1]+ ' latency_max:' + pingstatus[2]
            else:
                ping_check_microbox= ping_check_microbox + '0 '
                latency_check_microbox= latency_check_microbox + '0 '
#         print 'Not OK'
            print(ping_check_microbox)
            print(latency_check_microbox)






###pingstatus1=ping()
###if pingstatus1 == '0':
### print ("No Connection found")
###else:
### print 'latency_avg:' + pingstatus1[0] + ' latency_min:' + pingstatus1[1]+ ' latency_max:' + pingstatus1[2]


#p = subprocess.Popen(["ping","103.22.221.56"], stdout = subprocess.PIPE)

###pingstatus = check_ping()
###if pingstatus[0] == "Network Active":
### print 'latency_avg:' + pingstatus[0] + ' latency_min:' + pingstatus[1]+ ' latency_max:' + pingstatus[2]
###else:
### print 'Not OK'

#latencystatus=check_latency()
#print latencystatus

hostname=hostname.replace('smartx-', '')

print "******SENDING Ping+latency REPORT THROUGH KAFKA*********"
date=i.strftime('%Y/%m/%d %H:%M:%S')
def on_send_success(record_metadata):
    print(record_metadata.topic)
    print(record_metadata.partition)
    print(record_metadata.offset)
    print("Successfully Send")
filepath="/home/tein/ping_latency.log"
filepath_throughput="/home/tein/throughput.log"
filepath_missed_collection="/home/tein/measurement_collection_missed.log"
filepath_failures="/home/tein/measurement_failures.log"

MESSAGE="Connection"
loop='true'


def no_connection():
     print "Not connected with VCenter"
     if True:
#sys.argv[1]=="smartx-microbox_ping_latency":
                    MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+hostname+" "+ping_check_microbox
                    MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+hostname+" "+latency_check_microbox
                    MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+hostname+" "+vc_check_microbox
                    f = open(filepath, "a")
                    f.write(MESSAGE_ping)
                    f.write('\n')
                    f.write(MESSAGE_latency)
                    f.write('\n')
                    f.write(MESSAGE_vcenter)
                    f.write('\n')
                    f.close()
                    f = open(filepath_missed_collection, "a")
                    f.write(date+" "+"smartx-microbox_ping_latency"+"Connection Failed")
                    f.write('\n')
                    f.close()
     elif False:
#sys.argv[1]=="tcp_udp":
                    MESSAGE_TCP= "MESSAGE_TCP"+" "+date+" "+hostname+" "+ping_check_microbox
                    MESSAGE_UDP= "MESSAGE_UDP"+" "+date+" "+hostname+" "+latency_check_microbox
                    ft = open(filepath_throughput, "a")
                    ft.write(MESSAGE_TCP)
                    ft.write('\n')
                    ft.write(MESSAGE_UDP)
                    ft.write('\n')
                    ft.close()
     loop='false'
     print "ERROR in loop"
     pass
HOST_UP  = True if os.system("ping -c 1 " + "103.22.221.56") is 0 else False
print HOST_UP
if HOST_UP==False:
 print "No"+" "+ MESSAGE
 loop='false'
 vc_check_microbox='0'
 no_connection()



while (loop=='true'):
	try:
#		producer=KafkaProducer(bootstrap_servers=['103.22.221.56:9092'])
                producer=KafkaProducer(bootstrap_servers=['103.22.221.56:9092'],
		reconnect_backoff_ms=60000,
		reconnect_backoff_max_ms=10000000,
#		reconnect_time_interval_ms=1000,
                connections_max_idle_ms=600000,
#Default: 600000
                acks='all',
                retries=2147483647,
                retry_backoff_ms=540000,
#Default: 100
                request_timeout_ms=300000
		,max_block_ms=2147483647)
#max_block_ms
		loop='false'
	        print 'Broker connection esteblished'




##############################################################################Ping/Latency####################################################################################################

		if True:
#sys.argv[1]=="smartx-microbox_ping_latency":
		 MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+hostname+" "+ping_check_microbox
                 MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+hostname+" "+latency_check_microbox
                 MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+hostname+" "+vc_check_microbox
		 def on_send_success(record_metadata):
                            print(record_metadata.topic)
                            print(record_metadata.partition)
                            print(record_metadata.offset)
                 def on_send_error(excp):
                            print "error at check for smartx-microbox_ping_latency"
                            fp.close()
	         print "******SENDING TCP/UDP DAILY REPORT THROUGH KAFKA*********"
	         print "TRUE"
		 MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+hostname+" "+ping_check_microbox
                 MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+hostname+" "+latency_check_microbox
                 MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+hostname+" "+vc_check_microbox
	         print MESSAGE_ping
	         print MESSAGE_latency
	         print MESSAGE_vcenter
	         producer.send('microbox_daily_report_ping', key=b'ping1', value=MESSAGE_ping.split("MESSAGE_ping ",1)[1]).add_callback(on_send_success).add_errback(on_send_error)
        	 producer.send('microbox_daily_report_latency', key=b'latency1', value=MESSAGE_latency.split("MESSAGE_latency ",1)[1]).add_callback(on_send_success).add_errback(on_send_error)
	         producer.send('microbox_daily_report_vcenter', key=b'vcenter1', value=MESSAGE_vcenter.split("MESSAGE_vcenter ",1)[1]).add_callback(on_send_success).add_errback(on_send_error)
#		 if(os.stat("/home/tein/ping_latency.log").st_size > 0):
		 cnt=0
		 if os.path.exists(filepath) and os.path.getsize(filepath) > 0:
                  print "Found data in log file Sending....\n\n"
		  Measurements_delayed_count=0
                  with open(filepath ,"r") as fp:
                       all_lines = fp.readlines()
                  with open(filepath ,"r") as fp:
                        for newline in all_lines:
                          print("Line {}: {}".format(cnt, newline.strip()))
			  cnt += 1
			  Measurements_delayed_count += 1
			  if "MESSAGE_ping" in newline:
				producer.send('microbox_daily_report_ping', key=b'ping', value=newline.split("MESSAGE_ping ",1)[1].strip()).add_callback(on_send_success).add_errback(on_send_error)
			  elif "MESSAGE_latency" in newline:
				producer.send('microbox_daily_report_latency', key=b'ping', value=newline.split("MESSAGE_latency ",1)[1].strip()).add_callback(on_send_success).add_errback(on_send_error)
			  elif "MESSAGE_vcenter" in newline:
	                        producer.send('microbox_daily_report_vcenter', key=b'ping', value=newline.split("MESSAGE_vcenter ",1)[1].strip()).add_callback(on_send_success).add_errback(on_send_error)
			  def on_send_success(record_metadata):
                            print(record_metadata.topic)
                            print(record_metadata.partition)
                            print(record_metadata.offset)
                          def on_send_error(excp):
                            print "error1"
			    MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+hostname+" "+ping_check_microbox
	                    MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+hostname+" "+latency_check_microbox
           	            MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+hostname+" "+vc_check_microbox
                	    f = open(filepath, "a")
	                    f.write(MESSAGE_ping)
        	            f.write('\n')
                	    f.write(MESSAGE_latency)
	                    f.write('\n')
        	            f.write(MESSAGE_vcenter)
	                    f.write('\n')
        	            f.close()
                	    f = open(filepath_missed_collection, "a")
	                    f.write(date+" "+"smartx-microbox_ping_latency"+" "+"Kafka Connectivity Failed")
        	            f.write('\n')
                	    f.close()

#Sending the delayed(missed) measurements 
		  MESSAGE_delay= date+" "+hostname+" "+ str(Measurements_delayed_count/3)
		  producer.send('microbox_measurements_delayed', key=b'delay', value=MESSAGE_delay)
		  with open(filepath ,"w") as fp:
			  for i in range(cnt-1):
				print('')
		  fp.close()
		  print "All data send, log file is empty"

##############################################################################TCP/UDP####################################################################################################




        except KafkaError:
            # Decide what to do if produce request failed...
	    print KafkaError
	    if True:
#sys.argv[1]=="smartx-microbox_ping_latency":
		    vc_check_microbox='0'
		    MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+hostname+" "+ping_check_microbox
                    MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+hostname+" "+latency_check_microbox
                    MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+hostname+" "+vc_check_microbox
		    f = open(filepath, "a")
 		    f.write(MESSAGE_ping)
		    f.write('\n')
		    f.write(MESSAGE_latency)
		    f.write('\n')
		    f.write(MESSAGE_vcenter)
		    f.write('\n')
		    f.close()
		    f = open(filepath_missed_collection, "a")
		    f.write(date+" "+"smartx-microbox_ping_latency"+" "+"Kafka Connectivity Failed")
		    f.write('\n')
		    f.close()
	    loop='false'
            print "ERROR in loop"
            pass
	def on_send_error(excp):
                            print "error2"
#f.close()
def on_send_success(record_metadata):
    print(record_metadata.topic)
    print("Successfully Send")
#    print(record_metadata.partition)
#    print(record_metadata.offset)
def on_send_error(excp):
    print "error1"




