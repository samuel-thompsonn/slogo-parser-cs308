package slogo.frontEnd;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ResourceBundle;

public class History extends ClearableEntriesBox{

  private int currentlyHighlighted = 0;

  public History(Rectangle shape, Rectangle clearButtonShape, String description, ResourceBundle languageResources) {
    super(shape, clearButtonShape, description, languageResources);
  }

  /**
   * Removes all entries from the box and its display. Only clears if all commands present have been executed (that is,
   *    highlighted command must be the last command present
   */
  @Override
  protected void clearEntryBox(){
    if(currentlyHighlighted == myTextFlow.getChildren().size()-2) {
      myTextFlow.getChildren().clear();
      myTextFlow.getChildren().add(descriptionText);
      myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
      entryList.clear();
      displayableEntries.clear();
      currentlyHighlighted = 0;
    }
  }

  @Override
  protected void setChildDisplayableTexts(ResourceBundle languageResources){
    for(Text text : displayableEntries){
      text.setText(super.translateCommand(text.getText(), languageResources)); //TODO: translate the command
    }
  }

  protected void highlightNext() {
    if(currentlyHighlighted != 0){
      Text toUnhighlight = ((Text) myTextFlow.getChildren().get(currentlyHighlighted));
      toUnhighlight.setFill(Color.BLACK);
      toUnhighlight.setUnderline(false);
    }
    currentlyHighlighted++;
    Text toHighlight = ((Text) myTextFlow.getChildren().get(currentlyHighlighted));
    toHighlight.setFill(Color.GREEN);
    toHighlight.setUnderline(true);
  }
}
