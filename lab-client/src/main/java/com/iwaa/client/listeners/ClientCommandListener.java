package com.iwaa.client.listeners;

import com.iwaa.common.util.controllers.CommandAdmin;
import com.iwaa.common.util.controllers.CommandListener;
import com.iwaa.common.util.entities.User;

public class ClientCommandListener extends CommandListener {

    public ClientCommandListener(CommandAdmin commandAdmin) {
        super(commandAdmin, true);
    }

    public void launch() {
        setUser(new User());
        run();
    }

    @Override
    public void outputUserName() {
        System.out.print(ANSI_PURPLE + getUser().getLogin() + "$ " + ANSI_RESET);
    }
}
