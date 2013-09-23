package Lib;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Model.Parser;

/**
 * Xml Parser
 */
public class XmlParser implements Parser
{
    /**
     * Xml stream
     */
    private InputStream is;

    /**
     * Parsed xml root
     */
    private Element root;
    
    /**
     * Set input stream
     * 
     * @param is
     */
    public void setContent(InputStream is)
    {
        this.is = is;
    }

    /**
     * Parse
     * 
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public Parser parse() throws Exception
    {
    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = db.parse(is);
		
		root = document.getDocumentElement();

        return this;
    }

    /**
     * Public accessor for getValue()
     * Obscures root node
     *
     * @param key
     * @return string
     */
    public String getValue(String key)
    {
        return getValue(root, key);
    }
    
    /**
     * Recursive function to get node value by key names
     * Eg. "stats.weapons.deagle_shots"
     * 
     * @param node
     * @param key
     * @return string
     */
    private String getValue(Node node, String key)
    {
    	String[] keys = key.split("\\.");
    	NodeList nodes = node.getChildNodes();
		
    	for (int i = 0, j = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) { 
				continue;
			}
			
			Element element = (Element) nodes.item(i);

			if (element.getNodeName().equals(keys[0])) {
				j++;
				
				if (keys.length > 1) {
					if (isInt(keys[1])) {
						if(Integer.parseInt(keys[1]) != j)
							continue;
						else
							return getValue(element, reformKey(keys, 2));
					} else {
						return getValue(element, reformKey(keys, 1));
					}
				} else {
					return element.getTextContent();
				}
			}
		}
		
		return null;
    }
    
    /**
     * Reforms a key, skipping startPos keys at the beginning
     * 
     * @param keys
     * @param startPos
     * @return key
     */
    private String reformKey(String[] keys, int startPos)
    {
    	String key = "";
    	
		for (int i = startPos; i < keys.length; i++) {
			key += keys[i];
			if (i != keys.length) {
				key += ".";
			}
		}
		
		return key;
    }
    
    /**
     * Checks whether a key is an integer (item)
     * 
     * @param str
     */
    private boolean isInt(String str)
    {
    	try {
    		Integer.parseInt(str);
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    }
}