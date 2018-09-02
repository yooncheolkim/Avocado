package com.planet.avocado.data;

import java.util.List;

public class Product {
    public String id;
    public String type;
    public String name;
    public String imgPath;
    public Double avg;
    public String companyName;
    public List<String> commentList;
    public String userId;

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", avg=" + avg +
                ", companyName='" + companyName + '\'' +
                ", commentList=" + commentList +
                ", userId='" + userId + '\'' +
                '}';
    }
}
