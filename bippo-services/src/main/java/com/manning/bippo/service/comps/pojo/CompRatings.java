package com.manning.bippo.service.comps.pojo;

import java.io.Serializable;

public class CompRatings implements Serializable {

    public CompRating superior, equal, inferior;

    public CompRatings(CompRating superior, CompRating equal, CompRating inferior) {
        this.superior = superior;
        this.equal = equal;
        this.inferior = inferior;
    }
    
    public CompRatings(WeightedComp superior, WeightedComp equal, WeightedComp inferior) {
        this(new CompRating(superior), new CompRating(equal), new CompRating(inferior));
    }
}
