package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.Vector;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USERS") // Renames the table to avoid using a reserved keyword
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    private String email;
    private String name;

    private String password;

    public User() {
        super();
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }


}
