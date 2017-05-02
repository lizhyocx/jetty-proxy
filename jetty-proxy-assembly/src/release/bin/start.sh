#!/bin/sh

echo "start jetty-proxy server"

MAIN_CLASS="com.lizhy.Main"
opt_conf="../conf"
CLEAN_FLAG=1

################################
# functions
################################

run_main() {
  local MAIN_APPLICATION_CLASS

  if [ "$#" -gt 0 ]; then
    MAIN_APPLICATION_CLASS=$1
    shift
  else
    error "Must specify main application class" 1
  fi

  if [ ${CLEAN_FLAG} -ne 0 ]; then
    set -x
  fi
  #$EXEC $JAVA_HOME/bin/java $JAVA_OPTS $MAIN_JAVA_OPTS "${arr_java_props[@]}" -cp "$MAIN_CLASSPATH" \
  #    -Djava.library.path=$MAIN_JAVA_LIBRARY_PATH "$MAIN_APPLICATION_CLASS" "--conf-file conf/avro.conf --name a1"
  $EXEC $JAVA_HOME/bin/java $JAVA_OPTS $MAIN_JAVA_OPTS "${arr_java_props[@]}" -cp "$MAIN_CLASSPATH" \
      -Djava.library.path=$MAIN_JAVA_LIBRARY_PATH "$MAIN_APPLICATION_CLASS" &
}

################################
# main
################################

MAIN_CLASSPATH=""
MAIN_JAVA_LIBRARY_PATH=""
JAVA_OPTS="-Xmx1024M -Xms1024M -Xmn512M -XX:SurvivorRatio=6 -Xss1024k -XX:PermSize=128M -XX:MaxPermSize=256M -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:+CMSClassUnloadingEnabled -XX:+ExplicitGCInvokesConcurrent -XX:CMSInitiatingOccupancyFraction=80"

# make opt_conf absolute
if [[ -n "$opt_conf" && -d "$opt_conf" ]]; then
    opt_conf=$(cd $opt_conf; pwd)
fi

echo "opt_conf=" $opt_conf

if [ -z "${MAIN_HOME}" ]; then
    MAIN_HOME=$(cd $(dirname $0)/..; pwd)
fi
echo "MAIN_HOME=" $MAIN_HOME

# prepend $MAIN_HOME/lib jars to the specified classpath (if any)
if [ -n "${MAIN_CLASSPATH}" ]; then
    MAIN_CLASSPATH="${MAIN_HOME}/lib/*:MAIN_CLASSPATH"
else
    MAIN_CLASSPATH="${MAIN_HOME}/lib/*"
fi

# find java
if [ -z "${JAVA_HOME}" ] ; then
  warn "JAVA_HOME is not set!"
  # Try to use Bigtop to autodetect JAVA_HOME if it's available
  if [ -e /usr/libexec/bigtop-detect-javahome ] ; then
    . /usr/libexec/bigtop-detect-javahome
  elif [ -e /usr/lib/bigtop-utils/bigtop-detect-javahome ] ; then
    . /usr/lib/bigtop-utils/bigtop-detect-javahome
  fi

  # Using java from path if bigtop is not installed or couldn't find it
  if [ -z "${JAVA_HOME}" ] ; then
    JAVA_DEFAULT=$(type -p java)
    [ -n "$JAVA_DEFAULT" ] || error "Unable to find java executable. Is it in your PATH?" 1
    JAVA_HOME=$(cd $(dirname $JAVA_DEFAULT)/..; pwd)
  fi
fi

# prepend conf dir to classpath
if [ -n "$opt_conf" ]; then
  MAIN_CLASSPATH="$opt_conf:$MAIN_CLASSPATH"
fi

# allow dryrun
EXEC="exec"
if [ -n "${opt_dryrun}" ]; then
  warn "Dryrun mode enabled (will not actually initiate startup)"
  EXEC="echo"
fi

# finally, invoke the appropriate command
echo "JAVA_OPTS=" $JAVA_OPTS
echo "MAIN_CLASSPATH=" $MAIN_CLASSPATH
echo "MAIN_JAVA_LIBRARY_PATH" $MAIN_JAVA_LIBRARY_PATH
echo "MAIN_CLASS=" $MAIN_CLASS

run_main $MAIN_CLASS

exit 0