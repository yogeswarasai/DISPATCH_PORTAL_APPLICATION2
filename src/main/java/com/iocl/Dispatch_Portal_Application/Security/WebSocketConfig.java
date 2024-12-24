package com.iocl.Dispatch_Portal_Application.Security;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.iocl.Dispatch_Portal_Application.ServiceLayer.EmployeeService;
import com.iocl.Dispatch_Portal_Application.ServiceLayer.NotificationHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
	
	private final EmployeeService employeeService;


	  public WebSocketConfig(EmployeeService employeeService) {
	        this.employeeService = employeeService;
	    }

	    @Override
	    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	        registry.addHandler(new NotificationHandler(employeeService), "/notifications").setAllowedOrigins("*");
	    }
}

