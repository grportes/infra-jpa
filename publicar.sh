#!/bin/bash

clear

if [ -d target ]; then
  echo "removendo pasta target"
  rm -rf target
fi

echo "Gerando artefatos..."
./sbt clean reload compile
./sbt publishLocal

echo "ok!!"
