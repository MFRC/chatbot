package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.repository.ConversationRepository;
import com.saathratri.customerservice.service.criteria.ConversationCriteria;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.mapper.ConversationMapper;
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
 * Integration tests for the {@link ConversationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConversationResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWERS = "AAAAAAAAAA";
    private static final String UPDATED_ANSWERS = "BBBBBBBBBB";

    private static final String DEFAULT_RESERVATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_KEY_WORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEY_WORDS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conversations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConversationMockMvc;

    private Conversation conversation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversation createEntity(EntityManager em) {
        Conversation conversation = new Conversation()
            .question(DEFAULT_QUESTION)
            .answers(DEFAULT_ANSWERS)
            .reservationNumber(DEFAULT_RESERVATION_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .keyWords(DEFAULT_KEY_WORDS);
        return conversation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversation createUpdatedEntity(EntityManager em) {
        Conversation conversation = new Conversation()
            .question(UPDATED_QUESTION)
            .answers(UPDATED_ANSWERS)
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .keyWords(UPDATED_KEY_WORDS);
        return conversation;
    }

    @BeforeEach
    public void initTest() {
        conversation = createEntity(em);
    }

    @Test
    @Transactional
    void createConversation() throws Exception {
        int databaseSizeBeforeCreate = conversationRepository.findAll().size();
        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);
        restConversationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeCreate + 1);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
        assertThat(testConversation.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testConversation.getAnswers()).isEqualTo(DEFAULT_ANSWERS);
        assertThat(testConversation.getReservationNumber()).isEqualTo(DEFAULT_RESERVATION_NUMBER);
        assertThat(testConversation.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testConversation.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testConversation.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testConversation.getKeyWords()).isEqualTo(DEFAULT_KEY_WORDS);
    }

    @Test
    @Transactional
    void createConversationWithExistingId() throws Exception {
        // Create the Conversation with an existing ID
        conversationRepository.saveAndFlush(conversation);
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        int databaseSizeBeforeCreate = conversationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConversations() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList
        restConversationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId().toString())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].answers").value(hasItem(DEFAULT_ANSWERS)))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].keyWords").value(hasItem(DEFAULT_KEY_WORDS)));
    }

    @Test
    @Transactional
    void getConversation() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get the conversation
        restConversationMockMvc
            .perform(get(ENTITY_API_URL_ID, conversation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conversation.getId().toString()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.answers").value(DEFAULT_ANSWERS))
            .andExpect(jsonPath("$.reservationNumber").value(DEFAULT_RESERVATION_NUMBER))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.keyWords").value(DEFAULT_KEY_WORDS));
    }

    @Test
    @Transactional
    void getConversationsByIdFiltering() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        UUID id = conversation.getId();

        defaultConversationShouldBeFound("id.equals=" + id);
        defaultConversationShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllConversationsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where question equals to DEFAULT_QUESTION
        defaultConversationShouldBeFound("question.equals=" + DEFAULT_QUESTION);

        // Get all the conversationList where question equals to UPDATED_QUESTION
        defaultConversationShouldNotBeFound("question.equals=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllConversationsByQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where question in DEFAULT_QUESTION or UPDATED_QUESTION
        defaultConversationShouldBeFound("question.in=" + DEFAULT_QUESTION + "," + UPDATED_QUESTION);

        // Get all the conversationList where question equals to UPDATED_QUESTION
        defaultConversationShouldNotBeFound("question.in=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllConversationsByQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where question is not null
        defaultConversationShouldBeFound("question.specified=true");

        // Get all the conversationList where question is null
        defaultConversationShouldNotBeFound("question.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByQuestionContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where question contains DEFAULT_QUESTION
        defaultConversationShouldBeFound("question.contains=" + DEFAULT_QUESTION);

        // Get all the conversationList where question contains UPDATED_QUESTION
        defaultConversationShouldNotBeFound("question.contains=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllConversationsByQuestionNotContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where question does not contain DEFAULT_QUESTION
        defaultConversationShouldNotBeFound("question.doesNotContain=" + DEFAULT_QUESTION);

        // Get all the conversationList where question does not contain UPDATED_QUESTION
        defaultConversationShouldBeFound("question.doesNotContain=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllConversationsByAnswersIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where answers equals to DEFAULT_ANSWERS
        defaultConversationShouldBeFound("answers.equals=" + DEFAULT_ANSWERS);

        // Get all the conversationList where answers equals to UPDATED_ANSWERS
        defaultConversationShouldNotBeFound("answers.equals=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllConversationsByAnswersIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where answers in DEFAULT_ANSWERS or UPDATED_ANSWERS
        defaultConversationShouldBeFound("answers.in=" + DEFAULT_ANSWERS + "," + UPDATED_ANSWERS);

        // Get all the conversationList where answers equals to UPDATED_ANSWERS
        defaultConversationShouldNotBeFound("answers.in=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllConversationsByAnswersIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where answers is not null
        defaultConversationShouldBeFound("answers.specified=true");

        // Get all the conversationList where answers is null
        defaultConversationShouldNotBeFound("answers.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByAnswersContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where answers contains DEFAULT_ANSWERS
        defaultConversationShouldBeFound("answers.contains=" + DEFAULT_ANSWERS);

        // Get all the conversationList where answers contains UPDATED_ANSWERS
        defaultConversationShouldNotBeFound("answers.contains=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllConversationsByAnswersNotContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where answers does not contain DEFAULT_ANSWERS
        defaultConversationShouldNotBeFound("answers.doesNotContain=" + DEFAULT_ANSWERS);

        // Get all the conversationList where answers does not contain UPDATED_ANSWERS
        defaultConversationShouldBeFound("answers.doesNotContain=" + UPDATED_ANSWERS);
    }

    @Test
    @Transactional
    void getAllConversationsByReservationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where reservationNumber equals to DEFAULT_RESERVATION_NUMBER
        defaultConversationShouldBeFound("reservationNumber.equals=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the conversationList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultConversationShouldNotBeFound("reservationNumber.equals=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByReservationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where reservationNumber in DEFAULT_RESERVATION_NUMBER or UPDATED_RESERVATION_NUMBER
        defaultConversationShouldBeFound("reservationNumber.in=" + DEFAULT_RESERVATION_NUMBER + "," + UPDATED_RESERVATION_NUMBER);

        // Get all the conversationList where reservationNumber equals to UPDATED_RESERVATION_NUMBER
        defaultConversationShouldNotBeFound("reservationNumber.in=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByReservationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where reservationNumber is not null
        defaultConversationShouldBeFound("reservationNumber.specified=true");

        // Get all the conversationList where reservationNumber is null
        defaultConversationShouldNotBeFound("reservationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByReservationNumberContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where reservationNumber contains DEFAULT_RESERVATION_NUMBER
        defaultConversationShouldBeFound("reservationNumber.contains=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the conversationList where reservationNumber contains UPDATED_RESERVATION_NUMBER
        defaultConversationShouldNotBeFound("reservationNumber.contains=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByReservationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where reservationNumber does not contain DEFAULT_RESERVATION_NUMBER
        defaultConversationShouldNotBeFound("reservationNumber.doesNotContain=" + DEFAULT_RESERVATION_NUMBER);

        // Get all the conversationList where reservationNumber does not contain UPDATED_RESERVATION_NUMBER
        defaultConversationShouldBeFound("reservationNumber.doesNotContain=" + UPDATED_RESERVATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultConversationShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the conversationList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultConversationShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultConversationShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the conversationList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultConversationShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where phoneNumber is not null
        defaultConversationShouldBeFound("phoneNumber.specified=true");

        // Get all the conversationList where phoneNumber is null
        defaultConversationShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultConversationShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the conversationList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultConversationShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultConversationShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the conversationList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultConversationShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllConversationsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where startTime equals to DEFAULT_START_TIME
        defaultConversationShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the conversationList where startTime equals to UPDATED_START_TIME
        defaultConversationShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllConversationsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultConversationShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the conversationList where startTime equals to UPDATED_START_TIME
        defaultConversationShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllConversationsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where startTime is not null
        defaultConversationShouldBeFound("startTime.specified=true");

        // Get all the conversationList where startTime is null
        defaultConversationShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where endTime equals to DEFAULT_END_TIME
        defaultConversationShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the conversationList where endTime equals to UPDATED_END_TIME
        defaultConversationShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllConversationsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultConversationShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the conversationList where endTime equals to UPDATED_END_TIME
        defaultConversationShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllConversationsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where endTime is not null
        defaultConversationShouldBeFound("endTime.specified=true");

        // Get all the conversationList where endTime is null
        defaultConversationShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByKeyWordsIsEqualToSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where keyWords equals to DEFAULT_KEY_WORDS
        defaultConversationShouldBeFound("keyWords.equals=" + DEFAULT_KEY_WORDS);

        // Get all the conversationList where keyWords equals to UPDATED_KEY_WORDS
        defaultConversationShouldNotBeFound("keyWords.equals=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllConversationsByKeyWordsIsInShouldWork() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where keyWords in DEFAULT_KEY_WORDS or UPDATED_KEY_WORDS
        defaultConversationShouldBeFound("keyWords.in=" + DEFAULT_KEY_WORDS + "," + UPDATED_KEY_WORDS);

        // Get all the conversationList where keyWords equals to UPDATED_KEY_WORDS
        defaultConversationShouldNotBeFound("keyWords.in=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllConversationsByKeyWordsIsNullOrNotNull() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where keyWords is not null
        defaultConversationShouldBeFound("keyWords.specified=true");

        // Get all the conversationList where keyWords is null
        defaultConversationShouldNotBeFound("keyWords.specified=false");
    }

    @Test
    @Transactional
    void getAllConversationsByKeyWordsContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where keyWords contains DEFAULT_KEY_WORDS
        defaultConversationShouldBeFound("keyWords.contains=" + DEFAULT_KEY_WORDS);

        // Get all the conversationList where keyWords contains UPDATED_KEY_WORDS
        defaultConversationShouldNotBeFound("keyWords.contains=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllConversationsByKeyWordsNotContainsSomething() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        // Get all the conversationList where keyWords does not contain DEFAULT_KEY_WORDS
        defaultConversationShouldNotBeFound("keyWords.doesNotContain=" + DEFAULT_KEY_WORDS);

        // Get all the conversationList where keyWords does not contain UPDATED_KEY_WORDS
        defaultConversationShouldBeFound("keyWords.doesNotContain=" + UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void getAllConversationsByEndIsEqualToSomething() throws Exception {
        End end;
        if (TestUtil.findAll(em, End.class).isEmpty()) {
            conversationRepository.saveAndFlush(conversation);
            end = EndResourceIT.createEntity(em);
        } else {
            end = TestUtil.findAll(em, End.class).get(0);
        }
        em.persist(end);
        em.flush();
        conversation.setEnd(end);
        conversationRepository.saveAndFlush(conversation);
        UUID endId = end.getId();

        // Get all the conversationList where end equals to endId
        defaultConversationShouldBeFound("endId.equals=" + endId);

        // Get all the conversationList where end equals to UUID.randomUUID()
        defaultConversationShouldNotBeFound("endId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllConversationsByFaqsIsEqualToSomething() throws Exception {
        FAQs faqs;
        if (TestUtil.findAll(em, FAQs.class).isEmpty()) {
            conversationRepository.saveAndFlush(conversation);
            faqs = FAQsResourceIT.createEntity(em);
        } else {
            faqs = TestUtil.findAll(em, FAQs.class).get(0);
        }
        em.persist(faqs);
        em.flush();
        conversation.setFaqs(faqs);
        faqs.setConversation(conversation);
        conversationRepository.saveAndFlush(conversation);
        UUID faqsId = faqs.getId();

        // Get all the conversationList where faqs equals to faqsId
        defaultConversationShouldBeFound("faqsId.equals=" + faqsId);

        // Get all the conversationList where faqs equals to UUID.randomUUID()
        defaultConversationShouldNotBeFound("faqsId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllConversationsByCustomerServiceEntityIsEqualToSomething() throws Exception {
        CustomerServiceEntity customerServiceEntity;
        if (TestUtil.findAll(em, CustomerServiceEntity.class).isEmpty()) {
            conversationRepository.saveAndFlush(conversation);
            customerServiceEntity = CustomerServiceEntityResourceIT.createEntity(em);
        } else {
            customerServiceEntity = TestUtil.findAll(em, CustomerServiceEntity.class).get(0);
        }
        em.persist(customerServiceEntity);
        em.flush();
        conversation.setCustomerServiceEntity(customerServiceEntity);
        customerServiceEntity.setConversation(conversation);
        conversationRepository.saveAndFlush(conversation);
        UUID customerServiceEntityId = customerServiceEntity.getId();

        // Get all the conversationList where customerServiceEntity equals to customerServiceEntityId
        defaultConversationShouldBeFound("customerServiceEntityId.equals=" + customerServiceEntityId);

        // Get all the conversationList where customerServiceEntity equals to UUID.randomUUID()
        defaultConversationShouldNotBeFound("customerServiceEntityId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllConversationsByCustomerServiceUserIsEqualToSomething() throws Exception {
        CustomerServiceUser customerServiceUser;
        if (TestUtil.findAll(em, CustomerServiceUser.class).isEmpty()) {
            conversationRepository.saveAndFlush(conversation);
            customerServiceUser = CustomerServiceUserResourceIT.createEntity(em);
        } else {
            customerServiceUser = TestUtil.findAll(em, CustomerServiceUser.class).get(0);
        }
        em.persist(customerServiceUser);
        em.flush();
        conversation.setCustomerServiceUser(customerServiceUser);
        customerServiceUser.setConversation(conversation);
        conversationRepository.saveAndFlush(conversation);
        UUID customerServiceUserId = customerServiceUser.getId();

        // Get all the conversationList where customerServiceUser equals to customerServiceUserId
        defaultConversationShouldBeFound("customerServiceUserId.equals=" + customerServiceUserId);

        // Get all the conversationList where customerServiceUser equals to UUID.randomUUID()
        defaultConversationShouldNotBeFound("customerServiceUserId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConversationShouldBeFound(String filter) throws Exception {
        restConversationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId().toString())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].answers").value(hasItem(DEFAULT_ANSWERS)))
            .andExpect(jsonPath("$.[*].reservationNumber").value(hasItem(DEFAULT_RESERVATION_NUMBER)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].keyWords").value(hasItem(DEFAULT_KEY_WORDS)));

        // Check, that the count call also returns 1
        restConversationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConversationShouldNotBeFound(String filter) throws Exception {
        restConversationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConversationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConversation() throws Exception {
        // Get the conversation
        restConversationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConversation() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Update the conversation
        Conversation updatedConversation = conversationRepository.findById(conversation.getId()).get();
        // Disconnect from session so that the updates on updatedConversation are not directly saved in db
        em.detach(updatedConversation);
        updatedConversation
            .question(UPDATED_QUESTION)
            .answers(UPDATED_ANSWERS)
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .keyWords(UPDATED_KEY_WORDS);
        ConversationDTO conversationDTO = conversationMapper.toDto(updatedConversation);

        restConversationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
        assertThat(testConversation.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testConversation.getAnswers()).isEqualTo(UPDATED_ANSWERS);
        assertThat(testConversation.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testConversation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testConversation.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testConversation.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testConversation.getKeyWords()).isEqualTo(UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void putNonExistingConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();
        conversation.setId(UUID.randomUUID());

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();
        conversation.setId(UUID.randomUUID());

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();
        conversation.setId(UUID.randomUUID());

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConversationWithPatch() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Update the conversation using partial update
        Conversation partialUpdatedConversation = new Conversation();
        partialUpdatedConversation.setId(conversation.getId());

        partialUpdatedConversation.answers(UPDATED_ANSWERS).phoneNumber(UPDATED_PHONE_NUMBER);

        restConversationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversation))
            )
            .andExpect(status().isOk());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
        assertThat(testConversation.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testConversation.getAnswers()).isEqualTo(UPDATED_ANSWERS);
        assertThat(testConversation.getReservationNumber()).isEqualTo(DEFAULT_RESERVATION_NUMBER);
        assertThat(testConversation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testConversation.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testConversation.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testConversation.getKeyWords()).isEqualTo(DEFAULT_KEY_WORDS);
    }

    @Test
    @Transactional
    void fullUpdateConversationWithPatch() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Update the conversation using partial update
        Conversation partialUpdatedConversation = new Conversation();
        partialUpdatedConversation.setId(conversation.getId());

        partialUpdatedConversation
            .question(UPDATED_QUESTION)
            .answers(UPDATED_ANSWERS)
            .reservationNumber(UPDATED_RESERVATION_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .keyWords(UPDATED_KEY_WORDS);

        restConversationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversation))
            )
            .andExpect(status().isOk());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
        assertThat(testConversation.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testConversation.getAnswers()).isEqualTo(UPDATED_ANSWERS);
        assertThat(testConversation.getReservationNumber()).isEqualTo(UPDATED_RESERVATION_NUMBER);
        assertThat(testConversation.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testConversation.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testConversation.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testConversation.getKeyWords()).isEqualTo(UPDATED_KEY_WORDS);
    }

    @Test
    @Transactional
    void patchNonExistingConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();
        conversation.setId(UUID.randomUUID());

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conversationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();
        conversation.setId(UUID.randomUUID());

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();
        conversation.setId(UUID.randomUUID());

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConversation() throws Exception {
        // Initialize the database
        conversationRepository.saveAndFlush(conversation);

        int databaseSizeBeforeDelete = conversationRepository.findAll().size();

        // Delete the conversation
        restConversationMockMvc
            .perform(delete(ENTITY_API_URL_ID, conversation.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
