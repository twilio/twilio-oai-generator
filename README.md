# OpenAPI Generator for Twilio helper libraries
[![Tests](https://github.com/twilio/twilio-oai-generator/actions/workflows/test.yml/badge.svg)](https://github.com/twilio/twilio-oai-generator/actions/workflows/test.yml)

## Overview
This is a boiler-plate project to generate your own project derived from an OpenAPI specification.

Its goal is to get you started with the basic plumbing, so you can put in your own logic. It won't work without your changes applied. Continue reading this doc to get more details on how to do that.

## What's OpenAPI
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.

When properly described with OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more.

## How do I use this?
`Note: The instructions below will generate the twilio-go library. These instructions can also be used interchangeably to generate other language libraries.`

Clone this repo into your local machine. It will include:

```
.
|- README.md    // this file
|- pom.xml      // build script
|-- src
|--- main
|---- java
|----- com.twilio.oai.TwilioGoGenerator.java // generator file
|---- resources
|----- twilio-go // template files
|----- META-INF
|------ services
|------- org.openapitools.codegen.CodegenConfig
```

You _will_ need to make changes in at least the following:

`TwilioGoGenerator.java`

Templates in this folder:

`src/main/resources/twilio-go`

Once modified, you can run this:

```
mvn package
```

In your generator project. A single jar file will be produced in `target`. You can now use that with [OpenAPI Generator](https://openapi-generator.tech):

### For mac/linux:
```
java -cp /path/to/openapi-generator-cli.jar:/path/to/your.jar org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i /path/to/openapi.yaml -o ./test
```

(Do not forget to replace the values `/path/to/openapi-generator-cli.jar`, `/path/to/your.jar` and `/path/to/openapi.yaml` in the previous command)

Here is an example script to generate [twilio-go](https://github.com/twilio/twilio-go) from our [OpenAPI specification](https://github.com/twilio/twilio-oai): [build_twilio_go.py](./examples/build_twilio_go.py).

### For Windows
You will need to use `;` instead of `:` in the classpath, e.g.
```
java -cp /path/to/openapi-generator-cli.jar;/path/to/your.jar org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i /path/to/openapi.yaml -o ./test
```

Now your templates are available to the client generator, and you can write output values.

## But how do I modify this?
The `TwilioGoGenerator.java` has comments in it--lots of comments.  There is no good substitute for reading the code more, though.  See how the `TwilioGoGenerator` implements `CodegenConfig`. That class has the signature of all values that can be overridden.

You can also step through TwilioGoGenerator.java in a debugger.  Just debug the JUnit test in DebugCodegenLauncher. That runs the command line tool and lets you inspect what the code is doing.

For the templates themselves, you have a number of values available to you for generation. You can execute the `java` command from above while passing different debug flags to show the object you have available during client generation:

```
# The following additional debug options are available for all codegen targets:
# -DdebugOpenAPI prints the OpenAPI Specification as interpreted by the codegen
# -DdebugModels prints models passed to the template engine
# -DdebugOperations prints operations passed to the template engine
# -DdebugSupportingFiles prints additional data passed to the template engine

java -DdebugOperations -cp /path/to/openapi-generator-cli.jar:/path/to/your.jar org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i /path/to/openapi.yaml -o ./test
```

Will, for example, output the debug info for operations.
You can use this info in the `api.mustache` file.

## Local Test Setup for twilio-go
To run the unit tests or integration tests locally you need complete the following steps:
1. Install the [prism CLI](https://meta.stoplight.io/docs/prism/docs/getting-started/01-installation.md) to run the integration tests locally.
2. Run `make test-docker` from the root directory.
3. Navigate to the `twilio_api_v2010.yaml` located in `examples` and in a new terminal tab run the command `prism mock twilio_api_v2010.yaml`.
4. In `examples/go/go-client/helper/rest/api/v2010/api_test.go` TestMain function change `testClient.BaseURL = "http://prism_twilio:4010"` to `testClient.BaseURL = "http://localhost:4010"`.
5. Run the tests locally in your terminal with the command `go test ./... -v` while inside `examples/go`.

## Generating twilio-go

To generate [`twilio-go`](https://github.com/twilio/twilio-go) from [`twilio-oai`](https://github.com/twilio/twilio-oai)

### Setup

1. Clone this repo
2. Clone [twilio-oai](https://github.com/twilio/twilio-oai)
3. Clone [twilio-go](https://github.com/twilio/twilio-go)
4. Navigate to your local `twilio-oai-generator` and run `make install`

### Code Generation

Update `<path to>` and execute the following from the root of this repo:

* To generate the entire suite, run `make install && python3 examples/build_twilio_go.py <path to>/twilio-oai/spec/yaml <path to>/twilio-go`
* To generate the provider for a single domain such as studio, run `make install && python3 examples/build_twilio_go.py <path to>/twilio-oai/spec/yaml/twilio_studio_v2.yaml <path to>/twilio-go`

## Generating terraform-provider-twilio

### Setup

1. Clone this repo
2. Clone [twilio-oai](https://github.com/twilio/twilio-oai)
3. Clone [terraform-provider-twilio](https://github.com/twilio/terraform-provider-twilio)
4. Navigate to your local `twilio-oai-generator` and run `make install`

### Code Generation

Update `<path to>` and execute the following from the root of this repo:

* To generate the entire suite, run `make install && python3 examples/build_twilio_go.py <path to>/twilio-oai/spec/yaml <path to>/terraform-provider-twilio -l terraform`
* To generate the provider for a single domain such as studio, run `make install && python3 examples/build_twilio_go.py <path to>/twilio-oai/spec/yaml/twilio_studio_v2.yaml <path to>/terraform-provider-twilio -l terraform`
* Run `python3 examples/build_twilio_go.py -h` to see more details
