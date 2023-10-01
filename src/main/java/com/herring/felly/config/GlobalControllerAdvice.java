package com.herring.felly.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("X-Request-UUID")
    public String addRequestUUID(HttpServletRequest request) {
        // Retrieve the UUID from the request attribute and include it in the response
        return (String) request.getAttribute("X-Request-UUID");
    }
}
