package eus.ehu.ridesfx.businessLogic;

import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.Traveler;
import eus.ehu.ridesfx.domain.User;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import eus.ehu.ridesfx.msgClient.MsgClient;
import eus.ehu.ridesfx.uicontrollers.CarPoolChatController;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Interface that specifies the business logic.
 */

public interface BlFacade {
	/**
	 * This method retrieves the rides from two locations on a given date
	 *
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	List<Ride> getRides(String from, String to, Date date);
	List<Ride> getRidesFromDriver(String email);
	Ride cancelRide(Ride ride);

	/**
	 * This method retrieves from the database the dates a month for which there are events
	 *
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);


	/**
	 * This method retrieves from the database the dates in a month for which there are events
	 *
	 * @param date of the month for which days with events want to be retrieved
	 * @return collection of dates
	 */
	public Vector<Date> getEventsMonth(Date date);


	void setCurrentUser(User user);

	User getCurrentUser();

	Ride createRide(String text, String text1, Date date, int inputSeats, float price, String email) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;


	/**
	 * This method returns all the cities where rides depart
	 *
	 * @return collection of cities
	 */

	public List<String> getDepartCities();

	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city
	 *
	 * @param from the departure location of a ride
	 * @return all the arrival destinations
	 */

	public List<String> getDestinationCities(String from);


	List<Date> getDatesWithRides(String value, String value1);

	public User login(String email, String name);

	String register(String email, String username, String password, String repeatPassword, String role);

	void reenableRide(Ride ride);

	public void bookRide(Ride ride, Date date, int passengers, Traveler traveler);

	void sendMessage(int channel, String message);
	void setChatController(CarPoolChatController chatController);

	void closeMsgClient();

    MsgClient getMsgClient();
}
