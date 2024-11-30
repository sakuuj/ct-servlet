package by.sakujj.entity;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Client {
    private UUID id;

    private String username;
    private String email;
    private String password;
    private Integer age;
}