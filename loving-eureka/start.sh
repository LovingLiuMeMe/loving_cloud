echo "===========编译并跳过单元测试===================="
mvn clean package -Dmaven.test.skip=true
echo "============进入target目录==================="
cd target
echo "============运行jar包==================="
java -jar loving-0.0.1-SNAPSHOT.jar
