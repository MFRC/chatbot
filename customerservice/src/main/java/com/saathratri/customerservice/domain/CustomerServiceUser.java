package com.saathratri.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustomerServiceUser.
 */
@Entity
@Table(name = "customer_service_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "reservation_number")
    private String reservationNumber;

    @Column(name = "room_number")
    private String roomNumber;

    @JsonIgnoreProperties(value = { "end", "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Conversation conversation;

    @JsonIgnoreProperties(value = { "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne(mappedBy = "customerServiceUser")
    private CustomerService customerService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CustomerServiceUser id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public CustomerServiceUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public CustomerServiceUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public CustomerServiceUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public CustomerServiceUser phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReservationNumber() {
        return this.reservationNumber;
    }

    public CustomerServiceUser reservationNumber(String reservationNumber) {
        this.setReservationNumber(reservationNumber);
        return this;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public CustomerServiceUser roomNumber(String roomNumber) {
        this.setRoomNumber(roomNumber);
        return this;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public CustomerServiceUser conversation(Conversation conversation) {
        this.setConversation(conversation);
        return this;
    }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        if (this.customerService != null) {
            this.customerService.setCustomerServiceUser(null);
        }
        if (customerService != null) {
            customerService.setCustomerServiceUser(this);
        }
        this.customerService = customerService;
    }

    public CustomerServiceUser customerService(CustomerService customerService) {
        this.setCustomerService(customerService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerServiceUser)) {
            return false;
        }
        return id != null && id.equals(((CustomerServiceUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", reservationNumber='" + getReservationNumber() + "'" +
            ", roomNumber='" + getRoomNumber() + "'" +
            "}";
    }
}
