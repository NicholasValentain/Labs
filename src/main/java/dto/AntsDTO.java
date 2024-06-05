package dto;

import org.example.labs.controllres.UserAntController;

import java.io.Serializable;
import java.util.List;

public class AntsDTO extends DTO {
    public String UserName;
    public double[] xCoordinates;
    public double[] yCoordinates;
    public boolean[] type;

    public AntsDTO(String targetUserName, double[] xCoordinates, double[] yCoordinates, boolean[] type) {
        this.UserName = targetUserName;
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
        this.type = type;
    }

    public String getTargetUserName() {
        return UserName;
    }

    public double[] getXCoordinates() {
        return xCoordinates;
    }

    public double[] getYCoordinates() {
        return yCoordinates;
    }

    public boolean[] getType() {
        return type;
    }

}
