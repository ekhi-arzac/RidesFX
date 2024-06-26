package eus.ehu.ridesfx.businessLogic;

import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.dataAccess.DataAccess;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import eus.ehu.ridesfx.msgClient.MsgClient;
import eus.ehu.ridesfx.uicontrollers.CarPoolChatController;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * Implements the business logic as a web service.
 */
public class BlFacadeImplementation implements BlFacade {
    MsgClient msgClient;
	DataAccess dbManager;
	Config config = Config.getInstance();
	private User currentUser;
	boolean initialize;

	public BlFacadeImplementation()  {
		System.out.println("Creating BlFacadeImplementation instance");
		dbManager = new DataAccess(true);
		System.out.println("initialized");
		initialize = config.getDataBaseOpenMode().equals("initialize");

		if (initialize)
			dbManager.initializeDB();

	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
		return ride;
	}

	@Override
	public List<Ride> getRides(String origin, String destination, Date date) {
		List<Ride>  events = dbManager.getRides(origin, destination, date);
		return events;
	}

	@Override
	public List<Ride> getRidesFromDriver(String email) {
		List<Ride> events = dbManager.getRidesFromDriver(email);
		return events;
	}
	public Ride cancelRide(Ride ride) {
		Ride r = dbManager.cancelRide(ride);
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		return dates;
	}


	/**
	 * This method invokes the data access to retrieve the dates a month for which there are events
	 *
	 * @param date of the month for which days with events want to be retrieved
	 * @return collection of dates
	 */

	public Vector<Date> getEventsMonth(Date date) {
		Vector<Date>  dates = dbManager.getEventsMonth(date);
		return dates;
	}

	@Override
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	@Override
	public User getCurrentUser() {
		return this.currentUser;
	}


	public List<String> getDepartCities(){
		List<String> departLocations=dbManager.getDepartCities();
		return departLocations;

	}
	/**
	 * {@inheritDoc}
	 */
	public List<String> getDestinationCities(String from){
		List<String> targetCities=dbManager.getArrivalCities(from);
		return targetCities;
	}

	@Override
	public List<Date> getDatesWithRides(String value, String value1) {
		List<Date> dates = dbManager.getDatesWithRides(value, value1);
		return dates;
	}
	@Override
	public User login(String email, String password) {
		User user = dbManager.login(email,password);
		if (user != null) {

			try {
				msgClient = new MsgClient(user.getName());
				return user;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	@Override
	public String register(String email, String name, String password, String repeatPassword, String role) {
		try {
			msgClient = new MsgClient(name);
		} catch (IOException e) {
			return "msgClientError";
		}

		return dbManager.register(email, name, password, repeatPassword, role);

	}

	public void reenableRide(Ride ride) {
		dbManager.reenableRide(ride);
	}

	public void bookRide(Ride ride, Date date, int passengers, Traveler traveler){
		dbManager.bookRide(ride, date, passengers, traveler);
	}

	public void sendMessage(int channel, String message) {
		msgClient.sendMessage(channel, message);
	}

	@Override
	public void setChatController(CarPoolChatController chatController) {
		msgClient.setChatController(chatController);
	}

	@Override
	public void closeMsgClient() {
        try {
            msgClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public MsgClient getMsgClient() {
		return msgClient;
	}
	@Override
	public void createAlert(Traveler traveler, String from, String to, Date date, int numPlaces) {
		dbManager.createAlert(traveler, from, to, date, numPlaces);
	}

	@Override
	public List<Alert> getAlerts() {
		List<Alert> alerts = dbManager.getAlerts(currentUser);
		return alerts;
	}

	//gets the ride books of the current traveler

	public List<RideBook> getTravelerRideBooks() {
		List<RideBook> rideBooks = dbManager.getRideBooks((Traveler)getCurrentUser());
		return rideBooks;
	}

	@Override
	public List<RideBook> getRideBooks(Ride newSelection) {
		return dbManager.getBooksOfRide(newSelection);
	}

	public void manageBook(RideBook rideBook, RideBook.STATUS status) {
		dbManager.manageBook(rideBook, status);
	}

	@Override
	public void findAlert(Ride r) {
		dbManager.findAlert(r);
	}

	@Override
	public Ride findRide(Alert alert) {
		return dbManager.findRide(alert);
	}

	@Override
	public void deleteAlert(Alert alert) {
		dbManager.deleteAlert(alert);
	}
}

