import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.List;

public class GooglePage{

    public ExpandedChromeDriver driver;
    private WebElement Search_field;

    public GooglePage(ExpandedChromeDriver new_driver){
        driver = new_driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(id = "pnnext")
    public WebElement nextPageButton;

    @FindBy(className = "cur")
    private WebElement number;

    @FindBy(className = "g")
    private List<WebElement> paragraphs;

    @FindBy(css= "#fprs > a.spell_orig")
    private WebElement spell;




    boolean isExisting(WebElement elem) {
        try {
            elem.isDisplayed();
            return true;
        } catch (NoSuchElementException ex) {return false; }
    }

    void inputRequest(String request){
        Search_field  = driver.findElement(By.xpath("//input[@name=\"q\"]"));
        Search_field.click();
        Search_field.sendKeys(request);
        Search_field.sendKeys(Keys.ENTER);
    }

    String getNumberOfPage(){
        return (isExisting(number))? number.getText() : "1";
    }

    WebElement getSearchField() {return Search_field;}
    WebElement getSpell() {return spell;}

    boolean hasNextPage(){
        return isExisting(nextPageButton);
    }

    public GooglePage nextPage(){
        if (isExisting(nextPageButton)) nextPageButton.click();
        return new GooglePage(driver);
    }

    boolean isInPage(String company){
        for(WebElement iter: paragraphs)
            if (iter
                    .getText().toUpperCase()
                    .contains(
                            company.toUpperCase()
                    )
            ) return true;
        return false;
    }

    File getScreenshot() throws Exception{
        return driver.getFullScreenshotAs(OutputType.FILE);
    }
}