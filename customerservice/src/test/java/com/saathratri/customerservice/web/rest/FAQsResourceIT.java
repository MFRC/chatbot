package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.CustomerService;
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

    private static final String DEFAULT_ANSWERS = "AAAAAAAAAA";
    private static final String UPDATED_ANSWERS = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_WORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEY_WORDS = "BBBBBBBBBB";

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
        FAQs fAQs = new FAQs().answers(DEFAULT_ANSWERS).question(DEFAULT_QUESTION).keyWords(DEFAULT_KEY_WORDS);
        return fAQs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FAQs createUpdatedEntity(EntityManager em) {
        FAQs fAQs = new FAQs().answers(UPDATED_ANSWERS).question(UPDATED_QUESTION).keyWords(UPDATED_KEY_WORDS);
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
        assertThat(testFAQs.getAnswers()).isEqualTo(DEFAULT_ANSWERS);
        assertThat(testFAQs.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testFAQs.getKeyWords()).isEqualTo(DEFAULT_KEY_WORDS);
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(fAQs.getId().toString())))
            .andExpect(jsonPath("$.[*].answers").value(hasItem(DEFAULT_ANSWERS)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].keyWords").value(hasItem(DEFAULT_KEY_WORDS)));
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
            .andExpect(jsonPath("$.id").value(fAQs.getId().toString()))
            .andExpect(jsonPath("$.answers").value(DEFAULT_ANSWERS))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.keyWords").value(DEFAULT_KEY_WORDS));
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

    @Test
    @Transactional
    void getAllFAQsByAnswersIsEqualToSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where answers equals to DEFAULT_ANSWERS
        defaultFAQsShouldBeFound("answers.equals=" + DEFAULT_ANSWERS);

        // Get all the fAQsList where answers equals to UPDATED_ANSWERS
        defaultFAQsShouldNotBeFound("answers.equals=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllFAQsByAnswersIsInShouldWork() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where answers in DEFAULT_ANSWERS or UPDATED_ANSWERS
        defaultFAQsShouldBeFound("answers.in=" + DEFAULT_ANSWERS + "," + UPDATED_ANSWERS);

        // Get all the fAQsList where answers equals to UPDATED_ANSWERS
        defaultFAQsShouldNotBeFound("answers.in=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllFAQsByAnswersIsNullOrNotNull() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where answers is not null
        defaultFAQsShouldBeFound("answers.specified=true");

        // Get all the fAQsList where answers is null
        defaultFAQsShouldNotBeFound("answers.specified=false");
    }

    @Test
    @Transactional
    void getAllFAQsByAnswersContainsSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where answers contains DEFAULT_ANSWERS
        defaultFAQsShouldBeFound("answers.contains=" + DEFAULT_ANSWERS);

        // Get all the fAQsList where answers contains UPDATED_ANSWERS
        defaultFAQsShouldNotBeFound("answers.contains=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllFAQsByAnswersNotContainsSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where answers does not contain DEFAULT_ANSWERS
        defaultFAQsShouldNotBeFound("answers.doesNotContain=" + DEFAULT_ANSWERS);

        // Get all the fAQsList where answers does not contain UPDATED_ANSWERS
        defaultFAQsShouldBeFound("answers.doesNotContain=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllFAQsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where question equals to DEFAULT_QUESTION
        defaultFAQsShouldBeFound("question.equals=" + DEFAULT_QUESTION);

        // Get all the fAQsList where question equals to UPDATED_QUESTION
        defaultFAQsShouldNotBeFound("question.equals=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllFAQsByQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where question in DEFAULT_QUESTION or UPDATED_QUESTION
        defaultFAQsShouldBeFound("question.in=" + DEFAULT_QUESTION + "," + UPDATED_QUESTION);

        // Get all the fAQsList where question equals to UPDATED_QUESTION
        defaultFAQsShouldNotBeFound("question.in=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllFAQsByQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where question is not null
        defaultFAQsShouldBeFound("question.specified=true");

        // Get all the fAQsList where question is null
        defaultFAQsShouldNotBeFound("question.specified=false");
    }

    @Test
    @Transactional
    void getAllFAQsByQuestionContainsSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where question contains DEFAULT_QUESTION
        defaultFAQsShouldBeFound("question.contains=" + DEFAULT_QUESTION);

        // Get all the fAQsList where question contains UPDATED_QUESTION
        defaultFAQsShouldNotBeFound("question.contains=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllFAQsByQuestionNotContainsSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where question does not contain DEFAULT_QUESTION
        defaultFAQsShouldNotBeFound("question.doesNotContain=" + DEFAULT_QUESTION);

        // Get all the fAQsList where question does not contain UPDATED_QUESTION
        defaultFAQsShouldBeFound("question.doesNotContain=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllFAQsByKeyWordsIsEqualToSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where keyWords equals to DEFAULT_KEY_WORDS
        defaultFAQsShouldBeFound("keyWords.equals=" + DEFAULT_KEY_WORDS);

        // Get all the fAQsList where keyWords equals to UPDATED_KEY_WORDS
        defaultFAQsShouldNotBeFound("keyWords.equals=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllFAQsByKeyWordsIsInShouldWork() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where keyWords in DEFAULT_KEY_WORDS or UPDATED_KEY_WORDS
        defaultFAQsShouldBeFound("keyWords.in=" + DEFAULT_KEY_WORDS + "," + UPDATED_KEY_WORDS);

        // Get all the fAQsList where keyWords equals to UPDATED_KEY_WORDS
        defaultFAQsShouldNotBeFound("keyWords.in=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllFAQsByKeyWordsIsNullOrNotNull() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where keyWords is not null
        defaultFAQsShouldBeFound("keyWords.specified=true");

        // Get all the fAQsList where keyWords is null
        defaultFAQsShouldNotBeFound("keyWords.specified=false");
    }

    @Test
    @Transactional
    void getAllFAQsByKeyWordsContainsSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where keyWords contains DEFAULT_KEY_WORDS
        defaultFAQsShouldBeFound("keyWords.contains=" + DEFAULT_KEY_WORDS);

        // Get all the fAQsList where keyWords contains UPDATED_KEY_WORDS
        defaultFAQsShouldNotBeFound("keyWords.contains=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllFAQsByKeyWordsNotContainsSomething() throws Exception {
        // Initialize the database
        fAQsRepository.saveAndFlush(fAQs);

        // Get all the fAQsList where keyWords does not contain DEFAULT_KEY_WORDS
        defaultFAQsShouldNotBeFound("keyWords.doesNotContain=" + DEFAULT_KEY_WORDS);

        // Get all the fAQsList where keyWords does not contain UPDATED_KEY_WORDS
        defaultFAQsShouldBeFound("keyWords.doesNotContain=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllFAQsByConversationIsEqualToSomething() throws Exception {
        Conversation conversation;
        if (TestUtil.findAll(em, Conversation.class).isEmpty()) {
            fAQsRepository.saveAndFlush(fAQs);
            conversation = ConversationResourceIT.createEntity(em);
        } else {
            conversation = TestUtil.findAll(em, Conversation.class).get(0);
        }
        em.persist(conversation);
        em.flush();
        fAQs.setConversation(conversation);
        fAQsRepository.saveAndFlush(fAQs);
        UUID conversationId = conversation.getId();

        // Get all the fAQsList where conversation equals to conversationId
        defaultFAQsShouldBeFound("conversationId.equals=" + conversationId);

        // Get all the fAQsList where conversation equals to UUID.randomUUID()
        defaultFAQsShouldNotBeFound("conversationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllFAQsByCustomerServiceIsEqualToSomething() throws Exception {
        CustomerService customerService;
        if (TestUtil.findAll(em, CustomerService.class).isEmpty()) {
            fAQsRepository.saveAndFlush(fAQs);
            customerService = CustomerServiceResourceIT.createEntity(em);
        } else {
            customerService = TestUtil.findAll(em, CustomerService.class).get(0);
        }
        em.persist(customerService);
        em.flush();
        fAQs.setCustomerService(customerService);
        customerService.setFaqs(fAQs);
        fAQsRepository.saveAndFlush(fAQs);
        UUID customerServiceId = customerService.getId();

        // Get all the fAQsList where customerService equals to customerServiceId
        defaultFAQsShouldBeFound("customerServiceId.equals=" + customerServiceId);

        // Get all the fAQsList where customerService equals to UUID.randomUUID()
        defaultFAQsShouldNotBeFound("customerServiceId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFAQsShouldBeFound(String filter) throws Exception {
        restFAQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fAQs.getId().toString())))
            .andExpect(jsonPath("$.[*].answers").value(hasItem(DEFAULT_ANSWERS)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].keyWords").value(hasItem(DEFAULT_KEY_WORDS)));

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
        updatedFAQs.answers(UPDATED_ANSWERS).question(UPDATED_QUESTION).keyWords(UPDATED_KEY_WORDS);
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
        assertThat(testFAQs.getAnswers()).isEqualTo(UPDATED_ANSWERS);
        assertThat(testFAQs.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testFAQs.getKeyWords()).isEqualTo(UPDATED_KEY_WORDS);
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

        partialUpdatedFAQs.answers(UPDATED_ANSWERS).question(UPDATED_QUESTION).keyWords(UPDATED_KEY_WORDS);

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
        assertThat(testFAQs.getAnswers()).isEqualTo(UPDATED_ANSWERS);
        assertThat(testFAQs.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testFAQs.getKeyWords()).isEqualTo(UPDATED_KEY_WORDS);
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

        partialUpdatedFAQs.answers(UPDATED_ANSWERS).question(UPDATED_QUESTION).keyWords(UPDATED_KEY_WORDS);

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
        assertThat(testFAQs.getAnswers()).isEqualTo(UPDATED_ANSWERS);
        assertThat(testFAQs.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testFAQs.getKeyWords()).isEqualTo(UPDATED_KEY_WORDS);
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
