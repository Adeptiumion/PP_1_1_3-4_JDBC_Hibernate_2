package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {
    // реализуйте настройку соеденения с БД

    public static Statement getStatement() throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://localhost:3306/personsschema", "root", "root")
                .createStatement();
    }

}
