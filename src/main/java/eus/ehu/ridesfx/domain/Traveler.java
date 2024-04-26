package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("TRAVELER")
public class Traveler extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    private List<RideBook> rideBook;

    public Traveler(String email, String name) {
        super(email, name);
    }

    public Traveler() {
        super();
    }

    public void setPassword(String password){
        super.setPassword(password);
    }

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getPassword() {
        return super.getPassword();
    }

    public RideBook addRideBook(Ride ride, Date date, int passengers) {
        RideBook rideBook = new RideBook(ride, date, passengers, this);
        this.rideBook.add(rideBook);
        return rideBook;
    }
}
