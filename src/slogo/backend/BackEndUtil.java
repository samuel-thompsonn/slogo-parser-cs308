package slogo.backend;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @author Sam Thompson
 * Contains convenient methods for parsing and other back end functionality that are
 * completely stateless and could be useful in multiple places.
 */
public class BackEndUtil {

  public static final String RESOURCES_PACKAGE = "resources/languages/";
  public static final String SYNTAX_FILENAME = "Syntax";
  private static final ResourceBundle SYNTAX_BUNDLE = ResourceBundle.getBundle(RESOURCES_PACKAGE + SYNTAX_FILENAME);
  public static final String NO_MATCH_STRING = "NO MATCH";
  public static final String LIST_END = SYNTAX_BUNDLE.getString("ListEnd");
  public static final String LIST_START = SYNTAX_BUNDLE.getString("ListStart");
  public static final String COMMENT_LINE = "(^#(?s).*|\\s+)"; //The comment line in the resource file was leaving empty strings when splitting
  public static final String NEWLINE = SYNTAX_BUNDLE.getString("Newline");
  public static final String WHITESPACE = SYNTAX_BUNDLE.getString("Whitespace");


  private BackEndUtil() {
    //utility classes should not have public constructors, so this empty
    // constructor removes the implicit public constructor.
  }

  /**
   * @param tokenList The list of tokens to check for distance to the first 'outer' end bracket.
   * @return The number of tokens that have been checked by the time the first 'outer' end bracket
   * has been reached. Example: The input {"fd","50","bk","10","]"} returns 5.
   */
  public static int distanceToEndBracket(String[] tokenList) {
    int extraBrackets = 0;
    for (int i = 0; i < tokenList.length; i ++) {
      String token = tokenList[i];
      if (isOpenBracket(token)) {
        extraBrackets++;
      } else if (isClosedBracket(token)) {
        if (extraBrackets == 0) {
          return i + 1;
        } else {
          extraBrackets--;
        }
      }
    }
    return tokenList.length;
  }

  /**
   * @deprecated
   * @param text The string to check for a matching tag in the Syntax properties file.
   * @return The string identifier in Syntax.properties that matches the given text.
   */
  public static String getSymbol(String text) {
    for (String key : SYNTAX_BUNDLE.keySet()) {
      if (text.matches(SYNTAX_BUNDLE.getString(key))) {
        return key;
      }
    }
    return NO_MATCH_STRING;

  }

  /**
   * Used for debugging only, and has no impact on the running of the program. Prints out
   * the string list of tokens as one line of strings with parentheses surrounding them.
   * @param scriptTokens The tokens to print.
   * @param i The index of the first token to print in the list.
   */
  public static void printRemainingTokens(String[] scriptTokens, int i) {
    String[] remaining = Arrays.copyOfRange(scriptTokens, i, scriptTokens.length);
    for (String string : remaining) {
      System.out.printf("(%s) ", string);
    }
    System.out.println();
  }

  /**
   * Used to transform user-defined command instructions into a form that can be
   * stored in an XML file.
   * @param tokens An array of strings to turn into one string with spaces.
   * @return A single String that includes each token in the given array separated by a space.
   */
  public static String concatStringArray(String[] tokens) {
      StringBuffer sb = new StringBuffer();
      for (String string : tokens) {
        sb.append(string + " ");
      }
      return sb.toString();
  }

  /**
   * Used to transform a script from a single string into individual String tokens. Used when a
   * script is first put into the backend.
   * @param script The script to break up into tokens.
   * @return A list of String tokens representing the individual 'words' of the script that are
   * separated by whitespace. Ignores newlines and comments, and never generates empty tokens.
   */
  public static List<String> getTokenList(String script) {
    String[] scriptLines = script.split(NEWLINE);
    List<String> scriptTokenList = new ArrayList<>();
    for (String line : scriptLines) {
      System.out.println(line);
      if (!line.matches(COMMENT_LINE)) {
        scriptTokenList.addAll(Arrays.asList(line.strip().split(WHITESPACE)));
      }
    }
    return scriptTokenList;
  }

  /**
   * Returns arguments as a list in the correct order, since just popping the stack
   * returns values in the exact opposite of the correct order.
   * @param values The Stack of values that are available to be used by commands.
   * @param numArgs The number of arguments required in the list to be returned.
   * @return A list containing the most recent numArgs values in the stack in the order
   * in which they were added.
   */
  public static List<Double> getArgsFromStack(Stack<Double> values, int numArgs) {
    List<Double> argList = new ArrayList<>();
    for (int arg = 0; arg < numArgs; arg++) {
      argList.add(values.pop());
    }
    Collections.reverse(argList);
    return argList;
  }

  /**
   * @param syntax The filename of the properties file in the src/resources.languages/ folder
   *               to transform into a usable format for matching user input to regex expressions
   *               when parsing commands.
   * @return The contents of the given file, as a list of compiled regex expressions and
   * the associated key values from the left-hand column of the resource file.
   */
  public static List<Entry<String, Pattern>> interpretPatterns(String syntax) {
    List<Entry<String, Pattern>> patterns = new ArrayList<>();
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + syntax);
    for (String key : Collections.list(resources.getKeys())) {
      String regex = resources.getString(key);
      patterns.add(new SimpleEntry<>(key, Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
    }
    return patterns;
  }

  private static boolean isClosedBracket(String text) {
    return (text.matches(LIST_END));
  }
  private static boolean isOpenBracket(String text) {
    return (text.matches(LIST_START));
  }
}
