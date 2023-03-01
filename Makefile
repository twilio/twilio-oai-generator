.PHONY: install test generate test-docker

install:
	mvn clean package -DskipTests

test:
	mvn clean test

generate: install
	bash scripts/generate.sh

test-docker: generate
	bash scripts/prism.sh
