# UserAccountService
Sample Java Microservice to manage the User Accounts it is based on Grizzly

# This is maven project to build it use:

mvn clean install compile package

You will get a jar + lib folder for the dependencies in the target folder, run it using java -jar 

# You need to create the MySql schema before run the service using the file: initial_schema.sql

# The service expect the following:

## Port number to run the service against which should be sent as runtime parameter java -jar userAccountService.jar 8081
## Three environment variable to connect to MySQL

DBAAS_USER_NAME (default accounts) , DBAAS_USER_PASSWORD (default accounts), 
and DBAAS_DEFAULT_CONNECT_DESCRIPTOR (the default is localhost:3306/accounts)
which corresponds to DB user, password and connection string  
