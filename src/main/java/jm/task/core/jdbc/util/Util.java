package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;


public class Util {
    // реализуйте настройку соеденения с БД

    private static final Logger log = Logger.getLogger(Util.class.getName());
    private static final String url = "jdbc:mysql://localhost:3306/personsschema";
    private static final String login = "root";
    private static final String password = "root";
    private static SessionFactory sessionFactory;
    private static Connection connection;

    private Util() {


    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, login, password);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.severe("Message: \n" + e.getMessage());
            log.severe("SQL error code: " + e.getErrorCode() + "\n");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.severe("Message: \n" + e.getMessage());
            log.severe("SQL error code: " + e.getErrorCode() + "\n");
            e.printStackTrace();
        }
    }

    /*
          Комментарии для моего понимания и личного спокойствия.
     */


    // Этот метод делает настройку Hibernate, и возращает SessionFactory для взаимодействия с БД.
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration cfg = new Configuration(); // Создаю конфигурацию Hibernate.
            Properties settings = new Properties(); // В эту переменную буду пихать настройки Hibernate.

            // Драйвер, который будет использовать Hibernate.
            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");

            // Тут словил дежавю, когда писал на JDBC. (Определяю данные валидации к серверу.)
            settings.put(Environment.URL, url + "?useSSL=false");
            settings.put(Environment.USER, login);
            settings.put(Environment.PASS, password);

            // Я так понял диалект нужен для того, чтобы Hibernate понимал с каким типом базы ему работать.
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

            // Хочу видеть SQL в валидаторе.
            settings.put(Environment.SHOW_SQL, "true");

            // Определяет реализацию чего-то там (так и не вкурил), чекал на:
            // http://hibernate-refdoc.3141.ru/ch2.Architecture
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

            // Тут надо быть аккуратным (Мне эта настройка заруинила домашнюю бд).
            // validate : проверяет схему, не вносит изменений в базу данных.
            // update : обновляет схему.
            // create : создает схему, уничтожая предыдущие данные.
            // create-drop : удалить схему, когда SessionFactory закрывается явно, обычно, когда приложение остановлено.
            // none : ничего не делает со схемой, не вносит изменений в базу данных
            settings.put(Environment.HBM2DDL_AUTO, "");

            cfg.setProperties(settings); // Применяю настройки, что описал выше.
            cfg.addAnnotatedClass(User.class); // Добавляю в конфиг класс-модель, котор[ый/ая] описывает таблику.

            // Определяю реализацию управления сервисами.
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties()).build();

            // Инциализирую sessionFactory, которая потом породит session, методы которого уже будут манипулировать базой.
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        }

        return sessionFactory;
    }

}
