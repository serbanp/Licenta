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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

//@Component
public class UserBasedRecommenderAlgo {

   @PostConstruct
    public void algo() throws IOException, TasteException {
        DataModel dataModel = new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-100k\\u1.base"));
        DataModel testModel = new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-100k\\test1.test"));
        UserSimilarity userPearsonSimilarity=new PearsonCorrelationSimilarity(dataModel);
        UserSimilarity userCosineSimilarity=new UncenteredCosineSimilarity(dataModel);
        UserSimilarity userTanimotoSimilarity=new TanimotoCoefficientSimilarity(dataModel);
        UserSimilarity userLoglikelihoodSimilarity=new LogLikelihoodSimilarity(dataModel);
        UserSimilarity userEuclideanSimilarity=new EuclideanDistanceSimilarity(dataModel);
        UserNeighborhood userNeighborhood1=new NearestNUserNeighborhood(5,userPearsonSimilarity,dataModel);
        UserNeighborhood userNeighborhood2=new NearestNUserNeighborhood(5,userCosineSimilarity,dataModel);
        UserNeighborhood userNeighborhood3=new NearestNUserNeighborhood(5,userTanimotoSimilarity,dataModel);
        UserNeighborhood userNeighborhood4=new NearestNUserNeighborhood(5,userLoglikelihoodSimilarity,dataModel);
        UserNeighborhood userNeighborhood5=new NearestNUserNeighborhood(5,userEuclideanSimilarity,dataModel);
        UserBasedRecommender userBasedRecommender1=new GenericUserBasedRecommender(dataModel,userNeighborhood1,userPearsonSimilarity);
       UserBasedRecommender userBasedRecommender2=new GenericUserBasedRecommender(dataModel,userNeighborhood2,userCosineSimilarity);
       UserBasedRecommender userBasedRecommender3=new GenericUserBasedRecommender(dataModel,userNeighborhood3,userTanimotoSimilarity);
       UserBasedRecommender userBasedRecommender4=new GenericUserBasedRecommender(dataModel,userNeighborhood4,userLoglikelihoodSimilarity);
       UserBasedRecommender userBasedRecommender5=new GenericUserBasedRecommender(dataModel,userNeighborhood5,userEuclideanSimilarity);
      // System.out.println("START TESTING"+ "NO OF USERS AND ITEMS = "+testModel.getNumUsers()+" "+testModel.getNumItems());
        RecommenderTester recommenderTester1=new RecommenderTester(testModel,userBasedRecommender1);
       //System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+recommenderTester1.MeanAverageError());
       RecommenderTester recommenderTester2=new RecommenderTester(testModel,userBasedRecommender2);
       // System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+recommenderTester2.MeanAverageError());
       RecommenderTester recommenderTester3=new RecommenderTester(testModel,userBasedRecommender3);
       //System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+recommenderTester3.MeanAverageError());
       RecommenderTester recommenderTester4=new RecommenderTester(testModel,userBasedRecommender4);
       //System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+recommenderTester4.MeanAverageError());
       RecommenderTester recommenderTester5=new RecommenderTester(testModel,userBasedRecommender5);
       //System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+recommenderTester5.MeanAverageError());
    }
}
