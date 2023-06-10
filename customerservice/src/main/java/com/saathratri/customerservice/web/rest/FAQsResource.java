package com.saathratri.customerservice.web.rest;

import com.saathratri.customerservice.repository.FAQsRepository;
import com.saathratri.customerservice.service.FAQsQueryService;
import com.saathratri.customerservice.service.FAQsService;
import com.saathratri.customerservice.service.criteria.FAQsCriteria;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import com.saathratri.customerservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.saathratri.customerservice.domain.FAQs}.
 */
@RestController
@RequestMapping("/api")
public class FAQsResource {

    private final Logger log = LoggerFactory.getLogger(FAQsResource.class);

    private static final String ENTITY_NAME = "customerserviceFaQs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FAQsService fAQsService;

    private final FAQsRepository fAQsRepository;

    private final FAQsQueryService fAQsQueryService;

    public FAQsResource(FAQsService fAQsService, FAQsRepository fAQsRepository, FAQsQueryService fAQsQueryService) {
        this.fAQsService = fAQsService;
        this.fAQsRepository = fAQsRepository;
        this.fAQsQueryService = fAQsQueryService;
    }

    /**
     * {@code POST  /fa-qs} : Create a new fAQs.
     *
     * @param fAQsDTO the fAQsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fAQsDTO, or with status {@code 400 (Bad Request)} if the fAQs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fa-qs")
    public ResponseEntity<FAQsDTO> createFAQs(@Valid @RequestBody FAQsDTO fAQsDTO) throws URISyntaxException {
        log.debug("REST request to save FAQs : {}", fAQsDTO);
        if (fAQsDTO.getId() != null) {
            throw new BadRequestAlertException("A new fAQs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FAQsDTO result = fAQsService.save(fAQsDTO);
        return ResponseEntity
            .created(new URI("/api/fa-qs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fa-qs/:id} : Updates an existing fAQs.
     *
     * @param id the id of the fAQsDTO to save.
     * @param fAQsDTO the fAQsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fAQsDTO,
     * or with status {@code 400 (Bad Request)} if the fAQsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fAQsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fa-qs/{id}")
    public ResponseEntity<FAQsDTO> updateFAQs(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody FAQsDTO fAQsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FAQs : {}, {}", id, fAQsDTO);
        if (fAQsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fAQsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fAQsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FAQsDTO result = fAQsService.update(fAQsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fAQsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fa-qs/:id} : Partial updates given fields of an existing fAQs, field will ignore if it is null
     *
     * @param id the id of the fAQsDTO to save.
     * @param fAQsDTO the fAQsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fAQsDTO,
     * or with status {@code 400 (Bad Request)} if the fAQsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fAQsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fAQsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fa-qs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FAQsDTO> partialUpdateFAQs(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FAQsDTO fAQsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FAQs partially : {}, {}", id, fAQsDTO);
        if (fAQsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fAQsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fAQsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FAQsDTO> result = fAQsService.partialUpdate(fAQsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fAQsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fa-qs} : get all the fAQs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fAQs in body.
     */
    @GetMapping("/fa-qs")
    public ResponseEntity<List<FAQsDTO>> getAllFAQs(
        FAQsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FAQs by criteria: {}", criteria);
        Page<FAQsDTO> page = fAQsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fa-qs/count} : count all the fAQs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fa-qs/count")
    public ResponseEntity<Long> countFAQs(FAQsCriteria criteria) {
        log.debug("REST request to count FAQs by criteria: {}", criteria);
        return ResponseEntity.ok().body(fAQsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fa-qs/:id} : get the "id" fAQs.
     *
     * @param id the id of the fAQsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fAQsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fa-qs/{id}")
    public ResponseEntity<FAQsDTO> getFAQs(@PathVariable UUID id) {
        log.debug("REST request to get FAQs : {}", id);
        Optional<FAQsDTO> fAQsDTO = fAQsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fAQsDTO);
    }

    /**
     * {@code DELETE  /fa-qs/:id} : delete the "id" fAQs.
     *
     * @param id the id of the fAQsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fa-qs/{id}")
    public ResponseEntity<Void> deleteFAQs(@PathVariable UUID id) {
        log.debug("REST request to delete FAQs : {}", id);
        fAQsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
