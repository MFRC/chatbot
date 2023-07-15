package com.saathratri.orchestrator.web.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saathratri.landingpage.client.dto.LandingPageCustomerDTO;
import com.saathratri.landingpage.client.dto.LandingPageMessageEmailDTO;
import com.saathratri.landingpage.client.dto.LandingPageReviewEmailDTO;
import com.saathratri.messagesender.service.dto.ContactUsMessageDTO;
import com.saathratri.organizations.service.dto.AddressDTO;
import com.saathratri.organizations.service.dto.ApplicationDTO;
import com.saathratri.organizations.service.dto.ApplicationTajUserPropertyDTO;
import com.saathratri.organizations.service.dto.CustomerDTO;
import com.saathratri.organizations.service.dto.TajOrganizationDTO;
import com.saathratri.organizations.service.dto.TajUserDTO;
import com.saathratri.sienna.service.dto.CustomerReservationByHotelAndAccountDTO;
import com.saathratri.organizations.service.dto.OrganizationDTO;
import com.saathratri.organizations.service.dto.PersonDTO;
import com.saathratri.organizations.service.dto.PhoneDTO;
import com.saathratri.organizations.service.dto.TajEmailDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelAndLastNameAndFirstNameDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelAndLastNameAndFirstNameIdDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelAndMainStarRatingDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelAndMainStarRatingIdDTO;
import com.saathratri.tajvote.service.dto.CustomerReviewsByHotelIdDTO;
import com.saathratri.tajvote.service.dto.RatingByHotelAndReviewDTO;
import com.saathratri.tajvote.service.dto.RatingByHotelAndReviewIdDTO;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static final String ACTION_SEND_TEXT = "ACTION_SEND_TEXT";
    public static final String ACTION_SEND_WHATSAPP_MESSAGES = "ACTION_SEND_WHATSAPP_MESSAGES";
    public static final String ACTION_SEND_EMAIL = "ACTION_SEND_EMAIL";
    public static final String ACTION_MAKE_PHONE_CALL = "ACTION_MAKE_PHONE_CALL";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
    
    public static String getTaskId(TajUserDTO user, ApplicationDTO application, String actionName) {
        return new StringBuilder()
            .append("{ ")
            .append("userId: ")
			.append(user.getId())
			.append(", ")
            .append("username: ")
			.append(user.getUsername())
            .append(", ")
            .append("applicationId: ")
            .append(application.getId())
            .append(", ")
            .append("applicationName: ")
            .append(application.getName())
            .append("actionName: ")
            .append(actionName)
            .append(" }")
            .toString();
    }

    public static Instant getCurrentMillisecondMinutesFromEpoch() {
        Calendar calendar = Calendar.getInstance();
        calendar.add( Calendar.SECOND, 30);
        calendar.set( Calendar.SECOND, 0);
        calendar.set( Calendar.MILLISECOND, 0);

        return calendar.toInstant();
    }

    public static String getWouldRecommend(Boolean wouldRecommend) {
        if(wouldRecommend.booleanValue()) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public static boolean isStartupApplicationCronSchedularOnForCalendarService(List<ApplicationTajUserPropertyDTO> applicationUserProperties) {
        for(ApplicationTajUserPropertyDTO applicationUserProperty: applicationUserProperties) {
            if(isStartupApplicationCronSchedularSwitchOnForCalendarService(applicationUserProperty)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isStartupApplicationCronSchedularSwitchOnForCalendarService(ApplicationTajUserPropertyDTO applicationUserProperty) {
        if("RUN_CALENDAR_SERVICE_FOR_USER".equalsIgnoreCase(applicationUserProperty.getName().trim())){
            return "TRUE".equalsIgnoreCase(applicationUserProperty.getValue().trim());
        }

        return false;
    }

    public static CustomerReviewsByHotelAndLastNameAndFirstNameDTO customerDTOToCustomerReviewsByHotelAndLastNameAndFirstName(
        LandingPageCustomerDTO landingPageCustomerDTO, TajOrganizationDTO organization, CustomerDTO customer) {
        CustomerReviewsByHotelAndLastNameAndFirstNameIdDTO customerReviewsByHotelAndLastNameAndFirstNameId 
            = new CustomerReviewsByHotelAndLastNameAndFirstNameIdDTO();
        customerReviewsByHotelAndLastNameAndFirstNameId.setHotelId(organization.getId());
        customerReviewsByHotelAndLastNameAndFirstNameId.setLastName(customer.getPerson().getLastName());
        customerReviewsByHotelAndLastNameAndFirstNameId.setFirstName(customer.getPerson().getFirstName());
        customerReviewsByHotelAndLastNameAndFirstNameId.setWrittenDate(landingPageCustomerDTO.getReview().getWrittenDate());
        customerReviewsByHotelAndLastNameAndFirstNameId.setReviewId(landingPageCustomerDTO.getReview().getId());

        CustomerReviewsByHotelAndLastNameAndFirstNameDTO customerReviewsByHotelAndLastNameAndFirstName = new CustomerReviewsByHotelAndLastNameAndFirstNameDTO();
        customerReviewsByHotelAndLastNameAndFirstName.setId(customerReviewsByHotelAndLastNameAndFirstNameId);
        
        customerReviewsByHotelAndLastNameAndFirstName.setAddedSource(landingPageCustomerDTO.getAddedSource());
        customerReviewsByHotelAndLastNameAndFirstName.setComments(landingPageCustomerDTO.getReview().getComments());
        
        customerReviewsByHotelAndLastNameAndFirstName.setCustomerEmailAddresses(landingPageCustomerDTO.getEmailAddress());
        
        customerReviewsByHotelAndLastNameAndFirstName.setCustomerPhoneNumbers(landingPageCustomerDTO.getPhoneNumber());

        customerReviewsByHotelAndLastNameAndFirstName.setMainStarRating(landingPageCustomerDTO.getReview().getMainStarRating());
        customerReviewsByHotelAndLastNameAndFirstName.setWouldRecommend(landingPageCustomerDTO.getReview().getWouldRecommend());

        customerReviewsByHotelAndLastNameAndFirstName.setCustomerId(customer.getId());
        customerReviewsByHotelAndLastNameAndFirstName.setHotelName(organization.getName());
        customerReviewsByHotelAndLastNameAndFirstName.setHotelAddedDateTime(Long.valueOf(organization.getAddedDate().toEpochMilli()));
        customerReviewsByHotelAndLastNameAndFirstName.setHotelEmail(organization.getDefaultEmail().getEmail());
        customerReviewsByHotelAndLastNameAndFirstName.setHotelPhoneNumber(
            "+" + organization.getDefaultPhone().getCountryCode() +
            organization.getDefaultPhone().getNumber());

        return customerReviewsByHotelAndLastNameAndFirstName;
    }

    public static RatingByHotelAndReviewDTO customerDTOToRatingByHotelAndReview(
        LandingPageCustomerDTO landingPageCustomerDTO, TajOrganizationDTO organization
    ) {
        RatingByHotelAndReviewIdDTO ratingByHotelAndReviewId
            = new RatingByHotelAndReviewIdDTO();
        ratingByHotelAndReviewId.setHotelId(organization.getId());
        ratingByHotelAndReviewId.setReviewId(landingPageCustomerDTO.getReview().getId());
        ratingByHotelAndReviewId.setRatingId(landingPageCustomerDTO.getReview().getRating().getId());
        
        RatingByHotelAndReviewDTO ratingByHotelAndReview = new RatingByHotelAndReviewDTO();
        ratingByHotelAndReview.setId(ratingByHotelAndReviewId);

        ratingByHotelAndReview.setMainStarRating(landingPageCustomerDTO.getReview().getMainStarRating());
        ratingByHotelAndReview.setComments(landingPageCustomerDTO.getReview().getComments());
        ratingByHotelAndReview.setFriendlinessRating(landingPageCustomerDTO.getReview().getRating().getFriendlinessRating());
        ratingByHotelAndReview.setLocationRating(landingPageCustomerDTO.getReview().getRating().getLocationRating());
        ratingByHotelAndReview.setOverallRating(landingPageCustomerDTO.getReview().getRating().getOverallRating());
        ratingByHotelAndReview.setRoomCleanlinessRating(landingPageCustomerDTO.getReview().getRating().getRoomCleanlinessRating());
        ratingByHotelAndReview.setWouldRecommend(landingPageCustomerDTO.getReview().getWouldRecommend());

        return ratingByHotelAndReview;
    }

    public static CustomerReviewsByHotelAndMainStarRatingDTO customerDTOToCustomerReviewsByHotelAndMainStarRating(
        LandingPageCustomerDTO landingPageCustomerDTO,
        TajOrganizationDTO organization,
        CustomerDTO customer
    ) {
        CustomerReviewsByHotelAndMainStarRatingIdDTO customerReviewsByHotelAndMainStarRatingId
            = new CustomerReviewsByHotelAndMainStarRatingIdDTO();
        customerReviewsByHotelAndMainStarRatingId.setHotelId(organization.getId());
        customerReviewsByHotelAndMainStarRatingId.setMainStarRating(landingPageCustomerDTO.getReview().getMainStarRating());
        customerReviewsByHotelAndMainStarRatingId.setWrittenDate(landingPageCustomerDTO.getReview().getWrittenDate());
        customerReviewsByHotelAndMainStarRatingId.setReviewId(landingPageCustomerDTO.getReview().getId());

        CustomerReviewsByHotelAndMainStarRatingDTO customerReviewsByHotelAndMainStarRating = new CustomerReviewsByHotelAndMainStarRatingDTO();
        customerReviewsByHotelAndMainStarRating.setId(customerReviewsByHotelAndMainStarRatingId);

        customerReviewsByHotelAndMainStarRating.setAddedSource(landingPageCustomerDTO.getAddedSource());
        customerReviewsByHotelAndMainStarRating.setComments(landingPageCustomerDTO.getReview().getComments());
        
        customerReviewsByHotelAndMainStarRating.setCustomerEmailAddresses(landingPageCustomerDTO.getEmailAddress());
        
        customerReviewsByHotelAndMainStarRating.setCustomerPhoneNumbers(landingPageCustomerDTO.getPhoneNumber());

        customerReviewsByHotelAndMainStarRating.setCustomerId(customer.getId());
        customerReviewsByHotelAndMainStarRating.setFirstName(customer.getPerson().getFirstName());
        customerReviewsByHotelAndMainStarRating.setHotelAddedDateTime(Long.valueOf(organization.getAddedDate().toEpochMilli()));
        customerReviewsByHotelAndMainStarRating.setHotelEmail(organization.getDefaultEmail().getEmail());
        customerReviewsByHotelAndMainStarRating.setHotelName(organization.getName());
        customerReviewsByHotelAndMainStarRating.setHotelPhoneNumber(
            "+" + organization.getDefaultPhone().getCountryCode() +
            organization.getDefaultPhone().getNumber());
        customerReviewsByHotelAndMainStarRating.setWouldRecommend(landingPageCustomerDTO.getReview().getWouldRecommend());

        return customerReviewsByHotelAndMainStarRating;
    }

    public static CustomerReviewsByHotelDTO customerDTOToCustomerReviewsByHotel(
        LandingPageCustomerDTO landingPageCustomerDTO,
        TajOrganizationDTO organization,
        CustomerDTO customer
    ) {
        CustomerReviewsByHotelIdDTO customerReviewsByHotelId = new CustomerReviewsByHotelIdDTO();
        customerReviewsByHotelId.setHotelId(organization.getId());
        customerReviewsByHotelId.setWrittenDate(landingPageCustomerDTO.getReview().getWrittenDate());
        customerReviewsByHotelId.setReviewId(landingPageCustomerDTO.getReview().getId());

        CustomerReviewsByHotelDTO customerReviewsByHotel = new CustomerReviewsByHotelDTO();
        customerReviewsByHotel.setId(customerReviewsByHotelId);

        customerReviewsByHotel.setAddedSource(landingPageCustomerDTO.getAddedSource());
        customerReviewsByHotel.setComments(landingPageCustomerDTO.getReview().getComments());
        customerReviewsByHotel.setCustomerId(customer.getId());

        customerReviewsByHotel.setCustomerEmailAddresses(landingPageCustomerDTO.getEmailAddress());
        
        customerReviewsByHotel.setCustomerPhoneNumbers(landingPageCustomerDTO.getPhoneNumber());

        customerReviewsByHotel.setFirstName(customer.getPerson().getFirstName());
        customerReviewsByHotel.setLastName(customer.getPerson().getLastName());
        customerReviewsByHotel.setHotelAddedDateTime(Long.valueOf(organization.getAddedDate().toEpochMilli()));
        customerReviewsByHotel.setHotelEmail(organization.getDefaultEmail().getEmail());
        customerReviewsByHotel.setHotelName(organization.getName());
        customerReviewsByHotel.setHotelPhoneNumber(  
            "+" + organization.getDefaultPhone().getCountryCode() +
            organization.getDefaultPhone().getNumber());
        customerReviewsByHotel.setMainStarRating(landingPageCustomerDTO.getReview().getMainStarRating());
        customerReviewsByHotel.setWouldRecommend(landingPageCustomerDTO.getReview().getWouldRecommend());

        return customerReviewsByHotel;
    }

    public static LandingPageMessageEmailDTO getLandingPageMessageEmailDTO(
        TajOrganizationDTO organization, 
        TajOrganizationDTO parentOrganization,
        ApplicationDTO application,
        ContactUsMessageDTO contactUsMessageDTO
    ) {
        LandingPageMessageEmailDTO landingPageMessageEmailDTO = new LandingPageMessageEmailDTO();
        landingPageMessageEmailDTO.getEmailMessageDTO().setFrom(parentOrganization.getDefaultEmail().getEmail());
        landingPageMessageEmailDTO.getEmailMessageDTO().setSubject("Re: " + organization.getName() + " has a new message");

        /*
         * TODO: Change to email everyone in organization.
         */
        landingPageMessageEmailDTO.getEmailMessageDTO().setTo(parentOrganization.getDefaultEmail().getEmail());

        landingPageMessageEmailDTO.setOrganizationParentOrganizationName(parentOrganization.getName());
        landingPageMessageEmailDTO.setOrganizationDefaultApplicationName(application.getName());
        landingPageMessageEmailDTO.setOrganizationDefaultUrl(organization.getDefaultTajUrl().getUrl());
        landingPageMessageEmailDTO.setOrganizationName(organization.getName());

        landingPageMessageEmailDTO.setCustomerFullName(contactUsMessageDTO.getFullName());
        landingPageMessageEmailDTO.setCustomerEmail(contactUsMessageDTO.getEmailAddress());
        landingPageMessageEmailDTO.setCustomerPhoneNumber(contactUsMessageDTO.getPhoneNumber());
        landingPageMessageEmailDTO.setCustomerMessage(contactUsMessageDTO.getMessage());

        return landingPageMessageEmailDTO;
    }

    public static LandingPageReviewEmailDTO getLandingPageReviewEmailDTO(
        TajOrganizationDTO organization, 
        TajOrganizationDTO parentOrganization, 
        ApplicationDTO application, 
        CustomerDTO customer,
        LandingPageCustomerDTO landingPageCustomerDTO
        ) {
        LandingPageReviewEmailDTO landingPageReviewEmailDTO = new LandingPageReviewEmailDTO();
        landingPageReviewEmailDTO.getEmailMessageDTO().setFrom(parentOrganization.getDefaultEmail().getEmail());
        landingPageReviewEmailDTO.getEmailMessageDTO().setSubject("Re: " + organization.getName() + " has a new review");

        /*
         * TODO: Change to email everyone in organization.
         */
        landingPageReviewEmailDTO.getEmailMessageDTO().setTo(parentOrganization.getDefaultEmail().getEmail());

        landingPageReviewEmailDTO.setOrganizationDefaultApplicationName(application.getName());
        landingPageReviewEmailDTO.setApplicationDefaultUrl(application.getDefaultTajUrl().getUrl());
        landingPageReviewEmailDTO.setOrganizationDefaultUrl(organization.getDefaultTajUrl().getUrl());
        landingPageReviewEmailDTO.setOrganizationName(organization.getName());
        landingPageReviewEmailDTO.setOrganizationParentOrganizationName(parentOrganization.getName());
        landingPageReviewEmailDTO.setOrganizationName(organization.getName());
        landingPageReviewEmailDTO.setOrganizationId(organization.getId().toString());

        landingPageReviewEmailDTO.setCustomerReviewFirstName(customer.getPerson().getFirstName());
        landingPageReviewEmailDTO.setCustomerReviewLastName(customer.getPerson().getLastName());
        landingPageReviewEmailDTO.setCustomerPhoneNumber(landingPageCustomerDTO.getPhoneNumber());
        landingPageReviewEmailDTO.setCustomerEmail(landingPageCustomerDTO.getEmailAddress());

        landingPageReviewEmailDTO.setCustomerReviewComments(landingPageCustomerDTO.getReview().getComments());
        landingPageReviewEmailDTO.setCustomerReviewMainStarRating(landingPageCustomerDTO.getReview().getMainStarRating().toString());
        landingPageReviewEmailDTO.setCustomerReviewCleanlinessRating(landingPageCustomerDTO.getReview().getRating().getRoomCleanlinessRating().toString());
        landingPageReviewEmailDTO.setCustomerReviewFriendlinessRating(landingPageCustomerDTO.getReview().getRating().getFriendlinessRating().toString());
        landingPageReviewEmailDTO.setCustomerReviewLocationRating(landingPageCustomerDTO.getReview().getRating().getLocationRating().toString());
        landingPageReviewEmailDTO.setCustomerReviewOverallRating(landingPageCustomerDTO.getReview().getRating().getOverallRating().toString());
        landingPageReviewEmailDTO.setCustomerReviewWouldRecommend(landingPageCustomerDTO.getReview().getWouldRecommend().toString());

        landingPageReviewEmailDTO.setCustomerReviewWrittenDate(landingPageCustomerDTO.getReview().getWrittenDate().toString());
        landingPageReviewEmailDTO.setCustomerReviewId(landingPageCustomerDTO.getReview().getId().toString());
        
        return landingPageReviewEmailDTO;
    }

    public static OrganizationDTO getOrganizationDTO(TajOrganizationDTO organization) {
        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setOrganizationId(organization.getId());
        organizationDTO.setOrganizationName(organization.getName());
        organizationDTO.setOrganizationDefaultUrl(organization.getDefaultTajUrl().getUrl());
        organizationDTO.setOrganizationParentOrganizationName(organization.getParentOrganization().getName());
        organizationDTO.setOrganizationDefaultApplicationName(organization.getDefaultApplication().getName());

        return organizationDTO;
    }

    public static String getDateString(Long dateLong) throws ParseException  {
        Date date = new Date(dateLong);
        return DATE_FORMATTER.format(getDateWithoutTimeUsingFormat(date));
    }

    public static Date getDateWithoutTimeUsingFormat(Date date) throws ParseException {
        return DATE_FORMATTER.parse(DATE_FORMATTER.format(date));
    }

    public static String getDateInTimeZone(Long date, String timeZoneId) {
        DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        return DATE_FORMATTER.format(new Date(date));
    }

    public static TajEmailDTO getTajEmailInSet(String email, Set<TajEmailDTO> emails) {
        TajEmailDTO emailDTO = null;
        
        for(TajEmailDTO tajEmail : emails) {
            if(email.equalsIgnoreCase(tajEmail.getEmail())) {
                // If email is already in list / datastore.
                emailDTO = tajEmail;
                break;
            }
        }

        return emailDTO;
    }

    public static PhoneDTO getPhoneInSet(String countryCode, String phoneNumber, Set<PhoneDTO> phones) {
        PhoneDTO phoneDTO = null;
        
        for(PhoneDTO phone : phones) {
            if(countryCode.equals(phone.getCountryCode()) && phoneNumber.equals(phone.getNumber())) {
                // If phone is already in list / datastore.
                phoneDTO = phone;
                break;
            }
        }

        return phoneDTO;
    }

    public static AddressDTO getAddressInSet(
        String addressStreet1,
        String addressStreet2,
        String addressCity,
        String addressStateOrProvince,
        String addressZipOrPostalCode,
        String addressCountry,
        Set<AddressDTO> addresses) {

        AddressDTO addressDTO = null;
        
        for(AddressDTO address : addresses) {
            if(addressStreet1 != null && addressStreet1.equalsIgnoreCase(address.getStreet1()) &&
                addressStreet2 != null && addressStreet2.equalsIgnoreCase(address.getStreet2()) &&
                addressCity != null && addressCity.equalsIgnoreCase(address.getCity()) &&
                addressStateOrProvince != null && addressStateOrProvince.equalsIgnoreCase(address.getState()) &&
                addressZipOrPostalCode != null && addressZipOrPostalCode.equalsIgnoreCase(address.getPostalCode()) &&
                addressCountry != null && addressCountry.equalsIgnoreCase(address.getCountryCode())
            ) {
                // If address is already in list / datastore.
                addressDTO = address;
                break;
            }
        }

        return addressDTO;            
    }

    public static CustomerDTO createCustomerDTO(
        boolean isEmailValid,
        boolean isPhoneNumberValid,
        boolean isAddressValid,
        TajOrganizationDTO organizationDTO,
        AddressDTO addressDTO,
        PhoneDTO phoneDTO,
        CustomerReservationByHotelAndAccountDTO customerReservationByHotelAndAccountDTO
    ) {
        CustomerDTO customerDTO = new CustomerDTO();

        /******************************
         * Get Customer Organization  *
         ******************************/
        Set<TajOrganizationDTO> organizations = new HashSet<>();
        organizations.add(organizationDTO);
        customerDTO.setOrganizations(organizations);

        /********************************
         * Add Person since not exist   *
         ********************************/
        PersonDTO person = new PersonDTO();
        person.setFirstName(customerReservationByHotelAndAccountDTO.getFirstName());
        person.setLastName(customerReservationByHotelAndAccountDTO.getLastName());

        if(isEmailValid) {
            TajEmailDTO emailDTO = new TajEmailDTO();
            emailDTO.setEmail(customerReservationByHotelAndAccountDTO.getEmail());

            Set<TajEmailDTO> emails = new HashSet<>();
            emails.add(emailDTO);
            person.setEmails(emails);
        }

        if(isPhoneNumberValid) {
            Set<PhoneDTO> phones = new HashSet<>();
            phones.add(phoneDTO);
            person.setPhones(phones);
        }

        if(isAddressValid) {
            Set<AddressDTO> addresses = new HashSet<>();
            addresses.add(addressDTO);
            person.setAddresses(addresses);
        }

        person.setAddedDate(Utils.getCurrentMillisecondMinutesFromEpoch());

        /*********************************************************
         * To avoid org.hibernate.TransientPropertyValueException:
         *********************************************************/
        
         /********************************
         * Add Customer since not exist *
         ********************************/
        customerDTO.setPerson(person);
        customerDTO.setAddedDate(Utils.getCurrentMillisecondMinutesFromEpoch());

        return customerDTO;
    }
}