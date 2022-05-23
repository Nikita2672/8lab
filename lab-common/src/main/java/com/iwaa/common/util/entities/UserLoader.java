package com.iwaa.common.util.entities;

import com.iwaa.common.util.exceptions.EndOfStreamException;
import com.iwaa.common.util.io.DataReader;
import com.iwaa.common.util.utils.DataParser;

import java.io.IOException;
import java.io.InputStreamReader;

public class UserLoader {

    public String loadLogin() throws EndOfStreamException, IOException {
        DataReader reader = new DataReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter login: ");
            String data = reader.inputLine();
            try {
                String[] normalizeData = DataParser.normalize(data);
                return UserValidator.getValidatedLogin(normalizeData);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String loadPassword() throws EndOfStreamException, IOException {
        DataReader reader = new DataReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter password: ");
            String data = reader.inputLine();
            try {
                String[] normalizeData = DataParser.normalize(data);
                return UserValidator.getValidatedPassword(normalizeData);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public User loadUser() throws EndOfStreamException, IOException {
        return new User(loadLogin(), loadPassword());
    }
}
