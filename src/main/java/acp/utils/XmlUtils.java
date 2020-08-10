package acp.utils;

import java.io.StringWriter;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XmlUtils {

  public static String xml2string(Document doc) {
    Transformer transformer = null;
    try {
      transformer = TransformerFactory.newInstance().newTransformer();
    } catch (TransformerConfigurationException e1) {
      e1.printStackTrace();
    } catch (TransformerFactoryConfigurationError e1) {
      e1.printStackTrace();
    }
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    // Удаление лишней информации (текст между тегами, комментарии)
    Element root = doc.getDocumentElement();
    NodeList childs = root.getChildNodes();
    trimChilds(childs);
    doc.normalizeDocument();

    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new StringWriter());
    try {
      transformer.transform(source, result);
    } catch (TransformerException e1) {
      e1.printStackTrace();
    }
    String xmlString = result.getWriter().toString();
    return xmlString;
  }

  public static void trimChilds(NodeList nodes) {
    for (int i = 0; i < nodes.getLength(); i++) {
      Node nd = nodes.item(i);
      if ((nd instanceof Text || nd instanceof Comment)) {
        nd.setNodeValue(nd.getNodeValue().trim());
      }
      if (nd.getChildNodes().getLength() > 0) {
        trimChilds(nd.getChildNodes());
      }
    }
  }

  public static boolean existChild(Node parent, String childName) {
    NodeList childs = parent.getChildNodes();
    for (int i = 0; i < childs.getLength(); i++) {
      String chName = childs.item(i).getNodeName();
      if (chName.equals(childName)) {
        return true;
      }
    }
    return false;
  }

  public static boolean existAttr(Node nd, String attrName) {
    NamedNodeMap attrs = nd.getAttributes();
    Node attrNode = attrs.getNamedItem(attrName);
    if (attrNode != null) {
      return true;
    }
    return false;
  }

  public static boolean isValidNode(Node node) {
    if (node instanceof Text || node instanceof Comment) {
      return false;
    }
    return true;
  }

  public static ArrayList<Node> getChildren(Node parent) {
    ArrayList<Node> childNodes = new ArrayList<>();
    NodeList children = parent.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      if (isValidNode(node)) {
        childNodes.add(node);
      }
    }
    return childNodes;
  }

  public static Node getChild(Node parent, int n) {
    ArrayList<Node> children = getChildren(parent);
    if (n < 0 || n >= children.size()) {
      return null;
    }
    return children.get(n);
  }

  public static void deleteChild(Node parent, int n) {
    Node node = getChild(parent, n);
    if (node != null) {
      parent.removeChild(node);
    }
  }

  public static void deleteAttr(Node node, int n) {
    NamedNodeMap attrs = node.getAttributes();
    String attrName = attrs.item(n).getNodeName();
    attrs.removeNamedItem(attrName);
  }

  // public Element getBaby(Element parent, String childName) {
  //   NodeList nlist = parent.getElementsByTagName(childName);
  //   Element child = (Element) nlist.item(0);
  //   return child;
  // }

  // Методы обработки файла fieldvalid.properties

  public static ArrayList<String> getValidFields(String strKey, int modeProp) {
    ArrayList<String> res = new ArrayList<>();
    String fldValids = FieldValid.getString(strKey);
    if (fldValids != null) {
      StringTokenizer token = new StringTokenizer(fldValids, ",");
      while (token.hasMoreElements()) {
        String key = (String) token.nextElement();
        String fldValidsKey = FieldValid.getString(key);
        switch (modeProp) {
        case (0): // атрибут
          if (fldValidsKey == null) {
            res.add(key);
          }
          break;
        case (1): // параграф
          if (fldValidsKey != null) {
            res.add(key);
            break;
          }
          break;
        }
      }
    }
    return res;
  }

  public static ArrayList<String> getValidNodes(String strKey) {
    ArrayList<String> res = getValidFields(strKey, 1);
    return res;
  }

  public static ArrayList<String> getValidAttrs(String strKey) {
    ArrayList<String> res = getValidFields(strKey, 0);
    return res;
  }

  public static boolean existValidNode(String strKey, String childName) {
    ArrayList<String> validNodes = getValidNodes(strKey);
    for (String validNode : validNodes) {
      if (validNode.equals(childName)) {
        return true;
      }
    }
    return false;
  }

  public static boolean existValidAttr(String strKey, String attrName) {
    ArrayList<String> validAttrs = getValidAttrs(strKey);
    for (String validAttr : validAttrs) {
      if (validAttr.equals(attrName)) {
        return true;
      }
    }
    return false;
  }

  public static ArrayList<String> getNewNodes(Node node) {
    ArrayList<String> newFields = new ArrayList<>();
    String nodeName = node.getNodeName();
    ArrayList<String> validFields = getValidNodes(nodeName);
    for (String fld : validFields) {
      if (fld.equals("field")) {
        newFields.add(fld);
      } else if (existChild(node, fld) == false) {
        newFields.add(fld);
      }
    }
    return newFields;
  }

  public static ArrayList<String> getNewAttrs(Node node) {
    ArrayList<String> newFields = new ArrayList<>();
    String nodeName = node.getNodeName();
    ArrayList<String> validFields = getValidAttrs(nodeName);
    for (String fld : validFields) {
      if (existAttr(node, fld) == false) {
        newFields.add(fld);
      }
    }
    return newFields;
  }

  public static ArrayList<String> getOldNodes(Node node) {
    ArrayList<String> newFields = new ArrayList<>();
    String nodeName = node.getNodeName();
    ArrayList<String> validFields = getValidNodes(nodeName);
    for (String fld : validFields) {
      if (existChild(node, fld) == true) {
        newFields.add(fld);
      }
    }
    return newFields;
  }

  public static ArrayList<String> getOldAttrs(Node node) {
    ArrayList<String> newFields = new ArrayList<>();
    String nodeName = node.getNodeName();
    ArrayList<String> validFields = getValidAttrs(nodeName);
    for (String fld : validFields) {
      if (existAttr(node, fld) == true) {
        newFields.add(fld);
      }
    }
    return newFields;
  }

  public static int getCountNewNodes(Node node) {
    ArrayList<String> newFields = getNewNodes(node);
    return newFields.size();
  }

  public static int getCountNewAttrs(Node node) {
    ArrayList<String> newFields = getNewAttrs(node);
    return newFields.size();
  }

}
