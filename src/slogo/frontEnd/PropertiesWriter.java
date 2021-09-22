package slogo.frontEnd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

/**
 * The purpose of this class is to be able to save properties to a .properties file for later use.
 * Assuming all the necessary properties are passed as a map into the constructor it will work.
 * Might adjust so there is defaults and then the passed Map can override the defaults.
 */
public class PropertiesWriter {

  private static final int COLOR_MAX = 255;
  private OutputStream myOutput;
  private Properties myProperties;
  private static final Map<String, Color> defaultMap = new HashMap<>(){{
      put("0", Color.WHITE);
      put("1", Color.BLACK);
      put("2", Color.RED);
}};
  /**
   * Constructor that does the writing to a new file
   * @param fileNum the number of the UserConfigurable file to be put in front end resources file. Must end with .properties
   */
  public PropertiesWriter(String fileNum,Map<String, Color> colorPaletteMap) {
    try (OutputStream output = new FileOutputStream("src/slogo/frontEnd/Resources/UserConfigurable"+fileNum+".properties")) {
      myOutput = output;
      myProperties = new Properties();
      saveColorPalette(colorPaletteMap);
    } catch (IOException io) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText(io.getMessage());
      alert.showAndWait();
    }

  }
  public PropertiesWriter(String fileNum){
    this(fileNum, defaultMap);
  }
  private void saveColorPalette(Map<String, Color> colorPaletteMap) throws IOException {
    //System.out.println(colorPaletteMap);
    StringBuilder colors = new StringBuilder();
    // set the properties value
    for (String s : colorPaletteMap.keySet()) {
      Color color = colorPaletteMap.get(s);
      colors.append(s);
      colors.append(',');
      colors.append((int)(color.getRed()* COLOR_MAX));
      colors.append(',');
      colors.append((int)(color.getGreen()* COLOR_MAX));
      colors.append(',');
      colors.append((int)(color.getBlue() * COLOR_MAX));
      colors.append(' ');
      //System.out.println(colors.toString());
    }
    //System.out.println(colors.toString());
    myProperties.setProperty("DefaultPalette", colors.toString());


    // save properties to project root folder
    myProperties.store(myOutput, null);
    myOutput.close();
  }


}