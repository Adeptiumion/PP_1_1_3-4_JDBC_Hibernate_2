package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName()); // Просто захотелось.
    private Statement statement;


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        log.info("Statement is: " + statement + "\n"); // Логирую состояние.
        try {
            statement = Util.getStatement(); // Вызову метод класса-помошника для получения statement.
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS `user` (
                      `id` int NOT NULL AUTO_INCREMENT,
                      `name` varchar(45) NOT NULL,
                      `lastName` varchar(45) NOT NULL,
                      `age` int NOT NULL,
                      PRIMARY KEY (`id`)
                    )
                    """); // Запрос на создание таблички
        } catch (SQLException e) {
            log.info("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }


    }


    public void dropUsersTable() {
        try {
            statement = Util.getStatement();
            statement.execute("DROP TABLE user");
        } catch (SQLException e) {
            log.info("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            statement = Util.getStatement(); // Вызову метод класса-помошника для получения statement.
            statement.execute(new StringBuilder()
                    .append("INSERT INTO user(name, lastName, age) ")
                    .append("VALUES ('").append(name)
                    .append("', '")
                    .append(lastName)
                    .append("', '")
                    .append(age)
                    .append("')")
                    .toString()); // Добро пожаловать в нашу скромную таблицу, очередной юзверь.
        } catch (SQLException e) {
            log.info("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }
    }

    public void removeUserById(long id) {
        try {
            statement = Util.getStatement();
            statement.execute("DELETE FROM user WHERE id = " + id); // Таблица не входила в наш план...
        } catch (SQLException e) {
            log.info("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }

    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {

            statement = Util.getStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getByte(4)
                );
                user.setId(resultSet.getLong(1));
                userList.add(user);
                user = null;
            }

        } catch (SQLException e) {
            log.info("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.

        }
        return userList;
    }

    public void cleanUsersTable() {
        try {
            statement = Util.getStatement(); // Вызову метод класса-помошника для получения statement.
            statement.execute("TRUNCATE TABLE user"); // Ничего личного, просто бизнес.)
        } catch (SQLException e) {
            log.info("SQL exception message: " + e.getMessage() + "\n" +
                    "      SQL error code: " + e.getErrorCode() + "\n"); // Логирую то, почему я криворукий.
        }
    }
}
