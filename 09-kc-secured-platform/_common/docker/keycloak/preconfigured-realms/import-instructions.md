## Importing the existing realms defined in realms.json

### Introduction
The `realms.json` file in the `preconfigured-realms` folder contain two predefined realms including the realms users, clients and client-roles.  
The two realms are:-
 1. persons-platform
 2. hello-world-realm
 
 The persons-platform will be used for securing the persons tryout platform, ie. the Gateway, UI, and supporting micro-services.   
 
### Importing the realms
 1. Start up the keycloak containers and check they are running `docker ps` should show a container called `keycloak-app`  
 2. Shell into the running container `docker exec -it keycloak-app /bin/bash`
 3. Once in the shell run `opt/keycloak/bin/kc.sh import --file preconfigured-realms/realms.json --override false`
 4. Once the import has completed successfully exit the shell and stop the containers `docker compose stop`
 5. Once the containers have stopped restart them with `docker compose start`
 6. Check the containers are all up and running `docker ps`
 7. Log into [keycloak admin console](https://localhost:4499). The docker compose defines a keycloak admin user.
 
 