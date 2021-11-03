package org.abondar.industrial.occupancy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.abondar.industrial.occupancy.util.OccupancyUtil.GUEST_PRICES_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataServiceTest {

    @Autowired
    private DataService dataService;

    @Test
    public void readJsonTest() {
        var prices = dataService.getGuestPrices();

        assertFalse(prices.isEmpty());
        assertEquals(10, prices.size());
        assertEquals(23, prices.get(0));
    }
}
