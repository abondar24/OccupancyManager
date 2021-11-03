package org.abondar.industrial.occupancy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        assertTrue(noEconomy);


        var economyPrices = splitPrices.get(1);
        var noPremium =economyPrices.stream()
                .filter(pr->pr>100)
                .findAny()
                .isEmpty();

        assertEquals(4,economyPrices.size());
        assertTrue(noPremium);
    }

}
