package com.mvrcm.service;

import com.mvrcm.recommender.ContentBasedRecommender;
import com.mvrcm.recommender.ItemBasedRecommender;
import com.mvrcm.recommender.utils.RecommendedItem;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class RecommenderService {

    private final int NO_OF_RECOMMENDATIONS = 5;

    @Autowired
    ContentBasedRecommender contentBasedRecommender;

    @Autowired
    ItemBasedRecommender itemBasedRecommender;

    @Autowired
    UserService userService;

    public List<RecommendedItem> recommendContentBased(String username) {
        try {
            return contentBasedRecommender.recommend(userService.getByUsername(username).getId(), NO_OF_RECOMMENDATIONS);
        } catch (TasteException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<org.apache.mahout.cf.taste.recommender.RecommendedItem> recommendItemBasedFiltering(String username) {
        try {
            return itemBasedRecommender.recommend(userService.getByUsername(username).getId(), NO_OF_RECOMMENDATIONS);
        } catch (TasteException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<RecommendedItem> recommendHybrid(String username) {
        try {
            List<RecommendedItem> contentRecommendations = contentBasedRecommender.recommend(userService.getByUsername(username).getId(), 5);
            List<org.apache.mahout.cf.taste.recommender.RecommendedItem> itemBasedRecommendations = itemBasedRecommender.recommend(userService.getByUsername(username).getId(), 5);
            List<RecommendedItem> hybridRecommendations = new ArrayList<>();
            Iterator<org.apache.mahout.cf.taste.recommender.RecommendedItem> itemIterator = itemBasedRecommendations.iterator();
            int recommendedItemsCount = 0;
            while (recommendedItemsCount < NO_OF_RECOMMENDATIONS && itemIterator.hasNext()) {
                org.apache.mahout.cf.taste.recommender.RecommendedItem currentItem = itemIterator.next();
                if (currentItem.getValue() >= 8.5) {
                    RecommendedItem recommendedItem = new RecommendedItem(currentItem.getItemID(), currentItem.getValue());
                    hybridRecommendations.add(recommendedItem);
                    recommendedItemsCount++;
                } else break;
            }
            if (contentRecommendations.size() > 0 && recommendedItemsCount < NO_OF_RECOMMENDATIONS) {
                hybridRecommendations.add(contentRecommendations.get(0));
                recommendedItemsCount++;
            }
            itemIterator = itemBasedRecommendations.iterator();
            while (recommendedItemsCount < NO_OF_RECOMMENDATIONS && itemIterator.hasNext()) {
                org.apache.mahout.cf.taste.recommender.RecommendedItem currentItem = itemIterator.next();
                if (currentItem.getValue() >= 7.5) {
                    RecommendedItem recommendedItem = new RecommendedItem(currentItem.getItemID(), currentItem.getValue());
                    hybridRecommendations.add(recommendedItem);
                    recommendedItemsCount++;
                } else break;
            }
            hybridRecommendations.addAll(contentRecommendations.subList(1, contentRecommendations.size() + 1 - recommendedItemsCount));
            return hybridRecommendations;
        } catch (TasteException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
