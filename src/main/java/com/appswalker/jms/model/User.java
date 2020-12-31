package com.appswalker.jms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    private String name;
    private String password;
    private String gender;
}
