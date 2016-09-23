#!/bin/bash
if [ -e "application.pid" ]
then
  kill -TERM $(<application.pid)
  rm application.pid
fi
exit 0
