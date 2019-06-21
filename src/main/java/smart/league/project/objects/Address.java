package smart.league.project.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    String type;
    String usageCode;
    @JsonProperty("isWorkAddress")
    Boolean isWorkAddress;
    String city;
    String cityPreposition;
    Integer zipcode;
    String street;
    String number;
    String community;
    Coordinate coordinate;
    RouteCoordinate routeCoordinate;
    String entrance;
    @JsonProperty("streetview")
    @JsonIgnore
    StreetView streetview;
    Integer box;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsageCode() {
        return usageCode;
    }

    public void setUsageCode(String usageCode) {
        this.usageCode = usageCode;
    }

    public Boolean getWorkAddress() {
        return isWorkAddress;
    }

    public void setWorkAddress(Boolean workAddress) {
        isWorkAddress = workAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityPreposition() {
        return cityPreposition;
    }

    public void setCityPreposition(String cityPreposition) {
        this.cityPreposition = cityPreposition;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public RouteCoordinate getRouteCoordinate() {
        return routeCoordinate;
    }

    public void setRouteCoordinate(RouteCoordinate routeCoordinate) {
        this.routeCoordinate = routeCoordinate;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public StreetView getStreetview() {
        return streetview;
    }

    public void setStreetview(StreetView streetview) {
        this.streetview = streetview;
    }

    public Integer getBox() {
        return box;
    }

    public void setBox(Integer box) {
        this.box = box;
    }

    @Override
    public String toString() {
        return "Address{" +
                "type='" + type + '\'' +
                ", usageCode='" + usageCode + '\'' +
                ", isWorkAddress=" + isWorkAddress +
                ", city='" + city + '\'' +
                ", cityPreposition='" + cityPreposition + '\'' +
                ", zipcode=" + zipcode +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", community='" + community + '\'' +
                ", coordinate=" + coordinate +
                ", routeCoordinate=" + routeCoordinate +
                ", entrance='" + entrance + '\'' +
                ", streetview=" + streetview +
                ", box=" + box +
                '}';
    }
}
