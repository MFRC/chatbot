package com.saathratri.bookingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "address_street_1")
    private String addressStreet1;

    @Column(name = "address_street_2")
    private String addressStreet2;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_state_or_province")
    private String addressStateOrProvince;

    @Column(name = "address_country")
    private String addressCountry;

    @Column(name = "address_zip_or_postal_code")
    private String addressZipOrPostalCode;

    @Column(name = "address_is_home_or_business")
    private String addressIsHomeOrBusiness;

    @JsonIgnoreProperties(value = { "address" }, allowSetters = true)
    @OneToOne(mappedBy = "address")
    private CustomerInfo customer;

    @OneToMany(mappedBy = "address")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "loyaltyProgram", "address", "reservations" }, allowSetters = true)
    private Set<HotelInfo> hotels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Address id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddressStreet1() {
        return this.addressStreet1;
    }

    public Address addressStreet1(String addressStreet1) {
        this.setAddressStreet1(addressStreet1);
        return this;
    }

    public void setAddressStreet1(String addressStreet1) {
        this.addressStreet1 = addressStreet1;
    }

    public String getAddressStreet2() {
        return this.addressStreet2;
    }

    public Address addressStreet2(String addressStreet2) {
        this.setAddressStreet2(addressStreet2);
        return this;
    }

    public void setAddressStreet2(String addressStreet2) {
        this.addressStreet2 = addressStreet2;
    }

    public String getAddressCity() {
        return this.addressCity;
    }

    public Address addressCity(String addressCity) {
        this.setAddressCity(addressCity);
        return this;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStateOrProvince() {
        return this.addressStateOrProvince;
    }

    public Address addressStateOrProvince(String addressStateOrProvince) {
        this.setAddressStateOrProvince(addressStateOrProvince);
        return this;
    }

    public void setAddressStateOrProvince(String addressStateOrProvince) {
        this.addressStateOrProvince = addressStateOrProvince;
    }

    public String getAddressCountry() {
        return this.addressCountry;
    }

    public Address addressCountry(String addressCountry) {
        this.setAddressCountry(addressCountry);
        return this;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressZipOrPostalCode() {
        return this.addressZipOrPostalCode;
    }

    public Address addressZipOrPostalCode(String addressZipOrPostalCode) {
        this.setAddressZipOrPostalCode(addressZipOrPostalCode);
        return this;
    }

    public void setAddressZipOrPostalCode(String addressZipOrPostalCode) {
        this.addressZipOrPostalCode = addressZipOrPostalCode;
    }

    public String getAddressIsHomeOrBusiness() {
        return this.addressIsHomeOrBusiness;
    }

    public Address addressIsHomeOrBusiness(String addressIsHomeOrBusiness) {
        this.setAddressIsHomeOrBusiness(addressIsHomeOrBusiness);
        return this;
    }

    public void setAddressIsHomeOrBusiness(String addressIsHomeOrBusiness) {
        this.addressIsHomeOrBusiness = addressIsHomeOrBusiness;
    }

    public CustomerInfo getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerInfo customerInfo) {
        if (this.customer != null) {
            this.customer.setAddress(null);
        }
        if (customerInfo != null) {
            customerInfo.setAddress(this);
        }
        this.customer = customerInfo;
    }

    public Address customer(CustomerInfo customerInfo) {
        this.setCustomer(customerInfo);
        return this;
    }

    public Set<HotelInfo> getHotels() {
        return this.hotels;
    }

    public void setHotels(Set<HotelInfo> hotelInfos) {
        if (this.hotels != null) {
            this.hotels.forEach(i -> i.setAddress(null));
        }
        if (hotelInfos != null) {
            hotelInfos.forEach(i -> i.setAddress(this));
        }
        this.hotels = hotelInfos;
    }

    public Address hotels(Set<HotelInfo> hotelInfos) {
        this.setHotels(hotelInfos);
        return this;
    }

    public Address addHotel(HotelInfo hotelInfo) {
        this.hotels.add(hotelInfo);
        hotelInfo.setAddress(this);
        return this;
    }

    public Address removeHotel(HotelInfo hotelInfo) {
        this.hotels.remove(hotelInfo);
        hotelInfo.setAddress(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", addressStreet1='" + getAddressStreet1() + "'" +
            ", addressStreet2='" + getAddressStreet2() + "'" +
            ", addressCity='" + getAddressCity() + "'" +
            ", addressStateOrProvince='" + getAddressStateOrProvince() + "'" +
            ", addressCountry='" + getAddressCountry() + "'" +
            ", addressZipOrPostalCode='" + getAddressZipOrPostalCode() + "'" +
            ", addressIsHomeOrBusiness='" + getAddressIsHomeOrBusiness() + "'" +
            "}";
    }
}
