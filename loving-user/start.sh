echo "===========编译安装 并跳过单元测试===================="
mvn clean install -Dmaven.test.skip=true
echo "============进入target目录==================="
cd user-server/target
echo "============运行jar包==================="
nohup java -jar loving-user-server-0.0.1-SNAPSHOT.jar > log.file 2>&1 &

