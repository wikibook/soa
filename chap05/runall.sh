maven -d customjava build run-nonrecursive
maven -d customjava build run-recursive
maven -d annotations build run
maven -d bindinglang build run
maven -d xmladapter build run
asadmin start-domain
maven -d ../chap03/rest-get/endpoint-servlet build deploy
maven -d ../chap03/rest-post/endpoint-servlet build deploy
maven -d transform build run
maven -d ../chap03/rest-get/endpoint-servlet undeploy
maven -d ../chap03/rest-post/endpoint-servlet undeploy
asadmin stop-domain

