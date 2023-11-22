package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    // Хочу логировать вне зависимости от реализации UserDao. ↓
    private static final Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName());
    private final UserDao userDaoHibernate = new UserDaoJDBCImpl();

    @Override
    public void createUsersTable() {
        userDaoHibernate.createUsersTable();
    }

    @Override
    public void dropUsersTable() {
        userDaoHibernate.dropUsersTable();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        userDaoHibernate.saveUser(name, lastName, age);
        log.info("User с именем – " + name + " добавлен в базу данных\n"); // ← Вывод в консоль/логирование, добавленного юзверя.
    }

    @Override
    public void removeUserById(long id) {
        userDaoHibernate.removeUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDaoHibernate.getAllUsers();
        log.info(users.toString()); // ← Вывод в консоль/логирование, все существующих юзверей.
        return users;
    }

    @Override
    public void cleanUsersTable() {
        userDaoHibernate.cleanUsersTable();
    }
}
