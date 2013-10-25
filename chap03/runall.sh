asadmin start-domain
maven -d rest-get/endpoint-servlet build deploy
maven -d rest-post/endpoint-servlet build deploy
maven -d rest-get/endpoint-jaxws build deploy

maven -d rest-get/client-http build run-servlet
maven -d rest-get/client-jaxws build run-servlet
maven -d rest-post/client-http build run-servlet
maven -d rest-post/client-jaxws build run-servlet
maven -d xslt build run
maven -d rest-get/client-jaxws build run-jaxws

maven -d rest-get/endpoint-servlet undeploy
maven -d rest-post/endpoint-servlet undeploy
maven -d rest-get/endpoint-jaxws undeploy
asadmin stop-domain