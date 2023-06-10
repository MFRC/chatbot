package com.saathratri.bookingservice.web.rest;

import com.saathratri.bookingservice.repository.HotelInfoRepository;
import com.saathratri.bookingservice.service.HotelInfoService;
import com.saathratri.bookingservice.service.dto.HotelInfoDTO;
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
 * REST controller for managing {@link com.saathratri.bookingservice.domain.HotelInfo}.
 */
@RestController
@RequestMapping("/api")
public class HotelInfoResource {

    private final Logger log = LoggerFactory.getLogger(HotelInfoResource.class);

    private static final String ENTITY_NAME = "bookingserviceHotelInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HotelInfoService hotelInfoService;

    private final HotelInfoRepository hotelInfoRepository;

    public HotelInfoResource(HotelInfoService hotelInfoService, HotelInfoRepository hotelInfoRepository) {
        this.hotelInfoService = hotelInfoService;
        this.hotelInfoRepository = hotelInfoRepository;
    }

    /**
     * {@code POST  /hotel-infos} : Create a new hotelInfo.
     *
     * @param hotelInfoDTO the hotelInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hotelInfoDTO, or with status {@code 400 (Bad Request)} if the hotelInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hotel-infos")
    public ResponseEntity<HotelInfoDTO> createHotelInfo(@Valid @RequestBody HotelInfoDTO hotelInfoDTO) throws URISyntaxException {
        log.debug("REST request to save HotelInfo : {}", hotelInfoDTO);
        if (hotelInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new hotelInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HotelInfoDTO result = hotelInfoService.save(hotelInfoDTO);
        return ResponseEntity
            .created(new URI("/api/hotel-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hotel-infos/:id} : Updates an existing hotelInfo.
     *
     * @param id the id of the hotelInfoDTO to save.
     * @param hotelInfoDTO the hotelInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hotelInfoDTO,
     * or with status {@code 400 (Bad Request)} if the hotelInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hotelInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hotel-infos/{id}")
    public ResponseEntity<HotelInfoDTO> updateHotelInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody HotelInfoDTO hotelInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HotelInfo : {}, {}", id, hotelInfoDTO);
        if (hotelInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hotelInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotelInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HotelInfoDTO result = hotelInfoService.update(hotelInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hotelInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hotel-infos/:id} : Partial updates given fields of an existing hotelInfo, field will ignore if it is null
     *
     * @param id the id of the hotelInfoDTO to save.
     * @param hotelInfoDTO the hotelInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hotelInfoDTO,
     * or with status {@code 400 (Bad Request)} if the hotelInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hotelInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hotelInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hotel-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HotelInfoDTO> partialUpdateHotelInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody HotelInfoDTO hotelInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HotelInfo partially : {}, {}", id, hotelInfoDTO);
        if (hotelInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hotelInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotelInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HotelInfoDTO> result = hotelInfoService.partialUpdate(hotelInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hotelInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hotel-infos} : get all the hotelInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hotelInfos in body.
     */
    @GetMapping("/hotel-infos")
    public ResponseEntity<List<HotelInfoDTO>> getAllHotelInfos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HotelInfos");
        Page<HotelInfoDTO> page = hotelInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hotel-infos/:id} : get the "id" hotelInfo.
     *
     * @param id the id of the hotelInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hotelInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hotel-infos/{id}")
    public ResponseEntity<HotelInfoDTO> getHotelInfo(@PathVariable UUID id) {
        log.debug("REST request to get HotelInfo : {}", id);
        Optional<HotelInfoDTO> hotelInfoDTO = hotelInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hotelInfoDTO);
    }

    /**
     * {@code DELETE  /hotel-infos/:id} : delete the "id" hotelInfo.
     *
     * @param id the id of the hotelInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hotel-infos/{id}")
    public ResponseEntity<Void> deleteHotelInfo(@PathVariable UUID id) {
        log.debug("REST request to delete HotelInfo : {}", id);
        hotelInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
