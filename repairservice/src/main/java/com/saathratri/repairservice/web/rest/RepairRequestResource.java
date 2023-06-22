package com.saathratri.repairservice.web.rest;

import com.saathratri.repairservice.repository.RepairRequestRepository;
import com.saathratri.repairservice.service.RepairRequestQueryService;
import com.saathratri.repairservice.service.RepairRequestService;
import com.saathratri.repairservice.service.criteria.RepairRequestCriteria;
import com.saathratri.repairservice.service.dto.RepairRequestDTO;
import com.saathratri.repairservice.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.saathratri.repairservice.domain.RepairRequest}.
 */
@RestController
@RequestMapping("/api")
public class RepairRequestResource {

    private final Logger log = LoggerFactory.getLogger(RepairRequestResource.class);

    private static final String ENTITY_NAME = "repairserviceRepairRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepairRequestService repairRequestService;

    private final RepairRequestRepository repairRequestRepository;

    private final RepairRequestQueryService repairRequestQueryService;

    public RepairRequestResource(
        RepairRequestService repairRequestService,
        RepairRequestRepository repairRequestRepository,
        RepairRequestQueryService repairRequestQueryService
    ) {
        this.repairRequestService = repairRequestService;
        this.repairRequestRepository = repairRequestRepository;
        this.repairRequestQueryService = repairRequestQueryService;
    }

    /**
     * {@code POST  /repair-requests} : Create a new repairRequest.
     *
     * @param repairRequestDTO the repairRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repairRequestDTO, or with status {@code 400 (Bad Request)} if the repairRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/repair-requests")
    public ResponseEntity<RepairRequestDTO> createRepairRequest(@Valid @RequestBody RepairRequestDTO repairRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save RepairRequest : {}", repairRequestDTO);
        if (repairRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new repairRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepairRequestDTO result = repairRequestService.save(repairRequestDTO);
        return ResponseEntity
            .created(new URI("/api/repair-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /repair-requests/:id} : Updates an existing repairRequest.
     *
     * @param id the id of the repairRequestDTO to save.
     * @param repairRequestDTO the repairRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairRequestDTO,
     * or with status {@code 400 (Bad Request)} if the repairRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repairRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/repair-requests/{id}")
    public ResponseEntity<RepairRequestDTO> updateRepairRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody RepairRequestDTO repairRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RepairRequest : {}, {}", id, repairRequestDTO);
        if (repairRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repairRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repairRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RepairRequestDTO result = repairRequestService.update(repairRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repairRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /repair-requests/:id} : Partial updates given fields of an existing repairRequest, field will ignore if it is null
     *
     * @param id the id of the repairRequestDTO to save.
     * @param repairRequestDTO the repairRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repairRequestDTO,
     * or with status {@code 400 (Bad Request)} if the repairRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the repairRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the repairRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/repair-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RepairRequestDTO> partialUpdateRepairRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RepairRequestDTO repairRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RepairRequest partially : {}, {}", id, repairRequestDTO);
        if (repairRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repairRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repairRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepairRequestDTO> result = repairRequestService.partialUpdate(repairRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repairRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /repair-requests} : get all the repairRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repairRequests in body.
     */
    @GetMapping("/repair-requests")
    public ResponseEntity<List<RepairRequestDTO>> getAllRepairRequests(
        RepairRequestCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RepairRequests by criteria: {}", criteria);
        Page<RepairRequestDTO> page = repairRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repair-requests/count} : count all the repairRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/repair-requests/count")
    public ResponseEntity<Long> countRepairRequests(RepairRequestCriteria criteria) {
        log.debug("REST request to count RepairRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(repairRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /repair-requests/:id} : get the "id" repairRequest.
     *
     * @param id the id of the repairRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repairRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/repair-requests/{id}")
    public ResponseEntity<RepairRequestDTO> getRepairRequest(@PathVariable UUID id) {
        log.debug("REST request to get RepairRequest : {}", id);
        Optional<RepairRequestDTO> repairRequestDTO = repairRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repairRequestDTO);
    }

    /**
     * {@code DELETE  /repair-requests/:id} : delete the "id" repairRequest.
     *
     * @param id the id of the repairRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/repair-requests/{id}")
    public ResponseEntity<Void> deleteRepairRequest(@PathVariable UUID id) {
        log.debug("REST request to delete RepairRequest : {}", id);
        repairRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
