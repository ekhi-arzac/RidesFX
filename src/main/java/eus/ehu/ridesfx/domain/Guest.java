package eus.ehu.ridesfx.domain;

public class Guest extends User {

        public Guest(String email, String name) {
            super(email, name);
        }

        public String getEmail() {
            return super.getEmail();
        }

        public String getName() {
            return super.getName();
        }

        @Override
        public String toString() {
            return "Guest{" +
                    "email='" + super.getEmail() + '\'' +
                    ", name='" + super.getName() + '\'' +
                    '}';
        }
}
