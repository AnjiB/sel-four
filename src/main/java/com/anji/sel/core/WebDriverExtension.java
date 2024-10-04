package com.anji.sel.core;

import io.qameta.allure.Attachment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class WebDriverExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private static final ThreadLocal<WebDriver> THREAD_LOCAL_WEB_DRIVER = new ThreadLocal<>();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Optional<SeleniumWebDriver> annotation = getUseWebDriverAnnotation(context);
        if (annotation.isPresent()) {
            String browser = annotation.get().browser();
            WebDriver driver = WebDriverFactory.getDriver(browser);
            THREAD_LOCAL_WEB_DRIVER.set(driver);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        WebDriver driver = THREAD_LOCAL_WEB_DRIVER.get();
        if (driver != null) {
            // Take a screenshot if the test failed
            if (context.getExecutionException().isPresent()) {
                takeScreenshot(driver, context.getDisplayName());
            }
            driver.quit(); // Quit the driver and clean up
            THREAD_LOCAL_WEB_DRIVER.remove(); // Remove from ThreadLocal
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == WebDriver.class; // Check if parameter is WebDriver
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return THREAD_LOCAL_WEB_DRIVER.get(); // Provide the WebDriver instance
    }

    // Screenshot capture and Allure integration
    private synchronized void takeScreenshot(WebDriver driver, String testName) throws IOException {
        Path screenshotFolder = Paths.get(System.getProperty("user.dir")).resolve("./screenshots");
        if (Files.notExists(screenshotFolder)) {
            Files.createDirectories(screenshotFolder);
        }

        if (driver instanceof TakesScreenshot) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotFolder.toFile(), testName + ".png");
            FileUtils.copyFile(screenshot, destination);
            attachScreenshotToAllure(screenshot, testName); // Attach screenshot to Allure report
        }
    }

    @Attachment(value = "{testName} Screenshot", type = "image/png")
    public byte[] attachScreenshotToAllure(File screenshot, String testName) throws IOException {
        try (FileInputStream fis = new FileInputStream(screenshot)) {
            return fis.readAllBytes();
        }
    }

    // Utility method to extract the @SeleniumWebDriver annotation
    private Optional<SeleniumWebDriver> getUseWebDriverAnnotation(ExtensionContext context) {
        return context.getTestMethod()
                .map(method -> method.getAnnotation(SeleniumWebDriver.class))
                .or(() -> context.getTestClass().map(cls -> cls.getAnnotation(SeleniumWebDriver.class)));
    }
}
