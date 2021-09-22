package slogo.frontEnd;

import java.util.ResourceBundle;

/**
 * An interface for a component that has displayable text that is language dependent
 */
interface DisplayableTextOwner {

  /**
   * change the language and translate all displayable texts to the new language
   * @param languageResources the new language config file to translate with
   */
  void setDisplayableTexts(ResourceBundle languageResources);
}
