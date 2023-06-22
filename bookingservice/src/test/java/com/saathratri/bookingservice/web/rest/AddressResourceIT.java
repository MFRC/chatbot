package com.saathratri.bookingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.bookingservice.IntegrationTest;
import com.saathratri.bookingservice.domain.Address;
import com.saathratri.bookingservice.repository.AddressRepository;
import com.saathratri.bookingservice.service.dto.AddressDTO;
import com.saathratri.bookingservice.service.mapper.AddressMapper;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final String DEFAULT_ADDRESS_STREET_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_STREET_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_CITY = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_STATE_OR_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_STATE_OR_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_ZIP_OR_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_ZIP_OR_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_IS_HOME_OR_BUSINESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_IS_HOME_OR_BUSINESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .addressStreet1(DEFAULT_ADDRESS_STREET_1)
            .addressStreet2(DEFAULT_ADDRESS_STREET_2)
            .addressCity(DEFAULT_ADDRESS_CITY)
            .addressStateOrProvince(DEFAULT_ADDRESS_STATE_OR_PROVINCE)
            .addressCountry(DEFAULT_ADDRESS_COUNTRY)
            .addressZipOrPostalCode(DEFAULT_ADDRESS_ZIP_OR_POSTAL_CODE)
            .addressIsHomeOrBusiness(DEFAULT_ADDRESS_IS_HOME_OR_BUSINESS);
        return address;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .addressStreet1(UPDATED_ADDRESS_STREET_1)
            .addressStreet2(UPDATED_ADDRESS_STREET_2)
            .addressCity(UPDATED_ADDRESS_CITY)
            .addressStateOrProvince(UPDATED_ADDRESS_STATE_OR_PROVINCE)
            .addressCountry(UPDATED_ADDRESS_COUNTRY)
            .addressZipOrPostalCode(UPDATED_ADDRESS_ZIP_OR_POSTAL_CODE)
            .addressIsHomeOrBusiness(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddressStreet1()).isEqualTo(DEFAULT_ADDRESS_STREET_1);
        assertThat(testAddress.getAddressStreet2()).isEqualTo(DEFAULT_ADDRESS_STREET_2);
        assertThat(testAddress.getAddressCity()).isEqualTo(DEFAULT_ADDRESS_CITY);
        assertThat(testAddress.getAddressStateOrProvince()).isEqualTo(DEFAULT_ADDRESS_STATE_OR_PROVINCE);
        assertThat(testAddress.getAddressCountry()).isEqualTo(DEFAULT_ADDRESS_COUNTRY);
        assertThat(testAddress.getAddressZipOrPostalCode()).isEqualTo(DEFAULT_ADDRESS_ZIP_OR_POSTAL_CODE);
        assertThat(testAddress.getAddressIsHomeOrBusiness()).isEqualTo(DEFAULT_ADDRESS_IS_HOME_OR_BUSINESS);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        addressRepository.saveAndFlush(address);
        AddressDTO addressDTO = addressMapper.toDto(address);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().toString())))
            .andExpect(jsonPath("$.[*].addressStreet1").value(hasItem(DEFAULT_ADDRESS_STREET_1)))
            .andExpect(jsonPath("$.[*].addressStreet2").value(hasItem(DEFAULT_ADDRESS_STREET_2)))
            .andExpect(jsonPath("$.[*].addressCity").value(hasItem(DEFAULT_ADDRESS_CITY)))
            .andExpect(jsonPath("$.[*].addressStateOrProvince").value(hasItem(DEFAULT_ADDRESS_STATE_OR_PROVINCE)))
            .andExpect(jsonPath("$.[*].addressCountry").value(hasItem(DEFAULT_ADDRESS_COUNTRY)))
            .andExpect(jsonPath("$.[*].addressZipOrPostalCode").value(hasItem(DEFAULT_ADDRESS_ZIP_OR_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].addressIsHomeOrBusiness").value(hasItem(DEFAULT_ADDRESS_IS_HOME_OR_BUSINESS)));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().toString()))
            .andExpect(jsonPath("$.addressStreet1").value(DEFAULT_ADDRESS_STREET_1))
            .andExpect(jsonPath("$.addressStreet2").value(DEFAULT_ADDRESS_STREET_2))
            .andExpect(jsonPath("$.addressCity").value(DEFAULT_ADDRESS_CITY))
            .andExpect(jsonPath("$.addressStateOrProvince").value(DEFAULT_ADDRESS_STATE_OR_PROVINCE))
            .andExpect(jsonPath("$.addressCountry").value(DEFAULT_ADDRESS_COUNTRY))
            .andExpect(jsonPath("$.addressZipOrPostalCode").value(DEFAULT_ADDRESS_ZIP_OR_POSTAL_CODE))
            .andExpect(jsonPath("$.addressIsHomeOrBusiness").value(DEFAULT_ADDRESS_IS_HOME_OR_BUSINESS));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .addressStreet1(UPDATED_ADDRESS_STREET_1)
            .addressStreet2(UPDATED_ADDRESS_STREET_2)
            .addressCity(UPDATED_ADDRESS_CITY)
            .addressStateOrProvince(UPDATED_ADDRESS_STATE_OR_PROVINCE)
            .addressCountry(UPDATED_ADDRESS_COUNTRY)
            .addressZipOrPostalCode(UPDATED_ADDRESS_ZIP_OR_POSTAL_CODE)
            .addressIsHomeOrBusiness(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddressStreet1()).isEqualTo(UPDATED_ADDRESS_STREET_1);
        assertThat(testAddress.getAddressStreet2()).isEqualTo(UPDATED_ADDRESS_STREET_2);
        assertThat(testAddress.getAddressCity()).isEqualTo(UPDATED_ADDRESS_CITY);
        assertThat(testAddress.getAddressStateOrProvince()).isEqualTo(UPDATED_ADDRESS_STATE_OR_PROVINCE);
        assertThat(testAddress.getAddressCountry()).isEqualTo(UPDATED_ADDRESS_COUNTRY);
        assertThat(testAddress.getAddressZipOrPostalCode()).isEqualTo(UPDATED_ADDRESS_ZIP_OR_POSTAL_CODE);
        assertThat(testAddress.getAddressIsHomeOrBusiness()).isEqualTo(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .addressStreet2(UPDATED_ADDRESS_STREET_2)
            .addressCity(UPDATED_ADDRESS_CITY)
            .addressStateOrProvince(UPDATED_ADDRESS_STATE_OR_PROVINCE)
            .addressCountry(UPDATED_ADDRESS_COUNTRY)
            .addressIsHomeOrBusiness(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddressStreet1()).isEqualTo(DEFAULT_ADDRESS_STREET_1);
        assertThat(testAddress.getAddressStreet2()).isEqualTo(UPDATED_ADDRESS_STREET_2);
        assertThat(testAddress.getAddressCity()).isEqualTo(UPDATED_ADDRESS_CITY);
        assertThat(testAddress.getAddressStateOrProvince()).isEqualTo(UPDATED_ADDRESS_STATE_OR_PROVINCE);
        assertThat(testAddress.getAddressCountry()).isEqualTo(UPDATED_ADDRESS_COUNTRY);
        assertThat(testAddress.getAddressZipOrPostalCode()).isEqualTo(DEFAULT_ADDRESS_ZIP_OR_POSTAL_CODE);
        assertThat(testAddress.getAddressIsHomeOrBusiness()).isEqualTo(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .addressStreet1(UPDATED_ADDRESS_STREET_1)
            .addressStreet2(UPDATED_ADDRESS_STREET_2)
            .addressCity(UPDATED_ADDRESS_CITY)
            .addressStateOrProvince(UPDATED_ADDRESS_STATE_OR_PROVINCE)
            .addressCountry(UPDATED_ADDRESS_COUNTRY)
            .addressZipOrPostalCode(UPDATED_ADDRESS_ZIP_OR_POSTAL_CODE)
            .addressIsHomeOrBusiness(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddressStreet1()).isEqualTo(UPDATED_ADDRESS_STREET_1);
        assertThat(testAddress.getAddressStreet2()).isEqualTo(UPDATED_ADDRESS_STREET_2);
        assertThat(testAddress.getAddressCity()).isEqualTo(UPDATED_ADDRESS_CITY);
        assertThat(testAddress.getAddressStateOrProvince()).isEqualTo(UPDATED_ADDRESS_STATE_OR_PROVINCE);
        assertThat(testAddress.getAddressCountry()).isEqualTo(UPDATED_ADDRESS_COUNTRY);
        assertThat(testAddress.getAddressZipOrPostalCode()).isEqualTo(UPDATED_ADDRESS_ZIP_OR_POSTAL_CODE);
        assertThat(testAddress.getAddressIsHomeOrBusiness()).isEqualTo(UPDATED_ADDRESS_IS_HOME_OR_BUSINESS);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, address.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
