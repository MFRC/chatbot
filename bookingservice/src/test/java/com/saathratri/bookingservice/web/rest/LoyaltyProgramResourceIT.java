package com.saathratri.bookingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.bookingservice.IntegrationTest;
import com.saathratri.bookingservice.domain.LoyaltyProgram;
import com.saathratri.bookingservice.repository.LoyaltyProgramRepository;
import com.saathratri.bookingservice.service.dto.LoyaltyProgramDTO;
import com.saathratri.bookingservice.service.mapper.LoyaltyProgramMapper;
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
 * Integration tests for the {@link LoyaltyProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoyaltyProgramResourceIT {

    private static final String DEFAULT_LOYALTY_PROGRAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOYALTY_PROGRAM_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LOYALTY_PROGRAM_MEMBER = false;
    private static final Boolean UPDATED_LOYALTY_PROGRAM_MEMBER = true;

    private static final String DEFAULT_LOYALTY_PROGRAM_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LOYALTY_PROGRAM_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOYALTY_PROGRAM_TIER = "AAAAAAAAAA";
    private static final String UPDATED_LOYALTY_PROGRAM_TIER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/loyalty-programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LoyaltyProgramRepository loyaltyProgramRepository;

    @Autowired
    private LoyaltyProgramMapper loyaltyProgramMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoyaltyProgramMockMvc;

    private LoyaltyProgram loyaltyProgram;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyProgram createEntity(EntityManager em) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram()
            .loyaltyProgramName(DEFAULT_LOYALTY_PROGRAM_NAME)
            .loyaltyProgramMember(DEFAULT_LOYALTY_PROGRAM_MEMBER)
            .loyaltyProgramNumber(DEFAULT_LOYALTY_PROGRAM_NUMBER)
            .loyaltyProgramTier(DEFAULT_LOYALTY_PROGRAM_TIER);
        return loyaltyProgram;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyProgram createUpdatedEntity(EntityManager em) {
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram()
            .loyaltyProgramName(UPDATED_LOYALTY_PROGRAM_NAME)
            .loyaltyProgramMember(UPDATED_LOYALTY_PROGRAM_MEMBER)
            .loyaltyProgramNumber(UPDATED_LOYALTY_PROGRAM_NUMBER)
            .loyaltyProgramTier(UPDATED_LOYALTY_PROGRAM_TIER);
        return loyaltyProgram;
    }

    @BeforeEach
    public void initTest() {
        loyaltyProgram = createEntity(em);
    }

    @Test
    @Transactional
    void createLoyaltyProgram() throws Exception {
        int databaseSizeBeforeCreate = loyaltyProgramRepository.findAll().size();
        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);
        restLoyaltyProgramMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeCreate + 1);
        LoyaltyProgram testLoyaltyProgram = loyaltyProgramList.get(loyaltyProgramList.size() - 1);
        assertThat(testLoyaltyProgram.getLoyaltyProgramName()).isEqualTo(DEFAULT_LOYALTY_PROGRAM_NAME);
        assertThat(testLoyaltyProgram.getLoyaltyProgramMember()).isEqualTo(DEFAULT_LOYALTY_PROGRAM_MEMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramNumber()).isEqualTo(DEFAULT_LOYALTY_PROGRAM_NUMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramTier()).isEqualTo(DEFAULT_LOYALTY_PROGRAM_TIER);
    }

    @Test
    @Transactional
    void createLoyaltyProgramWithExistingId() throws Exception {
        // Create the LoyaltyProgram with an existing ID
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        int databaseSizeBeforeCreate = loyaltyProgramRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoyaltyProgramMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLoyaltyPrograms() throws Exception {
        // Initialize the database
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get all the loyaltyProgramList
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loyaltyProgram.getId().toString())))
            .andExpect(jsonPath("$.[*].loyaltyProgramName").value(hasItem(DEFAULT_LOYALTY_PROGRAM_NAME)))
            .andExpect(jsonPath("$.[*].loyaltyProgramMember").value(hasItem(DEFAULT_LOYALTY_PROGRAM_MEMBER.booleanValue())))
            .andExpect(jsonPath("$.[*].loyaltyProgramNumber").value(hasItem(DEFAULT_LOYALTY_PROGRAM_NUMBER)))
            .andExpect(jsonPath("$.[*].loyaltyProgramTier").value(hasItem(DEFAULT_LOYALTY_PROGRAM_TIER)));
    }

    @Test
    @Transactional
    void getLoyaltyProgram() throws Exception {
        // Initialize the database
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        // Get the loyaltyProgram
        restLoyaltyProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, loyaltyProgram.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loyaltyProgram.getId().toString()))
            .andExpect(jsonPath("$.loyaltyProgramName").value(DEFAULT_LOYALTY_PROGRAM_NAME))
            .andExpect(jsonPath("$.loyaltyProgramMember").value(DEFAULT_LOYALTY_PROGRAM_MEMBER.booleanValue()))
            .andExpect(jsonPath("$.loyaltyProgramNumber").value(DEFAULT_LOYALTY_PROGRAM_NUMBER))
            .andExpect(jsonPath("$.loyaltyProgramTier").value(DEFAULT_LOYALTY_PROGRAM_TIER));
    }

    @Test
    @Transactional
    void getNonExistingLoyaltyProgram() throws Exception {
        // Get the loyaltyProgram
        restLoyaltyProgramMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoyaltyProgram() throws Exception {
        // Initialize the database
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();

        // Update the loyaltyProgram
        LoyaltyProgram updatedLoyaltyProgram = loyaltyProgramRepository.findById(loyaltyProgram.getId()).get();
        // Disconnect from session so that the updates on updatedLoyaltyProgram are not directly saved in db
        em.detach(updatedLoyaltyProgram);
        updatedLoyaltyProgram
            .loyaltyProgramName(UPDATED_LOYALTY_PROGRAM_NAME)
            .loyaltyProgramMember(UPDATED_LOYALTY_PROGRAM_MEMBER)
            .loyaltyProgramNumber(UPDATED_LOYALTY_PROGRAM_NUMBER)
            .loyaltyProgramTier(UPDATED_LOYALTY_PROGRAM_TIER);
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(updatedLoyaltyProgram);

        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyProgramDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
        LoyaltyProgram testLoyaltyProgram = loyaltyProgramList.get(loyaltyProgramList.size() - 1);
        assertThat(testLoyaltyProgram.getLoyaltyProgramName()).isEqualTo(UPDATED_LOYALTY_PROGRAM_NAME);
        assertThat(testLoyaltyProgram.getLoyaltyProgramMember()).isEqualTo(UPDATED_LOYALTY_PROGRAM_MEMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramNumber()).isEqualTo(UPDATED_LOYALTY_PROGRAM_NUMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramTier()).isEqualTo(UPDATED_LOYALTY_PROGRAM_TIER);
    }

    @Test
    @Transactional
    void putNonExistingLoyaltyProgram() throws Exception {
        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();
        loyaltyProgram.setId(UUID.randomUUID());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyProgramDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoyaltyProgram() throws Exception {
        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();
        loyaltyProgram.setId(UUID.randomUUID());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoyaltyProgram() throws Exception {
        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();
        loyaltyProgram.setId(UUID.randomUUID());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoyaltyProgramWithPatch() throws Exception {
        // Initialize the database
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();

        // Update the loyaltyProgram using partial update
        LoyaltyProgram partialUpdatedLoyaltyProgram = new LoyaltyProgram();
        partialUpdatedLoyaltyProgram.setId(loyaltyProgram.getId());

        partialUpdatedLoyaltyProgram
            .loyaltyProgramName(UPDATED_LOYALTY_PROGRAM_NAME)
            .loyaltyProgramMember(UPDATED_LOYALTY_PROGRAM_MEMBER)
            .loyaltyProgramNumber(UPDATED_LOYALTY_PROGRAM_NUMBER);

        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyProgram.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoyaltyProgram))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
        LoyaltyProgram testLoyaltyProgram = loyaltyProgramList.get(loyaltyProgramList.size() - 1);
        assertThat(testLoyaltyProgram.getLoyaltyProgramName()).isEqualTo(UPDATED_LOYALTY_PROGRAM_NAME);
        assertThat(testLoyaltyProgram.getLoyaltyProgramMember()).isEqualTo(UPDATED_LOYALTY_PROGRAM_MEMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramNumber()).isEqualTo(UPDATED_LOYALTY_PROGRAM_NUMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramTier()).isEqualTo(DEFAULT_LOYALTY_PROGRAM_TIER);
    }

    @Test
    @Transactional
    void fullUpdateLoyaltyProgramWithPatch() throws Exception {
        // Initialize the database
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();

        // Update the loyaltyProgram using partial update
        LoyaltyProgram partialUpdatedLoyaltyProgram = new LoyaltyProgram();
        partialUpdatedLoyaltyProgram.setId(loyaltyProgram.getId());

        partialUpdatedLoyaltyProgram
            .loyaltyProgramName(UPDATED_LOYALTY_PROGRAM_NAME)
            .loyaltyProgramMember(UPDATED_LOYALTY_PROGRAM_MEMBER)
            .loyaltyProgramNumber(UPDATED_LOYALTY_PROGRAM_NUMBER)
            .loyaltyProgramTier(UPDATED_LOYALTY_PROGRAM_TIER);

        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyProgram.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoyaltyProgram))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
        LoyaltyProgram testLoyaltyProgram = loyaltyProgramList.get(loyaltyProgramList.size() - 1);
        assertThat(testLoyaltyProgram.getLoyaltyProgramName()).isEqualTo(UPDATED_LOYALTY_PROGRAM_NAME);
        assertThat(testLoyaltyProgram.getLoyaltyProgramMember()).isEqualTo(UPDATED_LOYALTY_PROGRAM_MEMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramNumber()).isEqualTo(UPDATED_LOYALTY_PROGRAM_NUMBER);
        assertThat(testLoyaltyProgram.getLoyaltyProgramTier()).isEqualTo(UPDATED_LOYALTY_PROGRAM_TIER);
    }

    @Test
    @Transactional
    void patchNonExistingLoyaltyProgram() throws Exception {
        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();
        loyaltyProgram.setId(UUID.randomUUID());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loyaltyProgramDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoyaltyProgram() throws Exception {
        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();
        loyaltyProgram.setId(UUID.randomUUID());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoyaltyProgram() throws Exception {
        int databaseSizeBeforeUpdate = loyaltyProgramRepository.findAll().size();
        loyaltyProgram.setId(UUID.randomUUID());

        // Create the LoyaltyProgram
        LoyaltyProgramDTO loyaltyProgramDTO = loyaltyProgramMapper.toDto(loyaltyProgram);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyProgramMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loyaltyProgramDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyProgram in the database
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoyaltyProgram() throws Exception {
        // Initialize the database
        loyaltyProgramRepository.saveAndFlush(loyaltyProgram);

        int databaseSizeBeforeDelete = loyaltyProgramRepository.findAll().size();

        // Delete the loyaltyProgram
        restLoyaltyProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, loyaltyProgram.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoyaltyProgram> loyaltyProgramList = loyaltyProgramRepository.findAll();
        assertThat(loyaltyProgramList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
