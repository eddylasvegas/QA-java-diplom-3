package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BrowserFactory {
    private static final Properties properties = new Properties();
    private static final int IMPLICIT_WAIT_SECONDS = 5;

    static {
        try (InputStream input = BrowserFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static WebDriver getDriver() {
        String browser = properties.getProperty("browser", "chrome").toLowerCase();
        WebDriver driver;

        switch (browser) {
            case "yandex":
                driver = startYandexBrowser();
                break;
            case "chrome":
            default:
                driver = startChrome();
        }

        // Устанавливаем Implicit Wait для всех создаваемых драйверов
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        return driver;
    }

    private static WebDriver startYandexBrowser() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\YandexDriver\\yandexdriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\Eldar\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    private static WebDriver startChrome() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }
}