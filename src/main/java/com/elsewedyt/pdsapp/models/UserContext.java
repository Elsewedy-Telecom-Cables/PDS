package com.elsewedyt.pdsapp.models;

public class UserContext {
    private static User currentUser;

    // Getter for the current user
    public static User getCurrentUser() {
        return currentUser;
    }

    // Setter for the current user
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
