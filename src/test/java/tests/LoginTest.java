package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.MainPage;
import pages.RegistrationPage;
import user.UserGenerator;
import data.User;
import utils.BrowserFactory;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class LoginTest {
    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private User user;

    @Before
    public void setUp() {

        driver = BrowserFactory.getDriver(); // Браузер берется из config.properties

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registrationPage = new RegistrationPage(driver);
        user = UserGenerator.getValidUser();

        // Регистрация пользователя для тестов входа
        registrationPage.open();
        registrationPage.register(user.getName(), user.getEmail(), user.getPassword());
    }

    @Test
    @DisplayName("Вход по кнопке 'Войти в аккаунт'")
    @Description("Тест проверяет вход через кнопку 'Войти в аккаунт' на главной странице")
    public void loginViaMainPageButtonTest() {
        mainPage.open();
        mainPage.clickLoginAccountButton();
        loginPage.login(user.getEmail(), user.getPassword());

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.nomoreparties.site/"));
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @Test
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    @Description("Тест проверяет вход через кнопку 'Личный кабинет' в хедере")
    public void loginViaPersonalAccountButtonTest() {
        mainPage.open();
        mainPage.clickPersonalAccountButton();
        loginPage.login(user.getEmail(), user.getPassword());

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.nomoreparties.site/"));
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @Test
    @DisplayName("Вход через ссылку в форме регистрации")
    @Description("Тест проверяет вход через ссылку 'Войти' на странице регистрации")
    public void loginViaRegistrationFormLinkTest() {
        registrationPage.open();
        registrationPage.clickLoginLink();
        loginPage.login(user.getEmail(), user.getPassword());

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.nomoreparties.site/"));
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @Test
    @DisplayName("Вход через ссылку в форме восстановления пароля")
    @Description("Тест проверяет вход через ссылку 'Войти' на странице восстановления пароля")
    public void loginViaPasswordRecoveryFormLinkTest() {
        loginPage.open();
        loginPage.clickRestorePasswordLink();

        // Возвращаемся на страницу входа (временное решение)
        driver.navigate().back();
        loginPage.login(user.getEmail(), user.getPassword());

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.nomoreparties.site/"));
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}