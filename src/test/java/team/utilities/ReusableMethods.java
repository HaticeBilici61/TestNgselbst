package team.utilities;

import org.apache.commons.io.FileUtils;
//import org.apache.poi.hpsf.Date;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import team.pages.HomePageUS3;
import team.pages.HomePageUS4;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class ReusableMethods {


    public static String getScreenshot(String name)  {
        // naming the screenshot with the current date to avoid duplication
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/test-output/Screenshots/" + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    //========Switching Window=====//
    public static void switchToWindow(String targetTitle) {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if (Driver.getDriver().getTitle().equals(targetTitle)) {
                return;
            }
        }
        Driver.getDriver().switchTo().window(origin);
    }

    //========Hover Over=====//
    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //==========Return a list of string given a list of Web Element====////
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //========Returns the Text of the element given an element locator==//
    public static List<String> getElementsText(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //   HARD WAIT WITH THREAD.SLEEP
//   waitFor(5);  => waits for 5 second => Thread.sleep(5000)
    public static void waitFor(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //===============Explicit Wait==============//
    public static WebElement waitForVisibility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickablility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickablility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }

    public static void waitForPageToLoad(long timeout) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + timeout + " seconds");
        }
    }

    //======Fluent Wait====//
    public static WebElement fluentWait(final WebElement webElement, int timeout) {
        //FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver()).withTimeout(timeinsec, TimeUnit.SECONDS).pollingEvery(timeinsec, TimeUnit.SECONDS);
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(3))//Wait 3 second each time
                .pollingEvery(Duration.ofSeconds(1))////Check for the element every 1 second
                .ignoring(NoSuchMethodException.class);
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return webElement;
            }
        });
        return element;
    }

    /**
     * Performs double click action on an element
     *
     * @param element
     */
    public static void doubleClick(WebElement element) {
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    /**
     * @param element
     * @param check
     */
    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    /**
     * Selects a random value from a dropdown list and returns the selected Web Element
     *
     * @param select
     * @return
     */
    public static WebElement selectRandomTextFromDropdown(Select select) {
        Random random = new Random();
        List<WebElement> weblist = select.getOptions();
        int optionIndex = 1 + random.nextInt(weblist.size() - 1);
        select.selectByIndex(optionIndex);
        return select.getFirstSelectedOption();
    }

    /**
     * Verifies whether the element matching the provided locator is displayed on page
     * fails if the element matching the provided locator is not found or not displayed
     *
     * @param by
     */
    public static void verifyElementDisplayed(By by) {
        try {
            assertTrue("Element not visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + by);
        }
    }

    /**
     * Verifies whether the element matching the provided locator is NOT displayed on page
     * fails if the element matching the provided locator is not found or not displayed
     *
     * @param by
     */
    public static void verifyElementNotDisplayed(By by) {
        try {
            assertFalse("Element should not be visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies whether the element matching the provided WebElement is NOT displayed on page
     * fails if the element matching the WebElement is not found or not displayed
     *
     * @paramWebElement
     */
    public static void verifyElementNotDisplayed(WebElement element) {
        try {
            assertFalse("Element should not be visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies whether the element is displayed on page
     * fails if the element is not found or not displayed
     *
     * @param element
     */
    public static void verifyElementDisplayed(WebElement element) {
        try {
            assertTrue("Element not visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + element);
        }
    }


//     public static void register(String username,String email,String password){
//         Driver.getDriver().get(ConfigReader.getProperty("url"));
//         HomePageUS3 homePage=new HomePageUS3();
//         homePage.register.click();
//         ReusableMethods.waitFor(2);
//         homePage.registerUsername.sendKeys(ConfigReader.getProperty("positive_email"));
//         homePage.registerEmail.sendKeys(ConfigReader.getProperty("positive_email"));
//         homePage.registerPassword.sendKeys(ConfigReader.getProperty("positive_password"));
//         homePage.checkbox.click();
//         homePage.registerSignUp.click();


    public static void clickByJS(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor)Driver.getDriver();
        js.executeScript("arguments[0].click();",element);
    }
    public static void scrollEndJS(){
        JavascriptExecutor js = (JavascriptExecutor)Driver.getDriver();
        js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    public static void scrollIntoViewJS(WebElement element){
        JavascriptExecutor js=(JavascriptExecutor)Driver.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);",element);

    }

    public static void takeScreenshotOfElement(WebElement element) throws IOException {
        //        1. take screenshot
        File image = element.getScreenshotAs(OutputType.FILE);
        //        2. save screenshot
        //        path
        String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        String path = System.getProperty("user.dir")+"/test-output/Screenshots/"+currentTime+"image.png";
        FileUtils.copyFile(image,new File(path));
    }



    public static void loginSingIn() {

        HomePageUS3 homePage3 = new HomePageUS3();
       // try {
            Driver.getDriver().get(ConfigReader.getProperty("url"));
           homePage3 .signIn.click();
            ReusableMethods.waitFor(2);
           homePage3.email.sendKeys(ConfigReader.getProperty("positive_email"));
            ReusableMethods.waitFor(2);
            homePage3.password.sendKeys(ConfigReader.getProperty("positive_password"));
            ReusableMethods.waitFor(2);
            homePage3.signInButonu.click();
            ReusableMethods.waitFor(2);
     //   } catch (Exception e) {

//        }
//        try {
//            homePage3.signout.click();
//        } catch (Exception e) {
//        }

    }}

         /*
     hangi tur tool lari kullaniyorsunuz
     kullandiginiz proje su an nedir
     test ng de hangi anotasyonlari kullaniyorsunuz
     pom frame work u nedir
     singelton driver nedir
     test ng de ne tur asertion vardir
     datalari aldiginiz kisi ve yerler
     javada kendinize kac verirsiniz
     sirkette yasadiginiz zorluklar ve cozum yollari
     version cont ne kullanirsiniz
     user story den demo toplantisina kadar yaptiginiz isler
     testten sonra hat cikarsa ne yaparsiniz

     */