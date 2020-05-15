.DEFAULT_GOAL := help

SWAGGER_EDITOR_PORT=80
MOCK_NAME=restapi
MOCK_PORT=4010
KEYCLOAK_PORT=8080
KEYCLOAK_MAN_PORT=9990
KEYCLOAK_NAME=keycloak-server
NETWORK_NAME=localhost.com

API_DEF_PATH=${PWD}/rest-api-schema/src/main/resources
API_DEF_FILE=openapi.yaml

## -- HELP --

## This help message
## Which can also be multiline
.PHONY: help
help:
	@printf "Usage\n";

	@awk '{ \
			if ($$0 ~ /^.PHONY: [a-zA-Z\-\_0-9]+$$/) { \
				helpCommand = substr($$0, index($$0, ":") + 2); \
				if (helpMessage) { \
					printf "\033[36m%-20s\033[0m %s\n", \
						helpCommand, helpMessage; \
					helpMessage = ""; \
				} \
			} else if ($$0 ~ /^[a-zA-Z\-\_0-9.]+:/) { \
				helpCommand = substr($$0, 0, index($$0, ":")); \
				if (helpMessage) { \
					printf "\033[36m%-20s\033[0m %s\n", \
						helpCommand, helpMessage; \
					helpMessage = ""; \
				} \
			} else if ($$0 ~ /^##/) { \
				if (helpMessage) { \
					helpMessage = helpMessage"\n                     "substr($$0, 3); \
				} else { \
					helpMessage = substr($$0, 3); \
				} \
			} else { \
				if (helpMessage) { \
					print "\n                     "helpMessage"\n" \
				} \
				helpMessage = ""; \
			} \
		}' \
		$(MAKEFILE_LIST)

## -- Mock --

## Start the mock container
.PHONY: mock
mock:
	docker run --rm -it -p ${MOCK_PORT}:4010 \
	--network ${NETWORK_NAME}	\
	--name ${MOCK_NAME} \
	-v ${API_DEF_PATH}/${API_DEF_FILE}:/tmp/${API_DEF_FILE} \
	stoplight/prism:3 mock -d -h 0.0.0.0 "/tmp/${API_DEF_FILE}"

## Test the mock service
.PHONY: test
test:
	curl -s http://127.0.0.1:4010/user/c9f68d07-e7e1-4b3a-9821-2beab218d180  -H "accept: application/json" | jq .


## -- Swagger --

## Start swagger editor to edit swagger definitions locally
.PHONY: editor
editor:
	docker run --rm -p ${SWAGGER_EDITOR_PORT}:8080 swaggerapi/swagger-editor

## -- Keycloak --

## Start keycloak server
.PHONY: up
up:
	docker run -it --rm -p ${KEYCLOAK_PORT}:8080 -p ${KEYCLOAK_MAN_PORT}:9990 -p 5005:5005 \
	--network ${NETWORK_NAME} \
	--name ${KEYCLOAK_NAME} \
	-v $(PWD)/src/main/resources/demo-realm.json:/tmp/demo-realm.json \
  -v $(PWD)/src/main/resources/cli:/opt/jboss/startup-scripts \
	-e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin \
  -e KEYCLOAK_STATISTICS=all \
  -e KEYCLOAK_LOGLEVEL=DEBUG \
  -e JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,address=*:5005,suspend=n \
	urbanandco/keycloak-training:10.0.0

## Create realm into keycloak
.PHONY: create
create:
	docker exec -it ${KEYCLOAK_NAME} \
	/opt/jboss/keycloak/bin/kcadm.sh create realms -s enabled=true -f /tmp/demo-realm.json --no-config --server http://localhost:8080/auth --realm master --user admin --password admin

## Build local project modules
.PHONY: install
install:
	(mvn clean install -DskipTests)

## Build local keycloak providers only
.PHONY: install-providers
install-providers:
	(cd ./jar-module && mvn clean install -DskipTests)

## Deploy ear module to keycloak.
.PHONY: deploy
deploy: install-providers
	(cd ./ear-module && mvn clean install -DskipTests wildfly:deploy -Dwildfly.username=keycloak -Dwildfly.password=keycloak)

## Re-deploy ear module to keycloak.
.PHONY: re-deploy
re-deploy:
	(cd ./ear-module && mvn install -DskipTests wildfly:deploy -Dwildfly.username=keycloak -Dwildfly.password=keycloak)

## Reload already deployed module in keycloak
.PHONY: reload
reload: install-providers re-deploy

## -- Useful --

.PHONY: log-on
log-on:
	@echo "Account url http://localhost:${KEYCLOAK_PORT}/auth/realms/demo/account/"

## SSH into keycloak container
.PHONY: ssh
ssh:
	docker exec -it ${KEYCLOAK_NAME} bash

## Show info about the project
.PHONY: info
info:
	@echo "Account url http://localhost:${KEYCLOAK_PORT}/auth/realms/demo/account/"
	@echo "Account url http://localhost:${KEYCLOAK_PORT}/auth/realms/demo/protocol/openid-connect/logout"



