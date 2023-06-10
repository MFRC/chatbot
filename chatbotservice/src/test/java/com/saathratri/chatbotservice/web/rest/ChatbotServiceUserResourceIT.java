package com.saathratri.chatbotservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.chatbotservice.IntegrationTest;
import com.saathratri.chatbotservice.domain.ChatbotServiceUser;
import com.saathratri.chatbotservice.repository.ChatbotServiceUserRepository;
import com.saathratri.chatbotservice.service.criteria.ChatbotServiceUserCriteria;
import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.chatbotservice.service.mapper.ChatbotServiceUserMapper;
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
 * Integration tests for the {@link ChatbotServiceUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatbotServiceUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chatbot-service-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChatbotServiceUserRepository chatbotServiceUserRepository;

    @Autowired
    private ChatbotServiceUserMapper chatbotServiceUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatbotServiceUserMockMvc;

    private ChatbotServiceUser chatbotServiceUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatbotServiceUser createEntity(EntityManager em) {
        ChatbotServiceUser chatbotServiceUser = new ChatbotServiceUser().name(DEFAULT_NAME).email(DEFAULT_EMAIL);
        return chatbotServiceUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatbotServiceUser createUpdatedEntity(EntityManager em) {
        ChatbotServiceUser chatbotServiceUser = new ChatbotServiceUser().name(UPDATED_NAME).email(UPDATED_EMAIL);
        return chatbotServiceUser;
    }

    @BeforeEach
    public void initTest() {
        chatbotServiceUser = createEntity(em);
    }

    @Test
    @Transactional
    void createChatbotServiceUser() throws Exception {
        int databaseSizeBeforeCreate = chatbotServiceUserRepository.findAll().size();
        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);
        restChatbotServiceUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeCreate + 1);
        ChatbotServiceUser testChatbotServiceUser = chatbotServiceUserList.get(chatbotServiceUserList.size() - 1);
        assertThat(testChatbotServiceUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChatbotServiceUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createChatbotServiceUserWithExistingId() throws Exception {
        // Create the ChatbotServiceUser with an existing ID
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        int databaseSizeBeforeCreate = chatbotServiceUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatbotServiceUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatbotServiceUserRepository.findAll().size();
        // set the field null
        chatbotServiceUser.setName(null);

        // Create the ChatbotServiceUser, which fails.
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        restChatbotServiceUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatbotServiceUserRepository.findAll().size();
        // set the field null
        chatbotServiceUser.setEmail(null);

        // Create the ChatbotServiceUser, which fails.
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        restChatbotServiceUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsers() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList
        restChatbotServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatbotServiceUser.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getChatbotServiceUser() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get the chatbotServiceUser
        restChatbotServiceUserMockMvc
            .perform(get(ENTITY_API_URL_ID, chatbotServiceUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatbotServiceUser.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getChatbotServiceUsersByIdFiltering() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        UUID id = chatbotServiceUser.getId();

        defaultChatbotServiceUserShouldBeFound("id.equals=" + id);
        defaultChatbotServiceUserShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where name equals to DEFAULT_NAME
        defaultChatbotServiceUserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the chatbotServiceUserList where name equals to UPDATED_NAME
        defaultChatbotServiceUserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChatbotServiceUserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the chatbotServiceUserList where name equals to UPDATED_NAME
        defaultChatbotServiceUserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where name is not null
        defaultChatbotServiceUserShouldBeFound("name.specified=true");

        // Get all the chatbotServiceUserList where name is null
        defaultChatbotServiceUserShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where name contains DEFAULT_NAME
        defaultChatbotServiceUserShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the chatbotServiceUserList where name contains UPDATED_NAME
        defaultChatbotServiceUserShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where name does not contain DEFAULT_NAME
        defaultChatbotServiceUserShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the chatbotServiceUserList where name does not contain UPDATED_NAME
        defaultChatbotServiceUserShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where email equals to DEFAULT_EMAIL
        defaultChatbotServiceUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the chatbotServiceUserList where email equals to UPDATED_EMAIL
        defaultChatbotServiceUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultChatbotServiceUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the chatbotServiceUserList where email equals to UPDATED_EMAIL
        defaultChatbotServiceUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where email is not null
        defaultChatbotServiceUserShouldBeFound("email.specified=true");

        // Get all the chatbotServiceUserList where email is null
        defaultChatbotServiceUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where email contains DEFAULT_EMAIL
        defaultChatbotServiceUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the chatbotServiceUserList where email contains UPDATED_EMAIL
        defaultChatbotServiceUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllChatbotServiceUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        // Get all the chatbotServiceUserList where email does not contain DEFAULT_EMAIL
        defaultChatbotServiceUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the chatbotServiceUserList where email does not contain UPDATED_EMAIL
        defaultChatbotServiceUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatbotServiceUserShouldBeFound(String filter) throws Exception {
        restChatbotServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatbotServiceUser.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restChatbotServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatbotServiceUserShouldNotBeFound(String filter) throws Exception {
        restChatbotServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatbotServiceUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChatbotServiceUser() throws Exception {
        // Get the chatbotServiceUser
        restChatbotServiceUserMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatbotServiceUser() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();

        // Update the chatbotServiceUser
        ChatbotServiceUser updatedChatbotServiceUser = chatbotServiceUserRepository.findById(chatbotServiceUser.getId()).get();
        // Disconnect from session so that the updates on updatedChatbotServiceUser are not directly saved in db
        em.detach(updatedChatbotServiceUser);
        updatedChatbotServiceUser.name(UPDATED_NAME).email(UPDATED_EMAIL);
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(updatedChatbotServiceUser);

        restChatbotServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatbotServiceUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
        ChatbotServiceUser testChatbotServiceUser = chatbotServiceUserList.get(chatbotServiceUserList.size() - 1);
        assertThat(testChatbotServiceUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChatbotServiceUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingChatbotServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();
        chatbotServiceUser.setId(UUID.randomUUID());

        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatbotServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatbotServiceUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatbotServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();
        chatbotServiceUser.setId(UUID.randomUUID());

        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatbotServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();
        chatbotServiceUser.setId(UUID.randomUUID());

        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotServiceUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatbotServiceUserWithPatch() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();

        // Update the chatbotServiceUser using partial update
        ChatbotServiceUser partialUpdatedChatbotServiceUser = new ChatbotServiceUser();
        partialUpdatedChatbotServiceUser.setId(chatbotServiceUser.getId());

        partialUpdatedChatbotServiceUser.email(UPDATED_EMAIL);

        restChatbotServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatbotServiceUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChatbotServiceUser))
            )
            .andExpect(status().isOk());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
        ChatbotServiceUser testChatbotServiceUser = chatbotServiceUserList.get(chatbotServiceUserList.size() - 1);
        assertThat(testChatbotServiceUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChatbotServiceUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateChatbotServiceUserWithPatch() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();

        // Update the chatbotServiceUser using partial update
        ChatbotServiceUser partialUpdatedChatbotServiceUser = new ChatbotServiceUser();
        partialUpdatedChatbotServiceUser.setId(chatbotServiceUser.getId());

        partialUpdatedChatbotServiceUser.name(UPDATED_NAME).email(UPDATED_EMAIL);

        restChatbotServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatbotServiceUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChatbotServiceUser))
            )
            .andExpect(status().isOk());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
        ChatbotServiceUser testChatbotServiceUser = chatbotServiceUserList.get(chatbotServiceUserList.size() - 1);
        assertThat(testChatbotServiceUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChatbotServiceUser.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingChatbotServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();
        chatbotServiceUser.setId(UUID.randomUUID());

        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatbotServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatbotServiceUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatbotServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();
        chatbotServiceUser.setId(UUID.randomUUID());

        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatbotServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = chatbotServiceUserRepository.findAll().size();
        chatbotServiceUser.setId(UUID.randomUUID());

        // Create the ChatbotServiceUser
        ChatbotServiceUserDTO chatbotServiceUserDTO = chatbotServiceUserMapper.toDto(chatbotServiceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotServiceUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chatbotServiceUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatbotServiceUser in the database
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatbotServiceUser() throws Exception {
        // Initialize the database
        chatbotServiceUserRepository.saveAndFlush(chatbotServiceUser);

        int databaseSizeBeforeDelete = chatbotServiceUserRepository.findAll().size();

        // Delete the chatbotServiceUser
        restChatbotServiceUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatbotServiceUser.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatbotServiceUser> chatbotServiceUserList = chatbotServiceUserRepository.findAll();
        assertThat(chatbotServiceUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
