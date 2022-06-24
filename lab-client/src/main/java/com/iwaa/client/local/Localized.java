package com.iwaa.client.local;

import java.util.ResourceBundle;

public interface Localized {

    default String localisation(Constants constants) {
        return getResourceBundle().getString(constants.getString());
    }

    ResourceBundle getResourceBundle();
}
