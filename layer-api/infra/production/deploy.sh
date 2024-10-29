#!/bin/bash

IS_GREEN=$(docker ps | grep green) # 현재 실행중인 App이 blue인지 확인합니다.
DEFAULT_CONF=" /etc/nginx/nginx.conf"

if [ -z $IS_GREEN  ];then # blue라면

  echo "### BLUE => GREEN ###"


  echo "1. green container up"
  docker-compose run --name green --detach clean01/layer-server_layer-api:latest

  while [ 1 = 1 ]; do
  echo "2. green health check..."
  sleep 3

  REQUEST=$(curl http://127.0.0.1:8080) # green으로 request
    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
            echo "health check success"
            break ;
            fi
  done;

  echo "3. reload nginx"
  sudo cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf
  sudo nginx -s rel

  echo "4. blue container down"
  docker-compose stop blue
else
  echo "### GREEN => BLUE ###"
  echo "1. blue container up"
  docker-compose run --name blue --detach clean01/layer-server_layer-api:latest


  while [ 1 = 1 ]; do
    echo "2. blue health check..."
    sleep 3
    REQUEST=$(curl http://127.0.0.1:8081) # blue로 request

    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    fi
  done;

  echo "3. reload nginx"
  sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "4. green container down"
  docker-compose stop green
fi