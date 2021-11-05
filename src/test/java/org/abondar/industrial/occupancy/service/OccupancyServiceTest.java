package org.abondar.industrial.occupancy.service;


import org.abondar.industrial.occupancy.data.BasicOccupancy;
import org.abondar.industrial.occupancy.data.Occupancy;
import org.abondar.industrial.occupancy.data.OccupancyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OccupancyServiceTest {

    @Autowired
    private OccupancyService occupancyService;

    @Autowired
    private DataService dataService;

    private static Stream<Arguments> occupancyArgs() {
        return Stream.of(
                Arguments.arguments(3, 3,
                        buildOccupancy(3,3,738,167.99)),
                Arguments.arguments(7, 5,
                        buildOccupancy(6,4,1054,189.99)),
                Arguments.arguments(2, 7,
                        buildOccupancy(2,4,583,189.99)),
                Arguments.arguments(7, 1,
                        buildOccupancy(7,1,1153.99,45))
        );
    }

    private static Occupancy buildOccupancy(int premiumRooms, int economyRooms, double premiumPrice, double economyPrice) {
        var occ = new Occupancy();

        var economyOccupancyData = new OccupancyData();
        economyOccupancyData.setRooms(economyRooms);
        economyOccupancyData.setPrice(BigDecimal.valueOf(economyPrice).stripTrailingZeros());
        occ.setEconomyData(economyOccupancyData);

        var premiumOccupancyData = new OccupancyData();
        premiumOccupancyData.setPrice(BigDecimal.valueOf(premiumPrice).stripTrailingZeros());
        premiumOccupancyData.setRooms(premiumRooms);
        occ.setPremiumData(premiumOccupancyData);

        return occ;
    }

    private static Stream<Arguments> basicOccupancyArgs() {
        var prices = List.of(374.0, 209.0, 155.0, 115.0, 101.0, 100.0);
        return Stream.of(
                Arguments.arguments(prices, 3,
                        new BasicOccupancy(3, 738.0)),
                Arguments.arguments(prices, 7,
                        new BasicOccupancy(6, 1054.0)),
                Arguments.arguments(prices, 2,
                        new BasicOccupancy(2, 583.0))
        );
    }

    @Test
    public void splitPricesTest() {
        var prices = dataService.getGuestPrices();
        var splitPrices = occupancyService.splitPrices(prices);

        assertEquals(2, splitPrices.size());

        var premiumPrices = splitPrices.get(0);
        var noEconomy = premiumPrices.stream()
                .filter(pr -> pr < 100)
                .findAny()
                .isEmpty();

        assertEquals(6, premiumPrices.size());
        assertEquals(374, premiumPrices.get(0));
        assertTrue(noEconomy);


        var economyPrices = splitPrices.get(1);
        var noPremium = economyPrices.stream()
                .filter(pr -> pr > 100)
                .findAny()
                .isEmpty();

        assertEquals(4, economyPrices.size());
        assertEquals(99.99, economyPrices.get(0));
        assertTrue(noPremium);
    }

    @ParameterizedTest
    @MethodSource("basicOccupancyArgs")
    public void calculateBasicOccupancyTest(List<Double> prices, int rooms, BasicOccupancy occupancy) {

        var res = occupancyService.calculateBasicOccupancy(prices, rooms);
        assertEquals(occupancy.getRooms(), res.getRooms());
        assertEquals(occupancy.getPrice(), res.getPrice());

    }

    @ParameterizedTest
    @MethodSource("occupancyArgs")
    public void calculateOccupancyTest(int premium, int economy, Occupancy occupancy) {
        var res = occupancyService.calculateOccupancy(premium, economy);

        assertEquals(occupancy.getPremiumData().getRooms(), res.getPremiumData().getRooms());
        assertEquals(occupancy.getPremiumData().getPrice(), res.getPremiumData().getPrice());
        assertEquals(occupancy.getEconomyData().getRooms(), res.getEconomyData().getRooms());
        assertEquals(occupancy.getEconomyData().getPrice(), res.getEconomyData().getPrice());
    }
}
