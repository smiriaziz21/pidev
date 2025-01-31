package Entites;

import Entites.Enum.Role;

public class Users {

    private static Users currentUser = null;
    private final int id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Users(int id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Users getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Users user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

}
