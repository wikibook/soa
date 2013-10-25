Check in the common code for entire soaj. 
The code in this will generate a jar called soa-core.jar which can be used in other modules.
Once you make a change in this, one should generate the jar and upload it to soabook.com through maven.

For the java logging to work propertly (and for the java logging tests to pass),
you must have a ${java.home}/lib/logging.properties (or else a
System.getProperty("java.util.logging.config.file") file that defines a 
java.util.logging.FileHandler.

