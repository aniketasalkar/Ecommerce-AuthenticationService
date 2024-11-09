package com.example.authenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class UserAuth extends BaseModel{
//    @Id
//    private Long id;
    private String email;
    private String password;
    private Date lastLogin;

}
