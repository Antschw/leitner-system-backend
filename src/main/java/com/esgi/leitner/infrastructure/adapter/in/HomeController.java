package com.esgi.leitner.infrastructure.adapter.in;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home controller to provide a default landing page for the API.
 * <p>
 * Displays a message confirming that the API is running and provides
 * a link to Swagger UI for API documentation.
 */
@RestController
@RequestMapping("/")
public class HomeController {

    /**
     * Returns a simple message confirming that the API is running.
     * Provides a link to the Swagger UI for API documentation.
     *
     * @return An HTML-formatted string containing a message and a link to Swagger UI.
     */
    @GetMapping
    public String home() {
        return "Leitner System API is running! Access Swagger at <a href='/swagger-ui/index.html'>Swagger UI</a>";
    }
}
