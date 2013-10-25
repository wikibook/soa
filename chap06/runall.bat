call mvn -f endpoint/pom.xml clean install
call ant -f endpoint/build.xml deploy
call mvn -f proxy/pom.xml clean install
call ant -f proxy/build.xml run-standalone run-container
call mvn -f xmlmessaging/pom.xml clean install
call ant -f xmlmessaging/build.xml run
call mvn -f castor/pom.xml clean install
call ant -f castor/build.xml run
call mvn -f asynchronous/pom.xml clean install
call ant -f asynchronous/build.xml run
call mvn -f handler/pom.xml clean install
call ant -f handler/build.xml run
call ant -f endpoint/build.xml undeploy

