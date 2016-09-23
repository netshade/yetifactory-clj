#!/bin/bash
/etc/init.d/codedeploy-agent restart
exec script/start.sh
