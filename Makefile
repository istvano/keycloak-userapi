.DEFAULT_GOAL := help

## -- HELP --

## This help message
## Which can also be multiline
.PHONY: help
help:
	@printf "Usage\n";

	@awk '{ \
			if ($$0 ~ /^.PHONY: [a-zA-Z\-\/\_0-9]+$$/) { \
				helpCommand = substr($$0, index($$0, ":") + 2); \
				if (helpMessage) { \
					printf "\033[36m%-20s\033[0m %s\n", \
						helpCommand, helpMessage; \
					helpMessage = ""; \
				} \
			} else if ($$0 ~ /^[a-zA-Z\-\/\_0-9.]+:/) { \
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
.PHONY: mock/up
mock/up:
	docker-compose -f docker-compose-api.yml up

## Test the mock service
.PHONY: mock/test
mock/test:
	curl -s http://127.0.0.1:4010/users/7aaddb11-1a6c-2e39-8de5-604cd4a14fef -H "accept: application/json" | jq .

## -- Docker --

## Start full stack with MySql
.PHONY: stack/up
stack/up:
	docker-compose up

## SSH into keycloak container
.PHONY: stack/ssh-kc
stack/ssh-kc:
	docker-compose exec keycloak bash

## -- Development --

## Create realm into keycloak
.PHONY: realm/create
realm/create:
	docker-compose exec keycloak \
	/opt/jboss/keycloak/bin/kcadm.sh create realms -s enabled=true -f /tmp/demo-realm.json --no-config --server http://localhost:8080/auth --realm master --user admin --password admin

## Delete realm into keycloak
.PHONY: realm/delete
realm/delete:
	docker-compose exec keycloak \
	/opt/jboss/keycloak/bin/kcadm.sh delete realms/demo --no-config --server http://localhost:8080/auth --realm master --user admin --password admin

## Delete realm into keycloak
.PHONY: realm/reload
realm/reload: realm/delete realm/create

## Build local project modules
.PHONY: mvn/install
mvn/install:
	(mvn clean install -DskipTests)

## Build user storage providers only
.PHONY: mvn/install-user-provider
mvn/install-user-provider:
	(cd ./user-provider-jar-module && mvn clean install -DskipTests)

## Deploy user storage providers only
.PHONY: mvn/deploy-user-provider
mvn/deploy-user-provider:
	(cd ./user-provider-ear-module && mvn clean wildfly:deploy -Dwildfly.username=keycloak -Dwildfly.password=keycloak)

## Start Keycloak and the API only
.PHONY: debug
debug:
	docker-compose -f docker-compose-dev.yml up

## Show openid info page
.PHONY: kc/info
kc/info:
	curl -v -k http://localhost:8080/auth/realms/demo/.well-known/openid-configuration  | jq '.'

## Get a token using password
.PHONY: kc/login
kc/login:
	curl -s -k --data 'username=alice&password=password&grant_type=password&client_id=demo-client&client_secret=8a899f1a-e391-4bbf-b29f-35f103fda84d' http://localhost:8080/auth/realms/demo/protocol/openid-connect/token |  jq

## -- Tools --

## Start swagger editor to edit swagger definitions locally
.PHONY: api/editor
api/editor:
	docker-compose -f docker-compose-swagger.yml up

## Show info about the project
.PHONY: info
info:
	@echo "Account url http://localhost/auth/realms/demo/account/"
	@echo "Account url http://localhost/auth/realms/demo/protocol/openid-connect/logout"



