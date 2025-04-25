package model;

import java.io.Serializable;

import model.Enums.AuState;

public class Address implements Serializable {
    private int addressId;
    private int streetNumber;
    private String street;
    private String suburb;
    private AuState state;
    private String postcode;

    public Address(int addressId, int streetNumber, String street, String suburb, AuState state, String postcode) {
        this.addressId = addressId;
        this.streetNumber = streetNumber;
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public AuState getState() {
        return state;
    }

    public String getPostcode() {
        return postcode;
    }
}
