## Overview
These notes describe how to <br/>
  ***i.***   create a self signed certificate<br/>
  ***ii.***  convert the self signed certificates in to ***pkcs12*** format, i.e. the format that java applications can use <br/>
  ***iii.*** setup an application to use ssl

## 1. Create a self signed certificate	using openssl
On Windows in the wsl-ubuntu distribution run the following command<br/>
<br/>
`openssl req -config loclahost-cert.config -newkey rsa -x509 -days 3650 -out keycloak.crt`<br/>
<br/>
NB: it requires a config file called localhost-cert.config. A sample copy of this [file can be found here](./localhost-cert.config)<br/>
This will generate two files called `localhost.crt` and `localhost.key`

   
## 2. Convert self signed certificates to pkcs12 format
  - change into the directory where the self signed certs were generated with `openssl` command in ***step 1***
  - run the conversion command `openssl pkcs12 -export -out localhost.p12 -inkey localhost.key -in localhost.crt`<br/>
  You will be prompted for an export password. Make a note of it as it will be needed to set the spring-boot-app `server.ssl.key-store-password` property<br/>
  ![Converting to pkcs12](./images/01-convert-to-pkcs12.png)
  
## 3. Setting up an application to use the generated certificate
  ***i.***   **enabling ssl in an application**<br/>
  Set the following three properties in the `application.properties` file<br/>
  ***Note:*** adding the `server.ssl.key-store` property will automatically enable ***ssl***. To disable ssl this property must be commented out or removed.<br/>
```
server.ssl.key-store
server.ssl.key-store-password
server.ssl.key-store-type
```
`server.ssl.key-store-type` should be set to *pkcs12* format. Hence the need to convert the self signed certificate to this format  