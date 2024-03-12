package eus.ehu.ridesfx;

import eus.ehu.ridesfx.dataAccess.DataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DBTest {

    private DataAccess db = new DataAccess();

    @BeforeEach
    public void setUp() {
        db.initializeDB();
    }

    @Test
    public void test() {
        assertThat(1+1 == 2).isTrue();
    }
}