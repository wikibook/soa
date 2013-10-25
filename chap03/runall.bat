call mvn -f rest-get/endpoint-servlet/pom.xml clean install
call mvn -f rest-post/endpoint-servlet/pom.xml clean install
call mvn -f rest-get/endpoint-jaxws/pom.xml clean install
call mvn -f rest-post/endpoint-jaxws/pom.xml clean install

call ant -f rest-get/endpoint-servlet/build.xml deploy
call ant -f rest-post/endpoint-servlet/build.xml deploy
call ant -f rest-get/endpoint-jaxws/build.xml deploy
call ant -f rest-post/endpoint-jaxws/build.xml deploy

call mvn -f rest-get/client-http/pom.xml clean install
call mvn -f rest-get/client-jaxws/pom.xml clean install
call mvn -f rest-post/client-http/pom.xml clean install
call mvn -f rest-post/client-jaxws/pom.xml clean install
call mvn -f xslt/pom.xml clean install

call ant -f rest-get/client-jaxws/build.xml run-jaxws
call ant -f rest-post/client-jaxws/build.xml run-jaxws

call ant -f rest-get/endpoint-servlet/build.xml undeploy
call ant -f rest-post/endpoint-servlet/build.xml undeploy
call ant -f rest-get/endpoint-jaxws/build.xml undeploy
call ant -f rest-post/endpoint-jaxws/build.xml undeploy
