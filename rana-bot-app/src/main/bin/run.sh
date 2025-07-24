#!/bin/sh
source /etc/profile
JAVA_EXECUTABLE=`which java`
BIN_PATH=$(cd `dirname $0`; pwd)
BASE_PATH="$(cd "${BIN_PATH}/.."; pwd)"

CONF_DIR="${BASE_PATH}/conf/"
LIB_DIR="${BASE_PATH}/lib/"
#构建 CLASSPATH
CLASSPATH=""
for jar in $LIB_DIR*.jar; do
    CLASSPATH=${CLASSPATH}:${jar}
done
CLASSPATH=${CLASSPATH#:}

echo -e "前台启动服务..."
JAVA_OPT="-Xms4g -Xmx8g"
MAIN_CLASS="org.springframework.boot.loader.JarLauncher"
# 使用exec确保Java进程直接被systemd管理
echo "java $JAVA_OPT -classpath $CLASSPATH:$CONF_DIR $MAIN_CLASS"
exec java $JAVA_OPT -classpath $CLASSPATH:$CONF_DIR $MAIN_CLASS