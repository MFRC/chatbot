package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.repository.FAQsRepository;
import com.saathratri.customerservice.service.criteria.FAQsCriteria;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import com.saathratri.customerservice.service.mapper.FAQsMapper;
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
 * Integration tests for the {@link FAQsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FAQsResourceIT {

    private static final String ENTITY_API_URL = "/api/fa-qs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FAQsRepository fAQsRepository;

    @Autowired
    private FAQsMapper fAQsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFAQsMockMvc;

    private FAQs fAQs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FAQs createEntity(EntityManager em) {
        FAQs fAQs = new FAQs();
        return fAQs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FAQs createUpdatedEntity(EntityManager em) {
        FAQs fAQs = new FAQs();
        return fAQs;
    }

    @BeforeEach
    public void initTest() {
        fAQs = createEntity(em);
    }

    @Test
    @Transactional
    void createFAQs() throws Exception {
        int databaseSizeBeforeCreate = fAQsRepository.findAll().size();
        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);
        restFAQsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeCreate + 1);
        FAQs testFAQs = fAQsList.get(fAQsList.size() - 1);
    }

    @Test
    @Transactional
    void createFAQsWithExistingId() throws Exception {
        // Create the FAQs with an existing ID
        fAQsRepository.saveAndFlush(fAQs);
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        int databaseSizeBeforeCreate = fAQsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFAQsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFAQs() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fAQs.getId().toString())));
    }

    @Test
    @Transactional
    void getFAQs() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get the fAQs
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL_ID, fAQs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fAQs.getId().toString()));
    }

    @Test
    @Transactional
    void getFAQsByIdFiltering() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        UUID id = fAQs.getId();

        defaultFAQsShouldBeFound("id.equals=" + id);
        defaultFAQsShouldNotBeFound("id.notEquals=" + id);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFAQsShouldBeFound(String filter) throws Exception {
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fAQs.getId().toString())));

        // Check, that the count call also returns 1
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFAQsShouldNotBeFound(String filter) throws Exception {
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFAQs() throws Exception {
        // Get the fAQs
        restFAQsMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFAQs() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();

        // Update the fAQs
        FAQs updatedFAQs = fAQsRepository.findById(fAQs.getId()).get();
        // Disconnect from session so that the updates on updatedFAQs are not directly saved in db
        em.detach(updatedFAQs);
        FAQsDTO fAQsDTO = fAQsMapper.toDto(updatedFAQs);

        restFAQsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fAQsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isOk());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
        FAQs testFAQs = fAQsList.get(fAQsList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingFAQs() throws Exception {
        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();
        fAQs.setId(UUID.randomUUID());

        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFAQsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fAQsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFAQs() throws Exception {
        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();
        fAQs.setId(UUID.randomUUID());

        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFAQsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFAQs() throws Exception {
        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();
        fAQs.setId(UUID.randomUUID());

        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFAQsMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFAQsWithPatch() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();

        // Update the fAQs using partial update
        FAQs partialUpdatedFAQs = new FAQs();
        partialUpdatedFAQs.setId(fAQs.getId());

        restFAQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFAQs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFAQs))
            )
            .andExpect(status().isOk());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
        FAQs testFAQs = fAQsList.get(fAQsList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateFAQsWithPatch() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();

        // Update the fAQs using partial update
        FAQs partialUpdatedFAQs = new FAQs();
        partialUpdatedFAQs.setId(fAQs.getId());

        restFAQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFAQs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFAQs))
            )
            .andExpect(status().isOk());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
        FAQs testFAQs = fAQsList.get(fAQsList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingFAQs() throws Exception {
        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();
        fAQs.setId(UUID.randomUUID());

        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFAQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fAQsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFAQs() throws Exception {
        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();
        fAQs.setId(UUID.randomUUID());

        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFAQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFAQs() throws Exception {
        int databaseSizeBeforeUpdate = fAQsRepository.findAll().size();
        fAQs.setId(UUID.randomUUID());

        // Create the FAQs
        FAQsDTO fAQsDTO = fAQsMapper.toDto(fAQs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFAQsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fAQsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FAQs in the database
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFAQs() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        int databaseSizeBeforeDelete = fAQsRepository.findAll().size();

        // Delete the fAQs
        restFAQsMockMvc
            .perform(delete(ENTITY_API_URL_ID, fAQs.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FAQs> fAQsList = fAQsRepository.findAll();
        assertThat(fAQsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
