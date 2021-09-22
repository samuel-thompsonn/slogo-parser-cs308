package slogo.backend;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Sam Thompson
 * Exports user-defined commands and variables as an XML file in the format used in
 * "data/userlibraries/outputTest.xml".
 * Used by an implementation of BackEndInternal in order to provide file i/o functionality.
 */
public class SLogoFileBuilder implements FileBuilder{
  private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("slogo/backend/resources/filebuilder");
  public static final String COMMAND_LABEL = resourceBundle.getString("CommandLabel");
  public static final String VARIABLE_NAME_LABEL = resourceBundle.getString("VariableNameLabel");
  public static final String COMMAND_NAME_LABEL = resourceBundle.getString("CommandNameLabel");
  public static final String COMMAND_CONTENTS_LABEL = resourceBundle.getString("CommandContentsLabel");
  public static final String VARIABLE_LABEL = resourceBundle.getString("VariableLabel");
  public static final String VARIABLE_VALUE_LABEL = resourceBundle.getString("VariableValueLabel");
  public static final String USER_LIBRARY_LABEL = resourceBundle.getString("UserLibraryLabel");
  public static final String INVALID_DATA_LABEL = resourceBundle.getString("InvalidDataLabel");
  public static final String COMMAND_ARGUMENT_LABEL = resourceBundle.getString("CommandArgumentLabel");
  public static final String COMMAND_ARGUMENT_NAME_LABEL = resourceBundle.getString("CommandArgumentNameLabel");
  public static final String VARIABLE_LIST_LABEL = resourceBundle.getString("VariableListLabel");
  public static final String COMMAND_LIST_LABEL = resourceBundle.getString("CommandListLabel");
  public static final String MISALIGNED_ERROR_MESSAGE = resourceBundle.getString("MisalignedErrorMessage");

  /**
   * Creates a file that contains all the information about user-defined commands and
   * variables given.
   * @param xmlFilePath The filepath (including the filename) where the XML file will be
   *                    created. If a file is there, it is overridden.
   * @param varMap A Map that links variable names to variable values.
   * @param argMap A Map that links command names to a list of argument names.
   * @param instructionMap A Map that links command names to a String script that is run when
   *                       the command is called.
   */
  public void makeXMLFile(String xmlFilePath, Map<String,Double> varMap, Map<String, List<String>> argMap, Map<String,String> instructionMap) {
    try {
      Document doc = instantiateDocument();
      Element userLib = doc.createElement(USER_LIBRARY_LABEL);
      try {
        Element varList = constructCommandList(argMap, instructionMap,doc);
        userLib.appendChild(varList);
      } catch (FileBuilderException e) {
        Element errorInfo = doc.createElement(INVALID_DATA_LABEL);
        userLib.appendChild(errorInfo);
      }
      Element commandList = constructVarList(varMap,doc);;
      doc.appendChild(userLib);
      userLib.appendChild(commandList);

      File xmlFile = new File(xmlFilePath);
      StreamResult streamResult = new StreamResult(xmlFile);
      DOMSource domSource = new DOMSource(doc);

      makeTransformer().transform(domSource,streamResult);
    } catch (ParserConfigurationException | TransformerException e) {
      //Failed to make XMLFile due to factors outside the scope of the program to fix.
    }
  }

  private Transformer makeTransformer() throws TransformerConfigurationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    return transformer;
  }

  private Element makeCommandElement(Document doc,  String name, List<String> arguments, String contents) {
    Element command = doc.createElement(COMMAND_LABEL);
    command.setAttribute(COMMAND_NAME_LABEL, name);
    command.setAttribute(COMMAND_CONTENTS_LABEL, contents);
    for (String arg : arguments) {
      Element enumArg = doc.createElement(COMMAND_ARGUMENT_LABEL);
      enumArg.setAttribute(COMMAND_ARGUMENT_NAME_LABEL,arg);
      command.appendChild(enumArg);
    }
    return command;
  }

  private Element makeVariableElement(Document doc, String name, String value) {
    Element command = doc.createElement(VARIABLE_LABEL);
    command.setAttribute(VARIABLE_NAME_LABEL, name);
    command.setAttribute(VARIABLE_VALUE_LABEL, value);
    return command;
  }

  private Element constructVarList(Map<String,Double> varMap, Document doc) {
    Element varList = doc.createElement(VARIABLE_LIST_LABEL);
    for (Entry variable : varMap.entrySet()) {
      varList.appendChild(makeVariableElement(doc,variable.getKey().toString(), variable.getValue().toString()));
    }
    return varList;
  }

  private Element constructCommandList(Map<String,List<String>> argMap, Map<String,String> instructionMap, Document doc)
      throws FileBuilderException {
    //Traverses through by key instead of by entry because there are two maps that
    //share a keySet. An error is thrown and handled if this isn't the case.
    if (!instructionMap.keySet().equals(argMap.keySet())) {
      throw new FileBuilderException(MISALIGNED_ERROR_MESSAGE);
    }
    Element commandList = doc.createElement(COMMAND_LIST_LABEL);
    for (String varName : instructionMap.keySet()) {
      commandList.appendChild(makeCommandElement(doc,varName, argMap.get(varName), instructionMap.get(varName)));
    }
    return commandList;
  }

  @Override
  public Map<String,String> loadCommandInstructions(String filepath) {
    File info = new File(filepath);
    try {
      NodeList commandsInFile = getNodesWithTag(info, COMMAND_LABEL);
      Map<String,String> commandInstructions = new HashMap<>();
      for (int i = 0; i < commandsInFile.getLength(); i ++) {
        String cmdName = commandsInFile.item(i).getAttributes().getNamedItem(COMMAND_NAME_LABEL).getTextContent();
        String content = commandsInFile.item(i).getAttributes().getNamedItem(COMMAND_CONTENTS_LABEL).getTextContent();
        commandInstructions.put(cmdName,content);
      }
      return commandInstructions;
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  @Override
  public Map<String,List<String>> loadCommandArguments(String filepath) {
    File info = new File(filepath);
    try {
      NodeList commandsInFile = getNodesWithTag(info, COMMAND_LABEL);
      Map<String,List<String>> commandArguments = new HashMap<>();
      for (int i = 0; i < commandsInFile.getLength(); i ++) {
        Entry<String,List<String>> argEntry = makeCommandArgumentEntry(commandsInFile.item(i));
        commandArguments.put(argEntry.getKey(),argEntry.getValue());
      }
      return commandArguments;
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  private Entry<String, List<String>> makeCommandArgumentEntry(Node command) {
    String cmdName = command.getAttributes().getNamedItem(COMMAND_NAME_LABEL).getTextContent();
    NodeList argsInCommand = command.getChildNodes();
    List<String> args = new ArrayList<>();
    for (int j = 1; j < argsInCommand.getLength(); j += 2) {
      args.add(argsInCommand.item(j).getAttributes().item(0).getTextContent());
    }
    return new AbstractMap.SimpleEntry(cmdName,args);
  }

  @Override
  public Map<String,Double> loadVariablesFromFile(String filepath) {
    File info = new File(filepath);
    try {
      NodeList variables = getNodesWithTag(info, VARIABLE_LABEL);
      Map<String,Double> vars = new HashMap<>();
      for (int i = 0; i < variables.getLength(); i ++) {
        String varName = variables.item(i).getAttributes().getNamedItem(VARIABLE_NAME_LABEL).getTextContent();
        double varValue = Double.parseDouble(variables.item(i).getAttributes().getNamedItem(
            VARIABLE_VALUE_LABEL).getTextContent());
        vars.put(varName,varValue);
      }
      return vars;
    } catch (SAXException | IOException | ParserConfigurationException e) {
      return new HashMap<>();
    }
  }

  private NodeList getNodesWithTag(File info, String tag)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = docFactory.newDocumentBuilder();
    Document doc = builder.parse(info);
    Element root = doc.getDocumentElement();
    return root.getElementsByTagName(tag);
  }

  @Override
  public void makeLibraryFile(String filePath, Map<String, Double> variables, Map<String, List<String>> commandArgs, Map<String,String> commandContents) {
    makeXMLFile(filePath, variables, commandArgs, commandContents);
  }

  private Document instantiateDocument() throws ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = factory.newDocumentBuilder();
    return docBuilder.newDocument();
  }
}