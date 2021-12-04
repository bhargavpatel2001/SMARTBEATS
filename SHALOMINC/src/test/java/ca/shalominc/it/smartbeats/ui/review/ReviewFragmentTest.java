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
        @Test
        public void isEmailTestCase1() throws Exception {
            assertFalse(RF.isEmailTestCase("Alice1234"));
        }
        @Test
        public void isEmailTestCase2() throws Exception {
            assertFalse(RF.isEmailTestCase("Alice1234@"));
        }
        @Test
        public void isEmailTestCase3() throws Exception {
            assertFalse(RF.isEmailTestCase("Alice1234@gmail"));
        }
        @Test
        public void isPhoneTestCase() throws Exception {
            assertTrue(RF.isPhoneTestCase("6479755009"));
        }
        @Test
        public void isPhoneTestCase2() throws Exception {
            assertFalse(RF.isPhoneTestCase("123bob123"));
        }
        @Test
        public void isPhoneTestCase3() throws Exception {
            assertFalse(RF.isPhoneTestCase("69"));
        }
        @Test
        public void isPhoneTestCase4() throws Exception {
            assertFalse(RF.isPhoneTestCase("669963900910"));
        }
}
