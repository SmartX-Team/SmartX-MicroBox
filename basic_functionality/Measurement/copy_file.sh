#!/bin/bash
input="ips1.txt"
PASS="fn!xo!ska!"
echo $1
if [ "$1" != "copy" ]; then
 if [ "$1" !="move" ]; then
   echo "Command usage (1) ./copy_file.sh move filename (2) ./copy_file.sh ssh filename"
fi
fi
echo $2
#while IFS= read -r line
while IFS=$' \t\n\r' read -r line
#while read line
do
  if [ -n "$line" ]; then
  result3=$line
  SUBSTR=$(echo $result3 | cut -d'=' -f 2)  
  fi
  echo $SUBSTR
  if [ "$SUBSTR" != "0.0.0.0" ]; then
   if ping -c 1 -W 1 "$SUBSTR"; then
    echo "$SUBSTR is alive"
    if [ "$1" == "copy" ]; then
     sudo sshpass -p $PASS scp $2 tein@$SUBSTR:
     echo "file copied"
     ssh tein@"$SUBSTR" "sudo cp $2 /opt/FlowAgent/" < /dev/null
     echo "file moved"
    elif [ "$1" == "ssh" ]; then
      ssh tein@"$SUBSTR" "cd /opt/measurement/;./$2" < /dev/null
      echo $RESULT1
      echo "Done"
    else
      echo "Command usage (1) ./copy_file.sh move filename (2) ./copy_file.sh ssh filename"
    fi
   else
    echo "$SUBSTR is not alive"
   echo "Done"
  fi
  fi
done <  "$input"

#  if [ "$SUBSTR" != "0.0.0.0" ]; then
#   sudo sshpass -p $PASS scp $1 tein@$SUBSTR:
#   echo "file copied"
#   ssh tein@"$SUBSTR" "sudo cp $1 /opt/measurement/" < /dev/null
#   echo "file moved"
#   ssh tein@"$SUBSTR" "cd /opt/measurement/;./$1" < /dev/null  
#   echo $RESULT1
#   echo "Done"
#  fi
#done <  "$input"

