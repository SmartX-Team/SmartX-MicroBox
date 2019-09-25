#!/usr/bin/python
from multiprocessing import Process
import sys
import time, threading
import zmq
import socket
from subprocess import call
import subprocess
import os

rocket = 0
snap_counter=0
collectd_counter=0
zookeeper_counter=0
data_plane_counter=0
management_plane_tracing_counter=0
control_plane_counter=0
perfsonar_counter=0
BOX_HOSTNAME = socket.gethostname()
ssh_counter=0
docker_counter=0
snapd_counter=0
down_time=0
def func1(): 
    time_=" "+time.ctime()
    print time_
    global rocket
    global management_plane_tracing_counter
    global ssh_counter
    global docker_counter
    global collectd_counter
    global zookeeper_counter
    global snapd_counter
    global down_time
    print 'start func1'
    while rocket < sys.maxint:
        rocket += 1
	time.sleep (3)
	print ('func1:%d'% rocket)

#########################check for snapd service############################################
        p = subprocess.Popen("sudo systemctl status snapd.service", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'active (running)' in output:
           print '\n\033[1;33;40m snapd \033[0m                         is \033[1;32;40m       running \033[0m'
	   snapd_counter=0
        else:
	   print '\n\033[1;33;40m snapd \033[0m                       is not \033[1;31;40m        running \033[0m'
           os.system("sudo systemctl start snapd.service")
           snapd_counter=snapd_counter+1
           print snapd_counter
           if snapd_counter>=2:
	    down_time=snapd_counter*10
            print"snapd is not running for {0} minutes".format(down_time)
            os.system("python /opt/agent/agent_report.py {0} agent_report snapd service_down {1}".format(BOX_HOSTNAME,down_time))

#########################check for ssh service############################################
        p = subprocess.Popen("sudo systemctl status ssh.service", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'active (running)' in output:
           print '\n\033[1;33;40m ssh \033[0m                         is \033[1;32;40m       running \033[0m'
           ssh_counter=0
        else:
           print '\n\033[1;33;40m ssh \033[0m                       is not \033[1;31;40m        running \033[0m'
           os.system("sudo systemctl start ssh.service")
           ssh_counter=ssh_counter+1
           print ssh_counter
           if ssh_counter>=2:
            down_time=ssh_counter*10
            print"ssh is not running for {0} minutes".format(down_time)
            os.system("python /opt/agent/agent_report.py {0} agent_report ssh service_down {1}".format(BOX_HOSTNAME,down_time))


#########################check for docker service##########################################
        p = subprocess.Popen("sudo systemctl status docker.service", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'active (running)' in output:
           print '\n\033[1;33;40m docker \033[0m                      is \033[1;32;40m       running \033[0m'
           docker_counter=0
        else:
           print '\n\033[1;33;40m docker \033[0m                       is not \033[1;31;40m        running \033[0m'
           os.system("sudo systemctl start docker.service")
           docker_counter=docker_counter+1
           print docker_counter
           if docker_counter>=2:
            down_time=docker_counter*10
            print"docker is not running for {0} minutes".format(down_time)
            os.system("python /opt/agent/agent_report.py {0} agent_report docker service_down {1}".format(BOX_HOSTNAME,down_time))


#########################check for management-plane-tracing service##########################################
        p = subprocess.Popen("sudo systemctl status  management-plane-tracing.service", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'active (running)' in output:
           print '\n\033[1;33;40m  management-plane-tracing \033[0m   is \033[1;32;40m       running \033[0m'
           management_plane_tracing_counter=0
        else:
           print '\n\033[1;33;40m  management-plane-tracing \033[0m                       is not \033[1;31;40m        running \033[0m'
           os.system("sudo systemctl start  management-plane-tracing.service")
           management_plane_tracing_counter=management_plane_tracing_counter+1
           print management_plane_tracing_counter
           if management_plane_tracing_counter>=2:
            down_time=management_plane_tracing_counter*10
            print"management_plane_tracing is not running for {0} minutes".format(down_time)
            os.system("python /opt/agent/agent_report.py {0} agent_report management_plane_tracing_counter service_down {1}".format(BOX_HOSTNAME,down_time))


#########################check for collectd service##########################################
	p = subprocess.Popen("sudo systemctl status collectd.service", stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	p_status = p.wait()
#    print output
        if 'active (running)' in output:
          print '\n\033[1;33;40m collectd \033[0m                   is \033[1;32;40m        running \033[0m'
          collectd_counter=0
        else:
           print '\n\033[1;33;40m collectd \033[0m                     is \033[1;31;40m     NOT running \033[0m'
           os.system("sudo systemctl start collectd.service")
           collectd_counter=collectd_counter+1
           print collectd_counter
        if collectd_counter==2:
           print"collectd is not running for ? minutes"
           os.system("python /opt/agent/agent_report.py {0} agent_report collectd service_down {1}".format(BOX_HOSTNAME,time_))
           collectd_counter=0
##########################check for Zookeeper service#########################################
        p = subprocess.Popen("service zookeeper status", stdout=subprocess.PIPE, shell=True)
        (output, err) = p.communicate()
        p_status = p.wait()
        if 'active (running)' in output:
           print '\n\033[1;33;40m zookeeper \033[0m                   is \033[1;32;40m       running \033[0m'
           zookeeper_counter=0
        else:
           print '\n\033[1;33;40m zookeeper \033[0m                       is not \033[1;31;40m        running \033[0m'
           os.system("sudo systemctl start zookeeper.service")
           zookeeper_counter=zookeeper_counter+1
           print zookeeper_counter
           if zookeeper_counter>=2:
            down_time=zookeeper_counter*10
            print"zookeeper is not running for {0} minutes".format(down_time)
            os.system("python /opt/agent/agent_report.py {0} agent_report zookeeper service_down {1}".format(BOX_HOSTNAME,down_time))
        time.sleep(30)
    print 'end func1'

def func2():
    global rocket
    print 'start func2'
    while rocket < sys.maxint:
        rocket += 1
	time.sleep (1)
        print ('func2:%d'% rocket)
	port = "5557"
        if len(sys.argv) > 1:
         port =  sys.argv[1]
         int(port)
        context = zmq.Context()
        socket = context.socket(zmq.REP)
        socket.bind("tcp://*:%s" % port)
#       socket.setblocking(False)
#       while True:
        time.sleep (1)
        print("---------------func2-------------------------")
        message = socket.recv()
        print "**func2**Received Status request from Center Agent: ", message
        time.sleep (0)
#    socket.send("Connected with Aget at Center :%s" % port)
        socket.send("Box Agent %s is Alive "% BOX_HOSTNAME)
    print 'end func2'

if __name__=='__main__':
    p1 = Process(target = func1)
    p1.start()
    p2 = Process(target = func2)
    p2.start()
