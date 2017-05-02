#!/bin/sh
pid=`ps -ef|grep jetty-proxy|grep -v grep|grep -v "start.sh"|awk '{print $2}'`
pss=`ps -ef|grep jetty-proxy|grep -v grep|grep -v "start.sh"|wc -l`

if [ $pss -eq "0" ]; then
    echo "jetty-proxy server has not start"
else
    echo "jetty-proxy server started,shutdown..."
    kill $pid
    echo "jetty-proxy shutdown"
fi