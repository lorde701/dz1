import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.bind.Element;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class InsuranceTest {
    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "drv/geckodriver.exe");

        driver = new ChromeDriver();
        baseUrl = "http://www.sberbank.ru/ru/person/";

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void test() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//span[contains(text(),'Застраховать себя')]")).click();

        WebElement dynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@class,'kit-link kit-link_color_black alt-menu-list__link alt-menu-list__link_level_1') and text() = 'Страхование путешественников']")));
        dynamicElement.click();

        assertEquals("Страхование путешественников", driver.findElement(By.xpath("//H1[text()='Страхование путешественников']"))
                .getText());
        driver.findElement(By.xpath("//SPAN[text()='Оформить онлайн']")).click();

        Set<String> oldWindowsSet = driver.getWindowHandles();
        driver.findElement(By.xpath("//IMG[@src='/portalserver/content/atom/contentRepository/content/person/travel/banner-zashita-traveler.jpg?id=f6c836e1-5c5c-4367-b0d0-bbfb96be9c53']"))
                .click();
        String newWindowHandle = (new WebDriverWait(driver, 10))
                .until(new ExpectedCondition<String>() {
                           public String apply(WebDriver driver) {
                               Set<String> newWindowsSet = driver.getWindowHandles();
                               newWindowsSet.removeAll(oldWindowsSet);
                               return newWindowsSet.size() > 0 ?
                                       newWindowsSet.iterator().next() : null;
                           }
                       }
                );

        driver.switchTo().window(newWindowHandle);
        WebElement webElement = driver.findElement(By.xpath("//DIV[text()='Необходимый минимум для оплаты медицинской помощи за границей']"));
        webElement.click();
        driver.findElement(By.xpath("//SPAN[text()='Оформить']")).click();



        fillField(By.name("insured0_surname"), "Petrov");
        fillField(By.name("insured0_name"), "Petr");
        fillField(By.name("insured0_birthDate"), "12102016");
        fillField(By.name("surname"), "Иванов");
        fillField(By.name("name"), "Иван");
        fillField(By.name("middlename"), "Иванович");
        driver.findElement(By.name("birthDate")).click();
        fillField((By.name("birthDate")), "01011985");
//        driver.findElement(By.name("birthDate")).clear();
//        driver.findElement(By.name("birthDate")).sendKeys("01011985");
        fillField(By.name("passport_series"), "4444");
        fillField(By.name("passport_number"), "555555");
        fillField(By.name("issueDate"), "07041995");
        fillField(By.name("issuePlace"), "fcvghbjnkm");


        assertEquals("Petrov", driver.findElement(By.name("insured0_surname")).getAttribute("value"));
        assertEquals("Petr", driver.findElement(By.name("insured0_name")).getAttribute("value"));
        System.out.println("Дата:    " + driver.findElement(By.name("insured0_birthDate")).getAttribute("value"));
        assertEquals("12.10.2016", driver.findElement(By.name("insured0_birthDate")).getAttribute("value"));

        assertEquals("Иванов", driver.findElement(By.name("surname")).getAttribute("value"));
        assertEquals("Иван", driver.findElement(By.name("name")).getAttribute("value"));
        assertEquals("Иванович", driver.findElement(By.name("middlename")).getAttribute("value"));
        System.out.println("data2:     " + driver.findElement(By.name("birthDate")).getAttribute("value"));
        assertEquals("01.01.1985", driver.findElement(By.name("birthDate")).getAttribute("value"));

        assertEquals("4444", driver.findElement(By.name("passport_series")).getAttribute("value"));
        assertEquals("555555", driver.findElement(By.name("passport_number")).getAttribute("value"));
        assertEquals("07.04.1995", driver.findElement(By.name("issueDate")).getAttribute("value"));
        assertEquals("fcvghbjnkm", driver.findElement(By.name("issuePlace")).getAttribute("value"));



        //assertEquals("", driver.findElement(By.name("Comment")).getAttribute("value"));
        driver.findElement(By.xpath("//SPAN[@ng-click='save()']")).click();
        assertEquals("Заполнены не все обязательные поля", driver.findElement(By.xpath("//div[text()='Заполнены не все обязательные поля']")).getText());
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void fillField(By locator, String value){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

}
