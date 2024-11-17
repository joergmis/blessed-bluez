package com.welie.blessed.bluez;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utility for various XML operations.
 *
 * @author hypfvieh
 *
 */
public final class XmlHelper {

    private XmlHelper() {

    }

    /**
     * Create a new {@link Document} instance from the given string, disabling validation.
     * @param _xmlStr xml string
     * @return document, never null
     * @throws IOException on error
     */
    public static Document parseXmlString(String _xmlStr) throws IOException {

        DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
        dbFac.setNamespaceAware(false);
        dbFac.setValidating(false);
        try {
            // disable all validation stuff
            dbFac.setFeature("http://xml.org/sax/features/namespaces", false);
            dbFac.setFeature("http://xml.org/sax/features/validation", false);
            dbFac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbFac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            return dbFac.newDocumentBuilder().parse(new ByteArrayInputStream(_xmlStr.getBytes(StandardCharsets.UTF_8)));

        } catch (IOException _ex) {
            throw _ex;
        } catch (Exception _ex) {
            String logString = _xmlStr;
            if (_xmlStr.length() > 250) {
                logString = _xmlStr.substring(0, 249);
            }
            throw new IOException("Failed to parse " + logString, _ex);
        }

    }

    /**
     * Use an Xpath expression on the given node or document.
     *
     * @param _xpathExpression xpath expression string
     * @param _xmlDocumentOrNode a {@link Document} or {@link Node} object
     * @return NodeList never null
     * @throws IOException on error
     */
    public static NodeList applyXpathExpressionToDocument(String _xpathExpression, Node _xmlDocumentOrNode)
            throws IOException {

        XPathFactory xfactory = XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();
        XPathExpression expr;
        try {
            expr = xpath.compile(_xpathExpression);
        } catch (XPathExpressionException _ex) {
            throw new IOException(_ex);
        }

        Object result;
        try {
            result = expr.evaluate(_xmlDocumentOrNode, XPathConstants.NODESET);
        } catch (Exception _ex) {
            throw new IOException(_ex);
        }

        return (NodeList) result;
    }
}
