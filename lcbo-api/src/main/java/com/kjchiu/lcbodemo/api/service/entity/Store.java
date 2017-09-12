package com.kjchiu.lcbodemo.api.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Store {
    private int id;
    private boolean isDead;
    private String name;
    private String tags;
    private String address1;
    private String address2;
    private String city;
    private String postalCode;
    private String telephone;

    public Store(
            @JsonProperty("id") int id,
            @JsonProperty("is_dead") boolean isDead,
            @JsonProperty("name") String name,
            @JsonProperty("tags") String tags,
            @JsonProperty("address_line_1") String address1,
            @JsonProperty("address_line_2") String address2,
            @JsonProperty("city") String city,
            @JsonProperty("postal_code") String postalCode,
            @JsonProperty("telephone") String telephone) {
        this.id = id;
        this.isDead = isDead;
        this.name = name;
        this.tags = tags;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public boolean isDead() {
        return isDead;
    }

    public String getName() {
        return name;
    }

    public String getTags() {
        return tags;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTelephone() {
        return telephone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", id)
                .append("isDead", isDead)
                .append("name", name)
                .append("tags", tags)
                .append("address1", address1)
                .append("address2", address2)
                .append("city", city)
                .append("postalCode", postalCode)
                .append("telephone", telephone)
                .toString();
    }

}

