package eus.ehu.ridesfx.dataAccess;

import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.configuration.UtilDate;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import jakarta.persistence.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;


/**
 * Implements the Data Access utility to the objectDb database
 */
public class DataAccess {

    protected EntityManager db;
    protected EntityManagerFactory emf;

    private BCrypt passwordGen;

    public DataAccess() {

        this.open();

    }

    public DataAccess(boolean initializeMode) {

        this.open(initializeMode);

    }

    public void open() {
        open(false);
    }


    public void open(boolean initializeMode) {

        Config config = Config.getInstance();

        System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());
        String fileName = config.getDatabaseName();
        if (initializeMode) {
            fileName = fileName + ";drop";
            System.out.println("Deleting the DataBase");
        }
        boolean local = config.isDataAccessLocal();
        if (local) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                // so destroy it manually.
                System.out.println("Error in DataAccess: " + e.getMessage());
                //StandardServiceRegistryBuilder.destroy(registry);
            }
            db = emf.createEntityManager();
            System.out.println("DataBase opened");
        }
    }



    public void reset() {
        db.getTransaction().begin();
        db.createNativeQuery("DELETE FROM USERS_RIDE").executeUpdate();
        db.createNativeQuery("DELETE FROM Ride_RideBook").executeUpdate();
        db.createNativeQuery("DELETE FROM RIDEBOOK").executeUpdate();
        db.createQuery("DELETE FROM Ride ").executeUpdate();
        db.createQuery("DELETE FROM User ").executeUpdate();
        db.getTransaction().commit();
    }

    public void initializeDB() {

        this.reset();

        db.getTransaction().begin();

        try {

            Calendar today = Calendar.getInstance();

            int month = today.get(Calendar.MONTH);
            int year = today.get(Calendar.YEAR);
            if (month == 12) {
                month = 1;
                year += 1;
            }


            //Create drivers
            Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
            String hashedPassword = BCrypt.hashpw("1234", BCrypt.gensalt(12));
            driver1.setPassword(hashedPassword);
            Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
            Driver driver3 = new Driver("driver3@gmail.com", "Test driver");
            Traveler traveler1 = new Traveler("traveler1@gmail.com", "Andrea Lezo");
            traveler1.setPassword(hashedPassword);


            //Create rides
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), 4, 7);
            driver1.addRide("Donostia", "Gasteiz", UtilDate.newDate(year, month, 6), 4, 8);
            driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);
            driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);
            driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
            driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
            driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);
            driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), 4, 7);
            driver1.addRide("Donostia", "Gasteiz", UtilDate.newDate(year, month, 6), 4, 8);
            driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);
            // May 2024
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(2024, 5, 15), 4, 7);
            driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(2024, 6, 15), 4, 7);
            driver1.addRide("Donostia", "Gasteiz", UtilDate.newDate(2024, 5, 6), 4, 8);
            driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(2024, 5, 25), 4, 4);
            driver1.addRide("Donostia", "Iruña", UtilDate.newDate(2024, 5, 7), 4, 8);
            driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(2024, 5, 15), 3, 3);
            driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(2024, 5, 25), 2, 5);
            driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(2024, 5, 6), 2, 5);
            driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(2024, 5, 14), 1, 3);


            db.persist(driver1);
            db.persist(driver2);
            db.persist(driver3);
            db.persist(traveler1);


            db.getTransaction().commit();

            System.out.println("Db initialized");
        } catch (Exception e) {
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

        TypedQuery<Ride> query = db.createQuery("SELECT ride FROM Ride ride "
                + "WHERE ride.date=?1 AND ride.status=?2 ", Ride.class);
        query.setParameter(1, date);
        query.setParameter(2, Ride.STATUS.ACTIVE);

        return query.getResultList();
    }

    public List<Ride> getRidesFromDriver(String email) {
        System.out.println(">> DataAccess: getRidesFromDriver email");

        TypedQuery<Ride> query = db.createQuery("SELECT ride FROM Ride ride "
                + "WHERE ride.driver.email=?1 ", Ride.class);
        query.setParameter(1, email);
        return query.getResultList();

    }

    /**
     * This method returns all the cities where rides depart
     * @return collection of cities
     */
    public List<String> getDepartCities(){
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.fromLocation FROM Ride r WHERE r.status=?1 ORDER BY r.fromLocation", String.class);
        query.setParameter(1, Ride.STATUS.ACTIVE);
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
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.toLocation FROM Ride r WHERE r.fromLocation=?1 AND r.status=?2 ORDER BY r.toLocation",String.class);
        query.setParameter(1, from);
        query.setParameter(2, Ride.STATUS.ACTIVE);
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


        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.status=?5 AND r.fromLocation=?1 AND r.toLocation=?2 AND r.date BETWEEN ?3 and ?4 ",Date.class);

        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, firstDayMonthDate);
        query.setParameter(4, lastDayMonthDate);
        query.setParameter(5, Ride.STATUS.ACTIVE);

        List<Date> dates = query.getResultList();
        for (Date d:dates){
            res.add(d);
        }
        return res;
    }

    public List<Date> getDatesWithRides(String from, String to) {
        System.out.println(">> DataAccess: getEventsFromTo");
        List<Date> res = new ArrayList<>();

        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.fromLocation=?1 AND r.toLocation=?2 AND r.status=?3",Date.class);

        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, Ride.STATUS.ACTIVE);
        List<Date> dates = query.getResultList();
        for (Date d:dates){
            res.add(d);
        }
        return res;
    }
    private void generateTestingData() {
        // create domain entities and persist them
    }
    
    public void close() {
        db.close();
        System.out.println("DataBase is closed");
    }

    /**
     * This method logs in a driver
     * @param email the email of the driver
     * @param password the name of the driver
     * @return the driver that has logged in
     */
    public User login(String email, String password) {
        System.out.println(">> DataAccess: login");
        //without using findç
        User user;

        try{
            TypedQuery<User> query = db.createQuery("SELECT d FROM User d WHERE d.email = :email", User.class)
                    .setParameter("email", email);
            user = query.getSingleResult();
        }
        catch(NoResultException e){
            user = null;
        }
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        else{
            user = null;
            return user;
        }

    }

    /**
     * This method registers a new driver in the database
     *
     * @param email    the email of the driver
     * @param name     the name of the driver
     * @param password the password of the driver
     * @return a string with the result of the registration
     */
    public String register(String email, String name, String password, String repeatPassword, String role) {
        System.out.println(">> DataAccess: register");

        if (email.isEmpty() || name.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || role == null) {
            return "emptyFields";
        }

        //regular expression check for email
        if (!email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            return "invalidEmail";
        }

        if(password.length() < 6 || password.length() > 20){
            return "invalidPassword";
        }

        if(!password.equals(repeatPassword)){
            return "passwordMismatch";
        }

        if(name.length() > 20){
            return "invalidName";
        }
        //when registering the introduced password must be hashed and salted
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        db.getTransaction().begin();
        User user = null;
        switch (role){
            case ("DRIVER") -> {
                user = new Driver(email, name);
            }
            case ("TRAVELER") -> {
                user = new Traveler(email, name);
            }
        }
        assert user != null;
        user.setPassword(hashedPassword);
        if (db.find(User.class, email) != null) {
            db.getTransaction().commit();
            return "emailExists";
        }
        db.persist(user);
        db.getTransaction().commit();
        return "success";

    }
    public RideBook bookRide (Ride ride, Date date, int passengers, Traveler traveler){
        db.getTransaction().begin();
        RideBook book = new RideBook(ride, date,passengers, traveler);
        db.persist(book);
        db.getTransaction().commit();
        System.out.println(">> DataAccess: bookRide");
        return book;
    }

    public Ride cancelRide(Ride ride) {
        db.getTransaction().begin();

        Ride r = db.find(Ride.class, ride.getRideNumber());
        r.setStatus(Ride.STATUS.CANCELLED);
        db.getTransaction().commit();
        return r;
    }

    public void reenableRide(Ride ride) {
        db.getTransaction().begin();
        Ride r = db.find(Ride.class, ride.getRideNumber());
        r.setStatus(Ride.STATUS.ACTIVE);
        db.getTransaction().commit();
    }
    //gets ridebooks of the user
    public List<RideBook> getRideBooks(Traveler t) {
        TypedQuery<RideBook> query = db.createQuery("SELECT rb FROM RideBook rb WHERE rb.traveler.email = :email", RideBook.class)
                .setParameter("email", t.getEmail());
        return query.getResultList();
    }
}



