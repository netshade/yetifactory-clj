#!/bin/bash
if [ -e "/tmp/ENV" ]
then
  source /tmp/ENV
fi
cd /app
exec lein with-profile production trampoline run -m yetifactory.core 
exit 0
