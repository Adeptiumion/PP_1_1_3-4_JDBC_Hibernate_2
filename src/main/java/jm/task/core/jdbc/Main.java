package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // реализуйте алгоритм здесь
        UserDao userDao = new UserDaoJDBCImpl();
        userDao.createUsersTable();
        userDao.saveUser("Ichigo", "Kurosaki", (byte) 16);
        userDao.saveUser("Antimage", "Antimagovich", (byte) 21);
        userDao.saveUser("Gordon", "Freeman", (byte) 27);
        userDao.saveUser("Kira", "Yoshikage", (byte) 33);
        userDao.getAllUsers();
        userDao.cleanUsersTable();
        userDao.dropUsersTable();
    }


}
