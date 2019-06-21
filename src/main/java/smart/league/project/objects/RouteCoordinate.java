package smart.league.project.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RouteCoordinate {

    String type;
    @JsonIgnore
    Coordinate[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }
}
