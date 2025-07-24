#!/bin/sh
source /etc/profile
JAVA_EXECUTABLE=`which java`
#当前文件路径
BIN_PATH=$(cd `dirname $0`; pwd)
#当前项目文件夹路径
BASE_PATH="$(cd "${BIN_PATH}/.."; pwd)"

echo -e "停止服务"
SERVER_PID=$(ps -ef | grep $BASE_PATH | grep java | grep -v grep | awk '{print $2}')
if [ "$SERVER_PID" == "" ];then
   echo -e "server is not running"
else
   echo -e server stop is pid $SERVER_PID
   kill -9 $SERVER_PID
fi