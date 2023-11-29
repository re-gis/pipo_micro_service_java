package com.manning.bippo.service.comps.pojo;

import com.google.common.collect.Lists;
import com.manning.bippo.dao.pojo.NtreisProperty;
import java.util.List;

public class BracketedComps {

    public NtreisProperty superior, equal, inferior;
    public CompRating superiorRating, equalRating, inferiorRating;

    public BracketedComps(NtreisProperty superior, CompRating supRating, NtreisProperty equal, CompRating eqRating, NtreisProperty inferior, CompRating infRating) {
        this.superior = superior;
        this.superiorRating = supRating;
        this.equal = equal;
        this.equalRating = eqRating;
        this.inferior = inferior;
        this.inferiorRating = infRating;
    }

    public BracketedComps(NtreisProperty superior, WeightedComp supWeight, NtreisProperty equal, WeightedComp eqWeight, NtreisProperty inferior, WeightedComp infWeight) {
        this(superior, new CompRating(supWeight), equal, new CompRating(eqWeight), inferior, new CompRating(infWeight));
    }

    public BracketedComps(NtreisProperty superior, NtreisProperty equal, NtreisProperty inferior) {
        this.superior = superior;
        this.equal = equal;
        this.inferior = inferior;
    }

    public List<NtreisProperty> toList() {
        return Lists.newArrayList(this.superior, this.equal, this.inferior);
    }
}
