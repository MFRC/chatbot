package com.saathratri.bookingservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.bookingservice.IntegrationTest;
import com.saathratri.bookingservice.domain.HotelInfo;
import com.saathratri.bookingservice.repository.HotelInfoRepository;
import com.saathratri.bookingservice.service.dto.HotelInfoDTO;
import com.saathratri.bookingservice.service.mapper.HotelInfoMapper;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HotelInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HotelInfoResourceIT {

    private static final String DEFAULT_HOTEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HOTEL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hotel-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private HotelInfoRepository hotelInfoRepository;

    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHotelInfoMockMvc;

    private HotelInfo hotelInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HotelInfo createEntity(EntityManager em) {
        HotelInfo hotelInfo = new HotelInfo().hotelName(DEFAULT_HOTEL_NAME);
        return hotelInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HotelInfo createUpdatedEntity(EntityManager em) {
        HotelInfo hotelInfo = new HotelInfo().hotelName(UPDATED_HOTEL_NAME);
        return hotelInfo;
    }

    @BeforeEach
    public void initTest() {
        hotelInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createHotelInfo() throws Exception {
        int databaseSizeBeforeCreate = hotelInfoRepository.findAll().size();
        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);
        restHotelInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeCreate + 1);
        HotelInfo testHotelInfo = hotelInfoList.get(hotelInfoList.size() - 1);
        assertThat(testHotelInfo.getHotelName()).isEqualTo(DEFAULT_HOTEL_NAME);
    }

    @Test
    @Transactional
    void createHotelInfoWithExistingId() throws Exception {
        // Create the HotelInfo with an existing ID
        hotelInfoRepository.saveAndFlush(hotelInfo);
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        int databaseSizeBeforeCreate = hotelInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotelInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHotelInfos() throws Exception {
        // Initialize the database
        hotelInfoRepository.saveAndFlush(hotelInfo);

        // Get all the hotelInfoList
        restHotelInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotelInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].hotelName").value(hasItem(DEFAULT_HOTEL_NAME)));
    }

    @Test
    @Transactional
    void getHotelInfo() throws Exception {
        // Initialize the database
        hotelInfoRepository.saveAndFlush(hotelInfo);

        // Get the hotelInfo
        restHotelInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, hotelInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hotelInfo.getId().toString()))
            .andExpect(jsonPath("$.hotelName").value(DEFAULT_HOTEL_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHotelInfo() throws Exception {
        // Get the hotelInfo
        restHotelInfoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHotelInfo() throws Exception {
        // Initialize the database
        hotelInfoRepository.saveAndFlush(hotelInfo);

        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();

        // Update the hotelInfo
        HotelInfo updatedHotelInfo = hotelInfoRepository.findById(hotelInfo.getId()).get();
        // Disconnect from session so that the updates on updatedHotelInfo are not directly saved in db
        em.detach(updatedHotelInfo);
        updatedHotelInfo.hotelName(UPDATED_HOTEL_NAME);
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(updatedHotelInfo);

        restHotelInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hotelInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
        HotelInfo testHotelInfo = hotelInfoList.get(hotelInfoList.size() - 1);
        assertThat(testHotelInfo.getHotelName()).isEqualTo(UPDATED_HOTEL_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHotelInfo() throws Exception {
        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();
        hotelInfo.setId(UUID.randomUUID());

        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hotelInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHotelInfo() throws Exception {
        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();
        hotelInfo.setId(UUID.randomUUID());

        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHotelInfo() throws Exception {
        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();
        hotelInfo.setId(UUID.randomUUID());

        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelInfoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHotelInfoWithPatch() throws Exception {
        // Initialize the database
        hotelInfoRepository.saveAndFlush(hotelInfo);

        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();

        // Update the hotelInfo using partial update
        HotelInfo partialUpdatedHotelInfo = new HotelInfo();
        partialUpdatedHotelInfo.setId(hotelInfo.getId());

        partialUpdatedHotelInfo.hotelName(UPDATED_HOTEL_NAME);

        restHotelInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHotelInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHotelInfo))
            )
            .andExpect(status().isOk());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
        HotelInfo testHotelInfo = hotelInfoList.get(hotelInfoList.size() - 1);
        assertThat(testHotelInfo.getHotelName()).isEqualTo(UPDATED_HOTEL_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHotelInfoWithPatch() throws Exception {
        // Initialize the database
        hotelInfoRepository.saveAndFlush(hotelInfo);

        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();

        // Update the hotelInfo using partial update
        HotelInfo partialUpdatedHotelInfo = new HotelInfo();
        partialUpdatedHotelInfo.setId(hotelInfo.getId());

        partialUpdatedHotelInfo.hotelName(UPDATED_HOTEL_NAME);

        restHotelInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHotelInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHotelInfo))
            )
            .andExpect(status().isOk());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
        HotelInfo testHotelInfo = hotelInfoList.get(hotelInfoList.size() - 1);
        assertThat(testHotelInfo.getHotelName()).isEqualTo(UPDATED_HOTEL_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHotelInfo() throws Exception {
        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();
        hotelInfo.setId(UUID.randomUUID());

        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hotelInfoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHotelInfo() throws Exception {
        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();
        hotelInfo.setId(UUID.randomUUID());

        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHotelInfo() throws Exception {
        int databaseSizeBeforeUpdate = hotelInfoRepository.findAll().size();
        hotelInfo.setId(UUID.randomUUID());

        // Create the HotelInfo
        HotelInfoDTO hotelInfoDTO = hotelInfoMapper.toDto(hotelInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelInfoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hotelInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HotelInfo in the database
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHotelInfo() throws Exception {
        // Initialize the database
        hotelInfoRepository.saveAndFlush(hotelInfo);

        int databaseSizeBeforeDelete = hotelInfoRepository.findAll().size();

        // Delete the hotelInfo
        restHotelInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, hotelInfo.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HotelInfo> hotelInfoList = hotelInfoRepository.findAll();
        assertThat(hotelInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
