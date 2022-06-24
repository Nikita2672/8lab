package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Coordinates;
import com.iwaa.common.util.data.Location;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.sql.SQLException;
import java.util.Collections;

import static com.iwaa.common.util.commands.ParametersConstants.ADD_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.COORDINATE_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_X_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_FROM_Z_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_X_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_Y_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.LOCATION_TO_Z_ARGS;
import static com.iwaa.common.util.commands.ParametersConstants.MAX_X_VALUE;
import static com.iwaa.common.util.commands.ParametersConstants.MAX_Y_VALUE;

public class AddIfMax extends AbstractCommand {

    public AddIfMax(CommandAdmin commandAdmin) {
        super(commandAdmin, "add_if_max", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции", ADD_ARGS);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            float coordinateX = Float.parseFloat(args[2].toString());
            if (coordinateX > MAX_X_VALUE) {
                throw new NumberFormatException();
            }
            float coordinateY = Float.parseFloat(args[COORDINATE_Y_ARGS].toString());
            if (coordinateY > MAX_Y_VALUE) {
                throw new NumberFormatException();
            }
            long locationFromX = Long.parseLong(args[LOCATION_FROM_X_ARGS].toString());
            int locationFromY = Integer.parseInt(args[LOCATION_FROM_Y_ARGS].toString());
            int locationFromZ = Integer.parseInt(args[LOCATION_FROM_Z_ARGS].toString());

            long locationToX = Long.parseLong(args[LOCATION_TO_X_ARGS].toString());
            int locationToY = Integer.parseInt(args[LOCATION_TO_Y_ARGS].toString());
            int locationToZ = Integer.parseInt(args[LOCATION_TO_Z_ARGS].toString());

            long distance = Long.parseLong(args[1].toString());

            if (distance <= 1) {
                throw new NumberFormatException();
            }

            Route routeToAdd = new Route((String) args[0], new Coordinates(coordinateX, coordinateY),
                    new Location(locationFromX, locationFromY, locationFromZ),
                    new Location(locationToX, locationToY, locationToZ), distance);
            return new Object[]{routeToAdd, args[ADD_ARGS]};
        } catch (NumberFormatException e) {
            return new Object[]{null, args[ADD_ARGS]};
        }
    }

    @Override
    public CommandResult execute(Object[] args) {
        if (args[0] != null) {
            Route newRoute = (Route) args[0];
            User user = (User) args[1];
            if (newRoute.compareTo(Collections.max(getCommandManager().getCollectionManager().getCollection())) > 0) {
                try {
                    getCommandManager().getDBWorker().addRoute(newRoute, user);
                } catch (SQLException e) {
                    return new CommandResult("Couldn't add route");
                }
                getCommandManager().getCollectionManager().add(newRoute);
                return new CommandResult("New Route was successfully added!");
            } else {
                return new CommandResult("Given route is not maximal");
            }
        } else {
            return new CommandResult("Data incorrect");
        }
    }
}
