package com.aepl.actions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * @author Suraj_Bhalerao
 * @version 1.0
 * 
 * 
 * The LocationActions class provides utility methods for interacting with 
 * dynamic location selection fields in web applications using Selenium WebDriver.
 * 
 * <p>
 * Features:
 * <ul>
 *   <li>Allows typing into an input field to trigger dynamic location suggestions.</li>
 *   <li>Selects a location from the displayed suggestions based on a match.</li>
 *   <li>Includes robust error handling and logging for better traceability.</li>
 * </ul>
 * <p>
 * Logging:
 * <ul>
 *   <li>Logs detailed information about the input and suggestions processing.</li>
 *   <li>Captures errors and exceptions for troubleshooting.</li>
 * </ul>
 * 
 */
public class LocationActions {
    private WebDriver driver;
    private WebDriverWait wait;

    private static final Logger logger = LogManager.getLogger(LocationActions.class);

    /**
     * Constructor to initialize the LocationActions instance.
     *
     * @param driver the WebDriver instance used to control the browser.
     * @throws IllegalArgumentException if the WebDriver instance is null.
     */
    public LocationActions(WebDriver driver) {
        if (driver == null) {
            logger.error("WebDriver instance is null. Cannot initialize LocationActions.");
            throw new IllegalArgumentException("WebDriver instance cannot be null");
        }

        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        logger.info("LocationActions instance initialized successfully.");
    }

    /**
     * Selects a location dynamically by typing into the input field and selecting 
     * a matching suggestion from the dropdown.
     *
     * @param inputLocator the locator for the input field.
     * @param suggestionsLocator the locator for the list of location suggestions.
     * @param locationToSelect the location to select from the suggestions.
     * @throws RuntimeException if the location is not found in the suggestions.
     */
    public void selectDynamicLocation(By inputLocator, By suggestionsLocator, String locationToSelect) {
        logger.info("Attempting to select location: " + locationToSelect);

        try {
            // Wait for the input field to become clickable and type partial location
            logger.info("Waiting for the input field to be clickable...");
            WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
            logger.info("Input field located. Typing the first few characters of the location...");
            inputField.sendKeys(locationToSelect.substring(0, 3));

            // Wait for suggestions to appear
            logger.info("Waiting for location suggestions to appear...");
            List<WebElement> suggestions = wait
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(suggestionsLocator));

            // Iterate through suggestions and select the matching location
            logger.info("Processing location suggestions...");
            for (WebElement suggestion : suggestions) {
                logger.info("Suggestion found: " + suggestion.getText());
                if (suggestion.getText().equalsIgnoreCase(locationToSelect)) {
                    logger.info("Matching location found. Clicking on suggestion: " + locationToSelect);
                    suggestion.click();
                    logger.info("Location selected successfully: " + locationToSelect);
                    return;
                }
            }

            // If no match is found, throw an exception
            logger.warn("Location not found in suggestions: " + locationToSelect);
            throw new RuntimeException("Location not found in suggestions: " + locationToSelect);

        } catch (Exception e) {
            logger.error("Error occurred while selecting location: " + locationToSelect, e);
            throw e; // Re-throw exception for upstream handling
        }
    }
}
