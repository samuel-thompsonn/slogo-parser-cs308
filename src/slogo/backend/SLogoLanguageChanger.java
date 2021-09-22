package slogo.backend;

/**
 * @author Sam Thompson
 * Changes the language of an Interpreter to the specified value when used on it.
 */
public class SLogoLanguageChanger implements Changer {
  private String myLanguage;

  /**
   * Constructor - sets the language to be used
   * @param language The name of the language to use. Must match the name of a
   *                 resource file in src/resources.languages/
   */
  public SLogoLanguageChanger(String language) {
    myLanguage = language;
  }

  /**
   * Changes the language of the Interpreter to the value
   * specified in the constructor.
   * @param backEnd The Interpreter to modify settings for.
   */
  @Override
  public void doChanges(Interpreter backEnd) {
    backEnd.setLanguage(myLanguage);
  }
}
