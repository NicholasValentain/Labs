package dto;

public class ListUserDTO extends DTO {
    private String[] userList;

    public ListUserDTO(String[] userList) {
        this.userList = userList;
    }

    public String[] getUserList() {
        return userList;
    }
}
