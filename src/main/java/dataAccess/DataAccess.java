package dataAccess;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * Implements the Data Access utility to the objectDb database
 */
public class DataAccess {

    protected EntityManager db;
    protected EntityManagerFactory emf;

    ConfigXML config = ConfigXML.getInstance();

    public DataAccess(boolean initializeMode) {
        System.out.println("Creating DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());
        open(initializeMode);
    }

    public DataAccess() {
        this(false);
    }


    /**
     * This is the data access method that initializes the database with some events and questions.
     * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
     */
    public void initializeDB(){

        db.getTransaction().begin();

        try {

            Calendar today = Calendar.getInstance();

            int month=today.get(Calendar.MONTH);
            int year=today.get(Calendar.YEAR);
            if (month==12) { month=1; year+=1;}


            //Create drivers
            Driver driver1=new Driver("driver1@gmail.com","Aitor Fernandez");
            Driver driver2=new Driver("driver2@gmail.com","Ane Gaztañaga");
            Driver driver3=new Driver("driver3@gmail.com","Test driver");


            //Create rides
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 4, 7);
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month+1,15), 4, 7);

            driver1.addRide("Donostia", "Gasteiz", UtilDate.newDate(year,month,6), 4, 8);
            driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 4);

            driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8);

            driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 3);
            driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 5);
            driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 5);

            driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3);



            db.persist(driver1);
            db.persist(driver2);
            db.persist(driver3);


            db.getTransaction().commit();
            System.out.println("Db initialized");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * This method retrieves from the database the dates in a month for which there are events
     *
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */
    public Vector<Date> getEventsMonth(Date date) {
        System.out.println(">> DataAccess: getEventsMonth");
        Vector<Date> res = new Vector<Date>();

        Date firstDayMonthDate = UtilDate.firstDayMonth(date);
        Date lastDayMonthDate = UtilDate.lastDayMonth(date);


        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT ride.date FROM Ride ride "
                + "WHERE ride.date BETWEEN ?1 and ?2", Date.class);
        query.setParameter(1, firstDayMonthDate);
        query.setParameter(2, lastDayMonthDate);
        List<Date> dates = query.getResultList();
        for (Date d : dates) {
            System.out.println(d.toString());
            res.add(d);
        }
        return res;
    }


    public void open(boolean initializeMode) {

        System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

        String fileName = config.getDataBaseFilename();
        if (initializeMode) {
            fileName = fileName + ";drop";
            System.out.println("Deleting the DataBase");
        }

        if (config.isDataAccessLocal()) {
            emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
            db = emf.createEntityManager();
        } else {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", config.getDataBaseUser());
            properties.put("javax.persistence.jdbc.password", config.getDataBasePassword());

            emf = Persistence.createEntityManagerFactory("objectdb://" + config.getDataAccessNode() +
                    ":" + config.getDataAccessPort() + "/" + fileName, properties);

            db = emf.createEntityManager();
        }
    }


    public void close() {
        db.close();
        System.out.println("DataBase is closed");
    }

    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
        System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail + " date " + date);
        try {
            if (new Date().compareTo(date) > 0) {
                throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
            }
            db.getTransaction().begin();

            Driver driver = db.find(Driver.class, driverEmail);
            if (driver.doesRideExists(from, to, date)) {
                db.getTransaction().commit();
                throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
            }
            Ride ride = driver.addRide(from, to, date, nPlaces, price);
            //next instruction can be obviated
            db.persist(driver);
            db.getTransaction().commit();

            return ride;
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            db.getTransaction().commit();
            return null;
        }


    }

    public List<Ride> getRides(String origin, String destination, Date date) {
        System.out.println(">> DataAccess: getRides origin/dest/date");
        Vector<Ride> res = new Vector<>();

        TypedQuery<Ride> query = db.createQuery("SELECT ride FROM Ride ride "
                + "WHERE ride.date=?1 ", Ride.class);
        query.setParameter(1, date);


        return query.getResultList();
    }


    /**
     * This method returns all the cities where rides depart
     * @return collection of cities
     */
    public List<String> getDepartCities(){
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
        List<String> cities = query.getResultList();
        return cities;

    }
    /**
     * This method returns all the arrival destinations, from all rides that depart from a given city
     *
     * @param from the departure location of a ride
     * @return all the arrival destinations
     */
    public List<String> getArrivalCities(String from){
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
        query.setParameter(1, from);
        List<String> arrivingCities = query.getResultList();
        return arrivingCities;

    }

    /**
     * This method retrieves from the database the dates a month for which there are events
     * @param from the origin location of a ride
     * @param to the destination location of a ride
     * @param date of the month for which days with rides want to be retrieved
     * @return collection of rides
     */
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        System.out.println(">> DataAccess: getEventsMonth");
        List<Date> res = new ArrayList<>();

        Date firstDayMonthDate= UtilDate.firstDayMonth(date);
        Date lastDayMonthDate= UtilDate.lastDayMonth(date);


        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);

        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, firstDayMonthDate);
        query.setParameter(4, lastDayMonthDate);
        List<Date> dates = query.getResultList();
        for (Date d:dates){
            res.add(d);
        }
        return res;
    }

    public List<Date> getDatesWithRides(String from, String to) {
        System.out.println(">> DataAccess: getEventsFromTo");
        List<Date> res = new ArrayList<>();

        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2",Date.class);

        query.setParameter(1, from);
        query.setParameter(2, to);
        List<Date> dates = query.getResultList();
        for (Date d:dates){
            res.add(d);
        }
        return res;
    }
}