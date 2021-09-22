package slogo.backend.commands.displaycommands;


import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.CommandResult;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Interpreter;

public class GetPenColorQuery extends Command {

    @Override
    public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
        BackEndInternal backEnd, Interpreter interpreter) {
        CommandResultBuilder builder = backEnd.startCommandResult(backEnd.getTurtles().get(0).getHeading(), backEnd.getTurtles().get(0).getPosition(), backEnd.getTurtles().get(0).getVisible());
        builder.setRetVal(backEnd.getPathColor());
        return List.of(builder.buildCommandResult());
    }

    @Override
    public List<String> findVars(String[] tokenList) {
        return null;
    }

}
