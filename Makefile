.DEFAULT_GOAL := help

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
	docker-compose -f docker-compose-api.yml up

## Test the mock service
.PHONY: test
test:
	curl -s http://127.0.0.1:4010/user/c9f68d07-e7e1-4b3a-9821-2beab218d180  -H "accept: application/json" | jq .

## -- Swagger --

## Start swagger editor to edit swagger definitions locally
.PHONY: editor
editor:
	docker-compose -f docker-compose-swagger.yml up

## -- Docker --

## Start full stack with MySql
.PHONY: up
up:
	docker-compose up

## Start Keycloak and the API only
.PHONY: dev
dev:
	docker-compose -f docker-compose-dev.yml up

## -- Development --

## Create realm into keycloak
.PHONY: create-realm
create-realm:
	docker-compose exec keycloak \
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

## SSH into keycloak container
.PHONY: ssh
ssh:
	docker-compose exec keycloak -it bash

## Show info about the project
.PHONY: info
info:
	@echo "Account url http://localhost/auth/realms/demo/account/"
	@echo "Account url http://localhost/auth/realms/demo/protocol/openid-connect/logout"



