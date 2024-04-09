package eus.ehu.ridesfx.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
@DiscriminatorValue("TRAVELER")
public class Traveler extends User implements Serializable {
    private static final long serialVersionUID = 1L;

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


}
