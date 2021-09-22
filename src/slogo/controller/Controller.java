package slogo.controller;

import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.stage.Stage;
import slogo.backend.BackEndExternal;
import slogo.backend.SLogoLanguageChanger;
import slogo.CommandResult;
import slogo.backend.SLogoParser;
import slogo.frontEnd.Visualizer;

public class Controller extends Application{

    private static final String LANGUAGE_INSTRUCTION = "language:";
    private static final int LI_LENGTH = LANGUAGE_INSTRUCTION.length();
    private static final List<String> UNDO_INSTRUCTIONS = List.of("undo", "chexiao", "annuler", "rückgängigmachen",
            "disfare", "desfazer", "otmenit", "deshacer", "urduundo");
    private static final List<String> REDO_INSTRUCTIONS = List.of("redo", "chongzuo", "refaire", "wiederholen",
            "rifare", "refazer", "povtorit", "rehacer", "urduredo");
  private static final List<String> LOAD_INSTRUCTION = List.of("Load");
  private static final List<String> SAVE_INSTRUCTION = List.of("Save");


  private final List<Visualizer> myVisualizers = new ArrayList<>();
    private final List<BackEndExternal> myModels = new ArrayList<>();
    private int numWorkspaces = 0;

    public static void main (String[] args) {
        launch(Controller.class, args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        myModels.add(new SLogoParser());
        int thisWorkspace = numWorkspaces; // need this variable because we don't want to pass in a dynamic value!
        ListChangeListener<String> instructionQueueListener = c -> processInstructionQueueEvent(thisWorkspace);
        myVisualizers.add(new Visualizer(instructionQueueListener, c-> {
          createNewWorkspace();
          } , thisWorkspace));
        myVisualizers.get(thisWorkspace).start(primaryStage);
        numWorkspaces++;
    }

    private void createNewWorkspace() {
        start(new Stage());
    }

    private void processInstructionQueueEvent(int workspace){
        //System.out.println(workspace);
        String input = myVisualizers.get(workspace).popInstructionQueue();
        if(input.length() >= LI_LENGTH && input.substring(0, LI_LENGTH).equals(LANGUAGE_INSTRUCTION)){
            SLogoLanguageChanger languageChanger = new SLogoLanguageChanger(input.substring(LI_LENGTH+1));
            myModels.get(workspace).applyChanger(languageChanger);
        }
        else {
            List<CommandResult> resultList = new ArrayList<>();
            if(UNDO_INSTRUCTIONS.contains(input)){
                resultList =  myModels.get(workspace).undo();
            } else if(REDO_INSTRUCTIONS.contains(input)){
                resultList =  myModels.get(workspace).redo();
            }
            else if (input.length() > 4 && LOAD_INSTRUCTION.contains(input.substring(0,4))){
              resultList = myModels.get(workspace).loadLibraryFile(input.substring(5)) ;
            }
            else if (input.length() > 4 && SAVE_INSTRUCTION.contains(input.substring(0,4))){
              myModels.get(workspace).writeLibraryFile(input.substring(5));
            }
            else {
                resultList = myModels.get(workspace).parseScript(input);
            }
            for (CommandResult result : resultList) {
                if(result.isActualCommand()) {
                    result.setMyOriginalInstruction(input);
                    myVisualizers.get(workspace).processResult(result);
                }
            }
        }
    }

}
