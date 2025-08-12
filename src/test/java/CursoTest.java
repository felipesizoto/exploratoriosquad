import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CursoTest {

    @Test
    public void testeFluxoCompleto() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize();
            driver.get("https://iterasys.learnworlds.com/home");

            // Clicar em Sign in para abrir modal de login
            WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Sign in')]")));
            signInLink.click();

            // Confirmar modal abriu (texto "Faça login ou cadastre-se para começar a aprender")
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Faça login ou cadastre-se para começar a aprender')]")));

            // Preencher email no modal correto
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='email' and contains(@class, 'sign-input')]")));
            emailInput.clear();
            emailInput.sendKeys("fsizoto@gmail.com");  // coloque seu email aqui

            // Preencher senha no modal correto
            WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password' and contains(@class, 'sign-input')]")));
            passwordInput.clear();
            passwordInput.sendKeys("Ripley88#");  // coloque sua senha aqui

            // Clicar no botão de login
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("submitLogin")));
            loginBtn.click();

            // Esperar um pouco para a página estabilizar
            Thread.sleep(3000);  // 3 segundos, pode ajustar

            // Esperar elemento Courses aparecer na página
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Courses')]")));

            // Recapturar e clicar no botão Courses para evitar stale element
            WebElement coursesBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Courses')]")));
            coursesBtn.click();

            // Esperar o campo de pesquisa aparecer
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[placeholder='Pesquisar cursos']")));
            searchInput.clear();
            searchInput.sendKeys("formação de product owner");            

            // Após digitar o curso (mantendo sua digitação lenta)

            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -190);");  // sobe a tela um pouco

            Thread.sleep(10000);

            // Agora buscar o link do curso com clique via JS para garantir
            WebElement cursoLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@class,'lw-course-card--stretched-link') and normalize-space(text())='Formação de Product Owner']")
            ));

            // Scroll para o elemento ficar visível
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cursoLink);
            Thread.sleep(500); // Pequena pausa para estabilizar a tela

            // Clique via JavaScript para evitar erros
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cursoLink);


            // Clicar na primeira aula (Apresentação do Curso)
            WebElement firstLesson = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'lw-course-contents-unit')]//div[contains(text(),'Apresentação do Curso')]")));
            firstLesson.click();

            try {
            WebElement playButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.playpause.playButton")));
                playButton.click();
            } catch (TimeoutException e) {
                // Botão play não apareceu, pode ser que o vídeo já esteja tocando, seguir normalmente
                System.out.println("Botão play não encontrado - talvez o vídeo já esteja rodando.");
            }


        } finally {
            // Fechar o navegador no final do teste
            driver.quit();
        }
    }
}
