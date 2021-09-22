package slogo.frontEnd;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;



/**
 * holds all the visualizer's nodes with displayable text so the visualizer can easily update their texts all at once
 */
public class DisplayableTextHolder implements DisplayableTextOwner{

  private final Map<MenuItem, String> menuItems = new HashMap<>();
  private final Map<Menu, String> menus = new HashMap<>();
  private final Map<Text, String> texts = new HashMap<>();
  private final Map<Button, String> buttons = new HashMap<>();

  /**
   * change the language and translate all displayable texts to the new language
   * @param languageResources the new language config file to translate with
   */
  @Override
  public void setDisplayableTexts(ResourceBundle languageResources) {
    for(Map.Entry<MenuItem, String> entry : menuItems.entrySet()){
      if(Visualizer.isNonNumeric(entry.getValue()))
      entry.getKey().setText(languageResources.getString(entry.getValue()));
    }
    for(Map.Entry<Menu, String> entry : menus.entrySet()){
      entry.getKey().setText(languageResources.getString(entry.getValue()));
    }
    for(Map.Entry<Text, String> entry : texts.entrySet()){
      entry.getKey().setText(languageResources.getString(entry.getValue()));
    }
    for(Map.Entry<Button, String> entry : buttons.entrySet()){
      entry.getKey().setText(languageResources.getString(entry.getValue()));
    }
  }

  protected void addMenuItem(MenuItem menuItem, String textKey){
    menuItems.put(menuItem, textKey);
  }

  protected void addMenu(Menu menu, String textKey){
    menus.put(menu, textKey);
  }

  protected void addText(Text text, String textKey){
    texts.put(text, textKey);
  }

  protected void addButton(Button button, String textKey){
    buttons.put(button, textKey);
  }
}
