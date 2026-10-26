# Example requests and responses — API Gateway mapping template (non-proxy)

Base URL: `https://0k2mxz2yxg.execute-api.us-east-1.amazonaws.com/prod`

This API exposes `GET /orders/{id}` using a non-proxy (custom) Lambda
integration. API Gateway extracts the `{id}` path parameter and builds
the JSON document `{"id": "<value>"}` through a mapping template before
invoking `getOrderFunction`.

## 1) GET /orders/ORD-001 -> 200 OK

```
HTTP/1.1 200 OK
Content-Type: application/json

{"id":"ORD-001","customerId":"CUST-42","product":"MacBook Air","quantity":1}
```

## 2) GET /orders/ORD-999 -> 404 Not Found

Lambda throws `OrderNotFoundException`, which the Java Lambda runtime
serializes as `{"errorMessage": "Order ORD-999 does not exist", ...}`.
API Gateway's integration response matches this with the selection
pattern `.*does not exist.*`, maps it to HTTP 404, and applies a
mapping template that reshapes the body into the project's error
format.

```
HTTP/1.1 404 Not Found
Content-Type: application/json

{"error": "ORDER_NOT_FOUND", "message": "Order ORD-999 does not exist"}
```

## Integration request mapping template (GET /orders/{id})

Content-Type: `application/json`

```velocity
{"id": "$input.params('id')"}
```

## Integration response mapping template (404)

Selection pattern: `.*does not exist.*`

```velocity
{"error": "ORDER_NOT_FOUND", "message": "$input.path('$.errorMessage')"}
```
