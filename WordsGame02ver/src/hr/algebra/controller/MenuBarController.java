/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.WordsGameApplication;
import hr.algebra.utils.MessageUtils;
import hr.algebra.utils.ReflectionUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Teodor
 */
public class MenuBarController implements Initializable {

    private static final String DOCUMENTATION_FILENAME = "documentation.html";
    private static final String PACKAGE_LOC = ".\\src\\hr\\algebra";
    private static final String CLASSES_PACKAGE
            = PACKAGE_LOC.substring(PACKAGE_LOC.indexOf("c") + 2).replace("\\", ".").concat(".");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void createDocumentation(ActionEvent event) {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DOCUMENTATION_FILENAME))) {

            String packageArray[] = new File(PACKAGE_LOC).list();

            StringBuilder classAndMembersInfo = new StringBuilder();
            appendStartingHtml(classAndMembersInfo);
            
            classAndMembersInfo
                            .append("<h2><b>DOKUMENTACIJA O PROJEKTU</b></h2>\n\n\n");

            for (String packageName : packageArray) {
                if (!packageName.equals(WordsGameApplication.class.getSimpleName().concat(".java"))) {

                    classAndMembersInfo
                            .append("<h1><b>")
                            .append("Naziv paketa: " + packageName)
                            .append("</h1></b>")
                            .append("\n\n\n\n");
                            

                    String classArray[] = new File(PACKAGE_LOC + "\\"
                            + packageName).list();

                    for (String className : classArray) {
                        classAndMembersInfo
                                .append("<h2>")
                                .append("Naziv klase: " + className)
                                .append("</h2>")
                                .append(System.lineSeparator());

                        if (className.substring(className.indexOf(".") + 1).equals("fxml")) {
                            classAndMembersInfo.append("<p>" + className + " klasa je napisana u XML-u </p>");
                        } else {
                            Class<?> clazz = Class.forName(CLASSES_PACKAGE.concat(packageName).concat("." + className.substring(0, className.indexOf("."))));
                            ReflectionUtils.readClassAndMembersInfo(clazz, classAndMembersInfo);

                        }

                        classAndMembersInfo
                                .append("\n\n\n");
                                
                    }
                } 
            }

            appendEndingHtml(classAndMembersInfo);
            writer.write(classAndMembersInfo.toString());

            MessageUtils.showInfoMessage("Info", "Documentation created!", "Check '" + DOCUMENTATION_FILENAME + "!");

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void appendStartingHtml(StringBuilder sb) {
        sb.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("<title>Dokumentacija projekta</title>\n")
                .append("</head>\n")
                .append("<body>\n");
    }

    private static void appendEndingHtml(StringBuilder sb) {
        sb.append("</body>\n")
                .append("</html>\n");
    }
}

