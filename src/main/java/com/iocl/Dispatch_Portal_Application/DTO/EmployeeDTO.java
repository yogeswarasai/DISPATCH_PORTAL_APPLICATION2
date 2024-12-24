package com.iocl.Dispatch_Portal_Application.DTO;

import lombok.Data;

@Data
public class EmployeeDTO {

	private String empName;
    private String empCode;
    private String designation;

    public EmployeeDTO(String empName, String empCode, String designation) {
        this.empName = empName;
        this.empCode = empCode;
        this.designation = designation;
    }
}
