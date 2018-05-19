package com.example.anuradha.regression.recipe_model;

public interface Recommend {
    String name();

    Recommendation recognize(final int id, final int recipeid);
}
