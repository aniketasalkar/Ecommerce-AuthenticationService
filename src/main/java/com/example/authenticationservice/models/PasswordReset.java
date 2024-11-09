package com.example.authenticationservice.models;

import java.util.Date;

public class PasswordReset extends BaseModel {
    private String email;
    private String resetToken;
    private Date tokenExpiry;
}
