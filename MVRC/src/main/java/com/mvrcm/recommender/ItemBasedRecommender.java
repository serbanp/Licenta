package com.mvrcm.recommender;

import com.google.common.collect.Sets;
import com.mvrcm.recommender.utils.RatingsDataModel;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.recommender.ByValueRecommendedItemComparator;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;


@Component
public class ItemBasedRecommender implements Recommender {

    @Autowired
    private RatingsDataModel ratingsDataModel;



    private DataModel dataModel;
    private ItemSimilarity itemSimilarity;
    private final double MIN_RATING=1.0;
    private final double MAX_RATING=10.0;

    private double normalizeRating(double minR,double maxR,double rating) {
        return (2*(rating-minR)-(maxR-minR))/(maxR-minR);
    }

    private double denormalizeRating(double minR,double maxR,double rating) {
        return ((rating+1)*(maxR-minR))/2+minR;
    }


    @PostConstruct
    public void init() {
        this.itemSimilarity=new TanimotoCoefficientSimilarity(ratingsDataModel.getModel());
    }

    @Override
    public List<RecommendedItem> recommend(long userID, int noOfRecommendations) throws TasteException {
        List<RecommendedItem> recommendedItems=new ArrayList<>();
        PreferenceArray preferencesFromUser = this.getDataModel().getPreferencesFromUser(userID);
        if (preferencesFromUser.length()==0)
            return Collections.emptyList();
        else {
            Set<Long> ratedItems=this.getRatedItems(preferencesFromUser.getIDs());
            Set<Long> notYetRated=this.getNotYetRated(this.getAllItems(),ratedItems);
            predictRatings(userID,notYetRated,ratedItems,recommendedItems);
        }
        Collections.sort(recommendedItems,new ByValueRecommendedItemComparator());
        if (noOfRecommendations<=recommendedItems.size())
            return recommendedItems.subList(0,noOfRecommendations);
        return recommendedItems;
    }

    @Override
    public List<RecommendedItem> recommend(long l, int i, boolean b) throws TasteException {
        return null;
    }

    public double predictRating(Long userID,Long itemID) throws TasteException {
        PreferenceArray preferencesFromUser = this.getDataModel().getPreferencesFromUser(userID);
        if (preferencesFromUser.length()==0)
            return 0;
        Set<Long> ratedItems=this.getRatedItems(preferencesFromUser.getIDs());
        Set<Long> allItems=this.getAllItems();
        Set<Long> notYetRated=this.getNotYetRated(allItems,ratedItems);
        if (!notYetRated.contains(itemID))
            return 0;
        double predictedRating = 0;
        double similaritiesSum = 0;
        for (Long ratedItemID : ratedItems) {
            double similarity = itemSimilarity.itemSimilarity(itemID, ratedItemID);
            if (!Double.isNaN(similarity)) {
                predictedRating += similarity * normalizeRating(this.MIN_RATING, this.MAX_RATING, getDataModel().getPreferenceValue(userID, ratedItemID));
                similaritiesSum += Math.abs(similarity);
            }
        }
        predictedRating = predictedRating / similaritiesSum;
        return denormalizeRating(this.MIN_RATING,this.MAX_RATING,predictedRating);
    }

    private void predictRatings(Long userId,Set<Long> unratedItems,Set<Long> ratedItems,List<RecommendedItem> recommendedItems) throws TasteException {
        for (Long unratedItemId : unratedItems) {
            double predictedRating = 0;
            double similaritiesSum = 0;
            for (Long ratedItem : ratedItems) {
                double similarity = itemSimilarity.itemSimilarity(unratedItemId, ratedItem);
                if (!Double.isNaN(similarity)) {
                    predictedRating += similarity * normalizeRating(this.MIN_RATING, this.MAX_RATING, getDataModel().getPreferenceValue(userId, ratedItem));
                    similaritiesSum += Math.abs(similarity);
                    System.out.println("similartities sum " + similaritiesSum + " " + ratedItem);
                }
            }
                predictedRating = predictedRating / similaritiesSum;
                if (similaritiesSum!=0)
                    recommendedItems.add(new com.mvrcm.recommender.utils.RecommendedItem(unratedItemId,(float)denormalizeRating(this.MIN_RATING,this.MAX_RATING,predictedRating)));
        }
    }

    private Set<Long> getAllItems() throws TasteException {
        LongPrimitiveIterator longPrimitiveIterator=ratingsDataModel.getModel().getItemIDs();
        Set<Long> allItems=new HashSet<>();
        while(longPrimitiveIterator.hasNext()) {
            allItems.add(longPrimitiveIterator.nextLong());
        }
        return allItems;
    }

    private Set<Long> getRatedItems(long[] ratedItemsIds) {
        Set<Long> ratedItems=new HashSet<>();
        for (long l:ratedItemsIds)
            ratedItems.add(l);
        return ratedItems;
    }

    private Set<Long> getNotYetRated(Set<Long> allItems,Set<Long> ratedItems) {
        allItems.removeIf(ratedItems::contains);
        return allItems;
    }

    @Override
    public List<RecommendedItem> recommend(long l, int i, IDRescorer idRescorer) throws TasteException {
        return null;
    }

    @Override
    public List<RecommendedItem> recommend(long l, int i, IDRescorer idRescorer, boolean b) throws TasteException {
        return null;
    }

    @Override
    public float estimatePreference(long l, long l1) throws TasteException {
        return (float)this.predictRating(l,l1);
    }

    @Override
    public void setPreference(long l, long l1, float v) throws TasteException {

    }

    @Override
    public void removePreference(long l, long l1) throws TasteException {

    }

    @Override
    public DataModel getDataModel() {
        return this.ratingsDataModel.getModel();
    }

    @Override
    public void refresh(Collection<Refreshable> collection) {

    }
}
