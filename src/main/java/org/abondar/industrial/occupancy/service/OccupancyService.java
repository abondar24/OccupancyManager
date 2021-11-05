package org.abondar.industrial.occupancy.service;

import org.abondar.industrial.occupancy.data.BasicOccupancy;
import org.abondar.industrial.occupancy.data.Occupancy;
import org.abondar.industrial.occupancy.data.OccupancyData;
import org.abondar.industrial.occupancy.exception.OccupancyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        var premiumOccupancy = calculateBasicOccupancy(premiumPrices, premiumRooms);
        BasicOccupancy economyOccupancy;
        BasicOccupancy upgradeOccupancy=null;

        var premiumLeftover = premiumRooms - premiumOccupancy.getRooms();
        var economyShortage = economyPrices.size() > economyRooms;

        if (premiumLeftover > 0 && economyShortage) {
            var upgradedEconomy = economyPrices.subList(0, premiumLeftover);
            upgradeOccupancy = calculateBasicOccupancy(upgradedEconomy, premiumLeftover);

            var leftEconomy = economyPrices.subList(premiumLeftover, economyPrices.size());
            economyOccupancy = calculateBasicOccupancy(leftEconomy, economyRooms);

        } else {
            economyOccupancy = calculateBasicOccupancy(economyPrices, economyRooms);
        }

        return buildOccupancy(premiumOccupancy,economyOccupancy,upgradeOccupancy);
    }

    private Occupancy buildOccupancy(BasicOccupancy premium, BasicOccupancy economy, BasicOccupancy upgrade){
        var occ = new Occupancy();

        var economyOccupancyData = new OccupancyData();
        economyOccupancyData.setRooms(economy.getRooms());
        economyOccupancyData.setPrice(convertPrice(economy.getPrice()));

        occ.setEconomyData(economyOccupancyData);

        var premiumOccupancyData = new OccupancyData();
        if (upgrade!=null){
            var price = premium.getPrice()+ upgrade.getPrice();
            premiumOccupancyData.setPrice(convertPrice(price));
            premiumOccupancyData.setRooms(upgrade.getRooms()+ premium.getRooms());
        } else {
            premiumOccupancyData.setPrice(convertPrice(premium.getPrice()));
            premiumOccupancyData.setRooms(premium.getRooms());
        }

        occ.setPremiumData(premiumOccupancyData);

        return occ;
    }

    private BigDecimal convertPrice(Double price){
        return BigDecimal.valueOf(price).stripTrailingZeros();
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

    public BasicOccupancy calculateBasicOccupancy(List<Double> prices, int rooms) {


        var roomLimit = Math.min(rooms, prices.size());
        var price = prices.stream()
                .limit(roomLimit)
                .mapToDouble(Double::doubleValue)
                .sum();


        return new BasicOccupancy(roomLimit,price);
    }
}
