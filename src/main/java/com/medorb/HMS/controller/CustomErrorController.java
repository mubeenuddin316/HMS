package com.medorb.HMS.controller;
import java.util.Enumeration;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            // Set specific error messages based on status code
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage", "Internal server error occurred.");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorMessage", "You don't have permission to access this resource.");
            }
        }
        
        // Get the exception if available
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exception != null) {
            model.addAttribute("errorMessage", exception.getMessage());
            
            // Add more detailed error information if needed
            StringBuilder details = new StringBuilder();
            details.append("Exception: ").append(exception.getClass().getName()).append("\n");
            
            // Add request attributes if they exist
            Enumeration<String> attrNames = request.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String name = attrNames.nextElement();
                if (name.startsWith("javax.servlet.error") || name.startsWith("org.springframework")) {
                    details.append(name).append(": ").append(request.getAttribute(name)).append("\n");
                }
            }
            
            model.addAttribute("errorDetails", details.toString());
        }
        
        // If no specific message was set, use a generic one
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", "An unexpected error occurred.");
        }
        
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
    
    @GetMapping("/trigger-error")
    public String triggerError() {
        throw new RuntimeException("This is a test error message");
    }
}