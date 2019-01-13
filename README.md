# Simple Money Transfer Service

Simple WEB-application for bank account management and performing transfer operations between them.

Supported only Java 8.

## How to run:

1. `git clone https://github.com/804/money-transfer-service.git`
2. `cd money-transfer-service`
3. `./gradlew shadowJar`
4. `java [properties] -jar build/libs/money-transfer-rest.jar server`

As 'properties' you can specify following properties:
 - `-Dhttp.port=%port%` - for HTTP port specifying (default is '8080')
 - `-Dhttp.host=%host%` - for HTTP host specifying (default is 'localhost')
 - `-Dlog.level=%level%` - for console log level specifying (default is 'INFO')
 
### Example:
`java -Dhttp.port=9090 -Dhttp.host=localhost -Dlog.level=TRACE -jar build/libs/money-transfer-rest.jar server` - run service on 'localhost:9090' with 'TRACE' log level

## API endpoints: 

 - GET: `/account?id=*account_id*` - getting account state from service.
  
  Request URL example: `/account?id=00000000000000000001`

  Response example:
   ```json
  {
      "id": "00000000000000000001",
      "amount": "USD 100"
  }
   ```

 - POST: `/account` - create empty account in service (contains only id and amount for the sake of simplicity)

  Request body is empty.
  
  Response example:
  
  ```json
  {
      "id": "00000000000000000001",
      "amount": "USD 0"
  }
  ```
  
 - PUT: `/money/recharge` - recharge account with specified amount
  
  Request example ('application/json' content type):
  ```json
  {
  	"accountTo": "00000000000000000001",
  	"amount": 50
  }
  ```
  where 'accountTo' - account ID, 'amount' - recharged money amount
  
 - PUT: `/money/transfer` - transfer amount from one account to another
  
  Request example ('application/json' content type):
  ```json
  {
  	"accountFrom": "00000000000000000000",
  	"accountTo": "00000000000000000001",
  	"amount": 10
  }
  ```
  where 'accountFrom', 'accountTo' - accounts ID, 'amount' - recharged money amount