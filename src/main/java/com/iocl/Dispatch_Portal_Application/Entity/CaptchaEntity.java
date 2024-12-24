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
@Table(name = "captcha" , schema = "public")
public class CaptchaEntity {

	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name = "id")
	  private UUID id;
	  
	  @Column(name="value")
	  private String value;
	 
	  @Column(name="expiry_time")
      private LocalDateTime expiryTime;
}
