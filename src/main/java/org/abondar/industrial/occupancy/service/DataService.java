package org.abondar.industrial.occupancy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.industrial.occupancy.data.GuestData;
import org.abondar.industrial.occupancy.exception.OccupancyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.abondar.industrial.occupancy.util.OccupancyUtil.ERR_DATA_READ;
import static org.abondar.industrial.occupancy.util.OccupancyUtil.GUEST_PRICES_FILE;

@Service
public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    private List<Double> guestPrices;

    @PostConstruct
    public void readPrices() {
        try {
            var mapper = new ObjectMapper();
            var data = mapper.readValue(new File(GUEST_PRICES_FILE), GuestData.class);
            this.guestPrices = data.getGuestPrices();
        } catch (IOException ex) {
            logger.error(ERR_DATA_READ);
            throw new OccupancyException(ex.getMessage());
        }
    }

    public List<Double> getGuestPrices() {
        return guestPrices;
    }
}
