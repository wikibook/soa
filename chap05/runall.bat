call mvn -f customjava/pom.xml clean install
call mvn -f annotations/pom.xml clean install
call mvn -f bindinglang/pom.xml clean install
call mvn -f xmladapter/pom.xml clean install
call mvn -f ../chap03/rest-post/endpoint-servlet/pom.xml clean install
call ant -f ../chap03/rest-post/endpoint-servlet/build.xml deploy
call mvn -f ../chap03/rest-get/endpoint-servlet/pom.xml clean install
call ant -f ../chap03/rest-get/endpoint-servlet/build.xml deploy
call mvn -f transform/pom.xml clean install
call ant -f ../chap03/rest-post/endpoint-servlet/build.xml undeploy
call ant -f ../chap03/rest-get/endpoint-servlet/build.xml undeploy
