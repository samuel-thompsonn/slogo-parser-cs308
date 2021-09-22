package slogo.controller;

import slogo.CommandResult;

/**
 * Allows the implementing class to be 'subscribed' to updates whenever the model changes,
 * without the model knowing what the class is. Classes with this interface must have
 * some behavior for interpreting command results.
 */
public interface ModelListener {

  /**
   * Handles updating the subscribed object based on the entire model's state whenever
   * there is a change in the model (usually do to a command executing).
   * @param update The new state information of the command, bundled into a CommandResult.
   *               Could be replaced later with the individual pieces of information if
   *               CommandResults prove inflexible or create unneeded couplings.
   */
  public void handleModelUpdate(CommandResult update);
}
