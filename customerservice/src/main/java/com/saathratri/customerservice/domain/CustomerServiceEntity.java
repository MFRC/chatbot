package com.saathratri.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustomerServiceEntity.
 */
@Entity
@Table(name = "customer_service_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "reservation_number")
    private String reservationNumber;

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "services")
    private String services;

    @Column(name = "prices")
    private Long prices;

    @Column(name = "amenities")
    private String amenities;

    @JsonIgnoreProperties(value = { "end", "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Conversation conversation;

    @JsonIgnoreProperties(value = { "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne(mappedBy = "customerServiceEntity")
    private CustomerService customerService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CustomerServiceEntity id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReservationNumber() {
        return this.reservationNumber;
    }

    public CustomerServiceEntity reservationNumber(String reservationNumber) {
        this.setReservationNumber(reservationNumber);
        return this;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Integer getRoomNumber() {
        return this.roomNumber;
    }

    public CustomerServiceEntity roomNumber(Integer roomNumber) {
        this.setRoomNumber(roomNumber);
        return this;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getServices() {
        return this.services;
    }

    public CustomerServiceEntity services(String services) {
        this.setServices(services);
        return this;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Long getPrices() {
        return this.prices;
    }

    public CustomerServiceEntity prices(Long prices) {
        this.setPrices(prices);
        return this;
    }

    public void setPrices(Long prices) {
        this.prices = prices;
    }

    public String getAmenities() {
        return this.amenities;
    }

    public CustomerServiceEntity amenities(String amenities) {
        this.setAmenities(amenities);
        return this;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public CustomerServiceEntity conversation(Conversation conversation) {
        this.setConversation(conversation);
        return this;
    }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        if (this.customerService != null) {
            this.customerService.setCustomerServiceEntity(null);
        }
        if (customerService != null) {
            customerService.setCustomerServiceEntity(this);
        }
        this.customerService = customerService;
    }

    public CustomerServiceEntity customerService(CustomerService customerService) {
        this.setCustomerService(customerService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerServiceEntity)) {
            return false;
        }
        return id != null && id.equals(((CustomerServiceEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceEntity{" +
            "id=" + getId() +
            ", reservationNumber='" + getReservationNumber() + "'" +
            ", roomNumber=" + getRoomNumber() +
            ", services='" + getServices() + "'" +
            ", prices=" + getPrices() +
            ", amenities='" + getAmenities() + "'" +
            "}";
    }
}
