package com.iwaa.common.util.entities;

import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.utils.DataParser;

import java.io.IOException;

public class UserLoader {

    public String loadLogin(String login) {
        System.out.print("Enter login: ");
        try {
            String[] normalizeData = DataParser.normalize(login);
            return UserValidator.getValidatedLogin(normalizeData);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String loadPassword(String password) {
        System.out.print("Enter password: ");
        try {
            String[] normalizeData = DataParser.normalize(password);
            return UserValidator.getValidatedPassword(normalizeData);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public User loadUser(String login, String password) throws EndOfStreamException, IOException {
        return new User(loadLogin(login), loadPassword(password));
    }
}
