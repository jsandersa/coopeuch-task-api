# coopeuch-task-api

## Despliegue

##### Crear rol para la API
Ocupando pgadmin o psql ejecute "script-1-creacion-rol.sql"

##### Crear base de datos para la API
Ocupando pgadmin o psql ejecute "script-2-creacion-bdd.sql"

##### Crear tablas correspondientes al modelo
Ocupando pgadmin o psql ejecute "script-3-creacion-tablas.sql"

#### Para ejecutar UnitTests
    mvnw test

#### Para levantar el servicio desde la carpeta del proyecto se debe ejecutar el siguiente comando 
    mvnw spring-boot:run

#### Fin.
