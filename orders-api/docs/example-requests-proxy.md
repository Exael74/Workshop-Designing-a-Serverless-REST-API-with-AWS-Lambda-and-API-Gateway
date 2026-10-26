# Example requests and responses — Lambda proxy integration

Base URL: `https://j6ao6q0bv3.execute-api.us-east-1.amazonaws.com/prod`

## 1) POST /orders -> 201 Created

Request:
```
POST /orders
Content-Type: application/json

{"customerId":"CUST-42","product":"MacBook Air","quantity":1}
```

Response:
```
HTTP/1.1 201 Created
Location: /orders/ORD-003
Content-Type: application/json

{"id":"ORD-003","customerId":"CUST-42","product":"MacBook Air","quantity":1}
```

## 2) GET /orders/ORD-001 -> 200 OK

```
HTTP/1.1 200 OK
Content-Type: application/json

{"id":"ORD-001","customerId":"CUST-42","product":"MacBook Air","quantity":1}
```

## 3) GET /orders/ORD-999 -> 404 Not Found

```
HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"ORDER_NOT_FOUND","message":"Order ORD-999 does not exist"}
```

## 4) GET /orders?customerId=CUST-42 -> 200 OK

```
HTTP/1.1 200 OK
Content-Type: application/json

[{"id":"ORD-001","customerId":"CUST-42","product":"MacBook Air","quantity":1},{"id":"ORD-003","customerId":"CUST-42","product":"MacBook Air","quantity":1}]
```

## 5) POST /orders invalid (quantity 0) -> 400 Bad Request

```
HTTP/1.1 400 Bad Request
Content-Type: application/json

{"error":"INVALID_ORDER_REQUEST","message":"quantity must be greater than zero"}
```

## 6) DELETE /orders/ORD-002 -> 204 No Content

```
HTTP/1.1 204 No Content
```

## 7) DELETE /orders/ORD-999 -> 404 Not Found

```
HTTP/1.1 404 Not Found
Content-Type: application/json

{"error":"ORDER_NOT_FOUND","message":"Order ORD-999 does not exist"}
```
