#!/bin/bash

IS_GREEN=$(sudo docker ps --format '{{.Ports}}' | grep 8080) # 현재 실행중인 App이 blue인지 확인합니다.

PORT_NUMBER=0
NEXT_DOCKER_COMPOSE_FILE=""
PREV_DOCKER_COMPOSE_FILE=""

if [ -z "$IS_GREEN"  ];then # green 실행되어야 함(현재 blue 실행중)
  PORT_NUMBER=8080
  NEXT_DOCKER_COMPOSE_FILE="docker-compose-green.yaml"
  PREV_DOCKER_COMPOSE_FILE="docker-compose-blue.yaml"
  DOWN_CONTAINER_NAME="layer-api-prod-blue"
else # blue 실행되어야 함(현재 green 실행중)
  PORT_NUMBER=8081
  NEXT_DOCKER_COMPOSE_FILE="docker-compose-blue.yaml"
  PREV_DOCKER_COMPOSE_FILE="docker-compose-green.yaml"
  DOWN_CONTAINER_NAME="layer-api-prod-green"
fi


# 여기서부터 배포 스크립트 시작
echo "1. pull latest image"
sudo docker compose -f $NEXT_DOCKER_COMPOSE_FILE pull

echo "2. container up"
sudo docker compose -f $NEXT_DOCKER_COMPOSE_FILE up -d

while true; do
  echo "3. health check..."
  sleep 3
  REQUEST=$(curl -s http://127.0.0.1:$PORT_NUMBER/actuator/health)
  if echo "$REQUEST" | grep -q '"status":"UP"'; then
    echo "✅ health check success"
    break
  fi
done

echo "4. reload nginx config"
export PROD_PORT=$PORT_NUMBER
sudo envsubst '${PROD_PORT}' < /home/ubuntu/layer-api/infra/nginx.template.conf | sudo tee /etc/nginx/nginx.conf > /dev/null
sudo nginx -s reload

echo "5. remove old container"
sudo docker compose -f $PREV_DOCKER_COMPOSE_FILE rm -s -f $DOWN_CONTAINER_NAME