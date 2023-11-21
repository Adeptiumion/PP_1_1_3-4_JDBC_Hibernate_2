package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName()); // Логирую на уровне error.
    private final Connection connection = Util.getConnection();


    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) { // Вызову метод класса-помошника для получения statement.
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS user (
                      `id` int NOT NULL AUTO_INCREMENT,
                      `name` varchar(45) NOT NULL,
                      `lastName` varchar(45) NOT NULL,
                      `age` int NOT NULL,
                      PRIMARY KEY (`id`)
                    )
                    """); // Запрос на создание таблички
        } catch (SQLException e) {
            log.severe("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) { // Вызову метод класса-помошника для получения statement.
            statement.execute("DROP TABLE user");
        } catch (SQLException e) {
            log.severe("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                INSERT INTO user(name, lastName, age) VALUES (?,?,?);
                 """)) { // Добро пожаловать в нашу скромную таблицу, очередной юзверь.
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.severe("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
            try {
                connection.rollback();
            } catch (SQLException z) {
                log.severe("Can't do rollback - check sql errorCode!");
                log.severe("SQL exception message: " + z.getMessage() + "\n" +
                        "      SQL error code: " + z.getErrorCode() + "\n"); // Если траблы с ролбэком.
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id = ?")) { // Таблица не входила в наш план...
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException a) {
            log.severe("SQL exception message: " + a.getMessage() + "\n" +
                    "      SQL error code: " + a.getErrorCode() + "\n");                                                  // Логирую то, почему я криворукий.
            try {
                connection.rollback();
            } catch (SQLException b) {
                log.severe("Can't do rollback - check sql errorCode!");
                log.severe("SQL exception message: " + b.getMessage() + "\n" +
                        "      SQL error code: " + b.getErrorCode() + "\n");                                                // Если траблы с ролбэком.
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getByte(4)
                );
                user.setId(resultSet.getLong(1));
                userList.add(user);
            }
            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException b) {
                log.severe("Can't do rollback - check sql errorCode!");
                log.severe("SQL exception message: " + b.getMessage() + "\n" +
                        "      SQL error code: " + b.getErrorCode() + "\n");
            }
            log.severe("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) { // Вызову метод класса-помошника для получения statement.
            statement.execute("TRUNCATE TABLE user"); // Ничего личного, просто бизнес.)
        } catch (SQLException e) {
            log.severe("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }
    }
}
