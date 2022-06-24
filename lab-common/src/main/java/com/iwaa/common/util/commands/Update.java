package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Coordinates;
import com.iwaa.common.util.data.Location;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.sql.SQLException;

import static com.iwaa.common.util.commands.ParametersConstants.COORDINATE_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.ID_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_X_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_Z_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_X_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_Z_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.UPDATE_ARGS;

public class Update extends AbstractCommand {

    public Update(CommandAdmin commandAdmin) {
        super(commandAdmin, "update", "обновить значение элемента коллекции, id которого равен заданному", UPDATE_ARGS);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        Route routeToUpdate = new Route((String) args[0], new Coordinates(Float.parseFloat(args[2].toString()),
                Float.parseFloat(args[COORDINATE_Y_ARGS].toString())),
                new Location(Long.parseLong(args[LOCATION_FROM_X_ARGS].toString()), Integer.parseInt(args[LOCATION_FROM_Y_ARGS].toString()),
                        Integer.parseInt(args[LOCATION_FROM_Z_ARGS].toString())), new Location(Long.parseLong(args[LOCATION_TO_X_ARGS].toString()),
                Integer.parseInt(args[LOCATION_TO_Y_ARGS].toString()), Integer.parseInt(args[LOCATION_TO_Z_ARGS].toString())), Long.parseLong(args[1].toString()));
        routeToUpdate.setId(Long.parseLong(args[ID_ARGS].toString()));
        return new Object[]{routeToUpdate, args[UPDATE_ARGS]};
    }

    @Override
    public CommandResult execute(Object[] args) {
        try {
            Route updatedRoute = (Route) args[0];
            User user = (User) args[1];
            updatedRoute.setAuthor((user.getLogin()));
            getCommandManager().getDBWorker().updateRoute(updatedRoute);
            getCommandManager().getCollectionManager().update(updatedRoute);
            return new CommandResult("Your Route was successfully Updated", true);
        } catch (SQLException e) {
            return new CommandResult("Couldn't update route because of DB problems.", false);
        }
    }
}
