# Application for storing payment records:

### Resources
- *config.properties* - Settings for exchange rate to USD for currency
- *log4j.properties* - Logging settings

### Build application
command: *mvn clean package*
Which create jar *"target/payments_app-1.0.0.jar"* in target directory 

### Run application
java -jar target/payments_app-1.0.0.jar 
There is testing file *data.txt*