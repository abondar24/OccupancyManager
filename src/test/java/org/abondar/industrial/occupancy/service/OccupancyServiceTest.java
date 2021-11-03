package org.abondar.industrial.occupancy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OccupancyServiceTest {

    @Autowired
    private OccupancyService occupancyService;

    @Autowired
    private DataService dataService;

    @Test
    public void splitPricesTest(){
        var prices = dataService.getGuestPrices();
        var splitPrices = occupancyService.splitPrices(prices);

        assertEquals(2,splitPrices.size());

        var premiumPrices = splitPrices.get(0);
        var noEconomy = premiumPrices.stream()
                .filter(pr->pr<100)
                .findAny()
                .isEmpty();

        assertEquals(6,premiumPrices.size());
        assertEquals(374,premiumPrices.get(0));
        assertTrue(noEconomy);


        var economyPrices = splitPrices.get(1);
        var noPremium =economyPrices.stream()
                .filter(pr->pr>100)
                .findAny()
                .isEmpty();

        assertEquals(4,economyPrices.size());
        assertEquals(99.99,economyPrices.get(0));
        assertTrue(noPremium);
    }


    @Test
    public void calculateBasicOccupancyTest(){
        var prices = List.of(374.0,209.0,155.0,115.0,101.0,100.0);

        var occupancyUsage = occupancyService.calculateBasicOccupancy(prices,3);
        assertEquals(3,occupancyUsage.getRooms());
        assertEquals(738,occupancyUsage.getPrice());


        occupancyUsage = occupancyService.calculateBasicOccupancy(prices,7);
        assertEquals(6,occupancyUsage.getRooms());
        assertEquals(1054,occupancyUsage.getPrice());

        occupancyUsage = occupancyService.calculateBasicOccupancy(prices,2);
        assertEquals(2,occupancyUsage.getRooms());
        assertEquals(583,occupancyUsage.getPrice());
    }

}
