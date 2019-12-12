#!/usr/bin/env bash
echo "============编译打包 activity==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 activity==================="
docker stop activity

echo "============删除容器 activity==================="
docker rm activity

echo "============删除镜像 activity==================="
docker rmi activity

echo "============构建镜像 auth==================="
docker build -t activity .

echo "============启动镜像 api-gateway 端口:6000==================="
docker run --restart=always -p 6000:6000 -t --name auth -d auth