package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.repository.EndRepository;
import com.saathratri.customerservice.service.criteria.EndCriteria;
import com.saathratri.customerservice.service.dto.EndDTO;
import com.saathratri.customerservice.service.mapper.EndMapper;
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
 * Integration tests for the {@link EndResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EndResourceIT {

    private static final String ENTITY_API_URL = "/api/ends";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EndRepository endRepository;

    @Autowired
    private EndMapper endMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEndMockMvc;

    private End end;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static End createEntity(EntityManager em) {
        End end = new End();
        return end;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static End createUpdatedEntity(EntityManager em) {
        End end = new End();
        return end;
    }

    @BeforeEach
    public void initTest() {
        end = createEntity(em);
    }

    @Test
    @Transactional
    void createEnd() throws Exception {
        int databaseSizeBeforeCreate = endRepository.findAll().size();
        // Create the End
        EndDTO endDTO = endMapper.toDto(end);
        restEndMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isCreated());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeCreate + 1);
        End testEnd = endList.get(endList.size() - 1);
    }

    @Test
    @Transactional
    void createEndWithExistingId() throws Exception {
        // Create the End with an existing ID
        endRepository.saveAndFlush(end);
        EndDTO endDTO = endMapper.toDto(end);

        int databaseSizeBeforeCreate = endRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEndMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnds() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList
        restEndMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(end.getId().toString())));
    }

    @Test
    @Transactional
    void getEnd() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get the end
        restEndMockMvc
            .perform(get(ENTITY_API_URL_ID, end.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(end.getId().toString()));
    }

    @Test
    @Transactional
    void getEndsByIdFiltering() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        UUID id = end.getId();

        defaultEndShouldBeFound("id.equals=" + id);
        defaultEndShouldNotBeFound("id.notEquals=" + id);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEndShouldBeFound(String filter) throws Exception {
        restEndMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(end.getId().toString())));

        // Check, that the count call also returns 1
        restEndMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEndShouldNotBeFound(String filter) throws Exception {
        restEndMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEndMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnd() throws Exception {
        // Get the end
        restEndMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnd() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        int databaseSizeBeforeUpdate = endRepository.findAll().size();

        // Update the end
        End updatedEnd = endRepository.findById(end.getId()).get();
        // Disconnect from session so that the updates on updatedEnd are not directly saved in db
        em.detach(updatedEnd);
        EndDTO endDTO = endMapper.toDto(updatedEnd);

        restEndMockMvc
            .perform(
                put(ENTITY_API_URL_ID, endDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isOk());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
        End testEnd = endList.get(endList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingEnd() throws Exception {
        int databaseSizeBeforeUpdate = endRepository.findAll().size();
        end.setId(UUID.randomUUID());

        // Create the End
        EndDTO endDTO = endMapper.toDto(end);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEndMockMvc
            .perform(
                put(ENTITY_API_URL_ID, endDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnd() throws Exception {
        int databaseSizeBeforeUpdate = endRepository.findAll().size();
        end.setId(UUID.randomUUID());

        // Create the End
        EndDTO endDTO = endMapper.toDto(end);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnd() throws Exception {
        int databaseSizeBeforeUpdate = endRepository.findAll().size();
        end.setId(UUID.randomUUID());

        // Create the End
        EndDTO endDTO = endMapper.toDto(end);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEndWithPatch() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        int databaseSizeBeforeUpdate = endRepository.findAll().size();

        // Update the end using partial update
        End partialUpdatedEnd = new End();
        partialUpdatedEnd.setId(end.getId());

        restEndMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnd))
            )
            .andExpect(status().isOk());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
        End testEnd = endList.get(endList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateEndWithPatch() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        int databaseSizeBeforeUpdate = endRepository.findAll().size();

        // Update the end using partial update
        End partialUpdatedEnd = new End();
        partialUpdatedEnd.setId(end.getId());

        restEndMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnd.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnd))
            )
            .andExpect(status().isOk());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
        End testEnd = endList.get(endList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingEnd() throws Exception {
        int databaseSizeBeforeUpdate = endRepository.findAll().size();
        end.setId(UUID.randomUUID());

        // Create the End
        EndDTO endDTO = endMapper.toDto(end);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEndMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, endDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnd() throws Exception {
        int databaseSizeBeforeUpdate = endRepository.findAll().size();
        end.setId(UUID.randomUUID());

        // Create the End
        EndDTO endDTO = endMapper.toDto(end);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnd() throws Exception {
        int databaseSizeBeforeUpdate = endRepository.findAll().size();
        end.setId(UUID.randomUUID());

        // Create the End
        EndDTO endDTO = endMapper.toDto(end);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(endDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the End in the database
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnd() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        int databaseSizeBeforeDelete = endRepository.findAll().size();

        // Delete the end
        restEndMockMvc
            .perform(delete(ENTITY_API_URL_ID, end.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<End> endList = endRepository.findAll();
        assertThat(endList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
