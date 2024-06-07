package com.example.calco.logic.business.entities;

import com.example.calco.logic.persistent.databases.AppDataBase;
import com.example.calco.logic.persistent.entities.PLimit;

import java.util.List;

public class LimitsLogic {

    public static void setLimit(Limit limit) {
        AppDataBase db = AppDataBase.getInstance();
        List<PLimit> pLimits = db.limitDao().findLimitByType(limit.getType().toString());
        if (pLimits.isEmpty()) {
            db.limitDao().insert(getPLimit(limit));
        }
        else if (pLimits.size() == 1) {
            db.limitDao().updateLimit(limit.getType().toString(), limit.getCalories(), limit.getCarbs(), limit.getFats(), limit.getProteins());
        }
        else {
            throw new IllegalStateException("More than one limit of the same type found in database");
        }
    }

    public static Limit getLimit(LimitType type) {
        AppDataBase db = AppDataBase.getInstance();
        List<PLimit> pLimits = db.limitDao().findLimitByType(type.toString());
        if (pLimits.isEmpty()) {
            return Limit.getDefault(type);
        }
        else if (pLimits.size() == 1) {
            PLimit pLimit = pLimits.get(0);
            return new Limit(type, pLimit.calories, pLimit.carbs, pLimit.fats, pLimit.proteins);
        }
        else {
            throw new IllegalStateException("More than one limit of the same type found in database");
        }
    }

    public static PLimit getPLimit(Limit limit) {
        PLimit pLimit = new PLimit();
        pLimit.type = limit.getType().toString();
        pLimit.calories = limit.getCalories();
        pLimit.carbs = limit.getCarbs();
        pLimit.fats = limit.getFats();
        pLimit.proteins = limit.getProteins();
        return pLimit;
    }
}
