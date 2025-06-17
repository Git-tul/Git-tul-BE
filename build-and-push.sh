#!/bin/bash

set -e

# 설정
APP_NAME="gittul-be"
IMAGE_NAME="ghcr.io/git-tul/${APP_NAME}"
TAG=$(date +%Y%m%d-%H%M%S)

echo "Building Docker image..."
docker build -t ${IMAGE_NAME}:"${TAG}" .

docker tag ${IMAGE_NAME}:"${TAG}" ${IMAGE_NAME}:latest

echo "Pushing Docker image to GHCR..."
docker push ${IMAGE_NAME}:"${TAG}"
docker push ${IMAGE_NAME}:latest

echo ""
echo "GHCR Image pushed:"
echo ${IMAGE_NAME}:${TAG}
