package com.energyapp.controller;

import com.energyapp.dto.HourPriceDTO;
import com.energyapp.service.ElprisAnalyzer;
import com.energyapp.service.ElprisFetch;
import com.energyapp.model.Elpris;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PriceController {

    private final ElprisFetch fetcher = new ElprisFetch();

    @GetMapping("/prices")
    public List<HourPriceDTO> getPrices(@RequestParam String zone) {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Elpris> prices = new ArrayList<>();

        prices.addAll(fetcher.getPrice(zone, today));
        prices.addAll(fetcher.getPrice(zone, tomorrow));

        return ElprisAnalyzer.groupByHour(prices);
    }
}