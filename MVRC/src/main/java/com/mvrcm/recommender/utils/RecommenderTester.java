package com.mvrcm.recommender.utils;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecommenderTester {
    private DataModel testModel;
    private Recommender recommender;

    public RecommenderTester(DataModel testModel, Recommender recommender) {
        this.testModel = testModel;
        this.recommender = recommender;
    }

    public double MeanAverageError() throws TasteException {
        double MAE = 0;
        long n ;
        LongPrimitiveIterator longPrimitiveIterator = testModel.getUserIDs();
        int noOfUsersWith_0_Predictions=0;
        int noOfZeros=0;
        int nrUsers=0;
        while (longPrimitiveIterator.hasNext()) {
            long userID = longPrimitiveIterator.nextLong();
            double totalError=0;
            PreferenceArray preferencesFromUser = testModel.getPreferencesFromUser(userID);
            n = 0;
            nrUsers++;
            for (Preference preference : preferencesFromUser) {
                double predictedRating=recommender.estimatePreference(userID,preference.getItemID());
                double error=Math.abs(preference.getValue() - predictedRating);
                if (!Double.isNaN(error))
                    totalError += error;
                else {
                    n--;
                }
            }
            n+= preferencesFromUser.length();
            double meanErrorForUser=totalError/n;
            if (Double.isNaN(meanErrorForUser)) {
                System.out.println("USERUL "+userID);
                noOfUsersWith_0_Predictions+=1;
            }
            else {
                MAE += meanErrorForUser;
            }
            if (nrUsers%5000==0)
                System.out.println("DONE " +nrUsers+" USERS");
        }
        System.out.println("NO OF USERS WITH 0 PREDICTIONS : "+noOfUsersWith_0_Predictions);
        System.out.println("MAE = "+MAE);
        return MAE / (testModel.getNumUsers()-noOfUsersWith_0_Predictions);
    }
}
