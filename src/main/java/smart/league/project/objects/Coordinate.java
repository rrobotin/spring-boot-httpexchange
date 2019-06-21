package smart.league.project.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Coordinate {

    @JsonDeserialize(as = Double.class)
    Number north;
    @JsonDeserialize(as = Double.class)
    Number east;
    String system;


    public Coordinate() {
    }

    public Coordinate(Number north) {
        this.north = north;
    }

    public Number getNorth() {
        return north;
    }

    public void setNorth(Number north) {
        this.north = north;
    }

    public Number getEast() {
        return east;
    }

    public void setEast(Number east) {
        this.east = east;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "north=" + north +
                ", east=" + east +
                ", system='" + system + '\'' +
                '}';
    }
}
