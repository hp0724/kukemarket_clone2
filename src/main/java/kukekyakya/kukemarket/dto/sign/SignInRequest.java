package kukekyakya.kukemarket.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequest {
    public String email;
    public String password;

}
