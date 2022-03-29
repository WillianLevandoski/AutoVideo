package com.AutoVideo;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.units.qual.s;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;


public class AzureReader {
	
	private WebDriver driver = null;
	private String url = "https://azure.microsoft.com/pt-br/services/cognitive-services/text-to-speech/"; 
	
	AzureReader() {
		if(driver == null) {
			driver = new Navegador();
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					}
	}

	public String gravarVoz(String texto, String nome) {
		driver.manage().window().maximize();
		esperar(1);

		WebElement campotxt = driver.findElement(By.cssSelector("textarea[id=ttstext]"));
		if(campotxt!= null && campotxt.isDisplayed() && campotxt.isEnabled() )
			campotxt.clear();
		esperar(2);
		
		WebElement ssml = driver.findElement(By.xpath("//*[@id=\"ssmlInputTab\"]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(ssml).click().perform();
		esperar(5);
		ssml.click();
		
		
		WebElement campoTexto = driver.findElement(By.cssSelector("textarea[id=ttsssml]"));
		campoTexto.clear();
		esperar(1);
		
		StringSelection selection = new StringSelection(texto);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		clipboard.setContents(selection, selection);
		campoTexto.click();
		campoTexto.sendKeys(Keys.CONTROL+ "v");
		esperar(3);	
		try {
			GravadorSom gravador = new GravadorSom();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						gravador.start();
					} catch (Exception e) {
						System.out.println("Erro ao start gravação voz. "+e.getMessage());
					}
				}
			}).start();
			esperar(1);
			WebElement botaoReproduzir = driver.findElement(By.cssSelector("button[id=playbtn]"));
			new Actions(driver).moveToElement(botaoReproduzir).perform();
			botaoReproduzir.click();
			verificarTerminoLeitura(gravador);
			String caminho = "/home/oem/Vídeos/Audios/"+nome+".wav";
			gravador.save(new File(caminho));
			driver.close();
			return caminho;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void esperar(int tempo) {
		try{
			Thread.sleep(tempo*1000);
		}catch(InterruptedException ie){
		}   
		
	}

	private void verificarTerminoLeitura(GravadorSom gravador) {

		try{
			Thread.sleep(2000);
		}catch(InterruptedException ie){
		}    

		WebElement botaoReproduzir = null;
		WebElement botaoInterroper = driver.findElement(By.cssSelector("button[id=stopbtn]"));
		if(botaoInterroper!= null) {
			botaoReproduzir = null;
			botaoReproduzir = driver.findElement(By.cssSelector("li[id=playli]"));
			if(botaoReproduzir != null && !botaoReproduzir.isDisplayed()) {
				verificarTerminoLeitura(gravador);
			}else {
				try {
					gravador.stop();
				} catch (IOException e) {
					System.out.println("Erro ao stop gravação voz. "+e.getMessage());
				}
			}	
		}
		
		
	}

}
