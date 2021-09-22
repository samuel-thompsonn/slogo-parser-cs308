package slogo.backend;

interface Changer {

  /**
   * Modifies the given Interpreter in some way based on the type of Changer.
   * @param backEnd The Interpreter to modify settings for.
   */
  void doChanges(Interpreter backEnd);
}
