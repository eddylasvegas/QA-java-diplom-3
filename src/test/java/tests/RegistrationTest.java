package tests;

import api.AuthApi;
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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RegistrationTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String REGISTER_URL = BASE_URL + "register";
    private static final String LOGIN_URL = BASE_URL + "login";

    private WebDriver driver;
    private RegistrationPage registrationPage;
    private User user;
    private String accessToken;

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
        // Генерируем данные
        User user = UserGenerator.getValidUser();
        // Регистрируем через UI
        registrationPage.register(user.getName(), user.getEmail(), user.getPassword());

        registrationPage.waitForRedirectToLogin();  // Используем метод Page Object для проверки перехода на страницу

        // Получаем токен для удаления через API
        accessToken = AuthApi.loginUser(user)
                .then()
                .extract()
                .path("accessToken");
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
        try {
            if (accessToken != null) {
                AuthApi.deleteUser(accessToken); // Игнорируем возможные ошибки
            }
        } catch (Exception e) {
            System.out.println("Ошибка очистки: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}