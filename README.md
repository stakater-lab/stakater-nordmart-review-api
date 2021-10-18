# stakater-nordmart-review

## Overview

A maven spring boot review application.

## Dependencies

It requires following things to be installed:

* Java: ^8.0.
* Maven
* Mongodb

## Deployment strategy

### Local deployment

To run the application locally use the command given below:

```
mvn clean spring-boot:run
```

## Local Development

### Prereqs

Install following tools:

- Java
- Maven
- Tilt
- Helm
- Docker etc.
- oc

### ToDos

- How to handle secrets? (Really don't want to use sealed secrets )

### Files

- Tiltfile
- tilt_options.json
- .tiltignore

When ready, run:

```
$ tilt up
```

When done:

```
$ tilt down
```