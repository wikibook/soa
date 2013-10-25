<?xml version="1.0" encoding="UTF-8"?>
<!--! <example xn="xslt-ns-and-output"> -->
<!--! <c>chap03</c><s>xslt</s> -->
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:oms="http://www.example.com/oms">
  <xsl:output method="xml" version="1.0" encoding="UTF-8"/>
  <!--! </example> -->
  <!--! <example xn="xslt-CustomerHistoryEntry"> -->
  <!--! <c>chap03</c><s>xslt</s> -->
  <xsl:template match="oms:Orders">
    <CustomerHistoryEntries xmlns="http://www.example.com/css"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.example.com/css
      http://soabook.com/example/css/custhistentries.xsd">
      <xsl:apply-templates/>
    </CustomerHistoryEntries>
  </xsl:template>
  <xsl:template match="oms:Order">
    <CustomerHistoryEntry xmlns="http://www.example.com/css">
      <CustomerNumber>
        <xsl:apply-templates select="./oms:OrderHeader/oms:CUST_NO"/>
      </CustomerNumber>
      <OrderLookupInfo>
        <xsl:apply-templates select="./oms:OrderKey"/>
        <xsl:apply-templates 
          select="./oms:OrderHeader/oms:PURCH_ORD_NO"/>
        <xsl:apply-templates 
          select="./oms:OrderItems/oms:item/oms:ITM_NUMBER"/>
        <xsl:apply-templates select="./oms:OrderText"/>        
      </OrderLookupInfo>
    </CustomerHistoryEntry>
  </xsl:template>
  <!--! </example> -->
  <!--! <example xn="xslt-DetailTemplates"> -->
  <!--! <c>chap03</c><s>xslt</s> -->
  <xsl:template match="oms:CUST_NO">
    <xsl:value-of select="."/>
  </xsl:template>
  <xsl:template match="oms:OrderKey">
    <OrderNumber xmlns="http://www.example.com/css">
      <xsl:value-of select="."/>
    </OrderNumber>
  </xsl:template>
  <xsl:template match="oms:PURCH_ORD_NO">
    <PURCH_ORD_NO xmlns="http://www.example.com/css">
      <xsl:value-of select="."/>
    </PURCH_ORD_NO>
  </xsl:template>
  <xsl:template match="oms:ITM_NUMBER">
    <ITM_NUMBER xmlns="http://www.example.com/css">
      <xsl:value-of select="."/>
    </ITM_NUMBER>
  </xsl:template>
  <xsl:template match="oms:OrderText">
    <OrderText xmlns="http://www.example.com/css">
      <xsl:value-of select="."/>
    </OrderText>
  </xsl:template>
  <!--! </example> -->
</xsl:stylesheet>

