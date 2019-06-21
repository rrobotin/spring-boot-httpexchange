package smart.league.project.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CheckIn {

    String id;
    Long createdAt;
    String type;
    Integer timeZoneOffset;
    @JsonProperty("with")
    List<User> with;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
