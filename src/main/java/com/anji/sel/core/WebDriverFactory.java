package com.anji.sel.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverFactory {

  public static WebDriver getDriver(String browser) {
    return getMeTheDriver(browser);
  }

  private static WebDriver getMeTheDriver(String browser) {

    return switch (browser.toLowerCase()) {
      case "chrome" -> {
        yield new ChromeDriver();
      }

      case "firefox" -> {
        yield new FirefoxDriver();
      }

      case "ie" -> {
        yield new InternetExplorerDriver();
      }

      case "safari" -> {
        yield new SafariDriver();
      }

      default -> throw new IllegalArgumentException("Unexpected value: " + browser);
    };
  }
}
