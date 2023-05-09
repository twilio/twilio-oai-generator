.PHONY: install test generate test-docker

install:
	mvn clean package -DskipTests

test:
	mvn clean test

generate: install
	bash scripts/generate.sh

# for specific language use LANGUAGES for ex: make test-docker LANGUAGES=node
test-docker: generate
	bash scripts/prism.sh
