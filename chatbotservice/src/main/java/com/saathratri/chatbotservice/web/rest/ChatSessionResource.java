package com.saathratri.chatbotservice.web.rest;

import com.saathratri.chatbotservice.repository.ChatSessionRepository;
import com.saathratri.chatbotservice.service.ChatSessionQueryService;
import com.saathratri.chatbotservice.service.ChatSessionService;
import com.saathratri.chatbotservice.service.criteria.ChatSessionCriteria;
import com.saathratri.chatbotservice.service.dto.ChatSessionDTO;
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
 * REST controller for managing {@link com.saathratri.chatbotservice.domain.ChatSession}.
 */
@RestController
@RequestMapping("/api")
public class ChatSessionResource {

    private final Logger log = LoggerFactory.getLogger(ChatSessionResource.class);

    private static final String ENTITY_NAME = "chatbotserviceChatSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatSessionService chatSessionService;

    private final ChatSessionRepository chatSessionRepository;

    private final ChatSessionQueryService chatSessionQueryService;

    public ChatSessionResource(
        ChatSessionService chatSessionService,
        ChatSessionRepository chatSessionRepository,
        ChatSessionQueryService chatSessionQueryService
    ) {
        this.chatSessionService = chatSessionService;
        this.chatSessionRepository = chatSessionRepository;
        this.chatSessionQueryService = chatSessionQueryService;
    }

    /**
     * {@code POST  /chat-sessions} : Create a new chatSession.
     *
     * @param chatSessionDTO the chatSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatSessionDTO, or with status {@code 400 (Bad Request)} if the chatSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-sessions")
    public ResponseEntity<ChatSessionDTO> createChatSession(@Valid @RequestBody ChatSessionDTO chatSessionDTO) throws URISyntaxException {
        log.debug("REST request to save ChatSession : {}", chatSessionDTO);
        if (chatSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatSessionDTO result = chatSessionService.save(chatSessionDTO);
        return ResponseEntity
            .created(new URI("/api/chat-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-sessions/:id} : Updates an existing chatSession.
     *
     * @param id the id of the chatSessionDTO to save.
     * @param chatSessionDTO the chatSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatSessionDTO,
     * or with status {@code 400 (Bad Request)} if the chatSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-sessions/{id}")
    public ResponseEntity<ChatSessionDTO> updateChatSession(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ChatSessionDTO chatSessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChatSession : {}, {}", id, chatSessionDTO);
        if (chatSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChatSessionDTO result = chatSessionService.update(chatSessionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatSessionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chat-sessions/:id} : Partial updates given fields of an existing chatSession, field will ignore if it is null
     *
     * @param id the id of the chatSessionDTO to save.
     * @param chatSessionDTO the chatSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatSessionDTO,
     * or with status {@code 400 (Bad Request)} if the chatSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chat-sessions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatSessionDTO> partialUpdateChatSession(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ChatSessionDTO chatSessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChatSession partially : {}, {}", id, chatSessionDTO);
        if (chatSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatSessionDTO> result = chatSessionService.partialUpdate(chatSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chat-sessions} : get all the chatSessions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatSessions in body.
     */
    @GetMapping("/chat-sessions")
    public ResponseEntity<List<ChatSessionDTO>> getAllChatSessions(
        ChatSessionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ChatSessions by criteria: {}", criteria);
        Page<ChatSessionDTO> page = chatSessionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chat-sessions/count} : count all the chatSessions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/chat-sessions/count")
    public ResponseEntity<Long> countChatSessions(ChatSessionCriteria criteria) {
        log.debug("REST request to count ChatSessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatSessionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chat-sessions/:id} : get the "id" chatSession.
     *
     * @param id the id of the chatSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-sessions/{id}")
    public ResponseEntity<ChatSessionDTO> getChatSession(@PathVariable UUID id) {
        log.debug("REST request to get ChatSession : {}", id);
        Optional<ChatSessionDTO> chatSessionDTO = chatSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatSessionDTO);
    }

    /**
     * {@code DELETE  /chat-sessions/:id} : delete the "id" chatSession.
     *
     * @param id the id of the chatSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-sessions/{id}")
    public ResponseEntity<Void> deleteChatSession(@PathVariable UUID id) {
        log.debug("REST request to delete ChatSession : {}", id);
        chatSessionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
