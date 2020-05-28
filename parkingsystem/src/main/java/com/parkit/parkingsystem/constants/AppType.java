package com.parkit.parkingsystem.constants;

import com.parkit.parkingsystem.model.Ticket;

/**
 * <b>Useful to make an easier presentation of Application</b>
 * <p>even vehicle which don't pay anything can be recorded as recurrent</p>
 */
public class AppType {
    /**
     * <ul>
     *     <li>If True, all vehicles can be recorded as recurrent; </li>
     *     <li>If False, only vehicle which have already pay something can be recurrent</li>
     * </ul>
     * @see com.parkit.parkingsystem.service.FareCalculatorService#calculateFare(Ticket)
     */
    public final static boolean PRESENTATION = true;
}
