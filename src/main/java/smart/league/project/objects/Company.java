package smart.league.project.objects;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Company {

    String id;
    String displayName;
    Metadata metadata;
    List<Attribute> attribute;
    List<Address> address;
    List<Phone> phone;
    List<Product> product;
    List<Image> image;
    Boolean allowReviews;
    Boolean allowOffers;
    List<String> offerCategory;
    List<Trade> trade;
    Reviews reviews;

    @JsonIgnore
    List<Agent> agent;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Attribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Attribute> attribute) {
        this.attribute = attribute;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public Boolean getAllowReviews() {
        return allowReviews;
    }

    public void setAllowReviews(Boolean allowReviews) {
        this.allowReviews = allowReviews;
    }

    public Boolean getAllowOffers() {
        return allowOffers;
    }

    public void setAllowOffers(Boolean allowOffers) {
        this.allowOffers = allowOffers;
    }

    public List<String> getOfferCategory() {
        return offerCategory;
    }

    public void setOfferCategory(List<String> offerCategory) {
        this.offerCategory = offerCategory;
    }

    public List<Trade> getTrade() {
        return trade;
    }

    public void setTrade(List<Trade> trade) {
        this.trade = trade;
    }

    public List<Agent> getAgent() {
        return agent;
    }

    public void setAgent(List<Agent> agent) {
        this.agent = agent;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", metadata=" + metadata +
                ", attribute=" + attribute +
                ", address=" + address +
                ", phone=" + phone +
                ", product=" + product +
                ", image=" + image +
                ", allowReviews=" + allowReviews +
                ", allowOffers=" + allowOffers +
                ", offerCategory=" + offerCategory +
                ", trade=" + trade +
                ", reviews=" + reviews +
                ", agent=" + agent +
                '}';
    }
}
