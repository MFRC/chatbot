package com.saathratri.customerservice.web.rest;

import com.saathratri.customerservice.repository.CustomerServiceUserRepository;
import com.saathratri.customerservice.service.CustomerServiceUserQueryService;
import com.saathratri.customerservice.service.CustomerServiceUserService;
import com.saathratri.customerservice.service.criteria.CustomerServiceUserCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
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
 * REST controller for managing {@link com.saathratri.customerservice.domain.CustomerServiceUser}.
 */
@RestController
@RequestMapping("/api")
public class CustomerServiceUserResource {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceUserResource.class);

    private static final String ENTITY_NAME = "customerserviceCustomerServiceUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerServiceUserService customerServiceUserService;

    private final CustomerServiceUserRepository customerServiceUserRepository;

    private final CustomerServiceUserQueryService customerServiceUserQueryService;

    public CustomerServiceUserResource(
        CustomerServiceUserService customerServiceUserService,
        CustomerServiceUserRepository customerServiceUserRepository,
        CustomerServiceUserQueryService customerServiceUserQueryService
    ) {
        this.customerServiceUserService = customerServiceUserService;
        this.customerServiceUserRepository = customerServiceUserRepository;
        this.customerServiceUserQueryService = customerServiceUserQueryService;
    }

    /**
     * {@code POST  /customer-service-users} : Create a new customerServiceUser.
     *
     * @param customerServiceUserDTO the customerServiceUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerServiceUserDTO, or with status {@code 400 (Bad Request)} if the customerServiceUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-service-users")
    public ResponseEntity<CustomerServiceUserDTO> createCustomerServiceUser(
        @Valid @RequestBody CustomerServiceUserDTO customerServiceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CustomerServiceUser : {}", customerServiceUserDTO);
        if (customerServiceUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerServiceUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerServiceUserDTO result = customerServiceUserService.save(customerServiceUserDTO);
        return ResponseEntity
            .created(new URI("/api/customer-service-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-service-users/:id} : Updates an existing customerServiceUser.
     *
     * @param id the id of the customerServiceUserDTO to save.
     * @param customerServiceUserDTO the customerServiceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerServiceUserDTO,
     * or with status {@code 400 (Bad Request)} if the customerServiceUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerServiceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-service-users/{id}")
    public ResponseEntity<CustomerServiceUserDTO> updateCustomerServiceUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CustomerServiceUserDTO customerServiceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerServiceUser : {}, {}", id, customerServiceUserDTO);
        if (customerServiceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerServiceUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerServiceUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerServiceUserDTO result = customerServiceUserService.update(customerServiceUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerServiceUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-service-users/:id} : Partial updates given fields of an existing customerServiceUser, field will ignore if it is null
     *
     * @param id the id of the customerServiceUserDTO to save.
     * @param customerServiceUserDTO the customerServiceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerServiceUserDTO,
     * or with status {@code 400 (Bad Request)} if the customerServiceUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerServiceUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerServiceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-service-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerServiceUserDTO> partialUpdateCustomerServiceUser(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerServiceUserDTO customerServiceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerServiceUser partially : {}, {}", id, customerServiceUserDTO);
        if (customerServiceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerServiceUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerServiceUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerServiceUserDTO> result = customerServiceUserService.partialUpdate(customerServiceUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerServiceUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-service-users} : get all the customerServiceUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerServiceUsers in body.
     */
    @GetMapping("/customer-service-users")
    public ResponseEntity<List<CustomerServiceUserDTO>> getAllCustomerServiceUsers(
        CustomerServiceUserCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerServiceUsers by criteria: {}", criteria);
        Page<CustomerServiceUserDTO> page = customerServiceUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-service-users/count} : count all the customerServiceUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-service-users/count")
    public ResponseEntity<Long> countCustomerServiceUsers(CustomerServiceUserCriteria criteria) {
        log.debug("REST request to count CustomerServiceUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerServiceUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-service-users/:id} : get the "id" customerServiceUser.
     *
     * @param id the id of the customerServiceUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerServiceUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-service-users/{id}")
    public ResponseEntity<CustomerServiceUserDTO> getCustomerServiceUser(@PathVariable UUID id) {
        log.debug("REST request to get CustomerServiceUser : {}", id);
        Optional<CustomerServiceUserDTO> customerServiceUserDTO = customerServiceUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerServiceUserDTO);
    }

    /**
     * {@code DELETE  /customer-service-users/:id} : delete the "id" customerServiceUser.
     *
     * @param id the id of the customerServiceUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-service-users/{id}")
    public ResponseEntity<Void> deleteCustomerServiceUser(@PathVariable UUID id) {
        log.debug("REST request to delete CustomerServiceUser : {}", id);
        customerServiceUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
