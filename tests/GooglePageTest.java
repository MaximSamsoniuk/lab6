import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static junit.framework.TestCase.assertTrue;



@RunWith(Parameterized.class)
public class GooglePageTest {
    private final static String start_url = "https://www.google.com";
    private final static String directory = "..";
    private static ExpandedChromeDriver driver;
    private static FileWriter output;
    private boolean isFound;
    private String company;
    private String request;
    private  GooglePage page;

    public GooglePageTest(String file, String com_, String req_, boolean bool) throws IOException {
        company = com_;
        request = req_;
        isFound = bool;
        output = new FileWriter(directory + file, true);
    }

    private void makeScreenshot(File file, String directory, String name) throws  IOException{
        FileUtils.copyFile(
                file,
                new File(String.format("%s%s.png", directory, name))
        );
    }

    private String getScreenshotsName(String number){
        return String.format("(Request_%s)company_%s-page#%s",
                request,
                company,
                number
        );
    }
    private boolean print(GooglePage page) throws Exception {
        String str = getScreenshotsName(page.getNumberOfPage());
        output.write(str + "\n");
        makeScreenshot(page.getScreenshot(), directory, str);
        return true;
    }

    @BeforeClass
    public static void begin() throws Exception {
        System.setProperty("webdriver.chrome.driver", "Selenium WebDriver/chromedriver.exe");
        driver = new ExpandedChromeDriver();
    }

    @Before
    public void before(){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @Test
    public void Test() throws Exception {
        List<File> screenshots = new ArrayList<File>();
        driver.get(start_url);
        page = new GooglePage(driver);
        page.inputRequest(request);
//        while (!page.isExisting(page.getSearchField())){Thread.sleep(100);}
        if(page.isExisting(page.getSpell())) page.getSpell().click();
        boolean result = !isFound;
        if(page.isInPage(this.company)) {
            result = isFound && print(page);
        }
        screenshots.add(page.getScreenshot());
        if (!result || !isFound)
            while (page.hasNextPage()) {
//                page = page.nextPage();
                if (page.isExisting(page.nextPageButton)){
                    page.nextPageButton.click();
                    page = new GooglePage(driver);


                };
                screenshots.add(page.getScreenshot());
                if(page.isInPage(this.company)){
                    result = isFound && print(page);
                }
            }
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        if (!isFound && result) {
            for(int i = 0; i < screenshots.size(); i++)
                makeScreenshot(
                        screenshots.get(i),
                        directory,
                        getScreenshotsName(String.valueOf(i + 1)));
            String str = "(Request: " + request + ") " + company + " was not found on any page!\n";
            output.write(str);
        }
        assertTrue(result);
        output.close();

    }

    @AfterClass
    public static void end(){
        driver.quit();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        String request1 = "мороженое купить";
        String request2 = "sheldon cooper smelly pooper";

        String company1_1 = "ласунка";
        String company1_2 = "YouTube";
        String company1_3 = "funcan";
        String outputFile1 = "result.txt";

        Object[][] data = new Object[][]{
                {outputFile1, company1_1, request1, true},
                {outputFile1, company1_2, request2, true},
                {outputFile1, company1_3, request2, false}
        };
        return Arrays.asList(data);
    }
}