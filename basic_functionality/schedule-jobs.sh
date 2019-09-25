#!/bin/bash

# Add scheduled jobs in crontab
{ crontab -l -u tein; echo '*/180 * * * * /bin/bash /opt/snap/snapChecker.sh'; } | crontab -u tein - 
{ crontab -l -u tein; echo '*/5 * * * * /bin/bash /opt/FlowAgent/transfer_iovisor_data.sh'; } | crontab -u tein -
{ crontab -l -u tein; echo '0 21 * * * /bin/bash /home/tein/active_monitoring/bandwidth-check.sh'; } | crontab -u tein -
{ crontab -l -u tein; echo '*/10 * * * * /bin/bash /home/tein/active_monitoring/ping_check_1.sh'; } | crontab -u tein -
sudo /etc/init.d/cron restart
