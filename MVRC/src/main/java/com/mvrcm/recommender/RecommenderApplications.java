package com.mvrcm.recommender;


import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

public class RecommenderApplications {

    //@PostConstruct
    public void Algo() throws IOException, TasteException {
        DataModel model=new FileDataModel(new File("D:\\Licenta2\\dataset\\ml-100k\\u1.base"));
        //UserSimilarity userSimilarity=new UncenteredCosineSimilarity(model);
        ItemSimilarity itemSimilarity=new UncenteredCosineSimilarity(model);
        //UserNeighborhood userNeighborhood=new NearestNUserNeighborhood(2,userSimilarity,model);
        //UserBasedRecommender recommender= new GenericUserBasedRecommender(model,userNeighborhood,userSimilarity);
        //System.out.println("ESTIMATED PREF FOR USER 1 ITEM 8 " +recommender.estimatePreference(1,8));
        //System.out.println("ESTIMATED PREF FOR USER 1 ITEM 6 " +recommender.estimatePreference(1,6));
        ItemBasedRecommender recommender=new GenericItemBasedRecommender(model,itemSimilarity);
    }
}
