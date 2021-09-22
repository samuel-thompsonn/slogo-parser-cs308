package slogo.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;
import slogo.CommandResult;

/**
 * @author Sam Thompson,
 * Serves as the outward-facing side of the model, and can interpret scripts passed to it by
 * the front end and return appropriate CommandResults. Handles 'meta-commands' such as
 * language changes, undoing, and redoing.
 */
public class SLogoParser implements BackEndExternal, Interpreter{

  public static final String STARTING_LANGUAGE = "English";
  public static final String SYNTAX_FILENAME = "Syntax";
  public static final String NO_MATCH_STRING = "NO MATCH";
  public static final String ERROR_BUNDLE_LOCATION = "slogo/backend/resources/parsererrors";

  private ResourceBundle errorBundle = ResourceBundle.getBundle(ERROR_BUNDLE_LOCATION);
  private final String UNUSED_VALUE_ERROR = errorBundle.getString("UnusedValueError");
  private final String UNKNOWN_COMMAND_ERROR = errorBundle.getString("UnknownCommandError");
  private final String END_OF_SCRIPT_ERROR = errorBundle.getString("EndOfScriptError");

  private BackEndInternal myBackEnd;
  private List<Entry<String, Pattern>> myLanguage;
  private List<Entry<String, Pattern>> mySyntax;

  private List<SLogoMemento> myPrevStates;
  private int myTimelineLocation;

  /**
   * Creates a SLogoParser attached to the given model.
   * Commands that it runs will modify backEnd.
   * @param backEnd The model to be modified by commands run by this Interpreter.
   */
  public SLogoParser(BackEndInternal backEnd) {
    myBackEnd = backEnd;
    myPrevStates = new ArrayList<>();
    myTimelineLocation = -1;
  }

  /**
   * By default, the constructor makes a new SLogoModel.
   */
  public SLogoParser() {
    this(new SLogoModel());
    mySyntax = BackEndUtil.interpretPatterns(SYNTAX_FILENAME);
    setLanguage(STARTING_LANGUAGE);
  }

  /**
   * {@inheritDoc}
   */
  public String getSymbol(String text) {
    return getSymbolInLanguages(text,List.of(myLanguage,mySyntax));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommandResult> parseScript(String script) {
    String[] scriptTokens = BackEndUtil.getTokenList(script).toArray(new String[0]);
    List<CommandResult> retList = parseCommandsList(scriptTokens);
    if (myTimelineLocation < myPrevStates.size()-1) {
      myPrevStates = new ArrayList<>(myPrevStates.subList(0,myTimelineLocation+1));
    }
    myTimelineLocation += 1;
    myPrevStates.add(myBackEnd.saveStateToMemento());
    return retList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommandResult> redo() {
    List<CommandResult> results = new ArrayList<>();
    if (myTimelineLocation < myPrevStates.size()-1) {
      myTimelineLocation += 1;
      results = myBackEnd.loadStateFromMemento(myPrevStates.get(myTimelineLocation), false, true);
    }
    return results;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommandResult> undo() {
    List<CommandResult> results = new ArrayList<>();
    if (myTimelineLocation > 0) {
      myTimelineLocation -= 1;
      results = myBackEnd.loadStateFromMemento(myPrevStates.get(myTimelineLocation),true,false);
    }
    return results;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CommandResult> loadLibraryFile(String filePath) {
    return myBackEnd.loadLibraryFile(filePath);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeLibraryFile(String filePath) {
    myBackEnd.writeLibraryFile(filePath);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applyChanger(Changer changer) {
    changer.doChanges(this);
  }

  /**
   * {@inheritDoc}
   */
  public List<CommandResult> parseCommandsList(String[] tokenList) {
    List<CommandResult> results = new ArrayList<>();
    int programCounter = 0;
    while (programCounter < tokenList.length) {
      try {
        Command command = identifyCommand(tokenList[programCounter]);
        String[] tokensToParse = Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length);
        results.addAll(parseSingleCommand(command, tokensToParse));
        programCounter += results.get(results.size() - 1).getTokensParsed() + 1;
      } catch (ParseException e) {
        CommandResultBuilder builder = myBackEnd.startCommandResult(0);
        builder.setErrorMessage(e.getMessage());
        results.add(builder.buildCommandResult());
        return results;
      }
    }
    return results;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLanguage(String language) {
    myLanguage = BackEndUtil.interpretPatterns(language);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPrimitiveCommand(String command) {
    return CommandFactory.hasCommand(getSymbol(command));
  }

  /**
   * {@inheritDoc}
   */
  public List<CommandResult> parseForRetVal(String[] tokenList) throws ParseException {

    String currentTokenType = getSymbol(tokenList[0]);
    if (isValue(currentTokenType)) {
      return List.of(parseValue(currentTokenType,tokenList[0]));
    }

    Command command = identifyCommand(tokenList[0]);
    return parseSingleCommand(command,
        Arrays.copyOfRange(tokenList, 1, tokenList.length));
  }

  private List<CommandResult> parseSingleCommand(Command command, String[] tokensToParse)
      throws ParseException {
    List<CommandResult> listResult;
    if (command.runsPerTurtle()) {
      listResult = parseCommandPerTurtle(command, tokensToParse);
    } else {
      listResult = parseCommand(command, tokensToParse);
    }
    return listResult;
  }

  private Command identifyCommand(String rawToken) throws ParseException {
    if (isValue(getSymbol(rawToken))) {
      throw new ParseException(UNUSED_VALUE_ERROR + rawToken);
    }
    try {
      return CommandFactory.makeCommand(getSymbol(rawToken));
    } catch (ParseException e) {
      Command command = myBackEnd.getUserCommand(rawToken);
      if (command != null) {
        return command;
      }
      throw new ParseException(UNKNOWN_COMMAND_ERROR + rawToken.toUpperCase());
    }
  }

  private List<CommandResult> parseCommandPerTurtle(Command command, String[] tokenList) throws ParseException {
    List<CommandResult> results = new ArrayList<>();
    for (Turtle activeTurtle : myBackEnd.getActiveTurtles()) {
      myBackEnd.setActiveTurtleID(activeTurtle.getId());
      results.addAll(parseCommand(command,tokenList));
    }
    myBackEnd.setActiveTurtleID(null);
    if (results.isEmpty()) {
      List<CommandResult> nonActionResult = (parseCommand(command,tokenList));
      results.addAll(nonActionResult);
    }
    return results;
  }

  private List<CommandResult> parseCommand(Command command, String[] tokenList)
      throws ParseException {
    Stack<Double> commandValues = new Stack<>();
    List<String> variableNames = getCommandVars(command, tokenList);
    List<CommandResult> results = new ArrayList<>();
    for (int programCounter = variableNames.size(); programCounter <= tokenList.length;
        programCounter++) {
      if (commandValues.size() >= command.getNumArgs()) {
        results.addAll(executeCurrentCommand(command, tokenList, commandValues, variableNames,
            programCounter));
        return results;
      }
      results.addAll(parseNextToken(tokenList,programCounter));
      commandValues.add(results.get(results.size()-1).getReturnVal());
      programCounter += results.get(results.size()-1).getTokensParsed();
    }
    throw new ParseException(END_OF_SCRIPT_ERROR);
  }

  private List<CommandResult> parseNextToken(String[] tokenList, int programCounter) throws ParseException {
    if (programCounter >= tokenList.length) {
      throw new ParseException(END_OF_SCRIPT_ERROR);
    }
    String currentTokenRaw = tokenList[programCounter];
    String currentTokenType = getSymbol(tokenList[programCounter]);
    if (isValue(currentTokenType)) {
      CommandResult valueResult = parseValue(currentTokenType, currentTokenRaw);
      return List.of(valueResult);
    } else {
      List<CommandResult> insideResult = parseCommand(identifyCommand(currentTokenRaw),
          Arrays.copyOfRange(tokenList, programCounter + 1, tokenList.length));
      return insideResult;
    }
  }

  private List<CommandResult> executeCurrentCommand(Command command, String[] tokenList,
      Stack<Double> commandValues, List<String> variableNames, int programCounter)
      throws ParseException {
    List<Double> argList = BackEndUtil.getArgsFromStack(commandValues, command.getNumArgs());
    List<CommandResult> results = new ArrayList<>((command.execute(argList, variableNames,
        Arrays.copyOfRange(tokenList, programCounter, tokenList.length),
        myBackEnd, this)));
    CommandResult lastResult = results.get(results.size() - 1);
    //This breaks the immutability of command results, but not in a way
    //that affects the front end, and it eliminates extraneous command results.
    lastResult.setTokensParsed(lastResult.getTokensParsed()+programCounter);
    return results;
  }

  private List<String> getCommandVars(Command command, String[] tokenList) {
    if (command.getNumVars() > 0) {
      return (command.findVars(tokenList));
    }
    return new ArrayList<>();
  }

  private CommandResult parseValue(String type, String token) throws ParseException {
    CommandResultBuilder builder;
    if (isVariable(type)) {
      builder = myBackEnd.startCommandResult(myBackEnd.getVariable(token.substring(1)));
    } else {
      builder = myBackEnd.startCommandResult(Double.parseDouble(token));
    }
    builder.setIsActualCommand(false);
    return builder.buildCommandResult();
  }

  private boolean isValue(String identity) {
    return identity.equals("Constant") || identity.equals("Variable");
  }

  private boolean isGroupOpen(String identity) {
    return identity.equals("GroupStart");
  }

  private boolean isGroupEnd(String identity) {
    return identity.equals("GroupEnd");
  }

  private boolean isVariable(String identity) {
    return identity.equals("Variable");
  }

  private String getSymbolInLanguages(String text, List<List<Entry<String,Pattern>>> languages) {
    for (List<Entry<String,Pattern>> language : languages) {
      for (Entry<String, Pattern> e : language) {
        if (match(text, e.getValue())) {
          return e.getKey();
        }
      }
    }
    return NO_MATCH_STRING;
  }

  //from the parser spike on the cs308 repo
  private boolean match(String text, Pattern regex) {
    return regex.matcher(text).matches();
  }
}