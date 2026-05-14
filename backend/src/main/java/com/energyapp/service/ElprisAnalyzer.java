

package com.energyapp.service;

import com.energyapp.model.Elpris;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ElprisAnalyzer {

    public static double mean(List<Elpris> prices) {
        return prices.stream()
                .mapToDouble(Elpris::getSEK)
                .average()
                .orElse(0);
    }

    public static Elpris cheapest(List<Elpris> prices) {
        return prices.stream()
                .min(Comparator.comparingDouble(Elpris::getSEK)
                        .thenComparing(Elpris::getTimeStart))
                .orElse(null);
    }

    public static Elpris mostExpensive(List<Elpris> prices) {
        return prices.stream()
                .max(Comparator.comparingDouble(Elpris::getSEK)
                        .thenComparing(Elpris::getTimeStart))
                .orElse(null);
    }

    public static List<Elpris> bestPeriod(List<Elpris> prices, int hours) {
        if (hours <= 0 || prices == null || prices.size() < hours) {
            return List.of();
        }

        double windowSum = 0d;

        for (int i = 0; i < hours; i++) {
            windowSum += prices.get(i).getSEK();
        }

        double minSum = windowSum;
        int bestStart = 0;

        for (int i = hours; i < prices.size(); i++) {
            windowSum += prices.get(i).getSEK() - prices.get(i - hours).getSEK();

            if (windowSum < minSum) {
                minSum = windowSum;
                bestStart = i - hours + 1;
            }
        }

        return prices.subList(bestStart, bestStart + hours);
    }
    public static double periodAverage(List<Elpris> prices, Elpris start, int hours) {
        if (start == null || prices == null || hours <= 0)
            return 0;
    int index = prices.indexOf(start);
        if (index < 0 || index + hours > prices.size())
            return 0;
    return prices.subList(index, index + hours).stream()
            .mapToDouble(Elpris::getSEK)
            .average()
            .orElse(0);
    }
    public static List<Elpris> groupByHour(List<Elpris> prices) {
        return prices.stream()
                .collect(Collectors.groupingBy(p ->
                        p.getTimeStart().truncatedTo(java.time.temporal.ChronoUnit.HOURS)
                ))
                .values().stream()
                .map(list -> list.stream()
                        .min(Comparator.comparing(Elpris::getSEK))
                        .orElse(list.get(0))
                )
                .sorted(Comparator.comparing(Elpris::getTimeStart))
                .toList();
    }
}
