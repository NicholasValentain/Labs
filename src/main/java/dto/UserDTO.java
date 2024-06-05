package dto;

public class UserDTO extends DTO {
    private String userName;
    private boolean status; // true - клиент присоединился, false - клиент отключился

    public UserDTO(String userName) {
        this.userName = userName;
    }

    public UserDTO(String userName, boolean status) {
        this.userName = userName;
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public boolean getStatus() {
        return status;
    }
}
