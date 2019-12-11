#!/usr/bin/env bash
echo "============编译打包 user==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 user==================="
docker stop user

echo "============删除容器 user==================="
docker rm user

echo "============删除镜像 user==================="
docker rmi user

echo "============构建镜像 user==================="
docker build -t user .

echo "============启动镜像 api-gateway 端口:4000==================="
docker run --restart=always -p 4000:4000 -t --name user -d user