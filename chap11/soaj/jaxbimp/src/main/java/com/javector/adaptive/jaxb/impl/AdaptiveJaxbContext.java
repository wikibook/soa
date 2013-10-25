/*
* Copyright 2006-2007 Javector Software LLC
*
* Licensed under the GNU General Public License, Version 2 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.gnu.org/copyleft/gpl.html
*
* THE SOURCE CODE AND ACCOMPANYING FILES ARE PROVIDED WITHOUT ANY WARRANTY,
* WRITTEN OR IMPLIED.
*
* The copyright holder provides this software under other licenses for those
* wishing to include it with products or systems not licensed under the GPL.
* Contact licenses@javector.com for more information.
*/
package com.javector.adaptive.jaxb.impl;

import com.javector.adaptive.framework.interfaces.BaseAdaptiveSerializer;
import com.javector.adaptive.framework.interfaces.BaseBindingRule;
import com.javector.adaptive.framework.interfaces.BaseSOAContext;
import com.javector.adaptive.framework.interfaces.BaseAdaptiveDeserializer;
import com.javector.adaptive.framework.model.RuleDTO;
import com.javector.adaptive.framework.model.ConfigurationDetailDTO;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.sun.tools.xjc.XJC2Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.net.URL;

public class AdaptiveJaxbContext extends BaseSOAContext {

  private static SoajLogger logger = LoggingFactory.getLogger(AdaptiveJaxbContext.class.getName());
    private URLClassLoader classLoader;


    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    public AdaptiveJaxbContext() throws SoajException {
        super();
    }

    protected BaseBindingRule newBindingRule(RuleDTO ruleDTO) {
        return null;
    }

    protected void loadSchemas() {
        int jarIndex = 0;
        for (URL aSchemaURLS : schemaURLS) {

            File classdir = new File(tempWorkDir + "/classes" + jarIndex);
            if (classdir.exists()) {
                classdir.delete();
            }
            classdir.mkdir();
            File srcdir = new File(tempWorkDir + "/src" + jarIndex);
                 if (srcdir.exists()) {
                     srcdir.delete();
                  }
            srcdir.mkdir();
            File schemafile = new File(tempWorkDir + "/schema" + jarIndex + ".xsd");
            try {
                InputStream is = aSchemaURLS.openStream();
                schemafile.createNewFile();
                FileOutputStream fos = new FileOutputStream(schemafile);
                byte[] chunk = new byte[1000];
                int bytesRead = is.read(chunk);
                while (bytesRead > 0) {
                    fos.write(chunk, 0, bytesRead);
                    bytesRead = is.read(chunk);
                }
                fos.close();
            } catch (IOException e) {
                throw new SoajRuntimeException("Exception while compiling schemas", e);
            }
            SchemaCompiler schemaCompiler = new SchemaCompiler();
            try {
                schemaCompiler.setSchema(schemafile.toURL().toExternalForm());
            } catch (MalformedURLException e) {
                throw new SoajRuntimeException("Please re-check the path to the schema xsd " + schemafile);
            }
            Project project = new Project();
            project.setDefault("XJCTask");
            project.setName("XJCTask");
            project.init();
            FileSet fileSet = new FileSet();
            fileSet.setFile(schemafile);
            fileSet.setProject(project);
            schemaCompiler.setDestdir(schemafile);
            schemaCompiler.setProject(project);
//            schemaCompiler.
            schemaCompiler.setClassDir(classdir);
            schemaCompiler.setDestdir(srcdir);
            schemaCompiler.setRemoveOldOutput(true);
            schemaCompiler.createArg().setValue("-npa");
            schemaCompiler.execute();

            try {

                classLoader = new URLClassLoader(new URL[]{classdir.toURL()},Thread.currentThread().getContextClassLoader());
                Thread.currentThread().setContextClassLoader(classLoader);
            } catch (Exception e) {
                throw new SoajRuntimeException("Exception while loading JAXB generated classes", e);
            }
        }
    }



    class SchemaCompiler extends XJC2Task {
        private String srcDir;
        private String classDir ;
        SchemaCompiler() {}

        public void setDestdir(File file) {
            srcDir = file.getAbsolutePath();
            super.setDestdir(file);
        }
        public void setClassDir(File file ){
            classDir = file.getAbsolutePath();
        }
        public void execute() throws BuildException {
            super.execute();
            try {
                compileJavaSources(getProject(), classDir, srcDir);
            } catch (SoajException e) {
                throw new BuildException(e);
            }

        }

    }
    private Path formClassPath(Project project) throws SoajException {
        Path path = new Path(project);
        FileSet fileSet = new FileSet();
        String property = System.getenv("JAXB_HOME");
        // String property = "D:/glassfish/rohit_home/";
        if( property!= null){
            fileSet.setDir(new File(property,"lib"));
        }else{
            throw new SoajException("Please set JAXB_HOME");
        }
        fileSet.setIncludes("javaee.jar");
        path.addFileset(fileSet);
        return path;
    }

    private void compileJavaSources(Project project, String classDir, String srcDir) throws SoajException {

      Javac javac = new Javac();
        Path path = new Path(project);
        DirSet dirSet = new DirSet();
        dirSet.setDir(new File(srcDir));
        path.addDirset(dirSet);
//            sun.tools.javac.Main
        javac.setProject(project);
        javac.setClasspath(formClassPath(project));
        javac.setSource("1.5");
        javac.setIncludejavaruntime(true);

//            javac.
        javac.setSrcdir(path);
        javac.setFork(true);
        javac.setDestdir(new File(classDir));
        javac.setVerbose(true);
        javac.setFailonerror(true);
        javac.setDebug(true);

        try {
          javac.execute();
        } catch (BuildException be) {
          logger.severe("classpath = " + javac.getClasspath());
          String[] compilerArgs = javac.getCurrentCompilerArgs();
          String argsString = "";
          for (int i=0; i<compilerArgs.length; i++) {
            argsString += compilerArgs[i] + " "; }
          logger.severe("currentCompilerArgs = " + argsString);
          File fileList[] = javac.getFileList();
          String fileListString = "";
          for (int i=0; i<fileList.length; i++) {
            fileListString += fileList[i].toString() + " ";
          }
          logger.severe("fileList = " + fileListString);
          logger.severe("sourcePath = " + javac.getSourcepath());
          logger.severe("srcDir = " + javac.getSrcdir());
          logger.severe("destDir = " + javac.getDestdir());
          logger.severe("location = " + be.getLocation());
          throw new SoajRuntimeException(
              "Failed to compile JAXB schema compiler generated classes.  " +
              "See the log for error messages.", be);
        }
    }


    protected BaseAdaptiveSerializer newSerializer() {
        ConfigurationDetailDTO  detailDTO = getConfigurationDetailDTO();
            try {
                Class aClass = Class.forName(detailDTO.getBaseAdaptiveSerializer());
                baseAdaptiveSerializer = (BaseAdaptiveSerializer)aClass.newInstance();
            } catch (Exception e) {
                throw new SoajRuntimeException("unable to create serializer instance = "+ detailDTO.getBaseAdaptiveSerializer(),e);
            }
        return baseAdaptiveSerializer;
    }


    protected BaseAdaptiveDeserializer newDeserializer() {
        ConfigurationDetailDTO detailDTO = getConfigurationDetailDTO();
            try {
                Class clas = Class.forName(detailDTO.getBaseAdaptiveDeserializer());
                baseAdaptiveDeserializer = (BaseAdaptiveDeserializer)clas.newInstance();
            } catch (Exception e) {
                throw new SoajRuntimeException("unable to create deserializer instance ",e);
            }
        return baseAdaptiveDeserializer;
    }


}
