package slogo.backend;

import java.lang.reflect.Constructor;
import java.util.ResourceBundle;
import static java.lang.Class.forName;

/**
 * @author Sam Thompson, James Rumsey
 * Handles creating instances of specific commands based on the name of the command. Relies
 * on slogo/backend/resources/commands.properties.
 * Doesn't need to be changed when adding a new command, as long as that command is included in the
 * resource file.
 */
public class CommandFactory {
  private static final String RESOURCES_PACKAGE = "slogo/backend/resources/commands";
  private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCES_PACKAGE);
  private static final String PATH_TO_CLASSES = "slogo.backend.commands.";

  private CommandFactory() {
    //This constructor exists to hide the implicit public constructor that would otherwise appear
  }


  /**
   * Before using this factory, the command should be identified using the properties files
   * that map commands to their names in a language. User-defined commands must be handled
   * separately.
   * @param type The String identifier for the command as listed in the left hand column of
   *             command properties files.
   * @return An instance of the Command referred by the given type.
   * @throws ParseException
   */
  public static Command makeCommand(String type) throws ParseException {
    try{
      String className = RESOURCES.getString(type);
      Class cls = forName(PATH_TO_CLASSES + className);
      Constructor cons = cls.getConstructor();
      Object obj = cons.newInstance();
      return (Command) obj;
    }
    //Catching generic exception because there are six different
    // types of exception and they all are symptoms of a command not existing or being
    // properly defined (i.e. no valid constructor).
    catch(Exception e){
      throw new ParseException("Don't know how to " + type);
    }
  }

  /**
   * @param type The String identifier for the command as listed in the left hand column of
               command properties files.
   * @return True if there exists a command with the given identifier. If 'false' is returned,
   * then running makeCommand with the given String will throw an exception.
   */
  public static boolean hasCommand(String type) {
    return RESOURCES.containsKey(type);
  }
}
