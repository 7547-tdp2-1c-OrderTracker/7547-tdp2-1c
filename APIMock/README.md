# MockServer
## installation
```sh
$ npm install
```

## running
```sh
$ npm start
```

## Services
By default server runs on port 8090.

Exposed URL are:
- http://localhost:8090/v1/clients
- http://localhost:8090/v1/clients/:client_id
- http://localhost:8090/v1/products
- http://localhost:8090/v1/products/_product_id

## Editing mocks
Inside defaults directory is one json file for every entity.
- clients.json
- products.json