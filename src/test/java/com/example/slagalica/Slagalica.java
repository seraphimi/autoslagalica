package com.example.slagalica;

import java.lang.*;
import java.lang.String;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;


import static java.time.temporal.ChronoUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static javax.swing.JOptionPane.showMessageDialog;


public class Slagalica {

    static ChromeOptions options = new ChromeOptions();

    static {
        options.addArguments("user-data-dir=C:\\Users\\komit\\AppData\\Local\\Google\\Chrome\\User Data");
    }

    static {
        System.setProperty("webdriver.chrome.driver", ".\\chromedriver-win64\\chromedriver.exe");
    }

    static WebDriver driver = new ChromeDriver(options);
    static WebDriverWait wait = new WebDriverWait(driver, Duration.of(60, SECONDS));
    static ArrayList<String> solvedSlagalicaArray;
    static ArrayList<String> solvedMojBrojArray;

    static Actions actions = new Actions(driver);
    static String originalWindow;

    static JavascriptExecutor js = (JavascriptExecutor) driver;

    static String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
            + "var elementTop = arguments[0].getBoundingClientRect().top;"
            + "window.scrollBy(0, elementTop-(viewPortHeight/2));";


    public static void main(String[] args) {


        try {


            driver.get("https://fb.slagalica-online.com/application/#");
            originalWindow = driver.getWindowHandle();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                showMessageDialog(null, e);
            }

            /*if (driver.findElement(By.xpath("//button[contains(@class,'dialog-header__close')]")).isDisplayed()) {
                js.executeScript("arguments[0].click();",
                        driver.findElement(By.xpath("//button[contains(@class,'dialog-header__close')]")));
            }*/


            WebElement pokreniIgru = wait.until(elementToBeClickable(By.xpath
                    ("/html/body/div[2]/div[4]/div[7]/div/div/div/div/div/div[1]/div[2]/div[1]/div[1]/a/div[1]")));
            pokreniIgru.click();

            WebElement prihvatiIgru = wait.until(elementToBeClickable((By.xpath
                    ("/html/body/div[2]/div[4]/div[7]/div/div/div/div/div/div[2]/div/div/div[4]/button[1]"))));

            prihvatiIgru.click();

            gameSlagalica();
            gameSlagalica();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                showMessageDialog(null, e);
            }

            gameMojBroj();
            gameMojBroj();


        } catch (Exception e) {
            showMessageDialog(null, "Prati uputstva.");
        }

    }

    public static void solveMojBroj(String finalNumber, List<String> numbers) {

        try {


            driver.switchTo().newWindow(WindowType.TAB);
            driver.get("https://www.ludara.com/resenja-moj-broj/");

            WebElement finalNumberInput = wait.until(elementToBeClickable(By.id("target")));
            finalNumberInput.sendKeys(finalNumber);


            List<WebElement> inputBrojevi = driver.findElements(By.xpath("//input[contains(@id,'number')]"));


            for (int i = 0, j = 0; i < inputBrojevi.size(); i++, j++) {
                wait.until(elementToBeClickable(inputBrojevi.get(i)));

                inputBrojevi.get(i).sendKeys(numbers.get(j));

            }


            WebElement solveBtn = wait.until(elementToBeClickable(By.id("solve-btn")));

            js.executeScript(scrollElementIntoMiddle, solveBtn);
            //selenium click(); method sometimes thinks objects are behind another object and that the wrong object will receive the click
            js.executeScript("arguments[0].click()", solveBtn);


            WebElement output = wait.until(presenceOfElementLocated(By.xpath("//article[@id='output']/h3[1]")));
            String resenjeMojBroj = output.getText().replace(" ", "").replace("*", "x");

            solvedMojBrojArray = new ArrayList<>(Arrays.asList(
                    resenjeMojBroj.split(
                            "(\\d+|[=\\-\\+\\*\\:\\(\\)])")
            )
            );//goofy ahh regex,th


            driver.switchTo().window(originalWindow);

        } catch (Exception e) {
            showMessageDialog(null, e);
        }


    }

    public static void gameMojBroj() {

        try {

            wait.until(presenceOfElementLocated(By.xpath("//div[(@class='game-status__text')]")));

            wait.until(textToBePresentInElementLocated(By.xpath("//div[(@class='game-status__text')]"), "PRONAĐI NAJBLIŽE REŠENJE"));

            while (driver.findElement(By.xpath("//div[(@class='game-status__text')]")).getText().equalsIgnoreCase("PRONAĐI NAJBLIŽE REŠENJE")) {

                wait.until(textToBePresentInElementLocated(By.xpath("//div[(@class='game-status__text')]"), "PRONAĐI NAJBLIŽE REŠENJE"));


                WebElement targetNumberEle = driver.findElement(By.xpath("//div[(@class='my-number__progress__number')]"));
                List<WebElement> numberBtns = driver.findElements(By.xpath("//button[(@class='my-number__number-button game-button')]  " +
                        "| //button[(@class='my-number__number-button my-number__number-button--middle game-button')] | //button[(@class='my-number__number-button my-number__number-button--big game-button')] "));


                ArrayList<String> numberList = new ArrayList<>(numberBtns.size());
                for (WebElement ele : numberBtns) {
                    numberList.add(ele.getText());
                }
                String targetNumber = targetNumberEle.getText();

                solveMojBroj(targetNumber, numberList);


                for (String j : solvedMojBrojArray) {
                    List<WebElement> allElements = driver.findElements(By.xpath("//button[(@class='my-number__number-button game-button')]  " +
                            "| //button[(@class='my-number__number-button my-number__number-button--middle game-button')]  | //button[(@class='my-number__number-button my-number__number-button--big game-button')]" +
                            "| //button[(@class='my-number__number-button my-number__number-button--operation game-button-darkblue')]"));

                    Iterator<WebElement> iter = allElements.iterator();


                    while (iter.hasNext()) {
                        WebElement ele = iter.next();
                        String znak = ele.getText().replace(" ", "");

                        if (znak.equalsIgnoreCase(j)) {

                            ele.click();
                            iter.remove();
                            break;
                        }

                    }
                }
                WebElement submitBtn = wait.until(elementToBeClickable(By.xpath("//button[@class='my-number__submit_word_button']")));
                submitBtn.click();
                wait.until(textToBePresentInElementLocated(By.xpath("//div[(@class='game-status__text')]"), "SLEDEĆA IGRA USKORO POČINJE"));


            }
        } catch (Exception e) {
            showMessageDialog(null, e);
        }

    }


    public static void gameSlagalica() {


        try {
            wait.until(presenceOfElementLocated(By.xpath("//div[(@class='game-status__text')]")));

            wait.until(textToBePresentInElementLocated(By.xpath("//div[(@class='game-status__text')]"), "PRONAĐI NAJDUŽU REČ"));

            WebElement gameStatus = driver.findElement(By.xpath("//div[(@class='game-status__text')]"));

            js.executeScript(scrollElementIntoMiddle, driver.findElement(By.xpath("/html/body/div[2]/div[4]/div[7]/div/div/div/div/div/div[1]/div[3]/div/div[1]/div[2]/div[8]/button")));

            while (gameStatus.getText().equals("PRONAĐI NAJDUŽU REČ")) {

                List<WebElement> elements = driver.findElements(By.xpath("//button[contains(@class,'puzzle__letter game-button')]"));

                StringBuilder SvaSlova = new StringBuilder();

                for (WebElement i : elements) {

                    SvaSlova.append(i.getText());

                }

                solveSlagalica(String.valueOf(SvaSlova));


                for (String j : solvedSlagalicaArray) {


                    List<WebElement> slovaButtons = driver.findElements(By.xpath("//button[(@class='puzzle__letter game-button')]"));
                    Iterator<WebElement> iter = slovaButtons.iterator();
                    while (iter.hasNext()) {

                        WebElement slovoButton = iter.next();
                        String letter = slovoButton.getText();


                        if (letter.equalsIgnoreCase(j)) {
                            js.executeScript("arguments[0].click();", slovoButton);
                            System.out.println(slovoButton.getAttribute("innerText"));
                            System.out.println(slovoButton);
                            iter.remove();


                            break;

                        }
                    }
                }
                WebElement submitBtn = wait.until(elementToBeClickable(By.xpath
                        ("/html/body/div[2]/div[4]/div[7]/div/div/div/div/div/div[1]/div[3]/div/div[1]/div[2]/div[8]/button")));

                submitBtn.click();

                wait.until(textToBePresentInElement(gameStatus, "SLEDEĆA IGRA USKORO POČINJE"));


            }
        } catch (Exception e) {
            showMessageDialog(null, e);
        }
    }


    public static void solveSlagalica(String args) {
        //uzima slova iz slagalice i salje ih ludari,vraca nazad resenj
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://www.ludara.com/resenja-slagalica/");

        try {


            WebElement letterInput = wait.until(elementToBeClickable(By.id("letters")));
            letterInput.sendKeys(args);

            WebElement sendLetters = wait.until(elementToBeClickable(By.id("words-btn")));
            sendLetters.click();

            wait.until(visibilityOfElementLocated(By.xpath("/html/body/div[1]/article/div[2]/div[1]/div[3]/div/div/article/h3")));
            WebElement parentElement = driver.findElement(By.xpath
                    ("/html/body/div[1]/article/div[2]/div[1]/div[3]/div/div/article/h3"));
            String solvedLetters = parentElement.getText();
            System.out.println(solvedLetters);

            solvedSlagalicaArray = new ArrayList<>(Arrays.asList(solvedLetters.split("")));


            for (int i = 0, j = 1; j < solvedSlagalicaArray.size(); i++, j++) {
                String a = solvedSlagalicaArray.get(i);
                String b = solvedSlagalicaArray.get(j);

                if (a.equalsIgnoreCase("L") & b.equalsIgnoreCase("J")) {

                    solvedSlagalicaArray.set(i, "LJ");
                    solvedSlagalicaArray.remove(j);

                } else if (a.equalsIgnoreCase("N") & b.equalsIgnoreCase("J")) {

                    solvedSlagalicaArray.set(i, "NJ");
                    solvedSlagalicaArray.remove(j);

                }

            }


            driver.switchTo().window(originalWindow);
        } catch (Exception e) {
            showMessageDialog(null, e);

        }
    }
}
