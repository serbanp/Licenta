package com.mvrcm.recommender;

import com.mvrcm.controller.MovieController;
import com.mvrcm.model.Actor;
import com.mvrcm.model.Genre;
import com.mvrcm.model.Movie;
import com.mvrcm.model.Tag;
import com.mvrcm.recommender.utils.RatingsDataModel;
import com.mvrcm.recommender.utils.RecommendedItem;
import com.mvrcm.recommender.utils.WeightWrapper;
import com.mvrcm.repository.MovieRepository;
import com.mvrcm.service.MovieService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ContentBasedRecommender {
    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingsDataModel ratingsDataModel;

    Map<Long, Set<String>> movieBagOfWordsMap;
    List<Movie> allMovies;
    List<Map<Long, Float>> similarityMatrix;
    float maxWeight=0;
    float minWeight=0;


    private static final String features = "Director,Actors,Genres,Plot,Tags";

    @PostConstruct
    public void init() throws TasteException {
        this.movieBagOfWordsMap = new HashMap<>();
        this.similarityMatrix = new ArrayList<>();
        this.allMovies = movieService.getAll();
        for (Movie movie : this.allMovies) {
            createBagOfWords(movie);
        }
        //this.computeSimilarities();
    }

    public List<RecommendedItem> recommend(Long userId,int howManyItems) throws TasteException {
        Map<String, WeightWrapper>  featuresWeight=this.userProfile(userId);
        this.normalizeWeights(featuresWeight);
        List<RecommendedItem> recommendedItems=new ArrayList<>();
        Map<Long,Float> profileSimilarities=this.computeUserProfileItemProfilesSimilarities(featuresWeight,userId);
        int i=0;
        for (Long key:profileSimilarities.keySet()) {
            recommendedItems.add(new RecommendedItem(key,profileSimilarities.get(key)));
            i++;
            if (i==howManyItems)
                break;
        }
        return recommendedItems;
    }

    private Float itemsSimilarity(Set<String> v1, Set<String> v2) {
        //tanimoto similarity ,jaccard index
        Set<String> intersection = new HashSet<>(v1);
        intersection.retainAll(v2);
        return (float) intersection.size() / (v1.size() + v2.size() - intersection.size());
    }

    private void normalizeWeights(Map<String,WeightWrapper> userProfile) {
        this.setMinMaxWeights(userProfile);
        for (WeightWrapper weightWrapper:userProfile.values()) {
            float weight=weightWrapper.getRating()/weightWrapper.getNoOfMovies();
            weightWrapper.setWeight(normalizeWeight(minWeight,maxWeight,weight));
        }
    }
    private float normalizeWeight(float minR,float maxR,float weight) {
        return (2*(weight-minR)-(maxR-minR))/(maxR-minR);
    }

    private void setMinMaxWeights(Map<String, WeightWrapper> userProfile) {
        for (WeightWrapper weightWrapper:userProfile.values()) {
            float currentWeight=weightWrapper.getRating()/weightWrapper.getNoOfMovies();
            if (currentWeight>maxWeight) maxWeight=currentWeight;
            if (currentWeight<minWeight) minWeight=currentWeight;
        }
    }


    private Map<Long,Float> computeUserProfileItemProfilesSimilarities(Map<String, WeightWrapper> userProfile,long userID) throws TasteException {
        Set<Long> ratedItems=this.getRatedItems(this.ratingsDataModel.getModel().getPreferencesFromUser(userID).getIDs());
        Set<Long> allItems=this.getAllItems();
        Set<Long> notYetRated=this.getNotYetRated(allItems,ratedItems);
        Map<Long,Float> similarities=new TreeMap<>();
        for (Long itemID:notYetRated) {
            float similarity=this.userProfileItemProfileSimilarity(userProfile,movieBagOfWordsMap.get(itemID));
            similarities.put(itemID,similarity);
            System.out.println("SIMILARITY WITH ITEM " + itemID + " = " + similarity);
        }
        return similarities.entrySet().stream()
                .sorted((Map.Entry.comparingByValue(Comparator.reverseOrder())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private float userProfileItemProfileSimilarity(Map<String, WeightWrapper> userProfile,Set<String> itemProfile) throws TasteException {
        float similarity=0;
        int similarFeatures=0;
        for (String itemFeature:itemProfile) {
            if(userProfile.containsKey(itemFeature)) {
                similarity+=userProfile.get(itemFeature).getWeight();
                similarFeatures++;
            }
        }
        if (similarFeatures>(double)itemProfile.size()*5/100+1)
            return similarity;
        return 0;
    }

    private void computeSimilarities() {
        for (Movie movie1 : this.allMovies) {
            Map<Long, Float> similaritiesWithMovie1 = new HashMap<>();
            long movie1ID = movie1.getId();
            for (Movie movie2 : this.allMovies.subList((int) movie1ID + 1, this.allMovies.size())) {
                long movie2ID = movie2.getId();
                float similarityBetween = this.itemsSimilarity(movieBagOfWordsMap.get(movie1ID), movieBagOfWordsMap.get(movie2ID));
                if (similarityBetween >= 0.1) {
                    similaritiesWithMovie1.put(movie1ID, similarityBetween);
                }
                if (similarityBetween >= 0.5) {
                    System.out.println(movie1ID + " " + movie2ID + " " + similarityBetween);
                }
            }
            similarityMatrix.add(similaritiesWithMovie1);
        }
    }

    public void createBagOfWords(Movie movie) {
        Set<String> bagOfWords = new HashSet<>();
        bagOfWords.add(movie.getDirector().getFullName());
        for (Actor actor : movie.getActors()) {
            bagOfWords.add(actor.getFullName());
        }
        for (Genre genre : movie.getGenres()) {
            bagOfWords.add(genre.getTitle());
        }
        for (Tag tag : movie.getTags()) {
            bagOfWords.add(tag.getTitle());
        }
        this.movieBagOfWordsMap.put(movie.getId(), bagOfWords);
    }

    public float getUserAverageRating(long userID) throws TasteException {
        PreferenceArray preferenceArray = ratingsDataModel.getModel().getPreferencesFromUser(userID);
        float avgRating = 0;
        for (int i = 0; i < preferenceArray.length(); i++)
            avgRating += preferenceArray.getValue(i);
        return avgRating / preferenceArray.length();
    }



    private Map<String,WeightWrapper> userProfile(long userID) throws TasteException {
        PreferenceArray preferenceArray = ratingsDataModel.getModel().getPreferencesFromUser(userID);
        Map<String, WeightWrapper>  featuresWeight=new HashMap<>();
        float averageUserRating=this.getUserAverageRating(userID);
        System.out.println("AVERAGE USER RATING " +averageUserRating);
        for (int i=0;i<preferenceArray.length();i++)
            for (String feature:movieBagOfWordsMap.get(preferenceArray.getItemID(i))) {
                    if (featuresWeight.containsKSyey(feature)) {
                            featuresWeight.get(feature).addRating(preferenceArray.getValue(i)-averageUserRating);
                            featuresWeight.get(feature).incrementNoOfMOvies();
                    }
                    else {
                        featuresWeight.put(feature,new WeightWrapper(preferenceArray.getValue(i)-averageUserRating,1));
                    }
        }
        return featuresWeight;
    }


    private Set<Long> getAllItems()  {
        return new HashSet<Long>(this.movieService.getAllMoviesIds());
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

}
