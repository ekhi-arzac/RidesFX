package eus.ehu.ridesfx.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("ALERT")
public class Alert implements Serializable {

    @Id
    private String email;
    private String fromLocation;
    private String toLocation;
    private int numPlaces;
    private Date date;

    public Alert(String email, String from, String to) {
        super();
        this.email = email;
        this.fromLocation = from;
        this.toLocation = to;
    }

    public Alert(String email, String fromLocation, String toLocation, Date date, int numPlaces) {
        this.email = email;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.numPlaces = numPlaces;
        this.date = date;
    }

    public Alert() {
        super();
    }
}
