package ca.shalominc.it.smartbeats.ui.review;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReviewFragmentTest {

        private ReviewFragment RF;

        @Before
        public void setUp() throws Exception {
            RF = new ReviewFragment();
            System.out.println("Runs every test case");
        }

        @After
        public void cleanUp() {
            System.out.println("Runs every end of the test case");
        }

        @Test
        public void isEmailTestCase() throws Exception {
            assertTrue(RF.isEmailTestCase("Bob1234@gmail.com"));
        }
}
