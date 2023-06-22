package com.saathratri.chatbotservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.chatbotservice.IntegrationTest;
import com.saathratri.chatbotservice.domain.ChatSession;
import com.saathratri.chatbotservice.domain.User;
import com.saathratri.chatbotservice.repository.ChatSessionRepository;
import com.saathratri.chatbotservice.service.criteria.ChatSessionCriteria;
import com.saathratri.chatbotservice.service.dto.ChatSessionDTO;
import com.saathratri.chatbotservice.service.mapper.ChatSessionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ChatSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatSessionResourceIT {

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/chat-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatSessionMockMvc;

    private ChatSession chatSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatSession createEntity(EntityManager em) {
        ChatSession chatSession = new ChatSession().startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
        return chatSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatSession createUpdatedEntity(EntityManager em) {
        ChatSession chatSession = new ChatSession().startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        return chatSession;
    }

    @BeforeEach
    public void initTest() {
        chatSession = createEntity(em);
    }

    @Test
    @Transactional
    void createChatSession() throws Exception {
        int databaseSizeBeforeCreate = chatSessionRepository.findAll().size();
        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);
        restChatSessionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeCreate + 1);
        ChatSession testChatSession = chatSessionList.get(chatSessionList.size() - 1);
        assertThat(testChatSession.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testChatSession.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void createChatSessionWithExistingId() throws Exception {
        // Create the ChatSession with an existing ID
        chatSessionRepository.saveAndFlush(chatSession);
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        int databaseSizeBeforeCreate = chatSessionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatSessionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatSessionRepository.findAll().size();
        // set the field null
        chatSession.setStartTime(null);

        // Create the ChatSession, which fails.
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        restChatSessionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatSessions() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatSession.getId().toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    void getChatSession() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get the chatSession
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, chatSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatSession.getId().toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    void getChatSessionsByIdFiltering() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        UUID id = chatSession.getId();

        defaultChatSessionShouldBeFound("id.equals=" + id);
        defaultChatSessionShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllChatSessionsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList where startTime equals to DEFAULT_START_TIME
        defaultChatSessionShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the chatSessionList where startTime equals to UPDATED_START_TIME
        defaultChatSessionShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllChatSessionsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultChatSessionShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the chatSessionList where startTime equals to UPDATED_START_TIME
        defaultChatSessionShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllChatSessionsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList where startTime is not null
        defaultChatSessionShouldBeFound("startTime.specified=true");

        // Get all the chatSessionList where startTime is null
        defaultChatSessionShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllChatSessionsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList where endTime equals to DEFAULT_END_TIME
        defaultChatSessionShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the chatSessionList where endTime equals to UPDATED_END_TIME
        defaultChatSessionShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllChatSessionsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultChatSessionShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the chatSessionList where endTime equals to UPDATED_END_TIME
        defaultChatSessionShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllChatSessionsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        // Get all the chatSessionList where endTime is not null
        defaultChatSessionShouldBeFound("endTime.specified=true");

        // Get all the chatSessionList where endTime is null
        defaultChatSessionShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllChatSessionsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            chatSessionRepository.saveAndFlush(chatSession);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        chatSession.setUser(user);
        chatSessionRepository.saveAndFlush(chatSession);
        String userId = user.getId();

        // Get all the chatSessionList where user equals to userId
        defaultChatSessionShouldBeFound("userId.equals=" + userId);

        // Get all the chatSessionList where user equals to "invalid-id"
        defaultChatSessionShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatSessionShouldBeFound(String filter) throws Exception {
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatSession.getId().toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));

        // Check, that the count call also returns 1
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatSessionShouldNotBeFound(String filter) throws Exception {
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChatSession() throws Exception {
        // Get the chatSession
        restChatSessionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatSession() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();

        // Update the chatSession
        ChatSession updatedChatSession = chatSessionRepository.findById(chatSession.getId()).get();
        // Disconnect from session so that the updates on updatedChatSession are not directly saved in db
        em.detach(updatedChatSession);
        updatedChatSession.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(updatedChatSession);

        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatSessionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
        ChatSession testChatSession = chatSessionList.get(chatSessionList.size() - 1);
        assertThat(testChatSession.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testChatSession.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void putNonExistingChatSession() throws Exception {
        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();
        chatSession.setId(UUID.randomUUID());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatSessionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatSession() throws Exception {
        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();
        chatSession.setId(UUID.randomUUID());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatSession() throws Exception {
        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();
        chatSession.setId(UUID.randomUUID());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatSessionWithPatch() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();

        // Update the chatSession using partial update
        ChatSession partialUpdatedChatSession = new ChatSession();
        partialUpdatedChatSession.setId(chatSession.getId());

        partialUpdatedChatSession.startTime(UPDATED_START_TIME);

        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatSession.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChatSession))
            )
            .andExpect(status().isOk());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
        ChatSession testChatSession = chatSessionList.get(chatSessionList.size() - 1);
        assertThat(testChatSession.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testChatSession.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void fullUpdateChatSessionWithPatch() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();

        // Update the chatSession using partial update
        ChatSession partialUpdatedChatSession = new ChatSession();
        partialUpdatedChatSession.setId(chatSession.getId());

        partialUpdatedChatSession.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatSession.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChatSession))
            )
            .andExpect(status().isOk());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
        ChatSession testChatSession = chatSessionList.get(chatSessionList.size() - 1);
        assertThat(testChatSession.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testChatSession.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingChatSession() throws Exception {
        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();
        chatSession.setId(UUID.randomUUID());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatSessionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatSession() throws Exception {
        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();
        chatSession.setId(UUID.randomUUID());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatSession() throws Exception {
        int databaseSizeBeforeUpdate = chatSessionRepository.findAll().size();
        chatSession.setId(UUID.randomUUID());

        // Create the ChatSession
        ChatSessionDTO chatSessionDTO = chatSessionMapper.toDto(chatSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatSessionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatSessionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatSession in the database
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatSession() throws Exception {
        // Initialize the database
        chatSessionRepository.saveAndFlush(chatSession);

        int databaseSizeBeforeDelete = chatSessionRepository.findAll().size();

        // Delete the chatSession
        restChatSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatSession.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatSession> chatSessionList = chatSessionRepository.findAll();
        assertThat(chatSessionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
