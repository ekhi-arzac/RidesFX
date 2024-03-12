package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;


/**
 * Implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BlFacade")
public class BlFacadeImplementation implements BlFacade {

	DataAccess dbManager;
	ConfigXML config = ConfigXML.getInstance();
	private Driver currentDriver;

	public BlFacadeImplementation()  {
		System.out.println("Creating BlFacadeImplementation instance");
		boolean initialize = config.getDataBaseOpenMode().equals("initialize");
		dbManager = new DataAccess(initialize);
		if (initialize)
			dbManager.initializeDB();
		dbManager.close();
	}

	public BlFacadeImplementation(DataAccess dam)  {
		System.out.println("Creating BlFacadeImplementation instance with DataAccess parameter");
		if (config.getDataBaseOpenMode().equals("initialize")) {
			dam.open(true);
			dam.initializeDB();
			dam.close();
		}
		dbManager = dam;
	}


	@WebMethod
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException {

		dbManager.open(false);
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
		dbManager.close();
		return ride;
	}

	@Override
	public List<Ride> getRides(String origin, String destination, Date date) {
		dbManager.open(false);
		List<Ride>  events = dbManager.getRides(origin, destination, date);
		dbManager.close();
		return events;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@WebMethod
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open(false);
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}


	/**
	 * This method invokes the data access to retrieve the dates a month for which there are events
	 *
	 * @param date of the month for which days with events want to be retrieved
	 * @return collection of dates
	 */

	@WebMethod
	public Vector<Date> getEventsMonth(Date date) {
		dbManager.open(false);
		Vector<Date>  dates = dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}

	@Override
	public void setCurrentDriver(Driver driver) {
		this.currentDriver = driver;
	}

	@Override
	public Driver getCurrentDriver() {
		return this.currentDriver;
	}

	public void close() {
		dbManager.close();
	}

	/**
	 * This method invokes the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */
	@WebMethod
	public void initializeBD(){
		dbManager.open(false);
		dbManager.initializeDB();
		dbManager.close();
	}


	@WebMethod public List<String> getDepartCities(){
		dbManager.open(false);

		List<String> departLocations=dbManager.getDepartCities();

		dbManager.close();

		return departLocations;

	}
	/**
	 * {@inheritDoc}
	 */
	@WebMethod public List<String> getDestinationCities(String from){
		dbManager.open(false);
		List<String> targetCities=dbManager.getArrivalCities(from);
		dbManager.close();

		return targetCities;
	}

	@Override
	public List<Date> getDatesWithRides(String value, String value1) {
		dbManager.open(false);
		List<Date> dates = dbManager.getDatesWithRides(value, value1);
		dbManager.close();
		return dates;
	}

	public Driver login(String email, String name) {
		dbManager.open(false);
		Driver driver = dbManager.login(email, name);
		dbManager.close();
		return driver;
	}

}
