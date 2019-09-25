#!/usr/bin/env python
import time
import os
import os.path
from kafka import KafkaProducer
from kafka.errors import KafkaError
from datetime import datetime
import sys

#producer=KafkaProducer(bootstrap_servers=['103.22.221.56:9092'],
#connections_max_idle_ms=40100000,
#acks='all',
#retries=2147483647,
#retry_backoff_ms=1000000,
#request_timeout_ms=305000,max_block_ms=2147483647)

print "******SENDING THROUGHPUT DAILY REPORT THROUGH KAFKA*********"
print sys.argv[1]
print sys.argv[2]
print sys.argv[3]
print sys.argv[4]
i = datetime.now()
date=i.strftime('%Y/%m/%d %H:%M:%S')
def on_send_success(record_metadata):
    print(record_metadata.topic)
    print(record_metadata.partition)
    print(record_metadata.offset)
filepath="/home/tein/ping_latency.log"
filepath_throughput="/home/tein/throughput.log"
filepath_missed_collection="/home/tein/measurement_collection_missed.log"
filepath_failures="/home/tein/measurement_failures.log"
#f = open("/home/tein/ping_latency.log", "a")
MESSAGE="Connection"
loop='true'

def no_connection():
     print "Not connected with VCenter"
     if sys.argv[1]=="smartx-microbox_ping_latency":
                    MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
                    MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
                    MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+sys.argv[2]+" "+sys.argv[5]
                    f = open(filepath, "a")
                    f.write(MESSAGE_ping)
                    f.write('\n')
                    f.write(MESSAGE_latency)
                    f.write('\n')
                    f.write(MESSAGE_vcenter)
                    f.write('\n')
                    f.close()
                    f = open(filepath_missed_collection, "a")
                    f.write(date+" "+sys.argv[1]+" "+"Connection Failed")
                    f.write('\n')
                    f.close()
     elif sys.argv[1]=="tcp_udp":
                    MESSAGE_TCP= "MESSAGE_TCP"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
                    MESSAGE_UDP= "MESSAGE_UDP"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
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
 sys.argv[5]='0'
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



		if sys.argv[1]=="smartx-microbox_ping_latency_test1":
 		 print MESSAGE_ping
		 def on_send_error(excp):
                            print "error3"
		 producer.send('microbox_daily_report_ping_test', key=b'ping', value=MESSAGE_ping).add_callback(on_send_success).add_errback(on_send_error)
                 future = producer.send('microbox_daily_report_ping_test', key=b'ping', value=MESSAGE_ping)
                 print future
                 cnt=0
		 if(os.stat("/home/tein/ping_latency.log").st_size > 0):
		  print "Found data in log file Sending....\n\n"
		  with open(filepath ,"r") as fp:
                       all_lines = fp.readlines()
		  with open(filepath ,"w") as fp:
			for newline in all_lines:
			  print("Line {}: {}".format(cnt, newline.strip()))
			  producer.send('microbox_daily_report_ping_test1', key=b'ping', value=newline.strip()).add_callback(on_send_success).add_errback(on_send_error)
			  cnt += 1
			  def on_send_success(record_metadata):
                            print(record_metadata.topic)
                            print(record_metadata.partition)
                            print(record_metadata.offset)
                          def on_send_error(excp):
                            print "error1"

##############################################################################Ping/Latency####################################################################################################

		if sys.argv[1]=="smartx-microbox_ping_latency":
		 MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
		 MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
		 MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+sys.argv[2]+" "+sys.argv[5]
		 def on_send_success(record_metadata):
                            print(record_metadata.topic)
                            print(record_metadata.partition)
                            print(record_metadata.offset)
                 def on_send_error(excp):
                            print "error at check for smartx-microbox_ping_latency"
                            fp.close()
	         print "******SENDING TCP/UDP DAILY REPORT THROUGH KAFKA*********"
	         print "TRUE"
        	 MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
	         MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
	         MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+sys.argv[2]+" "+sys.argv[5]
	         print MESSAGE_ping
	         print MESSAGE_latency
	         print MESSAGE_vcenter
	         producer.send('microbox_daily_report_ping', key=b'ping', value=MESSAGE_ping.split("MESSAGE_ping ",1)[1])
        	 producer.send('microbox_daily_report_latency', key=b'latency', value=MESSAGE_latency.split("MESSAGE_latency ",1)[1])
	         producer.send('microbox_daily_report_vcenter', key=b'vcenter', value=MESSAGE_vcenter.split("MESSAGE_vcenter ",1)[1])
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
			    MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
	                    MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
        	            MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+sys.argv[2]+" "+sys.argv[5]
                	    f = open(filepath, "a")
	                    f.write(MESSAGE_ping)
        	            f.write('\n')
                	    f.write(MESSAGE_latency)
	                    f.write('\n')
        	            f.write(MESSAGE_vcenter)
	                    f.write('\n')
        	            f.close()
                	    f = open(filepath_missed_collection, "a")
	                    f.write(date+" "+sys.argv[1]+" "+"Kafka Connectivity Failed")
        	            f.write('\n')
                	    f.close()

#Sending the delayed(missed) measurements 
		  MESSAGE_delay= date+" "+sys.argv[2]+" "+ str(Measurements_delayed_count/3)
		  producer.send('microbox_measurements_delayed', key=b'delay', value=MESSAGE_delay)
		  with open(filepath ,"w") as fp:
			  for i in range(cnt-1):
				print('')
		  fp.close()
		  print "All data send, log file is empty"

##############################################################################TCP/UDP####################################################################################################

		elif sys.argv[1]=="tcp_udp":
	         print "******SENDING THROUGHPUT DAILY REPORT THROUGH KAFKA*********"

        	 i = datetime.now()
	         date=i.strftime('%Y/%m/%d %H:%M:%S')
	         MESSAGE_TCP= "MESSAGE_TCP"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
	         MESSAGE_UDP= "MESSAGE_UDP"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
	         print MESSAGE_TCP
	         print MESSAGE_UDP
	         producer.send('microbox_daily_report_throughput_tcp', key=b'throughput_tcp', value=MESSAGE_TCP.split("MESSAGE_TCP ",1)[1])
		 producer.send('microbox_daily_report_throughput_udp', key=b'throughput_udp', value=MESSAGE_UDP.split("MESSAGE_UDP ",1)[1])
                 print "check file"
		 def on_send_success(record_metadata):
                            print(record_metadata.topic)
                            print(record_metadata.partition)
                            print(record_metadata.offset)
                 def on_send_error(excp):
                            print "error at check for smartx-microbox_ping_latency"
                            fp.close()
	         if os.path.exists(filepath_throughput) and os.path.getsize(filepath_throughput) > 0:
                   print "Found data in log file Sending....\n\n"
                   cnt=1
                   with open(filepath_throughput ,"r") as fp:
                       all_lines = fp.readlines()
                   with open(filepath_throughput ,"r") as fp:
                        for newline in all_lines:
                          print("Line {}: {}".format(cnt, newline.strip()))
                          if "MESSAGE_TCP" in newline:
                                producer.send('microbox_daily_report_throughput_tcp', key=b'throughput_tcp', value=newline.split("MESSAGE_TCP ",1)[1].strip()).add_callback(on_send_success).add_errback(on_send_error)
                          elif "MESSAGE_UDP" in newline:
                                producer.send('microbox_daily_report_throughput_udp', key=b'throughput_udp', value=newline.split("MESSAGE_UDP ",1)[1].strip()).add_callback(on_send_success).add_errback(on_send_error)
                          cnt += 1
                   with open(filepath_throughput ,"w") as fp:
                          for i in range(cnt-1):
                                print('')
                   print "All data send, log file is empty"
		producer.flush()
                producer.close()              



        except KafkaError:
            # Decide what to do if produce request failed...
	    print KafkaError
	    if sys.argv[1]=="smartx-microbox_ping_latency":
		    sys.argv[5]='0'
		    MESSAGE_ping= "MESSAGE_ping"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
                    MESSAGE_latency= "MESSAGE_latency"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
                    MESSAGE_vcenter= "MESSAGE_vcenter"+" "+date+" "+sys.argv[2]+" "+sys.argv[5]
		    f = open(filepath, "a")
 		    f.write(MESSAGE_ping)
		    f.write('\n')
		    f.write(MESSAGE_latency)
		    f.write('\n')
		    f.write(MESSAGE_vcenter)
		    f.write('\n')
		    f.close()
		    f = open(filepath_missed_collection, "a")
		    f.write(date+" "+sys.argv[1]+" "+"Kafka Connectivity Failed")
		    f.write('\n')
		    f.close()
	    elif sys.argv[1]=="tcp_udp":
		    MESSAGE_TCP= "MESSAGE_TCP"+" "+date+" "+sys.argv[2]+" "+sys.argv[3]
	            MESSAGE_UDP= "MESSAGE_UDP"+" "+date+" "+sys.argv[2]+" "+sys.argv[4]
		    ft = open(filepath_throughput, "a")
#		    ft = open("/home/tein/throughput.log", "a")
		    ft.write(MESSAGE_TCP)
                    ft.write('\n')
		    ft.write(MESSAGE_UDP)
                    ft.write('\n')
		    ft.close()
	    loop='false'
            print "ERROR in loop"
            pass
	def on_send_error(excp):
                            print "error2"
#f.close()
def on_send_success(record_metadata):
    print(record_metadata.topic)
#    print(record_metadata.partition)
#    print(record_metadata.offset)
def on_send_error(excp):
    print "error1"
