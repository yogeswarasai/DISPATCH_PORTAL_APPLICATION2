package com.iocl.Dispatch_Portal_Application.modal;

import java.util.UUID;

import lombok.Data;

@Data
public class CaptchaResponseData {
	
	private UUID id;
	    private byte[] image;
	    private String captchaValue;



		public CaptchaResponseData() {
			// TODO Auto-generated constructor stub
		}

		
}
