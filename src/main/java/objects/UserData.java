package objects;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserData {

    String name;
    String job;
    int id;
    String email;
    String password;
}
