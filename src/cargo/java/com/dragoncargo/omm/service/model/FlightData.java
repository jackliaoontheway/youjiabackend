package com.dragoncargo.omm.service.model;

import com.dragoncargo.general.model.Airline;
import com.dragoncargo.omm.model.NavicationInfo;

import lombok.Getter;
import lombok.Setter;

public class FlightData
{

    /**
     * 航线Code, SZ-JFK-CA
     */
    private @Setter @Getter String airlineCode;

    /**
     * 航线Name, 深圳直飞纽约国航
     */
    private @Setter @Getter String airlineName;

    /**
     * 航线区域 美东线 美西线
     */
    private @Setter @Getter String aviationAreaCode;

    private @Setter @Getter String aviationAreaName;

    /**
     * 始发地机场
     */
    private @Setter @Getter String departAirPortCode;

    private @Setter @Getter String departAirPortName;

    /**
     * 转飞地机场
     */
    private @Setter @Getter String transitAirPortCode;

    private @Setter @Getter String transitAirPortName;

    /**
     * 目的地机场
     */
    private @Setter @Getter String arrivingAirPortCode;

    private @Setter @Getter String arrivingAirPortName;

    /**
     * 航司
     */
    private @Setter @Getter String aviationCompanyCode;

    private @Setter @Getter String aviationCompanyName;

    private @Setter @Getter Boolean directFlight;

    private @Setter @Getter String description;

    public FlightData(NavicationInfo navicationInfo)
    {
        if (navicationInfo == null)
        {
            return;
        }
        Airline airline = navicationInfo.getAirline();
        if (airline == null)
        {
            return;
        }
        
        this.airlineCode = airline.getCode();
        this.airlineName = airline.getName();
        if (navicationInfo.getAviationAreaCode() != null)
        {
            this.aviationAreaCode = navicationInfo.getAviationAreaCode().getCode();
            this.aviationAreaName = navicationInfo.getAviationAreaCode().getName();
        }

        if (navicationInfo.getAviationCompany() != null)
        {
            this.aviationCompanyCode = navicationInfo.getAviationCompany().getCode();
            this.aviationCompanyName = navicationInfo.getAviationCompany().getName();
        }

        if (airline.getDepartAirPort() != null)
        {
            this.departAirPortCode = airline.getDepartAirPort().getCode();
            this.departAirPortName = airline.getDepartAirPort().getName();
        }

        if (airline.getArrivingAirPort() != null)
        {
            this.arrivingAirPortCode = airline.getArrivingAirPort().getCode();
            this.arrivingAirPortName = airline.getArrivingAirPort().getName();
        }

        if (airline.getTransitAirPort() != null)
        {
            this.transitAirPortCode = airline.getTransitAirPort().getCode();
            this.transitAirPortName = airline.getTransitAirPort().getName();
        }

        this.directFlight = navicationInfo.getDirectFlight();
        this.description = navicationInfo.getDescription();
    }

}
