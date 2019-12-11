#!/usr/bin/env bash
echo "============编译打包config==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 config==================="
docker stop config

echo "============删除容器 config==================="
docker rm config

echo "============删除镜像 config==================="
docker rmi config

echo "============构建镜像 config==================="
docker build -t config .

echo "============启动镜像 eureka-server 端口:9000==================="
docker run --restart=always -p 9000:9000 -t --name config -d config