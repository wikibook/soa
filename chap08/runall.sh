asadmin start-domain
maven -d nodescriptor-sibonly all
maven -d nodescriptor-sei all
maven -d nodescriptor-wsdl all
maven -d descriptor-webxml all
maven -d descriptor-ejbjar all
maven -d descriptor-webservice all
maven -d descriptor-sunweb all
maven -d descriptor-sunejbjar all
maven -d autodeploy all
maven -d catalog all
asadmin stop-domain