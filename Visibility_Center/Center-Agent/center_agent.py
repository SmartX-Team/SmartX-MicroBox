#!/usr/bin/python
from multiprocessing import Process
from time import sleep
import os
import zmq
import time
import sys
from subprocess import call
import subprocess
from datetime import datetime, timedelta
import time, threading
import socket
import atexit
import ipaddress

BOX_HOSTNAME = socket.gethostname()
print BOX_HOSTNAME
mongodb_counter=0
collectd_counter=0
measurement_counter=0
test_counter=0


#port = "5556"
#if len(sys.argv) > 1:
#    port =  sys.argv[1]
#    int(port)
#context = zmq.Context()
#socket = context.socket(zmq.REP)
#socket.bind("tcp://*:%s" % port)


def func1():
    global mongodb_counter
    global collectd_counter
    global measurement_counter
    global test_counter
    while True:
	print("****************************************")
        print("func1 up and running")
	p = subprocess.Popen("sudo systemctl status mongodb", stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	p_status = p.wait()
	if 'active (running)' in output:
	  print 'MogoDB is  \033[1;32;40m       running \033[0m'
	  mongodb_counter=0
	else:
#	  print 'MogoDB is NOT running'
	  os.system("echo 'MogoDB is NOT running'")
	  os.system("sudo systemctl start mongodb")
	  mongodb_counter= mongodb_counter+1
	  time1=mongodb_counter*10
	  os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report MongoDB down {0}".format(time1))

        p = subprocess.Popen("pgrep -af java | grep collectd_all.jar", stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	p_status = p.wait()
	if 'collectd_all.jar' in output:
	 print 'collectd (Jar)  \033[1;32;40m       running \033[0m'
	else:
	 print 'collectd (Jar) is NOT running'
	 os.system("/usr/bin/screen -dmS collectd sudo java -jar /home/netcs/active_monitoring/tests/collectd_all.jar  > /home/netcs/collectdjar_cron.log 2>&1")
	 collectd_counter= collectd_counter+1
         time1=collectd_counter*10
         os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report collectd down {0}".format(time1))


	p = subprocess.Popen("service elasticsearch  status", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'active (running)' in output:
          print 'elasticsearch   \033[1;32;40m       running \033[0m'
          mongodb_counter=0
        else:
          print 'elasticsearch  is NOT running'
#          os.system("echo 'MogoDB is NOT running'")
          os.system("sudo systemctl start elasticsearch")
          mongodb_counter= mongodb_counter+1
          time1=mongodb_counter*10
          os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report elasticsearch  down {0}".format(time1))

	p = subprocess.Popen("pgrep -af java | grep typeB_measurement.jar", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        print  output
        if 'typeB_measurement.jar' in output:
         print 'typeB_measurement (Jar)  \033[1;32;40m       running \033[0m'
         measurement_counter=0
        else:
         print 'typeB_measurement (Jar) is NOT running'
         os.system("/usr/bin/screen -dmS typeB_measurement sudo java -jar /home/netcs/active_monitoring/tests/typeB_measurement.jar  > /home/netcs/typeB_measurement_cron.log 2>&1")
         measurement_counter= measurement_counter+1
         time1=measurement_counter*10
         os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report TypeB_measurement down {0}".format(time1))	
	
        p = subprocess.Popen("pgrep -af java | grep microbox_measurement.jar", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
	print  output
        if 'microbox_measurement.jar' in output:
         print 'microbox_measurement (Jar)  \033[1;32;40m       running \033[0m'
	 measurement_counter=0
        else:
         print 'microbox_measurement (Jar) is NOT running'
         os.system("/usr/bin/screen -dmS Micro-Box_measurement sudo java -jar /home/netcs/active_monitoring/tests/microbox_measurement.jar  > /home/netcs/measurementjar_cron.log 2>&1")
         measurement_counter= measurement_counter+1
         time1=measurement_counter*10
         os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report Micro-Box_measurement down {0}".format(time1))




        p = subprocess.Popen("pgrep -af java | grep test.jar", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'collectd_all.jar' in output:
         print 'test (Jar)  \033[1;32;40m       running \033[0m'
	 test_counter=0
        else:
         print 'test (Jar) is NOT running'
#        os.system("nohup sudo java -jar /home/netcs/collectd_all.jar &")
         test_counter= test_counter+1
         time1=test_counter*10
         os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report test down {0}".format(time1))


	# Check for zookeeper Service
	p = subprocess.Popen("service zookeeper status", stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	p_status = p.wait()
	if 'active (running)' in output:
	 print 'ZooKeeper  \033[1;32;40m       running \033[0m'
	 zookeeper_counter=0
	else:
	 print 'Zookeper is NOT running'
	 os.system("echo 'Zookeeper is NOT running'")
	 os.system("sudo systemctl start zookeeper")
         zookeeper_counter= zookeeper_counter+1
         time1=zookeeper_counter*10
         os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_app_report zookeeper down {0}".format(time1))

	# Check for influxDB Service
	p = subprocess.Popen("service influxdb status", stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	p_status = p.wait()
	if 'active (running)' in output:
	 print 'influxdb  \033[1;32;40m       running \033[0m'
	else:
	 print 'influxdb is NOT running'
	 os.system("echo 'Influxdb is NOT running'")
	 os.system("sudo systemctl start influxdb")
        time.sleep(300)



#Center Agent reply back to connection request from Box Agent
def func2():
    port = "5556"
    if len(sys.argv) > 1:
       port =  sys.argv[1]
       int(port)
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind("tcp://*:%s" % port)
    while True:
	time.sleep (1)
        print("***************func2*************************")
	message = socket.recv()
	print "func2: Received Box Agent request : ", message
	time.sleep (1)
#    socket.send("Connected with Aget at Center :%s" % port)
	socket.send("func2: Connected with Agent at Center")



# Center Agent check running status of Box Agent
def func3():
    while True:
#     while read line
#      f = open("~/active_monitoring/agent/micro-box-config_short.yaml", "r")
      f = open('/home/netcs/active_monitoring/agent/micro-box-config_short.yaml')
      for line in f:
#	if [[ $line == *"smartx-microbox"* ]]; then
        if "smartx-microbox" in line:
#           box_agent=${line#*=}
           box_agent=line.rsplit('=', 1)[1].rstrip()
	   box_agent_name=line.rsplit('=', 1)[0]
	   time.sleep (1)
           print (box_agent)
#	   ip = ipaddress.ip_address(box_agent.rstrip())
# 	   print (ip)
	   print (box_agent_name)

	print "####################################"
	port = "5557"
	if len(sys.argv) > 1:
	    port =  sys.argv[1]
	    int(port)

	if len(sys.argv) > 2:
	    port1 =  sys.argv[2]
	    int(port1)

	context = zmq.Context()
        time.sleep (1)
	print("***********Checking Micro-Box Agent Status************")
	socket = context.socket(zmq.REQ)
	socket.setsockopt(zmq.LINGER, 0)

	print("box_agent:%s"% box_agent)
	print("port:%s"% port)
#	socket.connect ("tcp://%s:%s"% box_agent % port)
#	socket.connect ('tcp://{0}:{1}'.format(box_agent,port))
	socket.connect ("tcp://{0}:{1}".format(box_agent.rstrip(),port))
	if len(sys.argv) > 2:
	    socket.connect ("tcp://%s:%s"% box_agent % port1)
#	while True:
 	for request in range (1,2):
    	    time.sleep (1)
	    print "Sending status request to Micro-Box:{0}".format(box_agent), request,"..."
	    time.sleep (1)
	    socket.send ("From %s"% BOX_HOSTNAME)
   #  Get the reply.
	    poller = zmq.Poller()
	    poller.register(socket, zmq.POLLIN)
	    if poller.poll(6*1000): # 10s timeout in milliseconds
	      message = socket.recv()
#	      time.sleep (1)
	      print "Box Agent Status ", request, "[" '\x1b[0;30;47m' , message, '\x1b[0m' "]"
	    else:
#	      raise IOError("Timeout processing auth request")
#	      socket.close()
	      print "Box Agent Status" "[" '\x1b[0;37;41m' "Box Agent ",box_agent_name," is NOT Alive"  '\x1b[0m' "]" 
	      os.system("python /home/netcs/active_monitoring/agent/agent_report.py center_agent_report box_agent_status down {0}".format(box_agent_name))
      time.sleep(300)
def func4():
     while True:
	print "####################################"
	time.sleep(2)


if __name__ == '__main__':

#    port = "5556"
#    if len(sys.argv) > 1:
#       port =  sys.argv[1]
#       int(port)
#    context = zmq.Context()
#    socket = context.socket(zmq.REP)
#    socket.bind("tcp://*:%s" % port)

    proc1 = Process(target=func1)
    proc1.start()

    proc2 = Process(target=func2)
    proc2.start()

    proc3 = Process(target=func3)
    proc3.start()

#    proc4 = Process(target=func4)
#    proc4.start()
