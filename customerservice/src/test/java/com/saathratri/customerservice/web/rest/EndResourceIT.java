package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.domain.Report;
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

    private static final String DEFAULT_CLOSE_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_CLOSE_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_MORE_HELP = "AAAAAAAAAA";
    private static final String UPDATED_MORE_HELP = "BBBBBBBBBB";

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
        End end = new End().closeMessage(DEFAULT_CLOSE_MESSAGE).moreHelp(DEFAULT_MORE_HELP);
        return end;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static End createUpdatedEntity(EntityManager em) {
        End end = new End().closeMessage(UPDATED_CLOSE_MESSAGE).moreHelp(UPDATED_MORE_HELP);
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
        assertThat(testEnd.getCloseMessage()).isEqualTo(DEFAULT_CLOSE_MESSAGE);
        assertThat(testEnd.getMoreHelp()).isEqualTo(DEFAULT_MORE_HELP);
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(end.getId().toString())))
            .andExpect(jsonPath("$.[*].closeMessage").value(hasItem(DEFAULT_CLOSE_MESSAGE)))
            .andExpect(jsonPath("$.[*].moreHelp").value(hasItem(DEFAULT_MORE_HELP)));
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
            .andExpect(jsonPath("$.id").value(end.getId().toString()))
            .andExpect(jsonPath("$.closeMessage").value(DEFAULT_CLOSE_MESSAGE))
            .andExpect(jsonPath("$.moreHelp").value(DEFAULT_MORE_HELP));
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

    @Test
    @Transactional
    void getAllEndsByCloseMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where closeMessage equals to DEFAULT_CLOSE_MESSAGE
        defaultEndShouldBeFound("closeMessage.equals=" + DEFAULT_CLOSE_MESSAGE);

        // Get all the endList where closeMessage equals to UPDATED_CLOSE_MESSAGE
        defaultEndShouldNotBeFound("closeMessage.equals=" + UPDATED_CLOSE_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEndsByCloseMessageIsInShouldWork() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where closeMessage in DEFAULT_CLOSE_MESSAGE or UPDATED_CLOSE_MESSAGE
        defaultEndShouldBeFound("closeMessage.in=" + DEFAULT_CLOSE_MESSAGE + "," + UPDATED_CLOSE_MESSAGE);

        // Get all the endList where closeMessage equals to UPDATED_CLOSE_MESSAGE
        defaultEndShouldNotBeFound("closeMessage.in=" + UPDATED_CLOSE_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEndsByCloseMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where closeMessage is not null
        defaultEndShouldBeFound("closeMessage.specified=true");

        // Get all the endList where closeMessage is null
        defaultEndShouldNotBeFound("closeMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllEndsByCloseMessageContainsSomething() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where closeMessage contains DEFAULT_CLOSE_MESSAGE
        defaultEndShouldBeFound("closeMessage.contains=" + DEFAULT_CLOSE_MESSAGE);

        // Get all the endList where closeMessage contains UPDATED_CLOSE_MESSAGE
        defaultEndShouldNotBeFound("closeMessage.contains=" + UPDATED_CLOSE_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEndsByCloseMessageNotContainsSomething() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where closeMessage does not contain DEFAULT_CLOSE_MESSAGE
        defaultEndShouldNotBeFound("closeMessage.doesNotContain=" + DEFAULT_CLOSE_MESSAGE);

        // Get all the endList where closeMessage does not contain UPDATED_CLOSE_MESSAGE
        defaultEndShouldBeFound("closeMessage.doesNotContain=" + UPDATED_CLOSE_MESSAGE);
    }

    @Test
    @Transactional
    void getAllEndsByMoreHelpIsEqualToSomething() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where moreHelp equals to DEFAULT_MORE_HELP
        defaultEndShouldBeFound("moreHelp.equals=" + DEFAULT_MORE_HELP);

        // Get all the endList where moreHelp equals to UPDATED_MORE_HELP
        defaultEndShouldNotBeFound("moreHelp.equals=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllEndsByMoreHelpIsInShouldWork() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where moreHelp in DEFAULT_MORE_HELP or UPDATED_MORE_HELP
        defaultEndShouldBeFound("moreHelp.in=" + DEFAULT_MORE_HELP + "," + UPDATED_MORE_HELP);

        // Get all the endList where moreHelp equals to UPDATED_MORE_HELP
        defaultEndShouldNotBeFound("moreHelp.in=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllEndsByMoreHelpIsNullOrNotNull() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where moreHelp is not null
        defaultEndShouldBeFound("moreHelp.specified=true");

        // Get all the endList where moreHelp is null
        defaultEndShouldNotBeFound("moreHelp.specified=false");
    }

    @Test
    @Transactional
    void getAllEndsByMoreHelpContainsSomething() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where moreHelp contains DEFAULT_MORE_HELP
        defaultEndShouldBeFound("moreHelp.contains=" + DEFAULT_MORE_HELP);

        // Get all the endList where moreHelp contains UPDATED_MORE_HELP
        defaultEndShouldNotBeFound("moreHelp.contains=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllEndsByMoreHelpNotContainsSomething() throws Exception {
        // Initialize the database
        endRepository.saveAndFlush(end);

        // Get all the endList where moreHelp does not contain DEFAULT_MORE_HELP
        defaultEndShouldNotBeFound("moreHelp.doesNotContain=" + DEFAULT_MORE_HELP);

        // Get all the endList where moreHelp does not contain UPDATED_MORE_HELP
        defaultEndShouldBeFound("moreHelp.doesNotContain=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllEndsByReportIsEqualToSomething() throws Exception {
        Report report;
        if (TestUtil.findAll(em, Report.class).isEmpty()) {
            endRepository.saveAndFlush(end);
            report = ReportResourceIT.createEntity(em);
        } else {
            report = TestUtil.findAll(em, Report.class).get(0);
        }
        em.persist(report);
        em.flush();
        end.setReport(report);
        endRepository.saveAndFlush(end);
        UUID reportId = report.getId();

        // Get all the endList where report equals to reportId
        defaultEndShouldBeFound("reportId.equals=" + reportId);

        // Get all the endList where report equals to UUID.randomUUID()
        defaultEndShouldNotBeFound("reportId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllEndsByConversationIsEqualToSomething() throws Exception {
        Conversation conversation;
        if (TestUtil.findAll(em, Conversation.class).isEmpty()) {
            endRepository.saveAndFlush(end);
            conversation = ConversationResourceIT.createEntity(em);
        } else {
            conversation = TestUtil.findAll(em, Conversation.class).get(0);
        }
        em.persist(conversation);
        em.flush();
        end.setConversation(conversation);
        conversation.setEnd(end);
        endRepository.saveAndFlush(end);
        UUID conversationId = conversation.getId();

        // Get all the endList where conversation equals to conversationId
        defaultEndShouldBeFound("conversationId.equals=" + conversationId);

        // Get all the endList where conversation equals to UUID.randomUUID()
        defaultEndShouldNotBeFound("conversationId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEndShouldBeFound(String filter) throws Exception {
        restEndMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(end.getId().toString())))
            .andExpect(jsonPath("$.[*].closeMessage").value(hasItem(DEFAULT_CLOSE_MESSAGE)))
            .andExpect(jsonPath("$.[*].moreHelp").value(hasItem(DEFAULT_MORE_HELP)));

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
        updatedEnd.closeMessage(UPDATED_CLOSE_MESSAGE).moreHelp(UPDATED_MORE_HELP);
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
        assertThat(testEnd.getCloseMessage()).isEqualTo(UPDATED_CLOSE_MESSAGE);
        assertThat(testEnd.getMoreHelp()).isEqualTo(UPDATED_MORE_HELP);
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

        partialUpdatedEnd.closeMessage(UPDATED_CLOSE_MESSAGE);

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
        assertThat(testEnd.getCloseMessage()).isEqualTo(UPDATED_CLOSE_MESSAGE);
        assertThat(testEnd.getMoreHelp()).isEqualTo(DEFAULT_MORE_HELP);
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

        partialUpdatedEnd.closeMessage(UPDATED_CLOSE_MESSAGE).moreHelp(UPDATED_MORE_HELP);

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
        assertThat(testEnd.getCloseMessage()).isEqualTo(UPDATED_CLOSE_MESSAGE);
        assertThat(testEnd.getMoreHelp()).isEqualTo(UPDATED_MORE_HELP);
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
