package org.abondar.industrial.occupancy.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class BasicOccupancy {

    private int rooms;

    private Double price;
}
