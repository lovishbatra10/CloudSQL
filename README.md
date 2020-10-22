Set up credentials, connection info and database info as environment variables.

export GOOGLE_APPLICATION_CREDENTIALS=/path/to/service/account/key.json

export CLOUD_SQL_CONNECTION_NAME='<MY-PROJECT>:<INSTANCE-REGION>:<MY-DATABASE>'

export DB_USER='my-db-user'

export DB_PASS='my-db-pass'

export DB_NAME='my_db'



To evaluate time taken by different queries, change the Prepared Statement in 
the doPost function in IndexServlet.java and monitor the time taken in the logs
getting printed. Prepared Statement gets executed whenever "ClickHere" button 
gets clicked.


To start the server locally, run - 

mvn jetty:run

This will start a jetty server on port 8080. Open localhost:8080 on your browser
to visit the application.
