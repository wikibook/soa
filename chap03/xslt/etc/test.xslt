<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:oms="http://www.example.com/oms" xmlns:css="http://www.example.com/css">
  <xsl:output method="xml" version="1.0" encoding="UTF-8"/>
  <xsl:template match="oms:Orders">
    <CustomerHistoryEntries xmlns="http://www.example.com/css"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.example.com/css
      http://www.soabook.com/example/css/custhistentry.xsd">
      <xsl:apply-templates/>
    </CustomerHistoryEntries>
  </xsl:template>
  <xsl:template match="oms:Order">
    <CustomerHistoryEntry xmlns="http://www.example.com/css">
      <CustomerNumber>
        <xsl:apply-templates select="./oms:OrderHeader/oms:CUST_NO"/>
      </CustomerNumber>
    </CustomerHistoryEntry>
  </xsl:template>
  <xsl:template match="oms:CUST_NO">
    <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>
