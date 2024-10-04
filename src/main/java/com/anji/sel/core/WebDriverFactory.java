package com.anji.sel.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverFactory {

  public static WebDriver getDriver(String browser) {
    return getDriverInstance(browser);
  }

  public static WebDriver getDriverInstance(String browser) {
      return switch (browser.toLowerCase()) {
          case "chrome" -> new ChromeDriver();
          case "firefox" -> new FirefoxDriver();
          case "ie" -> new InternetExplorerDriver();
          case "safari" -> new SafariDriver();
          default -> throw new IllegalArgumentException("Unexpected value: " + browser);
      };
  }
}
