# stakater-nordmart-review

Spring Boot microservice for product review


## Introduction

This project implements review functionality for the products; it provides CRUDS API for reviews.


## User scenarios

### Review Operations

- Add: add a product review
- Delete: delete a product review
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

### Secrets Handling

Secrets are being managed using vault CSI provider and secretProviderClass. 
Set secretProviderClass to true and provide vault credentials and the path of secret along with credentials like name and type of the kubernetes secret you would like to create. 
Secret is then mounted into the pod using volumes. 
Previously, MONGO_HOST value was fetched from configMap and now it is being used in pod as an env variable. 

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
