package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.RegistrationPage;
import user.UserGenerator;
import data.User;
import utils.BrowserFactory;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class RegistrationTest {
    private WebDriver driver;
    private RegistrationPage registrationPage;

    @Before
    public void setUp() {

        driver = BrowserFactory.getDriver(); // Браузер берется из config.properties

        registrationPage = new RegistrationPage(driver);
        registrationPage.open();
    }

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Тест проверяет успешную регистрацию пользователя с валидными данными")
    public void successfulRegistrationTest() {
        User user = UserGenerator.getValidUser();
        registrationPage.register(user.getName(), user.getEmail(), user.getPassword());

        // перенаправление на страницу входа после успешной регистрации и проверка появления кнопки
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/login"));
    }

    @Test
    @DisplayName("Ошибка при регистрации с коротким паролем")
    @Description("Тест проверяет отображение ошибки при регистрации с паролем короче 6 символов")
    public void registrationWithShortPasswordTest() {
        User user = UserGenerator.getUserWithShortPassword();
        registrationPage.register(user.getName(), user.getEmail(), user.getPassword());

        assertTrue("Ошибка о коротком пароле не отображается",
                registrationPage.isPasswordErrorDisplayed());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}