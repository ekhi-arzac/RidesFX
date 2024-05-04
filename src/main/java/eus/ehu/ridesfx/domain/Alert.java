package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("ALERT")
public class Alert implements Serializable {
    public enum STATUS {
        AVAILABLE, NOT_AVAILABLE
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer alertNumber;

    @ManyToOne
    private Traveler traveler;

    private String fromLocation;
    private String toLocation;
    private int numPlaces;
    private Date date;

    private STATUS status;

    public Alert(Traveler traveler, String fromLocation, String toLocation, Date date, int numPlaces) {
        this.traveler = traveler;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.numPlaces = numPlaces;
        this.date = date;
        this.status = STATUS.NOT_AVAILABLE;
    }


    public Alert() {
        super();
    }

    public Integer getAlertNumber() {
        return alertNumber;
    }

    public void setAlertNumber(Integer alertNumber) {
        this.alertNumber = alertNumber;
    }

    public Traveler getTraveler() {
        return traveler;
    }

    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        if (status == STATUS.AVAILABLE) {
            return "Available";
        } else {
            return "Not Available";
        }
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
