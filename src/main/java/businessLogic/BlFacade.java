package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BlFacade  {
	/**
	 * This method retrieves the rides from two locations on a given date
	 *
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	@WebMethod List<Ride> getRides(String from, String to, Date date);

	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	@WebMethod public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);



	/**
	 * This method retrieves from the database the dates in a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
*/
	@WebMethod public Vector<Date> getEventsMonth(Date date);


    void setCurrentDriver(Driver driver);

	Driver getCurrentDriver();

	Ride createRide(String text, String text1, Date date, int inputSeats, float price, String email) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;



	/**
			* This method returns all the cities where rides depart
	 * @return collection of cities
	 */
	@WebMethod public List<String> getDepartCities();

	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city
	 *
	 * @param from the departure location of a ride
	 * @return all the arrival destinations
	 */
	@WebMethod public List<String> getDestinationCities(String from);


	List<Date> getDatesWithRides(String value, String value1);
}