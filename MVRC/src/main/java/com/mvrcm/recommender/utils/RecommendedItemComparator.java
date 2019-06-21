package com.mvrcm.recommender.utils;

import java.util.Comparator;

public class RecommendedItemComparator implements Comparator<RecommendedItem> {
    @Override
    public int compare(RecommendedItem o1, RecommendedItem o2) {
        return (int)(o1.getValue()-o2.getValue());
    }
}
