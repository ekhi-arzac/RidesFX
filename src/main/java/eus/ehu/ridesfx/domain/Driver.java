package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Entity
@DiscriminatorValue("DRIVER")
public class Driver extends User implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<Ride>();

	public Driver() {
		super();
	}

	public Driver(String email, String name) {
		super(email, name);
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



	public String toString(){
		return super.getEmail()+";"+super.getName()+rides;
	}

	/**
	 * This method creates a new ride for the driver
	 *
	 * @param
	 * @param
	 * @return
	 */
	public Ride addRide(String from, String to, Date date, int nPlaces, float price)  {
		Ride ride=new Ride(from,to,date,nPlaces,price, this);
		rides.add(ride);
		return ride;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 *
	 * @param from the origin location
	 * @param to the destination location
	 * @param date the date of the ride
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String from, String to, Date date)  {
		for (Ride r:rides)
			if ( (java.util.Objects.equals(r.getFromLocation(),from)) && (java.util.Objects.equals(r.getToLocation(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
				return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (super.getEmail() != other.getEmail())
			return false;
		return true;
	}

	public Ride removeRide(String from, String to, Date date) {
		boolean found=false;
		int index=0;
		Ride r=null;
		while (!found && index<=rides.size()) {
			r=rides.get(++index);
			if ( (java.util.Objects.equals(r.getFromLocation(),from)) && (java.util.Objects.equals(r.getToLocation(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
				found=true;
		}

		if (found) {
			rides.remove(index);
			return r;
		} else return null;
	}

    public String getPassword() {
		return super.getPassword();
    }
}