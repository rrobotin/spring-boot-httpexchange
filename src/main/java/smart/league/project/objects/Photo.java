package smart.league.project.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photo {

    String prefix;
    String suffix;
    @JsonProperty("default")
    Boolean isDefault;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
