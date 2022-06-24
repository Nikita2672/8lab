package com.iwaa.client.local;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class Local {

    public static final String ENGLISH = "english";
    public static final String FRANCHE = "franche";
    public static final String RUSSIAN = "russian";
    public static final String NORWEGIAN = "norwegian";

    public static final Map<String, ResourceBundle> LOCALS;
    private static final ResourceBundle RESOURCE_BUNDLE_DEFAULT = ResourceBundle.getBundle("lang");
    private static final ResourceBundle RESOURCE_BUNDLE_ENGLISH = ResourceBundle.getBundle("lang", new Locale("en", "US"));
    private static final ResourceBundle RESOURCE_BUNDLE_NORWEGIAN = ResourceBundle.getBundle("lang", new Locale("nb", "NO"));
    private static final ResourceBundle RESOURCE_BUNDLE_FRANCE = ResourceBundle.getBundle("lang", new Locale("fr", "FR"));
    private static final ResourceBundle RESOURCE_BUNDLE_RUSSIAN = ResourceBundle.getBundle("lang", new Locale("ru", "RU"));

    static {
        LOCALS = new HashMap<>();
        LOCALS.put(ENGLISH, RESOURCE_BUNDLE_ENGLISH);
        LOCALS.put(NORWEGIAN, RESOURCE_BUNDLE_NORWEGIAN);
        LOCALS.put(FRANCHE, RESOURCE_BUNDLE_FRANCE);
        LOCALS.put(RUSSIAN, RESOURCE_BUNDLE_RUSSIAN);
    }

    private Local() {

    }

    public static ResourceBundle getResourceBundleDefault() {
        return RESOURCE_BUNDLE_DEFAULT;
    }

}
