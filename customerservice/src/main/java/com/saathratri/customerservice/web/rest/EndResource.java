package com.saathratri.customerservice.web.rest;

import com.saathratri.customerservice.repository.EndRepository;
import com.saathratri.customerservice.service.EndQueryService;
import com.saathratri.customerservice.service.EndService;
import com.saathratri.customerservice.service.criteria.EndCriteria;
import com.saathratri.customerservice.service.dto.EndDTO;
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
 * REST controller for managing {@link com.saathratri.customerservice.domain.End}.
 */
@RestController
@RequestMapping("/api")
public class EndResource {

    private final Logger log = LoggerFactory.getLogger(EndResource.class);

    private static final String ENTITY_NAME = "customerserviceEnd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EndService endService;

    private final EndRepository endRepository;

    private final EndQueryService endQueryService;

    public EndResource(EndService endService, EndRepository endRepository, EndQueryService endQueryService) {
        this.endService = endService;
        this.endRepository = endRepository;
        this.endQueryService = endQueryService;
    }

    /**
     * {@code POST  /ends} : Create a new end.
     *
     * @param endDTO the endDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new endDTO, or with status {@code 400 (Bad Request)} if the end has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ends")
    public ResponseEntity<EndDTO> createEnd(@Valid @RequestBody EndDTO endDTO) throws URISyntaxException {
        log.debug("REST request to save End : {}", endDTO);
        if (endDTO.getId() != null) {
            throw new BadRequestAlertException("A new end cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EndDTO result = endService.save(endDTO);
        return ResponseEntity
            .created(new URI("/api/ends/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ends/:id} : Updates an existing end.
     *
     * @param id the id of the endDTO to save.
     * @param endDTO the endDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endDTO,
     * or with status {@code 400 (Bad Request)} if the endDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the endDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ends/{id}")
    public ResponseEntity<EndDTO> updateEnd(@PathVariable(value = "id", required = false) final UUID id, @Valid @RequestBody EndDTO endDTO)
        throws URISyntaxException {
        log.debug("REST request to update End : {}, {}", id, endDTO);
        if (endDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, endDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!endRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EndDTO result = endService.update(endDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ends/:id} : Partial updates given fields of an existing end, field will ignore if it is null
     *
     * @param id the id of the endDTO to save.
     * @param endDTO the endDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endDTO,
     * or with status {@code 400 (Bad Request)} if the endDTO is not valid,
     * or with status {@code 404 (Not Found)} if the endDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the endDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ends/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EndDTO> partialUpdateEnd(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EndDTO endDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update End partially : {}, {}", id, endDTO);
        if (endDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, endDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!endRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EndDTO> result = endService.partialUpdate(endDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ends} : get all the ends.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ends in body.
     */
    @GetMapping("/ends")
    public ResponseEntity<List<EndDTO>> getAllEnds(EndCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Ends by criteria: {}", criteria);
        Page<EndDTO> page = endQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ends/count} : count all the ends.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ends/count")
    public ResponseEntity<Long> countEnds(EndCriteria criteria) {
        log.debug("REST request to count Ends by criteria: {}", criteria);
        return ResponseEntity.ok().body(endQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ends/:id} : get the "id" end.
     *
     * @param id the id of the endDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the endDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ends/{id}")
    public ResponseEntity<EndDTO> getEnd(@PathVariable UUID id) {
        log.debug("REST request to get End : {}", id);
        Optional<EndDTO> endDTO = endService.findOne(id);
        return ResponseUtil.wrapOrNotFound(endDTO);
    }

    /**
     * {@code DELETE  /ends/:id} : delete the "id" end.
     *
     * @param id the id of the endDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ends/{id}")
    public ResponseEntity<Void> deleteEnd(@PathVariable UUID id) {
        log.debug("REST request to delete End : {}", id);
        endService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
