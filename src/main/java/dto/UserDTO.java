package dto;

public class UserDTO {
    public final String email;
    public final String password;
    public final String name;

    public UserDTO(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
