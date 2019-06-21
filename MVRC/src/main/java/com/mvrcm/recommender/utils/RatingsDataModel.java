package com.mvrcm.recommender.utils;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Component
public class RatingsDataModel {
    private DataModel ratingModel;
    private JDBCDataModel jdbcDataModel;
    private final String PRFERENCE_TABLE = "users_movies_ratings";

    @PersistenceContext
    EntityManager entityManager;

    private DataSource getDataSourceFromHibernateEntityManager() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        return info.getDataSource();
    }

    @PostConstruct
    private void initializeData() {
        this.jdbcDataModel = new PostgreSQLJDBCDataModel(getDataSourceFromHibernateEntityManager(), PRFERENCE_TABLE, "user_id", "movie_id", "rating", "");
        try {
            ratingModel = new ReloadFromJDBCDataModel(jdbcDataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }

    public DataModel getModel() {
        return this.ratingModel;
    }

    public void refresh() {
        try {
            ratingModel = new ReloadFromJDBCDataModel(jdbcDataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }
}
