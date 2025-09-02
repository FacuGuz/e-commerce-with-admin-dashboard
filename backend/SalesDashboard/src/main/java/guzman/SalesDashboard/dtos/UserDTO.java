package guzman.SalesDashboard.dtos;

import guzman.SalesDashboard.repositories.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullname;
    private String phoneNumber;
    private String address;
    private Role role;
}
