package com.mvrcm.recommender.utils;

public class RecommendedItem implements org.apache.mahout.cf.taste.recommender.RecommendedItem {
    private long itemId;
    private float value;

    public RecommendedItem(long itemId, float value) {
        this.itemId = itemId;
        this.value = value;
    }

    @Override
    public long getItemID() {
        return this.itemId;
    }

    @Override
    public float getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "RecommendedItem[" +
                "itemId=" + itemId +
                ", value=" + value +
                ']';
    }
}
