package com.iwaa.common.util.commands;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.data.Coordinates;
import com.iwaa.common.util.data.Location;
import com.iwaa.common.util.data.Route;
import com.iwaa.common.util.entities.User;
import com.iwaa.common.util.network.CommandResult;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

public class RemoveLower extends AbstractCommand {

    public RemoveLower(CommandAdmin commandAdmin) {
        super(commandAdmin, "remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", ADD_ARGS);
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
            User user = (User) args[1];
            int collectionLen = getCommandManager().getCollectionManager().getCollection().size();
            List<Route> routesToDelete = getCommandManager().getCollectionManager().getCollection().stream().filter(
                            route -> (Objects.equals(route.getAuthor(), user.getLogin()) && route.compareTo((Route) args[0]) < 0))
                    .collect(Collectors.toList());
            for (Route routeToDelete : routesToDelete) {
                try {
                    getCommandManager().getDBWorker().deleteRouteById(routeToDelete.getId());
                    getCommandManager().getCollectionManager().remove(routeToDelete);
                } catch (SQLException e) {
                    return new CommandResult("Couldn't delete because DB problems.", false);
                }
            }
            return new CommandResult((collectionLen - getCommandManager().getCollectionManager().getCollection().size()) + " object(s) was deleted", true);
        } else {
            return new CommandResult("Data incorrect", false);
        }
    }
}
