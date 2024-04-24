package com.example.calco.logic.business.entities;

/*
 * In project all info about mass in bd is stored int milligrams, so this enum also contains conversion multiplier
 */
public enum WeightUnits {
    Grams(0, "g", 1000),
    MilliGrams(1, "mg", 1);

    private final Integer unitId;
    // TODO use locale to determine different letters: g, г etc.
    private final String shortName;
    private final double conversionMultiplier;
    WeightUnits(Integer unitId, String shortName, double conversionMultiplier) {
        this.unitId = unitId;
        this.shortName = shortName;
        this.conversionMultiplier = conversionMultiplier;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public String getShortName() {
        return shortName;
    }

    public double getConversionMultiplier() {
        return conversionMultiplier;
    }
}
