package com.mvrcm.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;

@Component
public class CollaborativeFilteringRecommender {
    private DataModel dataModel;
    private final String preferenceTable="users_movies_ratings";

    @PersistenceContext
    EntityManager entityManager;

    private DataSource getDataSourceFromHibernateEntityManager() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        return info.getDataSource();
    }

    private void initializeData() {
        JDBCDataModel jdbcDataModel = new PostgreSQLJDBCDataModel(getDataSourceFromHibernateEntityManager(),preferenceTable,"user_id","movie_id","rating","");
        try {
            dataModel=new ReloadFromJDBCDataModel(jdbcDataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }
    private UserSimilarity getCorellationCoefficient(DataModel dataModel) {
        return new TanimotoCoefficientSimilarity(dataModel);
    }

    private UserNeighborhood getUserNeighborhood(Double threshold,UserSimilarity userSimilarity,DataModel dataModel) {
        return new ThresholdUserNeighborhood(threshold,userSimilarity,dataModel);
    }

    private UserBasedRecommender getUserBasedRecommender(DataModel dataModel,UserNeighborhood userNeighborhood,UserSimilarity userSimilarity) {
        return new GenericUserBasedRecommender(dataModel,userNeighborhood,userSimilarity);
    }

    @PostConstruct
    private void CollaborativeFiltering() {
        initializeData();
        UserSimilarity userSimilarity=getCorellationCoefficient(dataModel);
        UserNeighborhood userNeighborhood=getUserNeighborhood(0.1,userSimilarity,dataModel);
        UserBasedRecommender recommender=getUserBasedRecommender(dataModel,userNeighborhood,userSimilarity);
        try {
            for (long i=1;i<=10;i++) {
                System.out.println("RECOM FOR USER " + i);
                List<RecommendedItem> recommandations = recommender.recommend(i,5);
                for (RecommendedItem recommendedItem : recommandations) {
                    System.out.println(recommendedItem);
                }
            }
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }



}
