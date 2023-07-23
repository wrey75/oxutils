package com.oxande.commons.oxutils;

import org.w3c.dom.*;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlHelper {

    public static List<Element> children(Element xml) {
        List<Element> ret = new ArrayList<>();
        NodeList list = xml.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ret.add((Element) node);
            }
        }
        return ret;
    }

    public static List<Element> children(Element xml, String filter) {
        return children(xml).stream()
                .filter(node -> node.getNodeName().equalsIgnoreCase(filter))
                .collect(Collectors.toList());
    }

    public static Element child(Element xml, String filter) {
        List<Element> ret = children(xml, filter);
        if (ret.size() == 1) {
            return ret.get(0);
        } else if (ret.isEmpty()) {
            return null;
        } else {
            throw new UnsupportedOperationException("Tag <" + xml.getNodeName() + "> is expected to have exactly 1 child <" + filter + "> (" + ret.size() + " found)");
        }
    }

    /**
     * Converts a tag element to a map and using list for child contents
     *
     * @param element the XML element
     * @return the {@link Bindings}.
     */
    public static Bindings bindXML(Element element) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.putAll(mapXml(element));
        return bindings;
    }

    public static Map<String, Object> mapXml(Element element) {
        Map<String, Object> map = new SimpleBindings();
        NamedNodeMap attributeMap = element.getAttributes();
        map.put("_name", element.getTagName());
        for (int i = 0; i < attributeMap.getLength(); i++) {
            Attr attribute = (Attr) attributeMap.item(i);
            map.put(attribute.getName(), attribute.getValue());
        }
        List<Map<String, Object>> list = children(element).stream()
                .map(XmlHelper::mapXml)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            map.put("_text", element.getTextContent());
        } else {
            map.put("_contents", list);
        }
        return map;
    }
}
