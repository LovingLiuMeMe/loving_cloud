#!/usr/bin/env bash
echo "============编译打包 order==================="
mvn clean package -Dmaven.test.skip=true -U

echo "============停止容器 order==================="
docker stop order

echo "============删除容器 order==================="
docker rm order

echo "============删除镜像 order==================="
docker rmi order

echo "============构建镜像 order==================="
docker build -t order .

echo "============启动镜像 api-gateway 端口:2000==================="
docker run --restart=always -p 2000:2000 -t --name order -d order