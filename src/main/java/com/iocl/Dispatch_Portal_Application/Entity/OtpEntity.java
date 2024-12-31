package com.iocl.Dispatch_Portal_Application.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "otp")
public class OtpEntity {

	  @Id

	  @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="id")
	    private UUID id;
        @Column(name="emp_code")
	    private String empCode; // Assuming this is the employee code or identifier
        @Column(name="otp")
	    private int otp;
        @Column(name="created_at")
	    private LocalDateTime createdAt;
        @Column(name="expiration_at")
	    private LocalDateTime expirationAt;
}
