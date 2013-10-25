<?xml version="1.0" encoding="UTF-8"?>
<!--! <example xn="xslt"> -->
<!--! <c>chap04</c><s>sfwj</s> -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:custinfo="http://www.example.com/css/custinfo"
  xmlns:corp="http://www.example.com/corp">
  <xsl:output method="xml" version="1.0" encoding="UTF-8"/>
  <xsl:template match="custinfo:address">
    <address>
      <city><xsl:value-of select="corp:city"/></city>
      <phoneNumber>
        <areaCode><xsl:value-of
        select="substring-before(substring-after(corp:phone,'('),')')"
        /></areaCode>
        <exchange><xsl:value-of select="normalize-space(
        substring-before(substring-after(corp:phone,')'),'-'))"
        /></exchange>
        <number><xsl:value-of select="normalize-space(
          substring-after(corp:phone,'-'))"/></number>
      </phoneNumber>
      <state><xsl:value-of select="corp:state"/></state>
      <streetName><xsl:value-of select="substring-after(corp:addrLine1,' ')"
      />  - <xsl:value-of select="corp:addrLine2"/></streetName>
      <streetNum><xsl:value-of select="substring-before(corp:addrLine1,' ')"
      /></streetNum>
      <zip><xsl:value-of select="corp:zip"/></zip>
    </address>
  </xsl:template>
</xsl:stylesheet>
<!--! </example> -->  