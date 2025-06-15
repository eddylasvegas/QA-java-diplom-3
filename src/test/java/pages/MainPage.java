package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String ACTIVE_TAB_CLASS = "current";

    private final WebDriver driver;

    // Локаторы
    private final By constructorTitle = By.xpath("//h1[text()='Соберите бургер']");
    private final By bunsTab = By.cssSelector(".tab_tab__1SPyG:nth-child(1)");
    private final By saucesTab = By.cssSelector(".tab_tab__1SPyG:nth-child(2)");
    private final By fillingsTab = By.cssSelector(".tab_tab__1SPyG:nth-child(3)");
    private final By loginAccountButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By personalAccountButton = By.xpath(".//p[text()='Личный Кабинет']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(BASE_URL);
        waitForPageLoad();
    }

    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(constructorTitle));
    }

    public void clickBunsSection() {
        driver.findElement(bunsTab).click();
        waitBunsLoad();
    }

    public void clickSaucesSection() {
        driver.findElement(saucesTab).click();
        waitSaucesLoad();
    }

    public void clickFillingsSection() {
        driver.findElement(fillingsTab).click();
        waitFillingsLoad();
    }

    public void waitBunsLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.attributeContains(bunsTab, "class", ACTIVE_TAB_CLASS));
    }

    public void waitSaucesLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.attributeContains(saucesTab, "class", ACTIVE_TAB_CLASS));
    }

    public void waitFillingsLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.attributeContains(fillingsTab, "class", ACTIVE_TAB_CLASS));
    }

    // Методы проверки состояния
    public boolean isBunsActive() {
        return driver.findElement(bunsTab).getAttribute("class").contains(ACTIVE_TAB_CLASS);
    }

    public boolean isSaucesActive() {
        return driver.findElement(saucesTab).getAttribute("class").contains(ACTIVE_TAB_CLASS);
    }

    public boolean isFillingsActive() {
        return driver.findElement(fillingsTab).getAttribute("class").contains(ACTIVE_TAB_CLASS);
    }

    // метод проверки начального состояния вкладок конструктора бургеров, проверяет,
    // что раздел Булки активен по умолчанию, для switchToBunsSectionTest()
    public boolean isBunsActiveByDefault() {
        return isBunsActive() &&
                !isSaucesActive() &&
                !isFillingsActive();
    }

    public void clickLoginAccountButton() {
        driver.findElement(loginAccountButton).click();
    }

    public void clickPersonalAccountButton() {
        driver.findElement(personalAccountButton).click();
    }
}