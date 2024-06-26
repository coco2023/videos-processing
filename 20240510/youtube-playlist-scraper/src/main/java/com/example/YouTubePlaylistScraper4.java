package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;

public class YouTubePlaylistScraper4 {
    public static void main(String[] args) throws InterruptedException {

          System.setProperty("webdriver.chrome.driver", "D:\\Application\\WorkApp\\ChromeDriver\\chromedriver-win64\\chromedriver.exe");

          ChromeOptions options = new ChromeOptions();
          options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200");
          WebDriver driver = new ChromeDriver(options);
  
          try {
                    driver.get("https://www.youtube.com/playlist?list=PL_oohi_O51Z_lORk8SCG_4x1smii5ky7f");
                    // driver.get("https://www.youtube.com/playlist?list=PL159CD41EB36CFE86");
                    WebDriverWait wait = new WebDriverWait(driver, 50);
                    JavascriptExecutor js = (JavascriptExecutor) driver;

                    // Implement more effective scrolling to ensure all elements are loaded
                    int oldSize = 0;
                    List<WebElement> elements;
                    do {
                              js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                              // Wait for elements to become visible after scroll
                              wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a#video-title")));
                              elements = driver.findElements(By.cssSelector("a#video-title"));
                              if (elements.size() == oldSize) {
                                        System.out.println("no further elements");
                                        break;
                              }
                              oldSize = elements.size();
                              Thread.sleep(2000);  // Short pause to allow DOM to update
                    } while (true);

                    // Process the links as before
                    HashMap<String, String> videoLinks = new HashMap<>();
                    for (WebElement element : elements) {
                              String videoUrl = element.getAttribute("href");
                              String videoTitle = element.getAttribute("title");
                              videoLinks.put(videoUrl, videoTitle);
                    }
    
                    // Output results
                    videoLinks.forEach((url, title) -> System.out.println("URL: " + url + ", Title: " + title));
                    // Printing the total number of unique URLs collected
                    System.out.println("Total number of unique URLs collected: " + videoLinks.size());

                    // Serialize HashMap to JSON file
                    ObjectMapper mapper = new ObjectMapper();
                    File directory = new File("outputs");
                    if (!directory.exists()) {
                    directory.mkdirs();
                    }
                    mapper.writeValue(new File(directory, "videoLinks4.json"), videoLinks);
                    System.out.println("JSON file has been written successfully");

        } catch (Exception e) {
                    e.printStackTrace();
        } finally {
                    driver.quit();
        }
    }
}
