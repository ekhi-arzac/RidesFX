package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RideBook {
    public RideBook(Ride ride, Date date, String email, int passengers) {
    }

    public enum STATUS {
        ACCEPTED, CANCELLED, PENDING
    }

    @Id
    private int bookId;
    private String userEmail;

    @ManyToOne
    private Ride ride;
    private STATUS status;
    private Date date;
    private int passengers;

    //constructor
    public RideBook(String userEmail, Ride ride, Date date, int passengers) {
        //assign a randomID
        this.bookId =  (int) (Math.random() * 1000);
        this.userEmail = userEmail;
        this.ride = ride;
        this.status = STATUS.PENDING;
        this.date = date;
        this.passengers = passengers;
    }


    //getter and setters for attributes
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public Ride getRide() {
        return ride;
    }
    public void setRide(Ride ride) {
        this.ride = ride;
    }
    public STATUS getStatus() {
        return status;
    }
    public void setStatus(STATUS status) {
        this.status = status;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getPassengers() {
        return passengers;
    }
    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}
