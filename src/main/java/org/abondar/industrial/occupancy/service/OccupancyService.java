package org.abondar.industrial.occupancy.service;

import org.abondar.industrial.occupancy.data.Occupancy;
import org.abondar.industrial.occupancy.data.OccupancyUsage;
import org.abondar.industrial.occupancy.exception.OccupancyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.abondar.industrial.occupancy.util.OccupancyUtil.ERR_PRICES_EMPTY;
import static org.abondar.industrial.occupancy.util.OccupancyUtil.PREMIUM_MINIMUM_PRICE;

@Service
public class OccupancyService {

    private static final Logger logger = LoggerFactory.getLogger(OccupancyService.class);

    private final DataService dataService;

    @Autowired
    public OccupancyService(DataService dataService) {
        this.dataService = dataService;
    }

    public Occupancy calculateOccupancy(int premiumRooms, int economyRooms) {

        var guestPrices = dataService.getGuestPrices();
        if (guestPrices.isEmpty()) {
            throw new OccupancyException(ERR_PRICES_EMPTY);
        }

        var prices = splitPrices(guestPrices);
        var premiumPrices = prices.get(0);
        var economyPrices = prices.get(1);

        var premiumUsage = calculateBasicOccupancy(premiumPrices,premiumRooms);
        OccupancyUsage economyUsage = null;

        var premiumLeftover = premiumRooms - premiumUsage.getRooms();
        var economyShortage = economyPrices.size() > economyRooms;

        if (premiumLeftover>0 && economyShortage){
            var upgradedEconomy = economyPrices.subList(0,premiumLeftover);
            var upgradedUsage = calculateBasicOccupancy(upgradedEconomy,premiumLeftover);
            premiumUsage.setPrice(premiumUsage.getPrice()+upgradedUsage.getPrice());
            premiumUsage.setRooms(premiumUsage.getRooms()+upgradedUsage.getRooms());

            var leftEconomy = economyPrices.subList(premiumLeftover,economyPrices.size());
            economyUsage = calculateBasicOccupancy(leftEconomy,economyRooms);

        } else {
            economyUsage = calculateBasicOccupancy(economyPrices,economyRooms);
        }

        var occ = new Occupancy();
        occ.setPremiumData(premiumUsage);
        occ.setEconomyData(economyUsage);
        return occ;
    }

    public List<List<Double>> splitPrices(List<Double> guestPrices) {
        var premiumPrices = guestPrices.stream()
                .filter(pr -> pr >= PREMIUM_MINIMUM_PRICE)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        var economyPrices = guestPrices.stream()
                .filter(pr -> pr < PREMIUM_MINIMUM_PRICE)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return List.of(premiumPrices, economyPrices);

    }

    public OccupancyUsage calculateBasicOccupancy(List<Double> prices, int rooms) {
        var occupancyUsage = new OccupancyUsage();


        var roomLimit = Math.min(rooms, prices.size());
        var price = prices.stream()
                .limit(roomLimit)
                .mapToDouble(Double::doubleValue)
                .sum();

        occupancyUsage.setRooms(roomLimit);
        occupancyUsage.setPrice(price);
        return occupancyUsage;
    }
}
