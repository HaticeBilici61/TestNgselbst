package team.tests;

import org.testng.annotations.Test;
import team.pages.LoginPage_Deneme;
import team.utilities.ConfigReader;
import team.utilities.Driver;

public class Deneme {



        public String fakeMailAl() {
            Driver.getDriver().get("https://www.fakemail.net/");
            LoginPage_Deneme loginPage= new LoginPage_Deneme();
            String fakeMail = loginPage.fakeMail.getText();
            Driver.closeDriver();
            return fakeMail;
        }
        @Test
        public void testName2() {
            ConfigReader.setProperty("satici_email", fakeMailAl());
            ConfigReader.getProperty("url");
            //  ReusableMethods.login(ConfigReader.getProperty("satici_email"), ConfigReader.getProperty("satici_password"));
            Driver.closeDriver();
            //login methodu burada olusturmadigim icin uyutmaya aldim ...team ile biur deneme yaptik

        }

    }
