#!/bin/bash

LOCAL=~/.ivy2/local/br.com.infra/infra-jpa_2.12

clear

if [ -d $LOCAL ]; then
  echo "removendo repositorio local: $LOCAL"
  rm -rf $LOCAL
fi

if [ -d target ]; then
  echo "removendo pasta target"
  rm -rf target
fi

echo "Gerando artefatos..."
./sbt clean compile publishLocal

echo "ok!!"
