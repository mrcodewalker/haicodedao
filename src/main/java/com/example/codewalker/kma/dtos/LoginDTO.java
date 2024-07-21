package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class LoginDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
