echo "===========编译并跳过单元测试===================="
mvn clean package -Dmaven.test.skip=true
echo "============进入target目录==================="
cd order-server/target
echo "============运行jar包==================="
nohup java -jar loving-order-server-0.0.1-SNAPSHOT.jar > log.file 2>&1 &
