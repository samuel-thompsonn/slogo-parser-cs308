package slogo.frontEnd;

import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is used to manage the display elements of the history, variables, and user defined commands
 */
public class ClearableEntriesBox extends HBox implements DisplayableTextOwner {

    protected final TextFlow myTextFlow;
    protected final Text descriptionText;
    protected final List<String> entryList;
    protected final VBox rightSide;
    protected final Button clearButton;
    protected final List<Text> displayableEntries = new ArrayList<>();
    private final String myDescriptionKey;
    private String myLanguage;

    private static final double SPACING = 10;

    public ClearableEntriesBox(Rectangle shape, Rectangle clearButtonShape, String descriptionKey, ResourceBundle languageResources){
        myTextFlow = new TextFlow();
        myTextFlow.setPrefWidth(shape.getWidth());
        myTextFlow.setPrefHeight(shape.getHeight());
        myTextFlow.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(myTextFlow);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        clearButton = Visualizer.makeButton("clearEntryBox", clearButtonShape, this, languageResources);
        clearButton.setTooltip(new Tooltip(languageResources.getString("HoverText")));
        clearButton.setOnAction(event -> clearEntryBox());
        rightSide = new VBox(SPACING);
        rightSide.getChildren().add(clearButton);
        this.setSpacing(SPACING);
        this.getChildren().addAll(scrollPane, rightSide);
        myDescriptionKey = descriptionKey;
        descriptionText = new Text(languageResources.getString(descriptionKey) + "\n");
        descriptionText.setUnderline(true);
        descriptionText.setFill(Color.BLUE);
        myTextFlow.getChildren().add(descriptionText);
        myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
        entryList = new ArrayList<>();
        myLanguage = languageResources.getString("LanguageName");
    }

    /**
     * change the language and translate all displayable texts to the new language
     * @param newLanguageResources the new language config to translate with
     */
    @Override
    public void setDisplayableTexts(ResourceBundle newLanguageResources){
        clearButton.setText(newLanguageResources.getString("clearButton"));
        clearButton.setTooltip(new Tooltip(newLanguageResources.getString("HoverText")));
        descriptionText.setText(newLanguageResources.getString(myDescriptionKey) + "\n");
        setChildDisplayableTexts(newLanguageResources);
        myLanguage = newLanguageResources.getString("LanguageName");
    }

    /**
     * Subclasses should override this method. This method exists because subclasses can't override setDisplayableTexts
     * @param languageResources the new language config to translate with
     */
    protected void setChildDisplayableTexts(ResourceBundle languageResources){
        for(int i=0; i<displayableEntries.size(); i++){
            String commandPart = displayableEntries.get(i).getText().substring(entryList.get(i).length()+2);
            displayableEntries.get(i).setText(entryList.get(i) + ": " + translateCommand(commandPart, languageResources));
        }
    }

    /**
     * split it
     * get symbol with source properties file, if it isn't a match don't translate it
     * then translate each command using the destination properties file
     * @param script the series of commands to translate
     * @param languageResources the new language resource bundle
     * @return the translated script
     */
    protected String translateCommand(String script, ResourceBundle languageResources){
        String newLanguageName = languageResources.getString("LanguageName");
        ResourceBundle newLanguageProperties = ResourceBundle.getBundle("resources.languages." + newLanguageName);
        ResourceBundle oldLanguageProperties = ResourceBundle.getBundle("resources.languages." + myLanguage);
        StringBuilder translatedScript = new StringBuilder();
        for(String scriptPiece : script.split("\\s+")) {// split for any whitespace
            boolean foundMatch = false;
            for (String key : oldLanguageProperties.keySet()) {
                if (scriptPiece.matches(oldLanguageProperties.getString(key))) {
                    translatedScript.append(newLanguageProperties.getString(key).split("\\|")[0]).append(" ");
                    foundMatch = true;
                    break;
                }
            }
            if(!foundMatch) {
              translatedScript.append(scriptPiece).append(" ");
            }
        }
        translatedScript.append("\n");
        return translatedScript.toString();
    }

    /**
     * Removes all entries from the box and its display
     */
    protected void clearEntryBox(){
        myTextFlow.getChildren().clear();
        myTextFlow.getChildren().add(descriptionText);
        myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
        entryList.clear();
        displayableEntries.clear();
    }

    /**
     * Takes in the latest user entry and stores it so it can be displayed. Deletes old entry if necessary
     * @param entry the string to be added to the displayed entries
     * @param name the name of the entry that needs to be overwritten (or null)
     * @param action the lambda defining what happens when the entry is clicked
     */
    protected void addEntry(String entry, String name, Consumer<String> action) {
        myTextFlow.getChildren().remove(myTextFlow.getChildren().size() - 1);
        Text newText = new Text(entry + "\n");
        newText.setOnMouseClicked(event -> action.accept(newText.getText()));
        myTextFlow.getChildren().add(newText);
        displayableEntries.add(newText);
        checkDuplicates(name);
    }

    // not part of the API
    protected void checkDuplicates(String name){
        myTextFlow.getChildren().add(new Text("\n\n\n\n\n"));
        if(name != null){
            for(int i=0; i<entryList.size(); i++){
                if(name.equals(entryList.get(i))){
                    myTextFlow.getChildren().remove(i+1); // add 1 to account for description text
                    entryList.remove(i);
                    displayableEntries.remove(i);
                    break;
                }
            }
            entryList.add(name);
            // note that we add name, not entry, because we want to store only the NAME not the full text entry
        }
    }
}
