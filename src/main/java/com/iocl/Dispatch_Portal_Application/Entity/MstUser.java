package com.iocl.Dispatch_Portal_Application.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.iocl.Dispatch_Portal_Application.composite_pk.MstUserPK;

import lombok.Data;


@Entity
@Table(name = "mst_user")
@Data
@IdClass(MstUserPK.class)

public class MstUser {

	
	@Id
    @Column(name = "loc_code", length = 6)
    private String locCode;

    @Id
    @Column(name = "user_id", length = 10)
    private String userId;

    @Column(name = "user_name", length = 50)
    private String userName; 

    @Column(name = "mobile_no", length = 50)
    private Long mobileNumber; 

    @Column(name = "role_id", length = 10)
    private String roleId;

    @Column(name = "status", length = 1)
    private String status = "A";

    @Column(name = "created_by", length = 10)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;  
    
    @Column(name = "password", length = 50)
    private String password;
    

    
}
