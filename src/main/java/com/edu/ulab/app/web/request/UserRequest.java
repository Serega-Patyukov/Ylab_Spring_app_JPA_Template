package com.edu.ulab.app.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequest {
    private long id;

    private String fullName;
    private String title;
    private int age;
}
