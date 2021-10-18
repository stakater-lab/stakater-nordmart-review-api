# stakater-nordmart-review

# stakater-nordmart-review     

Spring Boot microservice for product review

## Introduction

This project implements review functionality for the products. It provides CRUDS API for reviews

## User scenarios

### Review Operations

- Add: add a product review
- Delete: delete a product review
- 
- Get: get all reviews for a given product


## Dependencies

It requires following things to be installed:

* Java: ^8.0.
* Maven
* Mongodb

## Dummy Data

Some dummy data is loaded into the system which can be found [here](https://github.com/stakater-lab/stakater-nordmart-review/blob/master/src/main/java/com/stakater/nordmart/service/ReviewServiceImpl.java#L30-L54)

## Deployment strategy

### Local deployment

To run the application locally use the command given below:

```bash
mvn clean spring-boot:run
```

## APIs

- `/api/review/{productId}`
  - Get a review for the given product id
- `/api/review/{productId}/{customerName}/{rating}/{text}`
  - Add a review for the given product id
- `/api/review/{reviewId}`
  - Delete a review for the given product id

## Configuration

_TODO_

## Data

To load dummy data!
```bash
curl localhost:8080/api/review/329199
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