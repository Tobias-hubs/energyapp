
package com.energyapp.service;

import com.energyapp.dto.HourPriceDTO;
import com.energyapp.model.Elpris;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ElprisAnalyzer {

    public static List<HourPriceDTO> groupByHour(List<Elpris> prices) {

        return prices.stream()
                .collect(Collectors.groupingBy(p ->
                        p.getTimeStart().truncatedTo(ChronoUnit.HOURS)
                ))
                .values().stream()
                .map(list -> {

                    double avg = list.stream()
                            .mapToDouble(Elpris::getSEK)
                            .average()
                            .orElse(0);

                    ZonedDateTime start = list.get(0).getTimeStart();
                    ZonedDateTime end = start.plusHours(1);

                    return new HourPriceDTO(
                            String.format("%02d:00", start.getHour()),
                            String.format("%02d:00", end.getHour()),
                            avg
                    );
                })
                .sorted(Comparator.comparing(dto ->
                        Integer.parseInt(dto.getTimeStart().substring(0, 2))
                ))
                .toList();
    }
}
