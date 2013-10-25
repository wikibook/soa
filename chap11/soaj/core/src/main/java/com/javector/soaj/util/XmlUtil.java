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
package com.javector.soaj.util;

import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// TODO this class may create serious performance bottlenecks.  need to refactor

public abstract class XmlUtil {

  // ----------------------------------------------- Constants

  // new line character
  public static final String NL = 
    System.getProperty("line.separator");
  // XSLT to format printing of raw XML - no spaces
  public static final String XSLT_RAW = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"no\"/><xsl:strip-space elements=\"*\"/><xsl:template match=\"*\"><xsl:copy><xsl:copy-of select=\"@*\"/><xsl:apply-templates/></xsl:copy></xsl:template></xsl:stylesheet>";
  public static final String XSLT_READABLE = 
    "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"xml\"/><xsl:param name=\"indent-increment\" select=\"'  '\" /><xsl:template match=\"*\"><xsl:param name=\"indent\" select=\"'&#xA;'\"/><xsl:value-of select=\"$indent\"/><xsl:copy><xsl:copy-of select=\"@*\" /><xsl:apply-templates><xsl:with-param name=\"indent\" select=\"concat($indent, $indent-increment)\"/></xsl:apply-templates><xsl:if test=\"*\"><xsl:value-of select=\"$indent\"/></xsl:if></xsl:copy></xsl:template><xsl:template match=\"comment()|processing-instruction()\"><xsl:copy /></xsl:template><xsl:template match=\"text()[normalize-space(.)='']\"/></xsl:stylesheet>";
// schema for WSDL 1.1 referenced by WS-I BP 1.1
  public static final String URL_XSD_WSIBP11_WSDL11 = "http://ws-i.org/profiles/basic/1.1/wsdl-2004-08-24.xsd";
  // schema for WSDL 1.1 SOAP 1.1 Binding referenced by WS-I BP 1.1
  public static final String URL_XSD_WSIBP11_WSDL11SOAP11 = "http://ws-i.org/profiles/basic/1.1/wsdlsoap-2004-08-24.xsd";
  // schema for SOAP 1.1 Envelope referenced by WS-I BP 1.1
  public static final String URL_XSD_WSIBP11_SOAP11 = "http://ws-i.org/profiles/basic/1.1/soap-envelope-2004-01-21.xsd";
  // Schema for validating WSDL 1.1 / SOAP 1.1 Binding
  private static Schema wsdl11soap11;

  // ----------------------------------------- Class Variables

  // Logger for logging exceptions and other events
  private static SoajLogger log = LoggingFactory.getLogger(XmlUtil.class.getName());

  // -------------------------------------- Instance Variables

  private static Transformer rawXformer;
  private static Transformer readableXformer;
  private static Transformer simpleXformer;
  
  static {
    try {
      // transformers are *not* thread safe
      // TODO make thread safe
      TransformerFactory transFac = 
        TransformerFactory.newInstance();
      StreamSource rawSS = 
        new StreamSource(new ByteArrayInputStream
            (XSLT_RAW.getBytes()));
      StreamSource readableSS = 
        new StreamSource(new ByteArrayInputStream
            (XSLT_READABLE.getBytes()));
      rawXformer = transFac.newTransformer(rawSS);
      readableXformer = transFac.newTransformer(readableSS);
      simpleXformer = transFac.newTransformer();
    } catch (Exception e) {
      System.out.println(IOUtil.stackTrace(e));
      //System.exit(1);
    }
  }

  // -------------------------------------------- Constructors

  // ------------------------------------------ Public Methods
 
  public static synchronized String toString(Source source) 
  throws SoajRuntimeException {

    if ( source == null ) { return null; }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamResult res = new StreamResult(out);
    try {
      simpleXformer.transform(source,res);
    } catch (TransformerException te) {
      throw new SoajRuntimeException
      ("Error transforming Source to String.", te);
    }
    // stream source can only be used once.  must reset
    if ( StreamSource.class.isInstance(source) ) {
      ((StreamSource) source).setInputStream(
          new ByteArrayInputStream(out.toByteArray()));
    }
    return out.toString();
    
  }
  
  public static synchronized String toString(Node n, boolean formatted) 
  throws SoajRuntimeException {
    
    if ( n == null ) { return null; }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamResult res = new StreamResult(out);
    try {
      if ( formatted ) {
        readableXformer.transform(new DOMSource(n), res);
      } else {
        rawXformer.transform(new DOMSource(n), res);
      }
    } catch (TransformerException te) {
      throw new SoajRuntimeException
      ("Error transforming DOM to String.", te);
    }
    return out.toString();
    
  }
  
  public static synchronized String toFormattedString(Node n) 
  throws SoajRuntimeException {
    return toString(n, true);
  }
  
  public static synchronized String toString(Node n) 
  throws SoajRuntimeException {
    return toString(n, false);
  }
  
  public static Schema getSchemaWSDL11SOAP11() {
    
    if ( wsdl11soap11 == null ) {
      SchemaFactory fac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      StreamSource[] schemas = {new StreamSource(URL_XSD_WSIBP11_WSDL11),
          new StreamSource(URL_XSD_WSIBP11_WSDL11SOAP11) };
      try {
        wsdl11soap11 =  fac.newSchema(schemas);
      } catch (SAXException e) {
        throw new SoajRuntimeException("Failed to create a validating schema for WSDL 1.1 / SOAP 1.1", e);
      }
    }
    return wsdl11soap11;
    
  }

  public static synchronized String formatXml(String xml) {
    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try {
      readableXformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(output));
    } catch (TransformerException e) {
      throw new SoajRuntimeException("Failed to format XML", e);
    }
    return output.toString();

  }
  

//  public static String domToString(Node n) throws GeneralException {
//    return domToString(n, false); }
//  
//  public static String domToIndentedString(Node n) 
//  throws GeneralException {
//    return domToString(n, true); }
//  
//  public static synchronized void domToFile(Node n, File f, boolean format)
//  throws GeneralException {
//    
//    if ( n == null ) { return; }
//    // create directory if it doesn't exist
//    File dir = f.getParentFile();
//    if ( dir != null ) {
//      if ( !dir.exists() ) {
//        dir.mkdirs();
//      }
//    }
//    try {
//      if ( format ) {
//        readableXformer.transform
//        (new DOMSource(n), new StreamResult(f));
//      } else {
//        rawXformer.transform
//        (new DOMSource(n), new StreamResult(f));
//      }
//    } catch (TransformerException te) {
//      throw new GeneralException
//      ("Error transforming DOM to File.", te);
//    }
//    
//  }

//  public static void domToFile(Node n, File f) throws GeneralException {
//    domToFile(n, f, false); }
//
//
//  public void saxToFile(SAXSource s, File f, boolean format)
//    throws GeneralException {
//
//    if ( s == null ) { return; }
//    try {
//      if ( format ) {
//	readableXformer.transform(s, new StreamResult(f));
//      } else {
//	rawXformer.transform(s, new StreamResult(f));
//      }
//    } catch (TransformerException te) {
//      throw new GeneralException
//	(Messages.get("xmlUtilErrXSLT"), te);
//    }
//
//  }
//
//
//  public void saxToFile(SAXSource s, File f) 
//    throws GeneralException { saxToFile(s, f, false); }
//
//  public DocumentBuilder getDocumentBuilder()
//    throws GeneralException {
//
//    try {
//      DocumentBuilderFactory docBuildFac = 
//	DocumentBuilderFactory.newInstance();
//      docBuildFac.setNamespaceAware(true);
//      return docBuildFac.newDocumentBuilder();
//    } catch (FactoryConfigurationError fe) {
//      throw new GeneralException("Can't get doc builder.", fe);
//    } catch (ParserConfigurationException pe) {
//      throw new GeneralException("Can't get doc builder.", pe);
//    }
//
//  }
//
//  public byte[] toBytes(Source s) throws GeneralException { 
//
//    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//    try {
//      rawXformer.transform(s, new StreamResult(bos));
//    } catch (TransformerException te) {
//      throw new GeneralException
//	(Messages.get("xmlUtilErrXSLT"), te);
//    }
//    return bos.toByteArray();
//
//  }
//
//  public Source toSource(String st) {
//
//    return new StreamSource(new StringReader(st));
//
//  }
//
//  public Document fileToDOM(File f) 
//    throws GeneralException {
//
//    if ( f == null ) { return null; }
//    try {
//      return getDocumentBuilder().parse(f);
//    } catch (SAXException se) {
//      throw new GeneralException("Parsing error.", se);
//    } catch (IOException ie) {
//      throw new GeneralException("File IO error.", ie);
//    }
//
//  }
//
//  public Transformer getReadableTransformer() {
//    return readableXformer;
//  }
//
//  public Transformer getRawTransformer() {
//    return rawXformer;
//  }
//
//  public Document createEmptyDocument()
//    throws GeneralException {
//
//    return getDocumentBuilder().newDocument();
//
//  }
//
//  public Element createVoidElement()
//    throws GeneralException {
//
//    try {
//      return createEmptyDocument().createElement("void");
//    } catch (DOMException de) {
//      throw new GeneralException(de);
//    }
//
//  }
//
//  public List getChildElements(Element e) 
//    throws GeneralException {
//
//    if ( e == null ) { return null; }
//    ArrayList alist = new ArrayList();
//    NodeList list = e.getChildNodes();
//    if ( list.getLength() == 0 ) { return alist; }
//    for (int i = 0; i < list.getLength(); i++) {
//      if ( list.item(i).getNodeType() == Node.ELEMENT_NODE ) {
//	alist.add((Element) list.item(i)); }
//    }
//    return alist;
//
//  }
//
//
//  public String getUnusedPrefix(XmlCursor cursor, String suggestedPrefix) {
//
//    if ( cursor == null ) return null;
//    if ( suggestedPrefix != null && !suggestedPrefix.equals("") &&
//	 cursor.namespaceForPrefix(suggestedPrefix) == null ) {
//      return suggestedPrefix;
//    }
//    int i = 1;
//    String BASE = WSConstants.NS_PREFIX_SOA_INFRATRUCTURE;
//    while ( cursor.namespaceForPrefix(BASE+i) != null ) { i++; }
//    return BASE+i;
//
//  }
//
//  /**
//   * Adds an xsi:type attribute to an XmlObject (or changes the value of its
//   * existing xsi:type attribute.
//   *
//   * @param xobj a <code>XmlObject</code> value
//   * @param xmlType a <code>QName</code> value
//   */
//  public void addOrChangeXsiTypeAttr(XmlObject xobj, QName xmlType) {
//
//    if ( xobj == null || xmlType == null ) return;
//    log.debug("changing type of xobj.xmlText() = " + xobj.xmlText());
//    log.debug("to xmlType = " + xmlType);
//    XmlCursor cursor = xobj.newCursor();
//    cursor.toStartDoc();
//    XmlCursor.TokenType nextType = cursor.toNextToken();
//    if ( !nextType.isStart() ) { // start element tag is "chopped off"
//      cursor.toStartDoc();
//    }
//    String prefix = cursor.prefixForNamespace(xmlType.getNamespaceURI());
//    boolean needToAddPrefixDeclaration = false;
//    if ( prefix.equals("") ) {
//      prefix = getUnusedPrefix(cursor, "default-ns");
//      needToAddPrefixDeclaration = true;
//    }
//    log.debug("prefix = " + prefix);
//    log.debug("default ns = " + cursor.namespaceForPrefix(""));
//    cursor.removeAttribute(new QName(WSConstants.URI_2001_SCHEMA_XSI, "type"));
//    log.debug("after removing xsi:type - xobj.xmlText() = " + xobj.xmlText());
//    // move forward as attr will be inserted BEFORE cursor (per XmlBeans Javadoc)
//    cursor.toNextToken();
//    cursor.insertAttributeWithValue
//      (new QName(WSConstants.URI_2001_SCHEMA_XSI, "type"),
//       prefix + ":" + xmlType.getLocalPart());
//    if ( needToAddPrefixDeclaration ) {
//      cursor.insertNamespace(prefix, xmlType.getNamespaceURI());
//    }
//    log.debug("after inserting new xsi:type - xobj.xmlText() = " + xobj.xmlText());
//
//
//  }
//
//  public void addAttributes(XmlObject xobj, Attributes attrs) {
//
//    if ( xobj == null ||
//	 attrs == null ||
//	 attrs.getLength() == 0 ) return;
//    XmlCursor cursor = xobj.newCursor();
//    // get to start of element
//    while ( !cursor.isStart() ) { cursor.toNextToken(); }
//    // get to a point where can insert attrs
//    if ( !cursor.toLastAttribute() ) {
//      cursor.toNextToken();
//    }
//    for ( int i=0; i < attrs.getLength(); i++ ) {
//      String localPart = attrs.getLocalName(i);
//      String namespaceURI = attrs.getURI(i);
//      String qName = null;
//      if ( namespaceURI == null ||
//	   namespaceURI.equals("") ) {
//	qName = attrs.getQName(i);
//      }
//      String value = attrs.getValue(i);
//      log.debug("localPart = " + localPart);
//      log.debug("namespaceURI = " + namespaceURI);
//      log.debug("qName = " + qName);
//      log.debug("value = " + value);
//      if ( qName == null ) {
//	cursor.insertAttributeWithValue
//	  (new QName(namespaceURI, localPart), value);
//      } else {
//	String[] qNameSplit = qName.split(":");
//	if ( qNameSplit.length > 1 ) {
//	  String prefix = qNameSplit[1];
//	  cursor.insertNamespace(prefix, value);
//	} else {
//	  cursor.insertAttributeWithValue(qName, value);
//	}
//      }
//    }
//
//  }
//
//  /**
//   * If the XmlObject has no document element, (i.e., "chopped off"), then
//   * the returned NameTypeAttrs.name == null.
//   */
//  public NameTypeAttrs getNameTypeAttrs(XmlObject xml) 
//    throws GeneralException {
//
//    if ( xml == null ) return null;
//    log.debug("entered getNameTypeAttrs(XmlObject xml).");
//    log.debug("xml.xmlText() = " + xml.xmlText());
//    NameTypeAttrs nt = new NameTypeAttrs();
//    XmlCursor cursor = xml.newCursor();
//    //    cursor.toStartDoc();
//    if ( cursor.isStartdoc() ) {
//      XmlCursor.TokenType nextType = cursor.toNextToken();
//      if ( !nextType.isStart() ) { // start element tag is "chopped off"
//	cursor.toStartDoc();
//      }
//    } else if ( cursor.isStart() ) {
//      ; // do nothing - this is OK
//    } else {
//      throw new GeneralException(Messages.get("xmlUtilErrXmlObjectNotElement"));
//    }
//    Map nsMap = new HashMap(); 
//    cursor.getAllNamespaces(nsMap);
//    log.debug("Namespace Map:");
//    Iterator debugItr = nsMap.entrySet().iterator();
//    while ( debugItr.hasNext() ) {
//      Map.Entry entry = (Map.Entry) debugItr.next();
//      log.debug("prefix = " + entry.getKey() + ", value = " + entry.getValue());
//    }
//    log.debug("cursor.getName() = " + cursor.getName());
//    // get the Name
//    nt.name = cursor.getName();
//    // get the Type
//    String typeAsString = cursor.getAttributeText
//      (new QName(WSConstants.URI_DEFAULT_SCHEMA_XSI, "type"));
//    if ( typeAsString != null ) {
//      String[] typeSplit = typeAsString.split(":");
//      String typeLocalName;
//      String typeURI;
//      if ( typeSplit.length < 2 ) { // type has no prefix (default namespace)
//	typeLocalName = typeAsString;
//	typeURI = (String) nsMap.get("");
//      } else {
//	typeLocalName = typeSplit[1];
//	typeURI = (String) nsMap.get(typeSplit[0]);
//      }
//      nt.type = new QName(typeURI, typeLocalName);
//    }
//    log.debug("nt.type = " + nt.type);
//    // get the Attributes
//    nt.attrs = new AttributesImpl();
//    while ( cursor.hasNextToken() ) {
//      cursor.toNextToken();
//      if ( cursor.isAnyAttr() ) {
//	QName qName = cursor.getName();
//	if ( cursor.isNamespace() ) {
//	  String nsPrefix = qName.getLocalPart();
//	  String value = qName.getNamespaceURI();
//	  String localName = "xmlns";
//	  if ( nsPrefix != null ) {
//	    localName += ":"+nsPrefix;
//	  }
//	  nt.attrs.addAttribute("",localName,"","CDATA",value);
//	} else { // isAttr
//	  String value = cursor.getTextValue();
//	  String localName = qName.getLocalPart();
//	  String attrNS = qName.getNamespaceURI();
//	  String prefix = "";
//	  Iterator itr = nsMap.entrySet().iterator();
//	  while ( itr.hasNext() ) {
//	    Map.Entry entry = (Map.Entry) itr.next();
//	    if ( attrNS.equals(entry.getValue()) ) {
//	      prefix = (String) entry.getKey();
//	      break;
//	    }
//	  }
//	  nt.attrs.addAttribute(attrNS,localName,prefix,"CDATA",value);
//	}
//      }
//    }
//
//    return nt;
//
//  }
//
//  public SchemaType getSimpleSchemaTypeFromQName(QName type)
//    throws GeneralException {
//
//    if ( type == null ) {
//      throw new GeneralException(Messages.get("generalNullEmptyParam"));
//    }
//    if ( type.equals(WSConstants.XSD_STRING) ) {
//      return org.apache.xmlbeans.XmlString.type;
//    } else if ( type.equals(WSConstants.XSD_BOOLEAN) ) {
//      return org.apache.xmlbeans.XmlBoolean.type;
//    } else if ( type.equals(WSConstants.XSD_DECIMAL) ) {
//      return org.apache.xmlbeans.XmlDecimal.type;
//    } else if ( type.equals(WSConstants.XSD_FLOAT) ) {
//      return org.apache.xmlbeans.XmlFloat.type;
//    } else if ( type.equals(WSConstants.XSD_DOUBLE) ) {
//      return org.apache.xmlbeans.XmlDouble.type;
//    } else if ( type.equals(WSConstants.XSD_DURATION) ) {
//      return org.apache.xmlbeans.XmlDuration.type;
//    } else if ( type.equals(WSConstants.XSD_DATETIME) ) {
//      return org.apache.xmlbeans.XmlDateTime.type;
//    } else if ( type.equals(WSConstants.XSD_TIME) ) {
//      return org.apache.xmlbeans.XmlTime.type;
//    } else if ( type.equals(WSConstants.XSD_DATE) ) {
//      return org.apache.xmlbeans.XmlDate.type;
//    } else if ( type.equals(WSConstants.XSD_GYEARMONTH) ) {
//      return org.apache.xmlbeans.XmlGYearMonth.type;
//    } else if ( type.equals(WSConstants.XSD_GYEAR) ) {
//      return org.apache.xmlbeans.XmlGYear.type;
//    } else if ( type.equals(WSConstants.XSD_GMONTHDAY) ) {
//      return org.apache.xmlbeans.XmlGMonthDay.type;
//    } else if ( type.equals(WSConstants.XSD_GDAY) ) {
//      return org.apache.xmlbeans.XmlGDay.type;
//    } else if ( type.equals(WSConstants.XSD_GMONTH) ) {
//      return org.apache.xmlbeans.XmlGMonth.type;
//    } else if ( type.equals(WSConstants.XSD_HEXBIN) ) {
//      return org.apache.xmlbeans.XmlHexBinary.type;
//    } else if ( type.equals(WSConstants.XSD_BASE64) ) {
//      return org.apache.xmlbeans.XmlBase64Binary.type;
//    } else if ( type.equals(WSConstants.XSD_ANYURI) ) {
//      return org.apache.xmlbeans.XmlAnyURI.type;
//    } else if ( type.equals(WSConstants.XSD_QNAME) ) {
//      return org.apache.xmlbeans.XmlQName.type;
//    } else if ( type.equals(WSConstants.XSD_NOTATION) ) {
//      return org.apache.xmlbeans.XmlNOTATION.type;
//    } else if ( type.equals(WSConstants.XSD_NORMALIZEDSTRING) ) {
//      return org.apache.xmlbeans.XmlNormalizedString.type;
//    } else if ( type.equals(WSConstants.XSD_TOKEN) ) {
//      return org.apache.xmlbeans.XmlToken.type;
//    } else if ( type.equals(WSConstants.XSD_LANGUAGE) ) {
//      return org.apache.xmlbeans.XmlLanguage.type;
//    } else if ( type.equals(WSConstants.XSD_NMTOKEN) ) {
//      return org.apache.xmlbeans.XmlNMTOKEN.type;
//    } else if ( type.equals(WSConstants.XSD_NMTOKENS) ) {
//      return org.apache.xmlbeans.XmlNMTOKENS.type;
//    } else if ( type.equals(WSConstants.XSD_NAME) ) {
//      return org.apache.xmlbeans.XmlName.type;
//    } else if ( type.equals(WSConstants.XSD_NCNAME) ) {
//      return org.apache.xmlbeans.XmlNCName.type;
//    } else if ( type.equals(WSConstants.XSD_ID) ) {
//      return org.apache.xmlbeans.XmlID.type;
//    } else if ( type.equals(WSConstants.XSD_IDREF) ) {
//      return org.apache.xmlbeans.XmlIDREF.type;
//    } else if ( type.equals(WSConstants.XSD_IDREFS) ) {
//      return org.apache.xmlbeans.XmlIDREFS.type;
//    } else if ( type.equals(WSConstants.XSD_ENTITY) ) {
//      return org.apache.xmlbeans.XmlENTITY.type;
//    } else if ( type.equals(WSConstants.XSD_ENTITIES) ) {
//      return org.apache.xmlbeans.XmlENTITIES.type;
//    } else if ( type.equals(WSConstants.XSD_INTEGER) ) {
//      return org.apache.xmlbeans.XmlInteger.type;
//    } else if ( type.equals(WSConstants.XSD_NONPOSITIVEINTEGER) ) {
//      return org.apache.xmlbeans.XmlNonPositiveInteger.type;
//    } else if ( type.equals(WSConstants.XSD_NEGATIVEINTEGER) ) {
//      return org.apache.xmlbeans.XmlNegativeInteger.type;
//    } else if ( type.equals(WSConstants.XSD_LONG) ) {
//      return org.apache.xmlbeans.XmlLong.type;
//    } else if ( type.equals(WSConstants.XSD_INT) ) {
//      return org.apache.xmlbeans.XmlInt.type;
//    } else if ( type.equals(WSConstants.XSD_SHORT) ) {
//      return org.apache.xmlbeans.XmlShort.type;
//    } else if ( type.equals(WSConstants.XSD_BYTE) ) {
//      return org.apache.xmlbeans.XmlByte.type;
//    } else if ( type.equals(WSConstants.XSD_NONNEGATIVEINTEGER) ) {
//      return org.apache.xmlbeans.XmlNonNegativeInteger.type;
//    } else if ( type.equals(WSConstants.XSD_UNSIGNEDLONG) ) {
//      return org.apache.xmlbeans.XmlUnsignedLong.type;
//    } else if ( type.equals(WSConstants.XSD_UNSIGNEDINT) ) {
//      return org.apache.xmlbeans.XmlUnsignedInt.type;
//    } else if ( type.equals(WSConstants.XSD_UNSIGNEDSHORT) ) {
//      return org.apache.xmlbeans.XmlUnsignedShort.type;
//    } else if ( type.equals(WSConstants.XSD_UNSIGNEDBYTE) ) {
//      return org.apache.xmlbeans.XmlUnsignedByte.type;
//    } else if ( type.equals(WSConstants.XSD_POSITIVEINTEGER) ) {
//      return org.apache.xmlbeans.XmlPositiveInteger.type;
//    } else {
//      return null;
//    }
//
//  }
//
//  public class NameTypeAttrs {
//    public QName name = null;
//    public QName type = null;
//    public AttributesImpl attrs = null;
//  }

}
