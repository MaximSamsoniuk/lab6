import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RozetkaPageTest {
    private final static String startUrl = "https://rozetka.com.ua/hudojestvennaya-literatura/c4326593/";
    private final static int price = 180;
    private static WebDriver driver;



    @Test

    public void Test1() throws Exception{
        System.setProperty("webdriver.chrome.driver", "Selenium WebDriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(startUrl);
        RozetkaPage page = new RozetkaPage(driver);
        page.set_filterMin(price);
        page.set_filterSubmit();
        Assert.assertTrue(page.get_filterMin() == price);
        boolean result = true;
        List<Integer> prices = page.getPrices();
        for(Integer iter: prices){
            if(iter < price) result = false;
        }
        System.out.println(prices.toString());
        Assert.assertTrue(result);
    }

    @After
    public void end(){

        driver.quit();
    }
}