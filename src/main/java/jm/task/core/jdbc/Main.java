package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Ichigo", "Kurosaki", (byte) 16);
        userService.saveUser("Antimage", "Antimagovich", (byte) 21);
        userService.saveUser("Gordon", "Freeman", (byte) 27);
        userService.saveUser("Kira", "Yoshikage", (byte) 33);
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeConnection();
    }


}
