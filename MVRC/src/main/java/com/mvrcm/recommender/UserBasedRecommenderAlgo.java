package com.mvrcm.recommender;

import com.mvrcm.recommender.utils.RecommenderTester;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class UserBasedRecommenderAlgo {

   // @PostConstruct
    public void algo() throws IOException, TasteException {
        DataModel dataModel = new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-100k\\u5.base"));
        DataModel testModel = new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-100k\\test5.test"));
        UserSimilarity userPearsonSimilarity=new PearsonCorrelationSimilarity(dataModel);
        UserSimilarity userCosineSimilarity=new UncenteredCosineSimilarity(dataModel);
        UserSimilarity userTanimotoSimilarity=new TanimotoCoefficientSimilarity(dataModel);
        UserSimilarity userLoglikelihoodSimilarity=new LogLikelihoodSimilarity(dataModel);
        UserSimilarity userEuclidianSimilarity=new EuclideanDistanceSimilarity(dataModel);
        UserNeighborhood userNeighborhood=new NearestNUserNeighborhood(50,userEuclidianSimilarity,dataModel);
        UserBasedRecommender userBasedRecommender=new GenericUserBasedRecommender(dataModel,userNeighborhood,userEuclidianSimilarity);
        userBasedRecommender.recommend(1,5);
        userBasedRecommender.estimatePreference(1,2);
        RecommenderTester recommenderTester=new RecommenderTester(testModel,userBasedRecommender);
        System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+recommenderTester.MeanAverageError());
    }
}
