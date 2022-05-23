package server.request;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.Request;

public class RequestExecutor {

    private final CommandAdmin commandAdmin;

    public RequestExecutor(CommandAdmin commandAdmin) {
        this.commandAdmin = commandAdmin;
    }

    public CommandResult execute(Request request) {
        request.getCommand().setCommandAdmin(commandAdmin);
        if (commandAdmin.getCommandsWithoutAuth().containsKey(request.getCommand().getName())) {
            return commandAdmin.executeCommand(request.getCommand(), request.getArgs());
        }
        long checkUserResult = commandAdmin.getDBWorker().checkUser(request.getUser());
        if (checkUserResult < 0) {
            return new CommandResult("DB problems, try again later.");
        }
        if (checkUserResult == 0) {
            return new CommandResult("Sign in/up first, call \"help\" to see list of commands.");
        }
        return commandAdmin.executeCommand(request.getCommand(), request.getArgs());
    }
}
