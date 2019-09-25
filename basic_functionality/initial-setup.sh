#!/bin/bash

# This script must be executed by root user
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

# Allow Passwordless sudo commands
echo "tein ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# Disable swap at the Mirco-Box and comment out the swap line in /etc/fstab
sudo swapoff -a
sudo sed -i "/swap/s/^/#/" /etc/fstab

# Add GIST Visibility Center into /etc/hosts file
echo "103.22.221.56   vc.manage.overcloud" >> /etc/hosts

# Change permissions for FlowAgent
sudo chown -R tein:tein FlowAgent/

# Change hostanme in Snap Telemetry Framework task file
hostname=`hostname`
sudo sed -i "s/hostname/$hostname/g" /opt/snap/tasks/snaptaskconfig.json
sudo systemctl restart snap-telemetry.service

# Update all the packages
sudo apt -y update && sudo apt -y upgrade && sudo apt -y autoremove
