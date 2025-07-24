#!/bin/sh
source /etc/profile
JAVA_EXECUTABLE=`which java`
BIN_PATH=$(cd `dirname $0`; pwd)
BASE_PATH="$(cd "${BIN_PATH}/.."; pwd)"

echo -e "停止服务"
SERVER_PID=$(ps -ef | grep $BASE_PATH | grep java | grep -v grep | awk '{print $2}')
if [ "$SERVER_PID" == "" ];then
   echo -e "server is not running"
else
   echo -e server is running,pid is $SERVER_PID
   kill -9 $SERVER_PID
fi

sleep 2

CONF_DIR="${BASE_PATH}/conf/"
LIB_DIR="${BASE_PATH}/lib/"
#构建 CLASSPATH，注意：使用冒号 ':' 作为路径分隔符
CLASSPATH=""
for jar in $LIB_DIR*.jar; do
    CLASSPATH=${CLASSPATH}:${jar}
done
#移除领先的冒号
CLASSPATH=${CLASSPATH#:}

echo -e "启动服务"
JAVA_OPT="-Xms4g -Xmx8g"
MAIN_CLASS="org.springframework.boot.loader.JarLauncher"
EXEC_CMD="$JAVA_EXECUTABLE $JAVA_OPT -javaagent:$CLASSPATH -classpath $CLASSPATH:$CONF_DIR $MAIN_CLASS"
nohup $EXEC_CMD >/dev/null 2>&1 &

sleep 2

#检查是否启动
SERVER_PID=$(ps -ef | grep $BASE_PATH | grep java | grep -v grep | awk '{print $2}')
if [ "$SERVER_PID" == "" ];then
   echo -e "server is not running"
else
   echo -e server is running,pid is $SERVER_PID
fi