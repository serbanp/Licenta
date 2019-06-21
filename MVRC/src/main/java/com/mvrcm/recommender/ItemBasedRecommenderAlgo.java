package com.mvrcm.recommender;

import com.mvrcm.model.User;
import com.mvrcm.recommender.utils.AdjustedCosineSimilarity;
import org.apache.hadoop.yarn.webapp.ResponseInfo;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;


@Component
public class ItemBasedRecommenderAlgo {

    //@PostConstruct
    public void algo() throws IOException, TasteException {
        DataModel dataModel = new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-10m\\ra.train"),"::");
        DataModel testModel=new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-10m\\atest.test"),"::");
        ItemSimilarity itemAdjustedCosineSimilarity= new AdjustedCosineSimilarity(dataModel);
        ItemSimilarity itemCosineSimilarity=new UncenteredCosineSimilarity(dataModel);
        ItemSimilarity itemTanimotoSimialrity=new TanimotoCoefficientSimilarity(dataModel);
        ItemSimilarity itemLoglikelihoodSimilarity=new LogLikelihoodSimilarity(dataModel);
        ItemSimilarity itemEuclidianSimilarity=new EuclideanDistanceSimilarity(dataModel);
//        ItemBasedRecommender recommender =new ItemBasedRecommender(dataModel,itemAdjustedCosineSimilarity);
//        ItemBasedRecommender recommender2=new ItemBasedRecommender(dataModel,itemCosineSimilarity);
//        ItemBasedRecommender recommender3=new ItemBasedRecommender(dataModel,itemEuclidianSimilarity);
//        ItemBasedRecommender recommender4=new ItemBasedRecommender(dataModel,itemLoglikelihoodSimilarity);
//        ItemBasedRecommender recommender5=new ItemBasedRecommender(dataModel,itemTanimotoSimialrity);
//        RecommenderTester recommenderTesterAdjusted=new RecommenderTester(testModel,recommender);
//        RecommenderTester recommenderTesterCosine=new RecommenderTester(testModel,recommender2);
//        RecommenderTester recommenderTesterEuclidean=new RecommenderTester(testModel,recommender3);
//        RecommenderTester recommenderTesterLogLike=new RecommenderTester(testModel,recommender4);
//        RecommenderTester recommenderTesterTanimoto=new RecommenderTester(testModel,recommender5);
        System.out.println(dataModel.getPreferenceValue(1,12)-1);
        //System.out.println("MEAN AVERAGE ERROR / NO OF USERS = "+ recommenderTesterAdjusted.MeanAverageError());
    }
}
