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
import data.User;
import utils.BrowserFactory;
import user.UserGenerator;
import api.AuthApi;
import java.time.Duration;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LoginTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String REGISTER_URL = BASE_URL + "register";
    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {

        driver = BrowserFactory.getDriver(); // Браузер берется из config.properties

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);

        user = UserGenerator.getValidUser();

        var response = AuthApi.registerUser(user); // Регистрация через API вместо UI
        accessToken = response.then().extract().path("accessToken");

    }

    @Test
    @DisplayName("Вход по кнопке 'Войти в аккаунт'")
    @Description("Тест проверяет вход через кнопку 'Войти в аккаунт' на главной странице")
    public void loginViaMainPageButtonTest() {
        driver.get(REGISTER_URL);
        loginPage.clickLoginLink();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase(); // Используем метод Page Object
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @Test
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    @Description("Тест проверяет вход через кнопку 'Личный кабинет' в хедере")
    public void loginViaPersonalAccountButtonTest() {
        mainPage.open();
        mainPage.clickPersonalAccountButton();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase(); // Используем метод Page Object
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @Test
    @DisplayName("Вход через ссылку в форме регистрации")
    @Description("Тест проверяет вход через ссылку 'Войти' на странице регистрации")
    public void loginViaRegistrationFormLinkTest() {
        driver.get(REGISTER_URL);
        loginPage.waitForUrlContainsRegister(); // Используем метод Page Object
        loginPage.clickLoginLink();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase(); // Используем метод Page Object
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @Test
    @DisplayName("Вход через ссылку в форме восстановления пароля")
    @Description("Тест проверяет вход через ссылку 'Войти' на странице восстановления пароля")
    public void loginViaPasswordRecoveryFormLinkTest() {
        loginPage.open();
        loginPage.clickRestorePasswordLink();

        // Возвращаемся на страницу входа
        driver.navigate().back();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase(); // Используем метод Page Object
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActiveByDefault());
    }

    @After
    public void tearDown() {
        // Удаление пользователя через API
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