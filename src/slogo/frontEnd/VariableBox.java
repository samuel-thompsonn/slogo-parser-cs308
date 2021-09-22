package slogo.frontEnd;

import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public class VariableBox extends ClearableEntriesBox implements DisplayableTextOwner{

  private static final Rectangle LABEL_SHAPE = new Rectangle(20, 20);
  private final TextArea valueText;
  private final Text label;

  public VariableBox(Rectangle shape, Rectangle clearButtonShape, String description, ResourceBundle languageResources) {
    super(shape, clearButtonShape, description, languageResources);
    String labelText = languageResources.getString("VariableChangerLabel");
    label = new Text(labelText);
    valueText = new TextArea();
    valueText.setMaxSize(LABEL_SHAPE.getWidth(), LABEL_SHAPE.getHeight());
    rightSide.getChildren().addAll(label, valueText);
  }

  /**
   * change the language and translate all displayable texts to the new language
   * @param languageResources the new language config to translate to
   */
  @Override
  protected void setChildDisplayableTexts(ResourceBundle languageResources) {
    label.setText(languageResources.getString("VariableChangerLabel"));
  }

  /**
   * Takes in the latest entry and stores it so it can be displayed. Deletes old entry if necessary
   * @param entry the name of the entry that needs to be overwritten (or null)
   * @param name the value of the variable
   * @param action the lambda defining what happens when the entry is clicked
   */
  @Override
  protected void addEntry(String entry, String name, Consumer<String> action){
    myTextFlow.getChildren().remove(myTextFlow.getChildren().size()-1);
    Text newText = new Text(entry + "\n");
    newText.setOnMouseClicked(event -> action.accept(valueText.getText()));
    myTextFlow.getChildren().add(newText);
    displayableEntries.add(newText);
    super.checkDuplicates(name);
  }

  @SuppressWarnings("EmptyMethod")
  @Override
  protected void clearEntryBox(){
    super.clearEntryBox();
  }
}
