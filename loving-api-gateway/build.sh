#!/usr/bin/env bash
echo "============编译打包 api-gateway==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 api-gateway==================="
docker stop api-gateway

echo "============删除容器 api-gateway==================="
docker rm api-gateway

echo "============删除镜像 api-gateway==================="
docker rmi api-gateway

echo "============构建镜像 api-gateway==================="
docker build -t api-gateway .

echo "============启动镜像 api-gateway 端口:8000==================="
docker run --restart=always -p 8000:8000 -t --name api-gateway -d api-gateway