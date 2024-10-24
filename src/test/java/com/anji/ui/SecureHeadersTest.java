package com.anji.ui;

import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v128.network.Network;

import com.anji.sel.core.SeleniumWebDriver;

@SeleniumWebDriver
public class SecureHeadersTest {
	
	private WebDriver driver;
	
	@BeforeEach
	void setup(WebDriver driver) {
		this.driver = driver;
	}
	
	
	@Test
	void networkTest() {
		DevTools devTools = ((ChromeDriver) driver).getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

		SoftAssertions softAssertions = new SoftAssertions();
		devTools.addListener(Network.responseReceived(), response -> {   
			Map<String, Object> headers = response.getResponse().getHeaders();
			softAssertions.assertThat(headers.containsKey("Strict-Transport-Security")).isTrue();
			softAssertions.assertThat(headers.containsKey("Content-Security-Policy")).isTrue();
			softAssertions.assertThat(headers.containsKey("X-XSS-Protection")).isTrue();
			softAssertions.assertThat(headers.containsKey("X-Frame-Options")).isTrue();
		});
		softAssertions.assertAll();
		driver.get("https://automationexercise.com/");
	}
}
