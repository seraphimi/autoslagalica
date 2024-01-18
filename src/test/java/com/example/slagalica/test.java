package com.example.slagalica;

import java.lang.*;
import java.lang.String;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;



import static java.time.temporal.ChronoUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static javax.swing.JOptionPane.showMessageDialog;
import java.awt.Toolkit;

public class test {

    static ChromeOptions options = new ChromeOptions();

    static {
        options.addArguments("user-data-dir=C:\\Users\\komit\\AppData\\Local\\Google\\Chrome\\User Data\\Default","--headless");
    }

    static {
        System.setProperty("webdriver.chrome.driver", ".\\chromedriver-win64\\chromedriver.exe");
    }

    static WebDriver driver = new ChromeDriver(options);
    static WebDriverWait wait = new WebDriverWait(driver, Duration.of(30, SECONDS));
    static JavascriptExecutor js = (JavascriptExecutor) driver;
    static char[] resenjeSlovaNiz;
    static char[] resenjeMojBrojNiz;
    static String SvaSlova;
    static ArrayList<String> sviBrojevi;
    static ArrayList<String> sveOperacije;
    static String trazeniBroj;
    static Actions actions = new Actions(driver);



    public static void main(String[] args) throws InterruptedException {
        
        driver.get("https://elearning.rcub.bg.ac.rs/moodle/login/index.php");

        wait.until(elementToBeClickable(By.xpath("/html/body/div[3]/div/section/div/div/div[1]/div[2]/form/div[1]/div[2]/input"))).sendKeys("seraphim");
        System.out.println("ovoradi");
        wait.until(elementToBeClickable(By.xpath("/html/body/div[3]/div/section/div/div/div[1]/div[2]/form/div[1]/div[5]/input"))).sendKeys("mojvrtija23");
        System.out.println("ovoradi");
        wait.until(elementToBeClickable(By.xpath("/html/body/div[3]/div/section/div/div/div[1]/div[2]/form/input[3]"))).click();
        System.out.println("ovoradi");


        driver.get("https://elearning.rcub.bg.ac.rs/moodle/course/view.php?id=1002");

        System.out.println("ovoradi");

        List<WebElement> elements = driver.findElements(By.xpath("//li[contains(@class,'activity resource modtype_resource ')]"));

        List<WebElement> elements2 = driver.findElements(By.xpath("//li[contains(@class,'activity resource modtype_resource ')]"));


        System.out.println("ovoradi");

        int i = 0;
        while(elements.size()==elements2.size()){
            Thread.sleep(1000);
            driver.navigate().refresh();
            elements2 = driver.findElements(By.xpath("//li[contains(@class,'activity resource modtype_resource ')]"));
            System.out.println(i++);
        }
        driver.close();
        Toolkit.getDefaultToolkit().beep();Toolkit.getDefaultToolkit().beep();Toolkit.getDefaultToolkit().beep();Toolkit.getDefaultToolkit().beep();Toolkit.getDefaultToolkit().beep();
        System.out.println("pogledajrez");
        showMessageDialog(null, "Pogledaj rezultate!");



    }
    public static void checkMoodle() {



    }
}
