# Selenium4 Based Test Automation Project
## Features
- A solid webdriver management using Junit5 Lifecycle Callbacks
- Embedding security testing in Selenium Functional tests
- Capturing and Analysing Web UI performance through Selenium Functional tests

# Prerequisites to run tests
- Java 17
- Maven
- Docker Desktop
- An IDE - IntelliJ / Eclipse or something similar

## Webdriver management using Junit5 Lifecycle Callbacks

Please refer to my medium article : https://medium.com/@boddupally.anji/streamlining-webdriver-management-in-ui-testing-a-junit-5-approach-with-annotations-e4b0d34af39f

# Embedding security testing in Selenium Functional tests

The general, we start functional testing as soon as development starts and with shift left approach, we try to embed Unit and Integration tests in CI pipelines to get faster feebdack about functionality of software we develop. While this is great practice, cost effective, we can also include security testing in functional testing which will help us to identify Security Vulnerabilities early in development process. We can leverage our functional tests with minimal setup to carry security testing, will achieve greater benefit.

# 1. Cross-Site Scripting (XSS)

In XSS, an attacker sends malicious script to an end user. Once the script is executed, attacker may steal cookies, session tokens an other sensitive information and sometimes, script may modifiy the content of the webpage.

### How to include XSS security tests in Selenium UI tests?

When we write UI tests, we can pass various types of XSS strigs to all input fields that accept text and make sure, XSS script does not get executed

   ```
   @Test
   void xssAttackTest() {
        this.driver.get("https://www.example.com");
        this.driver.findElement(By.id("username")).sendKeys("<script>alert('XSS');</script>");
        this.driver.findElement(By.id("password")).sendKeys("******");
        this.driver.findElement(By.id("login")).click();
        boolean isAlertPresent = false;
        try {
          driver.switchTo().alert();
          isAlertPresent = true;
        } catch (NoAlertPresentException e) {
          // do nothing
        }
        Assertions.assertThat(isAlertPresent).isFalse();
    }
   ```

Refer: https://owasp.org/www-community/attacks/xss/
  
# 2. Masking of sensitive information

When entering sensitive information, such as passwords, on a web page, it is essential to ensure it is masked. If this information isn’t masked, the browser may store it, potentially allowing other users with access to the device to view it, and leaving it vulnerable to hackers who could easily steal it.

### How to include this in Selenium UI tests?

When we write UI tests, we can pass various types of XSS strigs to all input fields that accept text and make sure, XSS script does not get executed

   ````
   @Test
   void xssAttackTest() {
        this.driver.get("https://www.example.com");
        Assertions.assertThat(loginPage.getPasswordWE().getAttribute("type")).isEqualTo("password");
        Assertions.assertThat(isAlertPresent).isFalse();
    }
   ````
   
# 3. SQL Injection

A SQL injection attack consists of insertion or “injection” of a SQL query via the input data from the client to the application. A successful SQL injection exploit can read sensitive data from the database, modify database data (Insert/Update/Delete), execute administration operations on the database (such as shutdown the DBMS), recover the content of a given file present on the DBMS file system and in some cases issue commands to the operating system. SQL injection attacks are a type of injection attack, in which SQL commands are injected into data-plane input in order to affect the execution of predefined SQL commands.

### How to include SQL Injection tests in Selenium UI tests?

   ```
   @Test
   void sqlInjectTest() {
        this.driver.get("https://www.example.com");
        this.driver.findElement(By.id("username")).sendKeys("\" or \"\"=\"");
        this.driver.findElement(By.id("password")).sendKeys("\" or \"\"=\"");
        this.driver.findElement(By.id("login")).click();
      // Assert login is not successful
    }
    
   ```

Refer:
1. https://www.w3schools.com/sql/sql_injection.asp
2. https://owasp.org/www-community/attacks/SQL_Injection


# 4. Secure Cookies

We must ensure that cookies containing sensitive information are marked as secure and can only be transmitted over HTTPS. Additionally, they should be marked as HttpOnly to prevent client-side scripts from accessing them. If a cookie is not secure or HttpOnly, the application may include this information in unsecure HTTP requests, allowing attackers to intercept the insecure connection and steal valuable information from the session cookies or manipulate them via XSS attacks.

- Secure Attribute: This attribute ensures that the cookie is only sent over HTTPS connections. Cookies with this attribute will not be included in HTTP requests at all.

- HttpOnly Attribute: This attribute allows the cookie to be transmitted over both HTTP and HTTPS but prevents access through client-side scripts.

### How to write Secure Cookies tests in Selenium UI tests?

   ```
   @Test
   void secureCookiesTest() {
	loginPage.loginValidUser(USERNAME, PASSWORD);
	Cookie sessionCukki = driver.manage().getCookieNamed("JSESSIONID");
	Assertions.assertThat(sessionCukki.isHttpOnly()).isTrue();
	Assertions.assertThat(sessionCukki.isSecure()).isTrue();
    }

   ```
 
# 5. Session Cookie Manipulation

Session cookies play a critical role in the functioning of web applications by facilitating the management of user sessions. Here are the key reasons why session cookies are important:

- **Maintaining Login State**: Session cookies are used to keep users logged in while they navigate through a web application. When a user logs in, the server creates a session and sends a session cookie to the client's browser. This cookie contains a unique session identifier that allows the server to recognize the user on subsequent requests.

- **Secure Access**: By using session cookies, web applications can verify user identities without requiring users to re-enter their credentials for every interaction.

- **Stateless Protocol**: HTTP is a stateless protocol, meaning each request is independent and does not retain information about previous requests. Session cookies help maintain stateful interactions by preserving user context (like preferences, shopping cart contents, or navigation history) between requests.
   
- **Data Storage**: While session cookies are primarily used for session management, they can also store minimal data relevant to the session, which can be accessed on subsequent requests.

- **Session Security**: Properly configured session cookies (e.g., using the `HttpOnly` and `Secure` flags) enhance security by preventing access to session identifiers via client-side scripts and ensuring that cookies are only transmitted over secure HTTPS connections. This helps protect against attacks like session hijacking and Cross-Site Scripting (XSS).

- **User Experience**: Session cookies allow web applications to personalize user experiences based on previous interactions. For example, they can remember user preferences (like language settings or theme choices) throughout the session.
   
- **Custom Features**: Applications can provide tailored content or features based on the user's profile or past behavior, enhancing engagement and satisfaction.

- **Reduced Load on Servers**: By managing session data on the client-side through cookies, web applications can reduce the amount of state information stored on the server. This can improve performance and scalability, especially in high-traffic applications.

- **Fast Access**: Since session cookies are stored in the user's browser, accessing session data is typically faster than querying a database or server-side store, leading to quicker response times for the user.

- **Session Isolation**: In multi-user environments (like web applications with collaborative features), session cookies help isolate user sessions. This ensures that user actions are confined to their sessions, maintaining privacy and preventing data leakage between users.


### How to include session management tests in Selenium UI tests?

   ```
   @Test
   void secureCookiesTest() {
	loginPage.loginValidUser(USERNAME, PASSWORD);
	homePage = new HomePage(this.driver);
	Assertions.assertThat(homePage.getUsername()).contains(USERNAME);
	driver.manage().deleteCookie(driver.manage().getCookieNamed("JSESSIONID"));
	driver.navigate().refresh();
	loginPage.loginValidUser(USERNAME, PASSWORD);
    }
    
   ```




