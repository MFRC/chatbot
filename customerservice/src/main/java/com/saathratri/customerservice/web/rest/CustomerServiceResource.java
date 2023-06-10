package com.saathratri.customerservice.web.rest;

import com.saathratri.customerservice.repository.CustomerServiceRepository;
import com.saathratri.customerservice.service.CustomerServiceQueryService;
import com.saathratri.customerservice.service.CustomerServiceService;
import com.saathratri.customerservice.service.criteria.CustomerServiceCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
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
 * REST controller for managing {@link com.saathratri.customerservice.domain.CustomerService}.
 */
@RestController
@RequestMapping("/api")
public class CustomerServiceResource {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceResource.class);

    private static final String ENTITY_NAME = "customerserviceCustomerService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerServiceService customerServiceService;

    private final CustomerServiceRepository customerServiceRepository;

    private final CustomerServiceQueryService customerServiceQueryService;

    public CustomerServiceResource(
        CustomerServiceService customerServiceService,
        CustomerServiceRepository customerServiceRepository,
        CustomerServiceQueryService customerServiceQueryService
    ) {
        this.customerServiceService = customerServiceService;
        this.customerServiceRepository = customerServiceRepository;
        this.customerServiceQueryService = customerServiceQueryService;
    }

    /**
     * {@code POST  /customer-services} : Create a new customerService.
     *
     * @param customerServiceDTO the customerServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerServiceDTO, or with status {@code 400 (Bad Request)} if the customerService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-services")
    public ResponseEntity<CustomerServiceDTO> createCustomerService(@Valid @RequestBody CustomerServiceDTO customerServiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerService : {}", customerServiceDTO);
        if (customerServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerServiceDTO result = customerServiceService.save(customerServiceDTO);
        return ResponseEntity
            .created(new URI("/api/customer-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-services/:id} : Updates an existing customerService.
     *
     * @param id the id of the customerServiceDTO to save.
     * @param customerServiceDTO the customerServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerServiceDTO,
     * or with status {@code 400 (Bad Request)} if the customerServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-services/{id}")
    public ResponseEntity<CustomerServiceDTO> updateCustomerService(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CustomerServiceDTO customerServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerService : {}, {}", id, customerServiceDTO);
        if (customerServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerServiceDTO result = customerServiceService.update(customerServiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerServiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-services/:id} : Partial updates given fields of an existing customerService, field will ignore if it is null
     *
     * @param id the id of the customerServiceDTO to save.
     * @param customerServiceDTO the customerServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerServiceDTO,
     * or with status {@code 400 (Bad Request)} if the customerServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerServiceDTO> partialUpdateCustomerService(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerServiceDTO customerServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerService partially : {}, {}", id, customerServiceDTO);
        if (customerServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerServiceDTO> result = customerServiceService.partialUpdate(customerServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-services} : get all the customerServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerServices in body.
     */
    @GetMapping("/customer-services")
    public ResponseEntity<List<CustomerServiceDTO>> getAllCustomerServices(
        CustomerServiceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerServices by criteria: {}", criteria);
        Page<CustomerServiceDTO> page = customerServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-services/count} : count all the customerServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-services/count")
    public ResponseEntity<Long> countCustomerServices(CustomerServiceCriteria criteria) {
        log.debug("REST request to count CustomerServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-services/:id} : get the "id" customerService.
     *
     * @param id the id of the customerServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-services/{id}")
    public ResponseEntity<CustomerServiceDTO> getCustomerService(@PathVariable UUID id) {
        log.debug("REST request to get CustomerService : {}", id);
        Optional<CustomerServiceDTO> customerServiceDTO = customerServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerServiceDTO);
    }

    /**
     * {@code DELETE  /customer-services/:id} : delete the "id" customerService.
     *
     * @param id the id of the customerServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-services/{id}")
    public ResponseEntity<Void> deleteCustomerService(@PathVariable UUID id) {
        log.debug("REST request to delete CustomerService : {}", id);
        customerServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
