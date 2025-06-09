package com.portal.bid.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SAMLResponseParser {
    /**
     * Extracts the email from a SAML Response XML using robust XML parsing.
     * 
     * @param samlResponse The full SAML Response XML string
     * @return Extracted email address or null if not found
     * @throws Exception If there are issues parsing the XML
     */
    public static String extractEmailFromAssertion(String samlResponse) {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // Parse the SAML Response XML
            Document document = builder.parse(new InputSource(new StringReader(samlResponse)));
            
            // Create XPath object
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            
            // Define namespace mappings
            xpath.setNamespaceContext(new SAMLNamespaceContext());
            
            // XPath query to find the NameID element
            String xpathExpression = "//saml2:NameID";
            NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, document, XPathConstants.NODESET);
            
            // Return the first found NameID text content
            if (nodes != null && nodes.getLength() > 0) {
                return nodes.item(0).getTextContent().trim();
            }
            
            return null;
        } catch (Exception e) {
            // Log the error or handle it appropriately
            System.err.println("Error extracting email from SAML Response: " + e.getMessage());
            throw new RuntimeException("Failed to parse SAML Response", e);
        }
    }
    
    /**
     * Custom Namespace Context to handle SAML namespaces
     */
    private static class SAMLNamespaceContext implements javax.xml.namespace.NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            switch (prefix) {
                case "saml2p": return "urn:oasis:names:tc:SAML:2.0:protocol";
                case "saml2": return "urn:oasis:names:tc:SAML:2.0:assertion";
                case "ds": return "http://www.w3.org/2000/09/xmldsig#";
                default: return null;
            }
        }
        
        @Override
        public String getPrefix(String namespaceURI) {
            // Not needed for our use case
            return null;
        }
        
        @Override
        public java.util.Iterator<String> getPrefixes(String namespaceURI) {
            // Not needed for our use case
            return null;
        }
    }
    
    // Example usage method
    public static void main(String[] args) {
        String samlResponse = "your SAML response XML string here";
        String email = extractEmailFromAssertion(samlResponse);
        System.out.println("Extracted Email: " + email);
    }
}