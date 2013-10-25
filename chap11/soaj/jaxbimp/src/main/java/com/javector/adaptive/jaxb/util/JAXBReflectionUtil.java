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
package com.javector.adaptive.jaxb.util;

import com.javector.adaptive.framework.interfaces.BaseSOAContext;
import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.adaptive.framework.util.SOAReflectionUtil;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.util.IOUtil;
import org.jvnet.jaxb.reflection.JAXBModelFactory;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeClassInfo;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeNonElement;
import org.jvnet.jaxb.reflection.model.runtime.RuntimePropertyInfo;
import org.jvnet.jaxb.reflection.model.runtime.RuntimeTypeInfoSet;
import org.jvnet.jaxb.reflection.runtime.reflect.Accessor;
import org.jvnet.jaxb.reflection.runtime.IllegalAnnotationsException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public class JAXBReflectionUtil extends BaseReflectionUtil {

    /**
     * This is a utility method create a jaxbObject from a xmlType QName
     *
     * @param xmlType
     * @param cl
     * @return new jaxbobject corresponding to xmlType
     */
    public  Object createXMLObject(QName xmlType, Object cl) {

        Object jaxbObject = null;
        try {
            Class<?> clazz = Class.forName(JAXBHelper.getFullyQualifiedClassName(xmlType), true, (ClassLoader)cl);
            jaxbObject = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jaxbObject;
    }

    /**
     * This method will take class loader in case of jaxb and schematypeloader in case of xml Beans
     * @param xmlSource
     * @param xmlType
     * @param cl
     * @return
     */

    public  Object createXMLObjectFromXMLSource(String xmlSource, QName xmlType, Object cl) {

         Object jaxbObject = null;
         try {
             ClassLoader classLoader = (ClassLoader)cl;
             String packageName = JAXBHelper.convertPackageFromXmlType(xmlType);
             Class<?> clas = Class.forName(JAXBHelper.getFullyQualifiedClassName(xmlType),true,classLoader);
             JAXBContext context = JAXBContext.newInstance(packageName,classLoader);
             Document doc = getXMldocFromXMLString(xmlSource);
             jaxbObject = context.createUnmarshaller().unmarshal(doc,clas);
         } catch (Exception e) {
             throw new RuntimeException("Exception getting jaxbObject from xml source =" +xmlSource,e);
         }
         return jaxbObject;
     }


    public Object createXMLObjectFromXmlNode(ClassLoader cl, Node xmlNode, Object sourceObject, Class jaxbMappingClass) {
           try {
               JAXBContext context = createJAXBContext(sourceObject,cl);
               Unmarshaller unmarshaller = context.createUnmarshaller();
               return unmarshaller.unmarshal(xmlNode,jaxbMappingClass);
           } catch (JAXBException e) {
               throw new SoajRuntimeException("unable to marshal the node for given node = "+ xmlNode.getNodeName()+"for given mapping class = "+ jaxbMappingClass.getName());
           }
       }



    /**
     * Will retrieve approprite jaxb Object from given xmlNode.
     * @param xmlDoc
     * @param xmlType
     * @param cl
     * @return
     */
    public  Object createXMLObjectFromXMLNode(Node xmlDoc, QName xmlType, Object cl) {

            Object jaxbObject = null;
            try {
                ClassLoader classLoader = (ClassLoader)cl;
                String packageName = JAXBHelper.convertPackageFromXmlType(xmlType);
                Class<?> clas = Class.forName(JAXBHelper.getFullyQualifiedClassName(xmlType),true,classLoader);
                JAXBContext context = JAXBContext.newInstance(packageName,classLoader);
                jaxbObject = context.createUnmarshaller().unmarshal(xmlDoc,clas);
            } catch (Exception e) {
                throw new RuntimeException("Exception getting jaxbObject from xml source =" +xmlDoc,e);
            }
            return jaxbObject;
        }



    public static Document getXMldocFromXMLString(String xmlData) {
          ByteArrayInputStream io = new ByteArrayInputStream(xmlData.getBytes());
         try {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          factory.setNamespaceAware(true);
          Document doc = factory.newDocumentBuilder().parse(io);
             System.out.println("xml data = " + getXmlString(doc));
             return doc;
         }catch(Exception ex) {
             throw new RuntimeException("unable to create document from src = " + xmlData);
         }
    }


    /**
     * This is a utility method to know if a xmlName of the element/attribute in QName is of simple type
     * Parent object is needed always to know the type of of its child
     *
     * @param parentJaxbObject
     * @param xmlName
     * @return true is
     * @
     */
    public  boolean isSimpleType(Object parentJaxbObject, QName xmlName)  {
        System.out.println("xmlName = " + xmlName);
        /**
         * used for maven debugging
        Class<?> aClass = null;
        try {
            aClass = Class.forName(JAXBModelFactory.class.getName(),true, this.getClass().getClassLoader());
            System.out.println("aClass = " + aClass);
        } catch (Throwable e) {
            System.out.println("this class failed= ");
        }
        try {
            aClass = Class.forName(JAXBModelFactory.class.getName(),true, System.class.getClassLoader());
            System.out.println("aClass = " + aClass);
        } catch (Throwable e) {
            System.out.println("System class failed= ");
        }
        try {
            aClass = Class.forName(JAXBModelFactory.class.getName(),true, Thread.currentThread().getContextClassLoader());
            System.out.println("aClass = " + aClass);
        } catch (Throwable e) {
            System.out.println("Thread class loader failed= ");
        }

         */

        try {
            RuntimeTypeInfoSet runtimeTypeInfoSet;
            runtimeTypeInfoSet = JAXBModelFactory.create(parentJaxbObject.getClass());
            RuntimeClassInfo typeInfo;
            typeInfo = (RuntimeClassInfo) runtimeTypeInfoSet.getTypeInfo(parentJaxbObject.getClass());
            RuntimeNonElement typeInfo1 = runtimeTypeInfoSet.getTypeInfo(typeInfo.getProperty(xmlName.getLocalPart()).getAccessor().getValueType());
            if (typeInfo1 == null) {
                return false;
            }

            return typeInfo1.isSimpleType();

        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while checking if the jaxb object is of simple type for name =" +xmlName + "for object =" + parentJaxbObject.getClass().getName(), e);
        }
    }



    public  boolean isSimpleType(Object parentJaxbObject)  {

        try {
            RuntimeTypeInfoSet runtimeTypeInfoSet;
            runtimeTypeInfoSet = JAXBModelFactory.create(parentJaxbObject.getClass());
            RuntimeClassInfo typeInfo;
            typeInfo = (RuntimeClassInfo) runtimeTypeInfoSet.getTypeInfo(parentJaxbObject.getClass());
            return typeInfo.isSimpleType();

        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while checking if the jaxb object is of simple type", e);
        }

    }

    /**
     * This will tell if the xmlName QName contained in parentJaxbObject is of arraytype
     *
     * @param parentJaxbObject
     * @param xmlName
     * @return true if its array type
     * @
     */
    public boolean isArrayType(Object parentJaxbObject, QName xmlName)  {
        try {
            RuntimeTypeInfoSet runtimeTypeInfoSet = JAXBModelFactory.create(parentJaxbObject.getClass());
            RuntimeClassInfo typeInfo = (RuntimeClassInfo) runtimeTypeInfoSet.getTypeInfo(parentJaxbObject.getClass());
            return ((RuntimePropertyInfo) typeInfo.getProperty(xmlName.getLocalPart())).isCollection();
        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while getting checking if the jaxb object is of array type", e);
        }
    }

    /**
     * This will set the value of xmlName in sourceJaxbObject to propertyValue
     * ( If needed automatic casting is done )
     *
     * @param sourceJaxbObject
     * @param propertyValue
     * @param xmlName
     * @param cl
     * @
     */
    public void setProperty(Object sourceJaxbObject, Object propertyValue, QName xmlName)  {
        try {
            if (propertyValue == null)
                return;
            System.out.println("java.classpath=" + System.getProperty("java.class.path"));
            RuntimeTypeInfoSet model = JAXBModelFactory.create(sourceJaxbObject.getClass());
            RuntimeClassInfo ci = (RuntimeClassInfo) model.getTypeInfo(sourceJaxbObject.getClass());
            Accessor acc = ci.getProperty(xmlName.getLocalPart()).getAccessor();

            Class sourceClass = acc.valueType;
            if (acc.valueType.isPrimitive()) {
                Class<? extends Object> wrapperClass;
                wrapperClass = SOAReflectionUtil.getWrapperClass(acc.valueType.getName());
                sourceClass = wrapperClass;
            }

            if(acc.getValueType().getName().equals(List.class.getName()) && !SOAReflectionUtil.isCollectionType(propertyValue)){
                List propertyList = new ArrayList();
                propertyList.add(propertyValue);
                propertyValue = propertyList;
            }

            // this is string type todo: is assignableform is better here some times
            if (sourceClass.isInstance(propertyValue)) {
                acc.set(sourceJaxbObject, propertyValue);
            } else {
                acc.set(sourceJaxbObject, SOAReflectionUtil.castMe(sourceClass, propertyValue));
            }
        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while setting the PropertyObject for sourceJaxb = " + sourceJaxbObject.getClass().getName()
                    + IOUtil.NL + "propertyName = " + xmlName.getLocalPart() + "  property value = " +propertyValue, e);
        }
    }


    /**
     * This returns the value of the xmlName element in the sourceJaxbObject
     *
     * @param sourceJaxbObject
     * @param xmlName
     * @return
     * @
     */
    public  Object getPropertyValue(Object sourceJaxbObject, QName xmlName)  {
        try {
            RuntimeTypeInfoSet model = JAXBModelFactory.create(sourceJaxbObject.getClass());
            RuntimeClassInfo ci = (RuntimeClassInfo) model.getTypeInfo(sourceJaxbObject.getClass());
            Accessor acc = ci.getProperty(xmlName.getLocalPart()).getAccessor();
            return acc.get(sourceJaxbObject);
        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while getting the PropertyObject for object = " + sourceJaxbObject.getClass().getName()+"for Qname = " + xmlName.toString(), e);
        }

    }

    /**
     * This returns the value of the xmlName element in the sourceJaxbObject
     *
     * @param sourceJaxbObject
     * @param xmlName
     * @return
     * @
     */
    public QName getPropertySchemaType(Object sourceJaxbObject, QName xmlName)  {
        try {
            RuntimeTypeInfoSet model = JAXBModelFactory.create(sourceJaxbObject.getClass());
            RuntimeClassInfo ci = (RuntimeClassInfo) model.getTypeInfo(sourceJaxbObject.getClass());
            //todo:bug in jax2b api
//            return ci.getProperty(xmlName.getLocalPart()).getSchemaType();
            return ((RuntimeClassInfo) ci.getProperty(xmlName.getLocalPart()).ref().iterator().next()).getTypeName();
//            return xmlName;
        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while getting the PropertyObject " + xmlName + " " +
                    sourceJaxbObject.getClass(), e);
        }

    }

    // MDH comment: is it possible to refactor to return List<T> ??
    public List<Object> getList(Object objectz, QName xmlName) {

        try {
            String methodName = "get" + JAXBHelper.getInitCap(xmlName.getLocalPart());
            Method method = objectz.getClass().getMethod(methodName, new Class[]{});
            return (List<Object>) method.invoke(objectz, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertXMLObjectToString(Object targetJaxbObject, ClassLoader cl, QName xmlName)  {
        Marshaller m;
        String xml ="";

        try {
            m = createJAXBContext(targetJaxbObject,cl).createMarshaller();
            m.setProperty("jaxb.formatted.output", Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            JAXBElement<? extends Object> jaxbElement1 = new JAXBElement(xmlName, targetJaxbObject.getClass(), targetJaxbObject);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m.marshal(jaxbElement1, stream);
            xml = stream.toString();
           // xml = JaxBXPathEvaluator.getXmlString(doc);
            return xml;
        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while printing jaxb object", e);
        }

    }



    public String convertXMLObjectToString(Object targetJaxbObject, ClassLoader cl)  {
        Marshaller m;
        String xml ="";

        try {
            m = createJAXBContext(targetJaxbObject,cl).createMarshaller();
            m.setProperty("jaxb.formatted.output", Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m.marshal(targetJaxbObject,stream);
            xml = stream.toString();
            return xml;
        } catch (Exception e) {
            throw new SoajRuntimeException("Exception while printing jaxb object", e);
        }

    }


    private JAXBContext createJAXBContext(Object targetJaxbObject, ClassLoader cl)  {

        try {
            return JAXBContext.newInstance(targetJaxbObject.getClass().getName().replaceAll("(.*)\\.([^\\.]*)+", "$1"),
                    cl);
        }catch(JAXBException ex) {
            throw new SoajRuntimeException("unable to create JAXBcontext ",ex);
        }
    }

    public Object createXMLObjectFromXmlNode(BaseSOAContext ctx, Node xmlNode, Object sourceObject, Class jaxbMappingClass) {
        try {
            JAXBContext context = createJAXBContext(sourceObject,ctx.getClassLoader());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(xmlNode,jaxbMappingClass);
        } catch (JAXBException e) {
            throw new SoajRuntimeException("unable to marshal the node for given node = "+ xmlNode.getNodeName()+"for given mapping class = "+ jaxbMappingClass.getName());
        }
    }



    public Class getClass(Object sourceJaxbObject, QName xmlName)  {
        try{
            RuntimeTypeInfoSet model = JAXBModelFactory.create(sourceJaxbObject.getClass());
            RuntimeClassInfo ci = (RuntimeClassInfo) model.getTypeInfo(sourceJaxbObject.getClass());
            List<? extends RuntimePropertyInfo> properties = ci.getProperties();
            Class valueType  = null;
            for (RuntimePropertyInfo runtimePropertyInfo : properties) {
                if( runtimePropertyInfo.getName().equals(xmlName.getLocalPart()) &&
                        runtimePropertyInfo.getSchemaType().getNamespaceURI().equals(xmlName.getNamespaceURI())){
                    return runtimePropertyInfo.getAccessor().getValueType();
                }
            }
            return valueType;
        }catch(Exception e){
            throw new SoajRuntimeException("Exception while trying to get class from property QName",e);
        }
    }

    /**
        * Will return nameSpace uri of the parent for wrap type for invoking the xml names for getting
        * PropertyObjects to be populated in java Object

        * @return
        * @
        */

       public String getNameSpaceUri(Object sourceJAXBObject)  {
           RuntimeTypeInfoSet infoSet;
           try {
               infoSet = JAXBModelFactory.create(sourceJAXBObject.getClass());
           } catch (IllegalAnnotationsException e) {
               throw new SoajRuntimeException("exception creating jaxbObject for class = " + sourceJAXBObject.getClass(),e);
           }
           QName name = ((RuntimeClassInfo) infoSet.getTypeInfo(sourceJAXBObject.getClass())).getElementName();
           String uri = name.getNamespaceURI();
           return uri;
       }


}

