package com.medorb.HMS;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;


@SpringBootApplication
public class HospitalManagementSystemApplication {

    public static void main(String[] args) {
        System.out.println("Application is Starting.....");
        //Run the application and store result in ctx
        ConfigurableApplicationContext ctx = SpringApplication.run(HospitalManagementSystemApplication.class, args);
		// 2) Grab the environment to find server port and context path

		Environment env = ctx.getEnvironment();

        // If not set, defaults to "8080" and "" (empty) for context path
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        // 3) Print your desired link to the console
        System.out.println("----------------------------------------------------------");
        System.out.println("Application is running! Access it here:");
        System.out.println("http://localhost:" + port + contextPath + "/");
        System.out.println("----------------------------------------------------------");
    }
 
}