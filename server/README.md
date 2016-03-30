# OrderTracker Server Side


El Order Tracker Server-Side contiene el servicio con los datos necesarios para poder exponer una API publica a ser consumida.

## Entorno

- Vagrant
- MySQL 5.6
- Java JDK 7
- Groovy

## Requirements

- Instalar [Vagrant](https://www.vagrantup.com/downloads.html) 1.8.1 (recomendado)

- Una vez instalado:

1. Pararse en la folder del server

  ```bash
  $ cd server
  ```
2. Levantar el servidor

  ```bash
  $ vagrant up
  ```

3. Validar que la instalación haya sido satifactoria.
4. Comunicarse por la virtual

  ```bash
  $ vagrant ssh
  ```

5. Desarrollar en nuestro entorno de trabajo. (ver sync)


## Synchronization

La raiz del proyecto **7547-tdp2-1c/** se encuentra del lado del guest en la folder **./vagrant/**

