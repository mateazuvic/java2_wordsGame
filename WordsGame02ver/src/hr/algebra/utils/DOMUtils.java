/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.controller.GameController;
import hr.algebra.model.Step;
import hr.algebra.model.Word;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Teodor
 */
public class DOMUtils {

    

    public static void saveSteps(List<Step> steps) {
        try {
            Document document = createDocument("steps");
            steps.forEach(s -> document.getDocumentElement().appendChild(createStepElement(s, document)));
            saveDocument(document, GameController.getFileNameDOM());
        } catch (TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Document createDocument(String root) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, root, null);
    }

    private static Node createStepElement(Step step, Document document) {
        Element element = document.createElement("step");
        element.appendChild(createElement(document, "writtenword", step.getWrittenWord().getName()));
        element.appendChild(createElement(document, "combination", step.getCombination().toString()));
        return element;
    }
    
    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }
    
    private static Document createDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    public static List<Step> loadSteps() {        
        List<Step> steps = new ArrayList<Step>();
        try { 
            Document document = createDocument(new File(GameController.getFileNameDOM()));
            NodeList nodes = document.getElementsByTagName("step");
            for (int i = 0; i < nodes.getLength(); i++) {
                steps.add(processStepNode((Element) nodes.item(i)));
            }      
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return steps;
    }

    private static Step processStepNode(Element element) { 
        String wordName = element.getElementsByTagName("writtenword").item(0).getTextContent();
        Word word = new Word(wordName, wordName.length());
     
        String combination = element.getElementsByTagName("combination").item(0).getTextContent();
        
      
         return new Step(
                 word,
                 combination
         );

    }

    
    

}
