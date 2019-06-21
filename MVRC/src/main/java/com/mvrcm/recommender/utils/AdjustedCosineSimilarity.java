package com.mvrcm.recommender.utils;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AdjustedCosineSimilarity implements ItemSimilarity {
    private DataModel dataModel;
    private Map<Long,Double> avgUserRatingMap;

    public AdjustedCosineSimilarity(DataModel dataModel) {
        this.dataModel=dataModel;
        this.avgUserRatingMap=new HashMap<>();
    }

    public double itemSimilarity(long itemID1, long itemID2) throws TasteException {
        PreferenceArray xPrefs = dataModel.getPreferencesForItem(itemID1);
        PreferenceArray yPrefs = dataModel.getPreferencesForItem(itemID2);
        int xLength = xPrefs.length();
        int yLength = yPrefs.length();
        double result = 0;
        if (xLength != 0 && yLength != 0) {
            double prodXY = 0;
            double sqrt1 = 0;
            double sqrt2 = 0;
            long xIndex = xPrefs.getUserID(0);
            long yIndex = yPrefs.getUserID(0);
            int xPrefIndex = 0;
            int yPrefIndex = 0;
            int count = 0;
            while (true) {
                int compare = xIndex < yIndex ? -1 : (xIndex > yIndex ? 1 : 0);
                if (compare == 0) {
                    double x;
                    double y;
                    double avgRatingUser;
                    if (avgUserRatingMap.containsKey(xIndex)) {
                        avgRatingUser = this.avgUserRatingMap.get(xIndex);
                    }
                    else {
                        avgUserRatingMap.put(xIndex,getAverageRatingForUser(xIndex));
                        avgRatingUser=avgUserRatingMap.get(xIndex);
                    }

                    x = (double) xPrefs.getValue(xPrefIndex);
                    y = (double) yPrefs.getValue(yPrefIndex);
                    prodXY += (x - avgRatingUser) * (y - avgRatingUser);
                    sqrt1 += (x - avgRatingUser) * (x - avgRatingUser);
                    sqrt2 += (y - avgRatingUser) * (y - avgRatingUser);
                    ++count;
                } else if (compare <= 0) {
                    ++xPrefIndex;
                    if (xPrefIndex >= xLength) {
                        if (yIndex == 9223372036854775807L) {
                            break;
                        }

                        xIndex = 9223372036854775807L;
                    } else {
                        xIndex = xPrefs.getUserID(xPrefIndex);
                    }
                }

                if (compare >= 0) {
                    ++yPrefIndex;
                    if (yPrefIndex >= yLength) {
                        if (xIndex == 9223372036854775807L) {
                            break;
                        }

                        yIndex = 9223372036854775807L;
                    } else {
                        yIndex = yPrefs.getUserID(yPrefIndex);
                    }
                }
            }
            result = prodXY / (Math.sqrt(sqrt1) * Math.sqrt(sqrt2));
        }
        return result;
    }


    private double getAverageRatingForUser(long userId) throws TasteException {
        PreferenceArray preferenceArray = dataModel.getPreferencesFromUser(userId);
        double avgRating = 0;
        for (int i = 0; i < preferenceArray.length(); i++)
            avgRating += preferenceArray.getValue(i);
        return avgRating / (double) preferenceArray.length();
    }

    @Override
    public double[] itemSimilarities(long l, long[] longs) throws TasteException {
        return null;
    }


    @Override
    public long[] allSimilarItemIDs(long l) throws TasteException {
        return null;
    }

    @Override
    public void refresh(Collection<Refreshable> collection) {

    }
}

