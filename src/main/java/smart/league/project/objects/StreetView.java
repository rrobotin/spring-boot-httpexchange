package smart.league.project.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreetView {

    Integer row;
    Integer col;
    String type;
    String uri;
    Number angle;
    @JsonProperty("isVideoAvailable")
    Boolean isVideoAvailable;
    Coordinate coordinate;

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Number getAngle() {
        return angle;
    }

    public void setAngle(Number angle) {
        this.angle = angle;
    }

    public Boolean getVideoAvailable() {
        return isVideoAvailable;
    }

    public void setVideoAvailable(Boolean videoAvailable) {
        isVideoAvailable = videoAvailable;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
