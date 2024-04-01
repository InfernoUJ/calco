package com.example.calco.ui.products.table;

public class ProductImpactRecordData {
    private String name;
    private Integer percentage;
    private Integer absoluteValue;
    private Integer imageId;

    public ProductImpactRecordData(String name, Integer percentage, Integer absoluteValue, Integer imageId) {
        this.name = name;
        this.percentage = percentage;
        this.absoluteValue = absoluteValue;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public Integer getAbsoluteValue() {
        return absoluteValue;
    }

    public Integer getImageId() {
        return imageId;
    }
}
