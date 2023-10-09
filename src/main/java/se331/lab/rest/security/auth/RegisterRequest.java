package se331.lab.rest.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  String firstname;
  String lastname;
  String username;
  String email;
  String password;
  String name;
  String image;
}
