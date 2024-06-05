package dto;

import java.util.List;

public class RequestDTO extends DTO {

    public String UserName;
    public int antCount;

    public RequestDTO(String targetUserName, int antCount) {
        this.UserName = targetUserName;
        this.antCount = antCount;
    }

    public String getUserName() {
        return UserName;
    }

    public int getAntCount() {
        return antCount;
    }

}
