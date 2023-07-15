package com.saathratri.bookingservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.bookingservice.domain.LoyaltyProgram} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyProgramDTO implements Serializable {

  private Long id;

  private String loyaltyProgramName;

  private Boolean loyaltyProgramMember;

  private String loyaltyProgramNumber;

  private String loyaltyProgramTier;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLoyaltyProgramName() {
    return loyaltyProgramName;
  }

  public void setLoyaltyProgramName(String loyaltyProgramName) {
    this.loyaltyProgramName = loyaltyProgramName;
  }

  public Boolean getLoyaltyProgramMember() {
    return loyaltyProgramMember;
  }

  public void setLoyaltyProgramMember(Boolean loyaltyProgramMember) {
    this.loyaltyProgramMember = loyaltyProgramMember;
  }

  public String getLoyaltyProgramNumber() {
    return loyaltyProgramNumber;
  }

  public void setLoyaltyProgramNumber(String loyaltyProgramNumber) {
    this.loyaltyProgramNumber = loyaltyProgramNumber;
  }

  public String getLoyaltyProgramTier() {
    return loyaltyProgramTier;
  }

  public void setLoyaltyProgramTier(String loyaltyProgramTier) {
    this.loyaltyProgramTier = loyaltyProgramTier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LoyaltyProgramDTO)) {
      return false;
    }

    LoyaltyProgramDTO loyaltyProgramDTO = (LoyaltyProgramDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, loyaltyProgramDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyProgramDTO{" +
            "id=" + getId() +
            ", loyaltyProgramName='" + getLoyaltyProgramName() + "'" +
            ", loyaltyProgramMember='" + getLoyaltyProgramMember() + "'" +
            ", loyaltyProgramNumber='" + getLoyaltyProgramNumber() + "'" +
            ", loyaltyProgramTier='" + getLoyaltyProgramTier() + "'" +
            "}";
    }
}
