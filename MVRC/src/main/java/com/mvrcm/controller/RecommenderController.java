package com.mvrcm.controller;


import com.mvrcm.recommender.ContentBasedRecommender;
import com.mvrcm.recommender.ItemBasedRecommender;
import com.mvrcm.recommender.utils.RecommendedItem;
import com.mvrcm.service.RecommenderService;
import com.mvrcm.service.UserService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommenderController {
    @Autowired
    private RecommenderService recommenderService;

    @GetMapping("/recommend/contentbased/{username}")
    public List<RecommendedItem> getRecommendationsForUser(@PathVariable String username) {
        return recommenderService.recommendContentBased(username);
    }

    @GetMapping("/recommend/itembased/{username}")
    public List<org.apache.mahout.cf.taste.recommender.RecommendedItem> getRecommendationsForUserWithItemBased(@PathVariable String username) {
        return recommenderService.recommendItemBasedFiltering(username);
    }

    @GetMapping("/recommend/{username}")
    public List<RecommendedItem> getHybridRecommendations(@PathVariable String username) {
        return recommenderService.recommendHybrid(username);
    }
}
