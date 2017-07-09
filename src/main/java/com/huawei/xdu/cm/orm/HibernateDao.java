package com.huawei.xdu.cm.orm;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class HibernateDao<T, PK extends Serializable> {

    @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) {
        //super.setSessionFactory(sessionFactory);
    }
}
