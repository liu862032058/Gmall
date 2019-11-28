package com.atguigu.gmall.bean;

import java.math.BigDecimal;

public class Movie {

    private String id;
    private String name;
    private BigDecimal doubanScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDoubanScore() {
        return doubanScore;
    }

    public void setDoubanScore(BigDecimal doubanScore) {
        this.doubanScore = doubanScore;
    }
}
