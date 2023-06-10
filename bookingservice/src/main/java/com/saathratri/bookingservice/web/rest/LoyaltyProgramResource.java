package com.saathratri.bookingservice.web.rest;

import com.saathratri.bookingservice.repository.LoyaltyProgramRepository;
import com.saathratri.bookingservice.service.LoyaltyProgramService;
import com.saathratri.bookingservice.service.dto.LoyaltyProgramDTO;
import com.saathratri.bookingservice.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.saathratri.bookingservice.domain.LoyaltyProgram}.
 */
@RestController
@RequestMapping("/api")
public class LoyaltyProgramResource {

    private final Logger log = LoggerFactory.getLogger(LoyaltyProgramResource.class);

    private static final String ENTITY_NAME = "bookingserviceLoyaltyProgram";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoyaltyProgramService loyaltyProgramService;

    private final LoyaltyProgramRepository loyaltyProgramRepository;

    public LoyaltyProgramResource(LoyaltyProgramService loyaltyProgramService, LoyaltyProgramRepository loyaltyProgramRepository) {
        this.loyaltyProgramService = loyaltyProgramService;
        this.loyaltyProgramRepository = loyaltyProgramRepository;
    }

    /**
     * {@code POST  /loyalty-programs} : Create a new loyaltyProgram.
     *
     * @param loyaltyProgramDTO the loyaltyProgramDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loyaltyProgramDTO, or with status {@code 400 (Bad Request)} if the loyaltyProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loyalty-programs")
    public ResponseEntity<LoyaltyProgramDTO> createLoyaltyProgram(@Valid @RequestBody LoyaltyProgramDTO loyaltyProgramDTO)
        throws URISyntaxException {
        log.debug("REST request to save LoyaltyProgram : {}", loyaltyProgramDTO);
        if (loyaltyProgramDTO.getId() != null) {
            throw new BadRequestAlertException("A new loyaltyProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoyaltyProgramDTO result = loyaltyProgramService.save(loyaltyProgramDTO);
        return ResponseEntity
            .created(new URI("/api/loyalty-programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loyalty-programs/:id} : Updates an existing loyaltyProgram.
     *
     * @param id the id of the loyaltyProgramDTO to save.
     * @param loyaltyProgramDTO the loyaltyProgramDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loyaltyProgramDTO,
     * or with status {@code 400 (Bad Request)} if the loyaltyProgramDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loyaltyProgramDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loyalty-programs/{id}")
    public ResponseEntity<LoyaltyProgramDTO> updateLoyaltyProgram(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody LoyaltyProgramDTO loyaltyProgramDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoyaltyProgram : {}, {}", id, loyaltyProgramDTO);
        if (loyaltyProgramDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loyaltyProgramDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loyaltyProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoyaltyProgramDTO result = loyaltyProgramService.update(loyaltyProgramDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loyaltyProgramDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loyalty-programs/:id} : Partial updates given fields of an existing loyaltyProgram, field will ignore if it is null
     *
     * @param id the id of the loyaltyProgramDTO to save.
     * @param loyaltyProgramDTO the loyaltyProgramDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loyaltyProgramDTO,
     * or with status {@code 400 (Bad Request)} if the loyaltyProgramDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loyaltyProgramDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loyaltyProgramDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loyalty-programs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoyaltyProgramDTO> partialUpdateLoyaltyProgram(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody LoyaltyProgramDTO loyaltyProgramDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoyaltyProgram partially : {}, {}", id, loyaltyProgramDTO);
        if (loyaltyProgramDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loyaltyProgramDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loyaltyProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoyaltyProgramDTO> result = loyaltyProgramService.partialUpdate(loyaltyProgramDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loyaltyProgramDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loyalty-programs} : get all the loyaltyPrograms.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loyaltyPrograms in body.
     */
    @GetMapping("/loyalty-programs")
    public ResponseEntity<List<LoyaltyProgramDTO>> getAllLoyaltyPrograms(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("hotel-is-null".equals(filter)) {
            log.debug("REST request to get all LoyaltyPrograms where hotel is null");
            return new ResponseEntity<>(loyaltyProgramService.findAllWhereHotelIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of LoyaltyPrograms");
        Page<LoyaltyProgramDTO> page = loyaltyProgramService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loyalty-programs/:id} : get the "id" loyaltyProgram.
     *
     * @param id the id of the loyaltyProgramDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loyaltyProgramDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loyalty-programs/{id}")
    public ResponseEntity<LoyaltyProgramDTO> getLoyaltyProgram(@PathVariable UUID id) {
        log.debug("REST request to get LoyaltyProgram : {}", id);
        Optional<LoyaltyProgramDTO> loyaltyProgramDTO = loyaltyProgramService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loyaltyProgramDTO);
    }

    /**
     * {@code DELETE  /loyalty-programs/:id} : delete the "id" loyaltyProgram.
     *
     * @param id the id of the loyaltyProgramDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loyalty-programs/{id}")
    public ResponseEntity<Void> deleteLoyaltyProgram(@PathVariable UUID id) {
        log.debug("REST request to delete LoyaltyProgram : {}", id);
        loyaltyProgramService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
