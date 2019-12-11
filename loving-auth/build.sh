#!/usr/bin/env bash
echo "============编译打包 auth==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 auth==================="
docker stop auth

echo "============删除容器 auth==================="
docker rm auth

echo "============删除镜像 auth==================="
docker rmi auth

echo "============构建镜像 auth==================="
docker build -t auth .

echo "============启动镜像 api-gateway 端口:6000==================="
docker run --restart=always -p 6000:6000 -t --name auth -d auth