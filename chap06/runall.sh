asadmin start-domain
maven -d endpoint build deploy
maven -d proxy build run-standalone
maven -d proxy build run-container
maven -d xmlmessaging build run
maven -d castor build run
maven -d asynchronous build run
maven -d handler build run
maven -d endpoint undeploy
asadmin stop-domain
