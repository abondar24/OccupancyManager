package org.abondar.industrial.occupancy.controller;

import org.abondar.industrial.occupancy.service.DataService;
import org.abondar.industrial.occupancy.service.OccupancyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@Import({OccupancyController.class, OccupancyService.class, DataService.class})
public class OccupancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOccupancy() throws Exception {

        mockMvc.perform(get("/occupancy/{premiumRooms}/{economyRooms}", 3, 3)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.premium.rooms").value(3))
                .andExpect(jsonPath("$.premium.price").value(738))
                .andExpect(jsonPath("$.economy.rooms").value(3))
                .andExpect(jsonPath("$.economy.price").value(167.99));


    }


    @Test
    public void testOccupancyNegativeRooms() throws Exception {

        mockMvc.perform(get("/occupancy/{premiumRooms}/{economyRooms}", -3, 3)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testOccupancyNoNumberRooms() throws Exception {

        mockMvc.perform(get("/occupancy/{premiumRooms}/{economyRooms}", "test", 3)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}
