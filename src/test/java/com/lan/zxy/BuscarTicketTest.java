package com.lan.zxy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;

/******************** Historia de usuario ***********************/
/* Historia: Como usuario quiero poder visualizar la pantalla de registro 
 * 
 * ===============================================================================
 * Prueba  de Aceptación: Verificar que cuando el usuario presione el boton de registro ,
 *  los campos de input de 
 * (Nombre, apellido, correo) se conviertan de color rojo, (cambia de clase)
 * 
 * 1. Ingresar a la pagina https://teleticket.com.pe/ 
 * 2. Dirigirise al botón de "Registrarse"
 * 3. Si aparece el panel de "Cookies" cerrarlo
 * 4. Presionar el botón de registrarse
 * 
 * Resultados Esperados: Los inputs de  (Nombre, Apellido, Correo) aparecen en Rojo
 * 
 * ================================================================================
 * 
 * Prueba de Aceptación: Verificar que puedo visualizar los tags de los campos de input
 *  en la pantalla de registro de usuario en Ingles
 * 
 * 1. Ingresar a la pagina https://teleticket.com.pe/ 
 * 2. Dirigirise al botón de "Registrarse"
 * 3. Si aparece el panel de "Cookies" cerrarlo
 * 4. Presionar el botón de English
 * 
 * Resultados Esperados: Todos los titulos de los campos de entrada del formulario de 
 * registro deben estar en Ingles
 * 
 * 
 */

public class BuscarTicketTest {
    private WebDriver driver;

    @BeforeTest
    public void setDriver() throws Exception {
        // this is the path where web driver was unzip
        String path = "C:\\Users\\Alan\\Downloads\\chrome-win64\\chrome-win64";

        System.setProperty("webdriver.chrome.driver", path);

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterTest
    public void closeDriver() throws Exception {
        driver.quit();
    }

    // TEST WILL FAIL BUT FAILED IS EXPECTED
    @Test
    public void verificarElIdiomaInglesDeLosTags(){
        fromHomePageGoToJoin();
        canCloseCookiesButton();

        // click button english
        WebElement englishButton = driver
                .findElement(By.xpath("//*[@id=\"btn-lang-en\"]"));
        myWait(3);
        englishButton.click();
        myWait(3);

        // check the language
        List<String> listOfTags = List.of(
            "//*[@id=\"body-content\"]/main/div/div/div/div[3]/p",
            "//*[@id=\"form-register\"]/div[1]/label",
            "//*[@id=\"form-register\"]/div[2]/label",
            "//*[@id=\"form-register\"]/div[3]/label",
            "//*[@id=\"form-register\"]/div[4]/label",
            "//*[@id=\"form-register\"]/div[5]/label", // phone
            "//*[@id=\"form-register\"]/div[6]/label", // country
            "//*[@id=\"IdentificationDocumentType\"]/label", // dni
            "//*[@id=\"form-register\"]/div[10]/label", // gender
            "//*[@id=\"form-register\"]/div[11]/label", // birthday         
            "//*[@id=\"form-register\"]/div[12]/div/label" // password
            
        );
        List<String> listOfResults = List.of(
            "Enter your details and save time on your next purchase",
            "Names",
            "Last Names",
            "Email",
            "Type of Phone",
            "Phone",
            "Country",
            "Identity Document Type",
            "Gender",
            "Birthdate",
            "Password"
        );

        /* Verificacion de resultados */
        for(int x = 0; x < listOfResults.size() ; x++){
            WebElement currentElement = driver.findElement(By.xpath(listOfTags.get(x)));
            String currentString = currentElement.getText();
            System.out.print("current: "+currentString+" <-> expected: "+listOfResults.get(x));
            Assert.assertEquals(listOfResults.get(x), currentString);

        }

        // README: this test will  fail cause the web page doesnt use english language 

        
    }

    private void canCloseCookiesButton() {
        // after enter the site of registration there is a box or cookies
        WebElement cookiesElement = driver.findElement(By.xpath("/html/body/div[4]/div/div[2]/button"));
        if (cookiesElement.isDisplayed()) {
            cookiesElement.click();
            myWait(5);
        }

    }

    @Test
    public void verificarMensajesDeRegistro() {
        /* preparación del test */
        fromHomePageGoToJoin();
        canCloseCookiesButton();
        scrollDown();

        // 2. Press button join
        WebElement joinButton = driver
                .findElement(By.xpath("/html/body/div[3]/main/div/div/div/div[3]/div/div/form/div[17]/div[2]/button"));
        myWait(3);
        joinButton.click();
        myWait(3);

        /****** Verificación de los resultados esperados */

        // 3. Verificar los resultados
        List<String> inputXpaths = List.of("//*[@id=\"Name\"]", "//*[@id=\"LastName\"]", "//*[@id=\"Email\"]");

        for (String currentInputXpath : inputXpaths) {
            WebElement inputNames = driver.findElement(By.xpath(currentInputXpath));
            String className = inputNames.getAttribute("class");
            Assert.assertEquals("form-control alert-danger", className);
        }

    }

    private void myWait(Integer seconds) {

        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void fromHomePageGoToJoin() {
        /* Enter webpage : https://teleticket.com.pe/ */
        String teleticket = "https://teleticket.com.pe/";
        driver.get(teleticket);

        /* Logic of test */
        WebElement joinLink = driver.findElement(By.xpath("/html/body/header/div[2]/div/div[4]/div/div[2]/a"));
        String linkText = joinLink.getText();
        System.out.println("Text del link: " + linkText);
        // wait 3 sec for rendering
        myWait(5);
        joinLink.click();
    }

    private void scrollDown() {
        // >>>>>>>>>>>>>>> SCROLL DOWN: >>>>>>>>>>>>>
        // This part of the code is necesary cause the button is hidden until the page
        // goes totally scrolled down
        // Create a JavascriptExecutor from the WebDriver
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        // Scroll down to the bottom of the page
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        // <<<<<<<<<<<<<<< SCROLL DOWN <<<<<<<<<<<
    }
}
