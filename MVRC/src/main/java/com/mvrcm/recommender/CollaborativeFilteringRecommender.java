package com.mvrcm.recommender;

import com.google.gson.Gson;
import com.mvrcm.data_parser.data_wrappers.PosterWrapper;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

//@Component
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
  //      try {
 //          dataModel=new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-100k\\u1.base"));
    //    } catch (IOException e) {
     //      e.printStackTrace();
        //}
    }
    private UserSimilarity getCorellationCoefficient(DataModel dataModel) {
        try {
            return new PearsonCorrelationSimilarity(dataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserNeighborhood getUserNeighborhood(Integer n,UserSimilarity userSimilarity,DataModel dataModel) {
        try {
            return new NearestNUserNeighborhood(n,userSimilarity,dataModel);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserBasedRecommender getUserBasedRecommender(DataModel dataModel,UserNeighborhood userNeighborhood,UserSimilarity userSimilarity) {
        return new GenericUserBasedRecommender(dataModel,userNeighborhood,userSimilarity);
    }


    @PostConstruct
    private void CollaborativeFiltering() throws TasteException {
        initializeData();
        RandomUtils.useTestSeed();
        UserSimilarity userSimilarity=getCorellationCoefficient(dataModel);
        UserNeighborhood userNeighborhood=getUserNeighborhood(10,userSimilarity,dataModel);
        UserBasedRecommender recommender=getUserBasedRecommender(dataModel,userNeighborhood,userSimilarity);
        //System.out.println("ESTIMATED PREF"+recommender.estimatePreference(1,313));
        System.out.println(recommender.estimatePreference(1,6));
        System.out.println(recommender.estimatePreference(1,10));
        System.out.println(recommender.estimatePreference(1,12));
        System.out.println(recommender.estimatePreference(1,14));
        //IRStatistics stats=evaluator2.evaluate(recommenderBuilder,null,dataModel,null,5,GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
    }




}
