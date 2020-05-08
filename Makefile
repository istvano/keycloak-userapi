.DEFAULT_GOAL := help

SWAGGER_EDITOR_NAME=swagger-editor
SWAGGER_EDITOR_PORT=8080
SWAGGER_UI_PORT=8080
SWAGGER_UI_NAME=swagger-ui

MOCK_NAME=mock-server
MOCK_PORT=4010

API_DEF_PATH=${PWD}
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
	docker run --rm -it -p ${MOCK_PORT}:4010 --name="${MOCK_NAME}" \
	-v ${API_DEF_PATH}/${API_DEF_FILE}:/tmp/${API_DEF_FILE} \
	stoplight/prism:3 mock -d -h 0.0.0.0 "/tmp/${API_DEF_FILE}"

## Test the mock service
.PHONY: test
test:
	curl -s http://127.0.0.1:4010/user/c9f68d07-e7e1-4b3a-9821-2beab218d180  -H "accept: application/json" | jq .


## -- Development --

## Start swagger editor to edit swagger definitions locally
.PHONY: editor
editor:
	docker run --rm -p ${SWAGGER_EDITOR_PORT}:8080 --name="${SWAGGER_EDITOR_NAME}" swaggerapi/swagger-editor
