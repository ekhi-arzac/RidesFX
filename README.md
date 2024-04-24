ShareTrip: a JavaFX (modular+hibernate+h2+properties) desktop application project 
==============================
[![Java CI with Maven](https://github.com/ekhi-arzac/RidesFX/actions/workflows/maven.yml/badge.svg)](https://github.com/ekhi-arzac/RidesFX/actions/workflows/maven.yml)
## Introduction
This application is a desktop application that allows carpooling between individuals. Any user can offer their own rides to other users or request a ride from other users. 

The application is developed using JavaFX, Hibernate, H2, and Properties. 

## Sprints

* ### Sprint 1
  ---

For this first sprint, we needed to do the requirements analysis and start designing some features for the application.

* #### Requirements analysis:
The requirement analysis has been done using Star UML and can be found [in the UML branch](https://github.com/ekhi-arzac/RidesFX/tree/UML). The requirements analysis is composed of the following documents:
* ##### Use Case Diagram: 
    There are four actors in our system: the guest, who can only view the rides; the user, who can offer and request rides, depending on the type of user it is, a driver or a traveler. The driver can offer rides, and the traveler can request rides.
* ##### Domain model: 
  The key points of our domain model are the two types of users: driver and traveler. The driver has a one-to-many relationships to the class Ride, while the traveler has a one-to-many relationship with the bookings. The user class also has a one to many relationship with the class Message. And finally,the traveler can also generate several alerts.
* ##### Event flow: 
  The event flow can be found in the use case diagram. There is one event flow for each actor: register for the guest, view all messages for the user, offer a ride for the driver, and request a ride for the traveler.
* #### Login and register functionalities: 
  The user can register and login to the application. To achieve this, we have used te `LoginController` and `RegisterController` classes.
  * ##### Register:
    The user can register by filling in the required fields: name, password and email. There are also error messages that will appear if the user tries to register with an already existing email or if the registration is not valid.
  * ##### Login:
    The user can log in by filling in the required fields: email and password. There are also error messages that will appear if the user tries to log in with an invalid email or password.
* #### Borderpane layout: 
  The application has a borderpane layout, with a top bar that contains the logo and the user's name, a left bar that contains the menu, and a center pane that contains the main content of the application.
  In the `MainGUI` class, we have created the `showSceneInCenter` which allows us to change the center pane of the application.
```Java
  public void showSceneInCenter(String window) {
  switch (window) {
  case "queryRides" -> mainPane.setCenter(queryRidesLag.ui);
  case "createRide" -> mainPane.setCenter(createRideLag.ui);
  case "login" -> mainPane.setCenter(loginLag.ui);
  case "register" -> mainPane.setCenter(registerLag.ui);
  }
  ```
Using this method we are able to change the center pane of the application to the desired one.

* ### Sprint 2
  ---

For this second sprint, we needed to implement at least two use cases.
* #### User database:
First of all, big changes were made in the database. Now, It is a user database, where users can access as a driver or as a traveler. If the user is a driver, he can offer rides, and if the user is a traveler, he can request rides.
* #### Book a ride:
The main use case we implemented was to book a ride. Now, when a traveler queries a ride, the book button exists, so that they can book it. This creates a ride request that will be stored into the database.
* #### Messenger functionality:
The second use case is the messaging option. It is still in a very early stage, but now the driver can send messages with another driver logged in at the same time, with the same ride identification.
* #### Guest user:
Also, when initializing the application, instead of a test driver, we will use a guest. This guest will be able to see all the rides, but not to book them.
* #### Event flows and sequence diagram:
On the other hand, we have implemented new event flows, for the: Check availability of rides, create alert, and remove alert in the use case diagram. 

* ### Sprint 3
  ---

* For the third sprint we are going to fix fill the deficiencies that the project had in the last sprint:
  - Add the hours that we work in each issue in the second sprint
  - Do the planification of the third sprint 
  - Fix the sequence diagram
  - Implement a correct keyboard navigation through the text fields 
  - Update the documentation to do the understanding opf the structure easier
 
* Therefore we will implement new functions for the program
#### Logout feature:
The users should have the option to Logout from the program whenever they want 
  
  

_Authors: Amets Martiarena, Ekhi Arzac, Jon Reboiro._





