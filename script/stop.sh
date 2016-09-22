#!/bin/bash
if [ -e "application.pid" ]
then
  pkill -TERM -P $(<application.pid)
  rm application.pid
fi
exit 0
