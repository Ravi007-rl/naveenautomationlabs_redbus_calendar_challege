package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    private static WebDriver driver;
    public static void main( String[] args )
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = WebDriverManager.chromedriver().capabilities(options).create();
        driver.get("https://www.redbus.in/");
        driver.manage().window().maximize();

        List<String> weekEnds = getWeekEnds("Dec 2025");
        System.out.println(weekEnds);
        driver.quit();
    }

    private static List<String> getWeekEnds(String monthAndYear) {
        //Locators
        final By datePickerLocator = By.cssSelector("div.labelCalendarContainer");
        final By monthYearLocator = By.cssSelector("div[class*='IconBlock']:nth-of-type(2)");
        final By nextButtonLocator = By.cssSelector("div[class*='IconBlock']:nth-of-type(3)");
        final By weekEndLocator = By.cssSelector("span.fgdqFw , span.bwoYtA");

        //actions
        waitTillElementIsClickable(datePickerLocator).click();

        while(true) {
            String wholeString = waitTillElementIsVisible(monthYearLocator).getText();
            String monthYearText = wholeString.split("\\n")[0];
            String holidayText = wholeString.split("\\n").length ==1 ? "0 Holiday" : wholeString.split("\\n")[1];

            System.out.println(monthYearText);
            System.out.println(holidayText+"\n");

            if(!monthYearText.equals(monthAndYear)) waitTillElementIsClickable(nextButtonLocator).click();
            else break;
        }
        return waitTillAllElementLocated(weekEndLocator).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private static WebElement waitTillElementIsVisible(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static WebElement waitTillElementIsClickable(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private static List<WebElement> waitTillAllElementLocated(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
}