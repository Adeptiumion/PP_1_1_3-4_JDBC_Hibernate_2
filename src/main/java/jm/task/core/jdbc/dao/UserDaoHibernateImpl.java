package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();
    private static final Logger HibernateLogger = Logger.getLogger(UserDaoHibernateImpl.class.getName()); // Логирую на уровне error и info.

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        // Открою сессию.
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            HibernateLogger.info("Session is open ?(createUsersTable) -> " + session.isOpen());
            transaction = session.beginTransaction();
            session.createSQLQuery("""
                            CREATE TABLE IF NOT EXISTS `user` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(45) NOT NULL,
                              `lastName` varchar(45) NOT NULL,
                              `age` int NOT NULL,
                              PRIMARY KEY (`id`)
                            )
                            """)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            HibernateLogger.severe("Error creating table!");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        // Открою сессию.
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            HibernateLogger.info("Session is open ?(dropUsersTable) -> " + session.isOpen());
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS user").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            HibernateLogger.severe("Error dropping table!");
        }
    }

    // ✔
    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            HibernateLogger.info("Session is open ?(saveUser) -> " + session.isOpen());
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            HibernateLogger.severe("Error save user in table!");
            e.printStackTrace();
        }
    }

    // ?
    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            HibernateLogger.info("Session is open ?(removeUserById) -> " + session.isOpen());
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null)
                session.delete(user);
            else
                HibernateLogger.severe("Error 404: User with id = " + id + "is not found!");
            transaction.commit();
        } catch (Exception e) {
            HibernateLogger.severe("Error remove user in table!");
        }
    }

    // ????
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            HibernateLogger.info("Session is open ?(getAllUsers) -> " + session.isOpen());
            transaction = session.beginTransaction();
            // CriteriaBuilder -> CriteriaQuery<User> -> Root<User> -> select -> Query -> getResultList();
            CriteriaQuery<User> cq = session.getCriteriaBuilder().createQuery(User.class);
            cq.select(cq.from(User.class));
            users = session.createQuery(cq).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            HibernateLogger.severe("Error getting users! Check table or availability of table.");
            e.printStackTrace();
        }
        return users;
    }

    // ✔
    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            HibernateLogger.info("Session is open ?(cleanUsersTable) -> " + session.isOpen());
            transaction = session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE user").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            HibernateLogger.severe("Error cleaning table. Check availability of table.");
        }
    }
}
