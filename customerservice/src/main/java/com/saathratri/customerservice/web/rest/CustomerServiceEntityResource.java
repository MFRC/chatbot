package com.saathratri.customerservice.web.rest;

import com.saathratri.customerservice.repository.CustomerServiceEntityRepository;
import com.saathratri.customerservice.service.CustomerServiceEntityQueryService;
import com.saathratri.customerservice.service.CustomerServiceEntityService;
import com.saathratri.customerservice.service.criteria.CustomerServiceEntityCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import com.saathratri.customerservice.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.saathratri.customerservice.domain.CustomerServiceEntity}.
 */
@RestController
@RequestMapping("/api")
public class CustomerServiceEntityResource {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceEntityResource.class);

    private static final String ENTITY_NAME = "customerserviceCustomerServiceEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerServiceEntityService customerServiceEntityService;

    private final CustomerServiceEntityRepository customerServiceEntityRepository;

    private final CustomerServiceEntityQueryService customerServiceEntityQueryService;

    public CustomerServiceEntityResource(
        CustomerServiceEntityService customerServiceEntityService,
        CustomerServiceEntityRepository customerServiceEntityRepository,
        CustomerServiceEntityQueryService customerServiceEntityQueryService
    ) {
        this.customerServiceEntityService = customerServiceEntityService;
        this.customerServiceEntityRepository = customerServiceEntityRepository;
        this.customerServiceEntityQueryService = customerServiceEntityQueryService;
    }

    /**
     * {@code POST  /customer-service-entities} : Create a new customerServiceEntity.
     *
     * @param customerServiceEntityDTO the customerServiceEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerServiceEntityDTO, or with status {@code 400 (Bad Request)} if the customerServiceEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-service-entities")
    public ResponseEntity<CustomerServiceEntityDTO> createCustomerServiceEntity(
        @Valid @RequestBody CustomerServiceEntityDTO customerServiceEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CustomerServiceEntity : {}", customerServiceEntityDTO);
        if (customerServiceEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerServiceEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerServiceEntityDTO result = customerServiceEntityService.save(customerServiceEntityDTO);
        return ResponseEntity
            .created(new URI("/api/customer-service-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-service-entities/:id} : Updates an existing customerServiceEntity.
     *
     * @param id the id of the customerServiceEntityDTO to save.
     * @param customerServiceEntityDTO the customerServiceEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerServiceEntityDTO,
     * or with status {@code 400 (Bad Request)} if the customerServiceEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerServiceEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-service-entities/{id}")
    public ResponseEntity<CustomerServiceEntityDTO> updateCustomerServiceEntity(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CustomerServiceEntityDTO customerServiceEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerServiceEntity : {}, {}", id, customerServiceEntityDTO);
        if (customerServiceEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerServiceEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerServiceEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerServiceEntityDTO result = customerServiceEntityService.update(customerServiceEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerServiceEntityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-service-entities/:id} : Partial updates given fields of an existing customerServiceEntity, field will ignore if it is null
     *
     * @param id the id of the customerServiceEntityDTO to save.
     * @param customerServiceEntityDTO the customerServiceEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerServiceEntityDTO,
     * or with status {@code 400 (Bad Request)} if the customerServiceEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerServiceEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerServiceEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-service-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerServiceEntityDTO> partialUpdateCustomerServiceEntity(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerServiceEntityDTO customerServiceEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerServiceEntity partially : {}, {}", id, customerServiceEntityDTO);
        if (customerServiceEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerServiceEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerServiceEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerServiceEntityDTO> result = customerServiceEntityService.partialUpdate(customerServiceEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerServiceEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-service-entities} : get all the customerServiceEntities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerServiceEntities in body.
     */
    @GetMapping("/customer-service-entities")
    public ResponseEntity<List<CustomerServiceEntityDTO>> getAllCustomerServiceEntities(
        CustomerServiceEntityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerServiceEntities by criteria: {}", criteria);
        Page<CustomerServiceEntityDTO> page = customerServiceEntityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-service-entities/count} : count all the customerServiceEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-service-entities/count")
    public ResponseEntity<Long> countCustomerServiceEntities(CustomerServiceEntityCriteria criteria) {
        log.debug("REST request to count CustomerServiceEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerServiceEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-service-entities/:id} : get the "id" customerServiceEntity.
     *
     * @param id the id of the customerServiceEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerServiceEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-service-entities/{id}")
    public ResponseEntity<CustomerServiceEntityDTO> getCustomerServiceEntity(@PathVariable UUID id) {
        log.debug("REST request to get CustomerServiceEntity : {}", id);
        Optional<CustomerServiceEntityDTO> customerServiceEntityDTO = customerServiceEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerServiceEntityDTO);
    }

    /**
     * {@code DELETE  /customer-service-entities/:id} : delete the "id" customerServiceEntity.
     *
     * @param id the id of the customerServiceEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-service-entities/{id}")
    public ResponseEntity<Void> deleteCustomerServiceEntity(@PathVariable UUID id) {
        log.debug("REST request to delete CustomerServiceEntity : {}", id);
        customerServiceEntityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
