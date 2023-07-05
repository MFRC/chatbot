package com.saathratri.chatbotservice.web.rest;

import com.saathratri.chatbotservice.repository.ChatbotServiceUserRepository;
import com.saathratri.chatbotservice.service.ChatbotServiceUserQueryService;
import com.saathratri.chatbotservice.service.ChatbotServiceUserService;
import com.saathratri.chatbotservice.service.criteria.ChatbotServiceUserCriteria;
import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.chatbotservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.saathratri.chatbotservice.domain.ChatbotServiceUser}.
 */
@RestController
@RequestMapping("/api")
public class ChatbotServiceUserResource {

    private final Logger log = LoggerFactory.getLogger(ChatbotServiceUserResource.class);

    private static final String ENTITY_NAME = "chatbotserviceChatbotServiceUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatbotServiceUserService chatbotServiceUserService;

    private final ChatbotServiceUserRepository chatbotServiceUserRepository;

    private final ChatbotServiceUserQueryService chatbotServiceUserQueryService;

    public ChatbotServiceUserResource(
        ChatbotServiceUserService chatbotServiceUserService,
        ChatbotServiceUserRepository chatbotServiceUserRepository,
        ChatbotServiceUserQueryService chatbotServiceUserQueryService
    ) {
        this.chatbotServiceUserService = chatbotServiceUserService;
        this.chatbotServiceUserRepository = chatbotServiceUserRepository;
        this.chatbotServiceUserQueryService = chatbotServiceUserQueryService;
    }

    /**
     * {@code POST  /chatbot-service-users} : Create a new chatbotServiceUser.
     *
     * @param chatbotServiceUserDTO the chatbotServiceUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatbotServiceUserDTO, or with status {@code 400 (Bad Request)} if the chatbotServiceUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chatbot-service-users")
    public ResponseEntity<ChatbotServiceUserDTO> createChatbotServiceUser(@Valid @RequestBody ChatbotServiceUserDTO chatbotServiceUserDTO)
        throws URISyntaxException {
        log.debug("REST request to save ChatbotServiceUser : {}", chatbotServiceUserDTO);
        if (chatbotServiceUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatbotServiceUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatbotServiceUserDTO result = chatbotServiceUserService.save(chatbotServiceUserDTO);
        return ResponseEntity
            .created(new URI("/api/chatbot-service-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chatbot-service-users/:id} : Updates an existing chatbotServiceUser.
     *
     * @param id the id of the chatbotServiceUserDTO to save.
     * @param chatbotServiceUserDTO the chatbotServiceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatbotServiceUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatbotServiceUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatbotServiceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chatbot-service-users/{id}")
    public ResponseEntity<ChatbotServiceUserDTO> updateChatbotServiceUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ChatbotServiceUserDTO chatbotServiceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChatbotServiceUser : {}, {}", id, chatbotServiceUserDTO);
        if (chatbotServiceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatbotServiceUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatbotServiceUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChatbotServiceUserDTO result = chatbotServiceUserService.update(chatbotServiceUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatbotServiceUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chatbot-service-users/:id} : Partial updates given fields of an existing chatbotServiceUser, field will ignore if it is null
     *
     * @param id the id of the chatbotServiceUserDTO to save.
     * @param chatbotServiceUserDTO the chatbotServiceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatbotServiceUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatbotServiceUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatbotServiceUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatbotServiceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chatbot-service-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatbotServiceUserDTO> partialUpdateChatbotServiceUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ChatbotServiceUserDTO chatbotServiceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChatbotServiceUser partially : {}, {}", id, chatbotServiceUserDTO);
        if (chatbotServiceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatbotServiceUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatbotServiceUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatbotServiceUserDTO> result = chatbotServiceUserService.partialUpdate(chatbotServiceUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatbotServiceUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chatbot-service-users} : get all the chatbotServiceUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatbotServiceUsers in body.
     */
    @GetMapping("/chatbot-service-users")
    public ResponseEntity<List<ChatbotServiceUserDTO>> getAllChatbotServiceUsers(
        ChatbotServiceUserCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ChatbotServiceUsers by criteria: {}", criteria);
        Page<ChatbotServiceUserDTO> page = chatbotServiceUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chatbot-service-users/count} : count all the chatbotServiceUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/chatbot-service-users/count")
    public ResponseEntity<Long> countChatbotServiceUsers(ChatbotServiceUserCriteria criteria) {
        log.debug("REST request to count ChatbotServiceUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatbotServiceUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chatbot-service-users/:id} : get the "id" chatbotServiceUser.
     *
     * @param id the id of the chatbotServiceUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatbotServiceUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chatbot-service-users/{id}")
    public ResponseEntity<ChatbotServiceUserDTO> getChatbotServiceUser(@PathVariable UUID id) {
        log.debug("REST request to get ChatbotServiceUser : {}", id);
        Optional<ChatbotServiceUserDTO> chatbotServiceUserDTO = chatbotServiceUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatbotServiceUserDTO);
    }

    /**
     * {@code DELETE  /chatbot-service-users/:id} : delete the "id" chatbotServiceUser.
     *
     * @param id the id of the chatbotServiceUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chatbot-service-users/{id}")
    public ResponseEntity<Void> deleteChatbotServiceUser(@PathVariable UUID id) {
        log.debug("REST request to delete ChatbotServiceUser : {}", id);
        chatbotServiceUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
