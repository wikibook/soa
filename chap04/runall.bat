call ant -f jaxwsworkaround/build.xml gen-java
call mvn -f sfwj/modules/schema2java/pom.xml clean install
call ant -f sfwj/build.xml run-deserializer1
call mvn -f sfwj/pom.xml clean install
call ant -f sfwj/build.xml run-deserializer2
