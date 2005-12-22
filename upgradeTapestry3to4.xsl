<?xml version="1.0"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

 <xsl:output method = "xml" indent = "yes" omit-xml-declaration = "yes" 
 	doctype-public="-//Apache Software Foundation//Tapestry Specification 4.0//EN"
	doctype-system="http://jakarta.apache.org/tapestry/dtd/Tapestry_4_0.dtd"/> 

   <xsl:template match="*">
   		<xsl:copy>
   			<xsl:copy-of select="@*" />
   			<xsl:apply-templates></xsl:apply-templates>
   		</xsl:copy>
   </xsl:template> 
   <xsl:template match="parameter">
   	<parameter>
		<xsl:copy-of select="@*[not(name() = 'type' or name() = 'direction' or name()='persistent')]" />

	</parameter>
   </xsl:template>
   <xsl:template match="property-specification">
   	<property>
	<xsl:copy-of select="@name" />
		<xsl:if test="./@persistent='yes'">
			<xsl:attribute name="persist">session</xsl:attribute>
		</xsl:if>
	</property>
   </xsl:template>
   <xsl:template match="bean/set-property">
   	<set>
		<xsl:copy-of select="@*" />
   		<xsl:apply-templates></xsl:apply-templates>
	</set>
   </xsl:template>
   <xsl:template match="inherited-binding">
   	<binding>
		<xsl:copy-of select="@name" />
		<xsl:value-of select="@parameter-name" />
	</binding>
   </xsl:template>
   <xsl:template match="static-binding">
   	<binding>
		<xsl:copy-of select="@name" />
		literal:<xsl:value-of select="text()" />
	</binding>
   </xsl:template>
   <xsl:template match="private-asset">
   	<asset>
		<xsl:copy-of select="@name" />
		<xsl:attribute name="path">classpath:<xsl:value-of select="@resource-path" /></xsl:attribute></asset>
   </xsl:template>   
   
   </xsl:stylesheet>
