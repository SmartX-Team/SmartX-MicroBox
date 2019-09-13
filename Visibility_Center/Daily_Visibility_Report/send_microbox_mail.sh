#!/bin/sh
#now="$(date +'%Y%m%d')"
now="$( date +%Y%m%d -d "yesterday")"
#now="20180823"
export REPLYTO=ahmad@smartx.kr
#mail  -a "From:ahmad@smartx.kr"  -a "Content-Type:text/html"  -s "[$now] Testing of Daily Report for Active/Passive Monitoring" -t jongwon@smartx.kr,usman@smartx.kr,ahmad@smartx.kr < /home/netcs/active_monitoring/Daily-report-data.csv

#mail  -a "From:ahmad@smartx.kr"  -a "Content-Type:text/html"  -s "[$now] Converged OF@TEIN+/KOREN Testing of Daily(24h) Report for Active/Passive Monitoring & Collection" -t Ops@oftein.net < /home/netcs/active_monitoring/Daily-report-data.csv
mail  -a  "Content-Type:text/html"  -s "[$now] Converged (OF@TEIN+/KOREN) Playground: Daily Visibility Report" -t Ops@oftein.net < /home/netcs/active_monitoring/result1.html
mail  -a  "Content-Type:text/html"  -s "[$now] Micro-Box (OF@TEIN+) Playground: Daily Visibility Report" -t Ops@oftein.net < /home/netcs/active_monitoring/result2.html


#mail  -a "From:ahmad@smartx.kr"  -a "Content-Type:text/html"  -s "[$now]  Converged (OF@TEIN+/KOREN) Playground: Daily Visibility Report" -t ahmad@smartx.kr < /home/netcs/active_monitoring/result.html
