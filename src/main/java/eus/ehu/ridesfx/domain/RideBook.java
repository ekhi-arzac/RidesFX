package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RideBook {
    public RideBook(Ride ride, Date date, String email, int passengers) {
    }

    public RideBook() {

    }

    public enum STATUS {
        ACCEPTED, CANCELLED, PENDING
    }

    @Id
    private int bookId;
    private String userEmail;

    @ManyToOne
    private Ride ride;
    @ManyToOne
    private Traveler traveler;
    private STATUS status;
    private Date date;
    private int passengers;

    //constructor
    public RideBook(Ride ride,Date date, int passengers, Traveler traveler) {
        //assign a randomID
        this.bookId =  (int) (Math.random() * 1000);
        this.ride = ride;
        this.status = STATUS.PENDING;
        this.date = date;
        this.passengers = passengers;
        this.traveler = traveler;
    }


    //getter and setters for attributes
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public STATUS getStatus() { return status; }
    public void setStatus(STATUS status) { this.status = status; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public int getPassengers() { return passengers; }
    public void setPassengers(int passengers) { this.passengers = passengers; }


    //getters from the ride object
    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }
    public Driver getDriver() { return ride.getDriver(); }
    public float getPrice() { return ride.getPrice(); }
    public int getNSeats() { return ride.getNumPlaces(); }
    public String getDriverName() { return ride.getDriver().getName(); }
    public String getContactEmail() { return ride.getDriver().getEmail(); }

    public String toString() {
        return "RideBook{" +
                "bookId=" + bookId +
                ", userEmail='" + userEmail + '\'' +
                ", ride=" + ride +
                ", status=" + status +
                ", date=" + date +
                ", passengers=" + passengers +
                '}';
    }
}
