#!/bin/bash

IS_DEV_GREEN=$(sudo docker ps --format '{{.Ports}}' | grep -E '\b8090->8080\b') # 현재 실행중인 App이 blue인지 확인합니다.
IS_PROD_GREEN=$(sudo docker ps --format '{{.Ports}}' | grep -E '\b8080->8080\b') # 현재 실행중인 App이 blue인지 확인합니다.

DEV_PORT=0
PROD_PORT=0
NEXT_DOCKER_COMPOSE_FILE=""
PREV_DOCKER_COMPOSE_FILE=""

if [ -z "$IS_DEV_GREEN"  ];then # green 실행되어야 함(현재 blue 실행중)
  DEV_PORT=8090
  NEXT_DOCKER_COMPOSE_FILE="docker-compose-green.yaml"
  PREV_DOCKER_COMPOSE_FILE="docker-compose-blue.yaml"
  DOWN_CONTAINER_NAME="layer-api-dev-blue"
else # blue 실행되어야 함(현재 green 실행중)
  DEV_PORT=8091
  NEXT_DOCKER_COMPOSE_FILE="docker-compose-blue.yaml"
  PREV_DOCKER_COMPOSE_FILE="docker-compose-green.yaml"
  DOWN_CONTAINER_NAME="layer-api-dev-green"
fi

if [ -z "$IS_PROD_GREEN"  ];then # 현재 prod 는 blue 실행중 -> 그대로 유지
  PROD_PORT=8081
else # 현재 prod 는 green 실행중 -> 그대로 유지
  PROD_PORT=8080
fi

# 여기서부터 배포 스크립트 시작
echo "1. pull latest image"
sudo docker compose -f $NEXT_DOCKER_COMPOSE_FILE pull

echo "2. container up"
sudo docker compose -f $NEXT_DOCKER_COMPOSE_FILE up -d

while true; do
  echo "3. health check..."
  sleep 3
  REQUEST=$(curl -s http://127.0.0.1:$DEV_PORT/actuator/health)
  if echo "$REQUEST" | grep -q '"status":"UP"'; then
    echo "✅ health check success"
    break
  fi
done

echo "4. reload nginx config"

export DEV_PORT=$DEV_PORT
export PROD_PORT=$PROD_PORT
envsubst '${DEV_PORT} ${PROD_PORT}' < /home/ubuntu/layer-api/infra/nginx.template.conf | sudo tee /etc/nginx/nginx.conf > /dev/null

sudo nginx -s reload

echo "5. remove old container"
sudo docker compose -f $PREV_DOCKER_COMPOSE_FILE rm -s -f $DOWN_CONTAINER_NAME