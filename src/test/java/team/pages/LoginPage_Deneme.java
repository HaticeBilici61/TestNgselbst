package team.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import team.utilities.Driver;


public class LoginPage_Deneme {

    public LoginPage_Deneme(){
        PageFactory.initElements(Driver.getDriver(),this);
    }

    @FindBy(xpath = "//span[@id='email']")
    public WebElement fakeMail;
}
