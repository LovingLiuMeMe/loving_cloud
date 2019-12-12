<<<<<<< HEAD
echo "===========编译并跳过单元测试===================="
mvn clean package -Dmaven.test.skip=true
=======
echo "===========编译安装 并跳过单元测试===================="
mvn clean install -Dmaven.test.skip=true
>>>>>>> 315060cae65f6c4ff291604aa149344303af89b5
echo "============进入target目录==================="
cd product-server/target
echo "============运行jar包==================="
nohup java -jar loving-product-server-0.0.1-SNAPSHOT.jar > log.file 2>&1 &

