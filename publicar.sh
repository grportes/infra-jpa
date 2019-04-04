#!/bin/bash

clear

if [ -d target ]; then
  echo "removendo pasta target"
  rm -rf target
fi

echo "Gerando artefatos..."
./sbt clean
./sbt reload
./sbt compile
./sbt publishLocal

echo "ok!!"
