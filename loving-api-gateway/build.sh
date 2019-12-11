#!/usr/bin/env bash
echo "============编译打包eureka-server==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 eureka-server==================="
docker stop eureka-server

echo "============删除容器 eureka-server==================="
docker rm eureka-server

echo "============删除镜像 eureka-server==================="
docker rmi eureka-server

echo "============构建镜像 eureka-server==================="
docker build -t eureka-server .

echo "============启动镜像 eureka-server 端口:1000==================="
docker run --restart=always -p 1000:1000 -t --name eureka-server -d eureka-server