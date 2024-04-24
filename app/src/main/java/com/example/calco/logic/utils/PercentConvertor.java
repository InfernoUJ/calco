package com.example.calco.logic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PercentConvertor {
    public static <T> List<Map.Entry<T, Integer>> getPercentImpact(List<Map.Entry<T, Integer>> absoluteImpact) {
        int n = absoluteImpact.size();
        int totalMass = 0;
        for(int i = 0; i < n; i++) {
            totalMass += absoluteImpact.get(i).getValue();
        }

        List<Integer> entitiesPercents = new ArrayList<>(n);
        int currMassDifference = 0;
        for (int i = 0; i < n; i++) {
            int mass = absoluteImpact.get(i).getValue();
            double percent = (double) mass / totalMass * 100;
            if (percent < 1) {
                entitiesPercents.add(1);
                currMassDifference += 1;
            }
            else {
                if (currMassDifference > 0) {
                    entitiesPercents.add((int)Math.floor(percent));
                    currMassDifference -= 1;
                }
                else {
                    entitiesPercents.add((int)Math.ceil(percent));
                    currMassDifference += 1;
                }
            }
        }

        // subtract extra weight from the heaviest
        List<Integer> indices = IntStream.range(0, n)
                .boxed()
                .sorted((i1, i2) -> Integer.compare(absoluteImpact.get(i2).getValue(), absoluteImpact.get(i1).getValue()))
                .limit(currMassDifference)
                .collect(Collectors.toList());
        for (int index: indices) {
            entitiesPercents.set(index, entitiesPercents.get(index)-1);
        }

        List<Map.Entry<T, Integer>> entitiesWithPercents = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (entitiesPercents.get(i) != 0) {
                entitiesWithPercents.add(Map.entry(absoluteImpact.get(i).getKey(), entitiesPercents.get(i)));
            }
        }

        return entitiesWithPercents;
    }

    public static <T> List<Map.Entry<T, Integer>> getPercentImpact(List<T> entities, Function<T, Integer> absoluteImpactResolver) {
        List<Map.Entry<T, Integer>> absoluteImpacts = entities.stream().map(entity -> Map.entry(entity, absoluteImpactResolver.apply(entity))).toList();
        return getPercentImpact(absoluteImpacts);
    }
}
