package com.energyapp.controller;

import com.energyapp.service.ElprisFetch;
import com.energyapp.model.Elpris;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PriceController {

    private final ElprisFetch fetcher = new ElprisFetch();

    @GetMapping("/prices")
    public List<Elpris> getPrices(@RequestParam String zone) {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Elpris> prices = new java.util.ArrayList<>();

        prices.addAll(fetcher.getPrice(zone, today));
        prices.addAll(fetcher.getPrice(zone, tomorrow));

        prices.sort(java.util.Comparator.comparing(Elpris::getTimeStart));

        return prices;
    }
}