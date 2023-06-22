package com.saathratri.bookingservice.web.rest;

import com.saathratri.bookingservice.repository.CustomerInfoRepository;
import com.saathratri.bookingservice.service.CustomerInfoService;
import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import com.saathratri.bookingservice.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.saathratri.bookingservice.domain.CustomerInfo}.
 */
@RestController
@RequestMapping("/api")
public class CustomerInfoResource {

    private final Logger log = LoggerFactory.getLogger(CustomerInfoResource.class);

    private static final String ENTITY_NAME = "bookingserviceCustomerInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerInfoService customerInfoService;

    private final CustomerInfoRepository customerInfoRepository;

    public CustomerInfoResource(CustomerInfoService customerInfoService, CustomerInfoRepository customerInfoRepository) {
        this.customerInfoService = customerInfoService;
        this.customerInfoRepository = customerInfoRepository;
    }

    /**
     * {@code POST  /customer-infos} : Create a new customerInfo.
     *
     * @param customerInfoDTO the customerInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerInfoDTO, or with status {@code 400 (Bad Request)} if the customerInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-infos")
    public ResponseEntity<CustomerInfoDTO> createCustomerInfo(@Valid @RequestBody CustomerInfoDTO customerInfoDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerInfo : {}", customerInfoDTO);
        if (customerInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerInfoDTO result = customerInfoService.save(customerInfoDTO);
        return ResponseEntity
            .created(new URI("/api/customer-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-infos/:id} : Updates an existing customerInfo.
     *
     * @param id the id of the customerInfoDTO to save.
     * @param customerInfoDTO the customerInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerInfoDTO,
     * or with status {@code 400 (Bad Request)} if the customerInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-infos/{id}")
    public ResponseEntity<CustomerInfoDTO> updateCustomerInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CustomerInfoDTO customerInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerInfo : {}, {}", id, customerInfoDTO);
        if (customerInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerInfoDTO result = customerInfoService.update(customerInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-infos/:id} : Partial updates given fields of an existing customerInfo, field will ignore if it is null
     *
     * @param id the id of the customerInfoDTO to save.
     * @param customerInfoDTO the customerInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerInfoDTO,
     * or with status {@code 400 (Bad Request)} if the customerInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerInfoDTO> partialUpdateCustomerInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerInfoDTO customerInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerInfo partially : {}, {}", id, customerInfoDTO);
        if (customerInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerInfoDTO> result = customerInfoService.partialUpdate(customerInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-infos} : get all the customerInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerInfos in body.
     */
    @GetMapping("/customer-infos")
    public ResponseEntity<List<CustomerInfoDTO>> getAllCustomerInfos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CustomerInfos");
        Page<CustomerInfoDTO> page = customerInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-infos/:id} : get the "id" customerInfo.
     *
     * @param id the id of the customerInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-infos/{id}")
    public ResponseEntity<CustomerInfoDTO> getCustomerInfo(@PathVariable UUID id) {
        log.debug("REST request to get CustomerInfo : {}", id);
        Optional<CustomerInfoDTO> customerInfoDTO = customerInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerInfoDTO);
    }

    /**
     * {@code DELETE  /customer-infos/:id} : delete the "id" customerInfo.
     *
     * @param id the id of the customerInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-infos/{id}")
    public ResponseEntity<Void> deleteCustomerInfo(@PathVariable UUID id) {
        log.debug("REST request to delete CustomerInfo : {}", id);
        customerInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
