package io.github.zaur2025.userservicetests.util;

import io.github.zaur2025.userservicetests.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            // Загружаем конфиг из hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // Регистрируем нашу сущность
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Ошибка создания SessionFactory: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    public static void resetSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        sessionFactory = null;
        getSessionFactory(); // пересоздаст с новыми System properties
    }
}