package org.app.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserModel {
    int id;
    String username;
    String password;
    Date grantedDate;
}
