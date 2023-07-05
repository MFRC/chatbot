package com.saathratri.customerservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saathratri.customerservice.IntegrationTest;
import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.domain.Report;
import com.saathratri.customerservice.repository.ReportRepository;
import com.saathratri.customerservice.service.criteria.ReportCriteria;
import com.saathratri.customerservice.service.dto.ReportDTO;
import com.saathratri.customerservice.service.mapper.ReportMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportResourceIT {

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_REPORT_NUMBER = 1L;
    private static final Long UPDATED_REPORT_NUMBER = 2L;
    private static final Long SMALLER_REPORT_NUMBER = 1L - 1L;

    private static final String DEFAULT_MORE_HELP = "AAAAAAAAAA";
    private static final String UPDATED_MORE_HELP = "BBBBBBBBBB";

    private static final Integer DEFAULT_SATISFACTION = 1;
    private static final Integer UPDATED_SATISFACTION = 2;
    private static final Integer SMALLER_SATISFACTION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMockMvc;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .time(DEFAULT_TIME)
            .reportNumber(DEFAULT_REPORT_NUMBER)
            .moreHelp(DEFAULT_MORE_HELP)
            .satisfaction(DEFAULT_SATISFACTION);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report()
            .time(UPDATED_TIME)
            .reportNumber(UPDATED_REPORT_NUMBER)
            .moreHelp(UPDATED_MORE_HELP)
            .satisfaction(UPDATED_SATISFACTION);
        return report;
    }

    @BeforeEach
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();
        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        restReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testReport.getReportNumber()).isEqualTo(DEFAULT_REPORT_NUMBER);
        assertThat(testReport.getMoreHelp()).isEqualTo(DEFAULT_MORE_HELP);
        assertThat(testReport.getSatisfaction()).isEqualTo(DEFAULT_SATISFACTION);
    }

    @Test
    @Transactional
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        reportRepository.saveAndFlush(report);
        ReportDTO reportDTO = reportMapper.toDto(report);

        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].reportNumber").value(hasItem(DEFAULT_REPORT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].moreHelp").value(hasItem(DEFAULT_MORE_HELP)))
            .andExpect(jsonPath("$.[*].satisfaction").value(hasItem(DEFAULT_SATISFACTION)));
    }

    @Test
    @Transactional
    void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc
            .perform(get(ENTITY_API_URL_ID, report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.reportNumber").value(DEFAULT_REPORT_NUMBER.intValue()))
            .andExpect(jsonPath("$.moreHelp").value(DEFAULT_MORE_HELP))
            .andExpect(jsonPath("$.satisfaction").value(DEFAULT_SATISFACTION));
    }

    @Test
    @Transactional
    void getReportsByIdFiltering() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        UUID id = report.getId();

        defaultReportShouldBeFound("id.equals=" + id);
        defaultReportShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllReportsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where time equals to DEFAULT_TIME
        defaultReportShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the reportList where time equals to UPDATED_TIME
        defaultReportShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllReportsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where time in DEFAULT_TIME or UPDATED_TIME
        defaultReportShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the reportList where time equals to UPDATED_TIME
        defaultReportShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    void getAllReportsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where time is not null
        defaultReportShouldBeFound("time.specified=true");

        // Get all the reportList where time is null
        defaultReportShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber equals to DEFAULT_REPORT_NUMBER
        defaultReportShouldBeFound("reportNumber.equals=" + DEFAULT_REPORT_NUMBER);

        // Get all the reportList where reportNumber equals to UPDATED_REPORT_NUMBER
        defaultReportShouldNotBeFound("reportNumber.equals=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber in DEFAULT_REPORT_NUMBER or UPDATED_REPORT_NUMBER
        defaultReportShouldBeFound("reportNumber.in=" + DEFAULT_REPORT_NUMBER + "," + UPDATED_REPORT_NUMBER);

        // Get all the reportList where reportNumber equals to UPDATED_REPORT_NUMBER
        defaultReportShouldNotBeFound("reportNumber.in=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber is not null
        defaultReportShouldBeFound("reportNumber.specified=true");

        // Get all the reportList where reportNumber is null
        defaultReportShouldNotBeFound("reportNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber is greater than or equal to DEFAULT_REPORT_NUMBER
        defaultReportShouldBeFound("reportNumber.greaterThanOrEqual=" + DEFAULT_REPORT_NUMBER);

        // Get all the reportList where reportNumber is greater than or equal to UPDATED_REPORT_NUMBER
        defaultReportShouldNotBeFound("reportNumber.greaterThanOrEqual=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber is less than or equal to DEFAULT_REPORT_NUMBER
        defaultReportShouldBeFound("reportNumber.lessThanOrEqual=" + DEFAULT_REPORT_NUMBER);

        // Get all the reportList where reportNumber is less than or equal to SMALLER_REPORT_NUMBER
        defaultReportShouldNotBeFound("reportNumber.lessThanOrEqual=" + SMALLER_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber is less than DEFAULT_REPORT_NUMBER
        defaultReportShouldNotBeFound("reportNumber.lessThan=" + DEFAULT_REPORT_NUMBER);

        // Get all the reportList where reportNumber is less than UPDATED_REPORT_NUMBER
        defaultReportShouldBeFound("reportNumber.lessThan=" + UPDATED_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByReportNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where reportNumber is greater than DEFAULT_REPORT_NUMBER
        defaultReportShouldNotBeFound("reportNumber.greaterThan=" + DEFAULT_REPORT_NUMBER);

        // Get all the reportList where reportNumber is greater than SMALLER_REPORT_NUMBER
        defaultReportShouldBeFound("reportNumber.greaterThan=" + SMALLER_REPORT_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByMoreHelpIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where moreHelp equals to DEFAULT_MORE_HELP
        defaultReportShouldBeFound("moreHelp.equals=" + DEFAULT_MORE_HELP);

        // Get all the reportList where moreHelp equals to UPDATED_MORE_HELP
        defaultReportShouldNotBeFound("moreHelp.equals=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllReportsByMoreHelpIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where moreHelp in DEFAULT_MORE_HELP or UPDATED_MORE_HELP
        defaultReportShouldBeFound("moreHelp.in=" + DEFAULT_MORE_HELP + "," + UPDATED_MORE_HELP);

        // Get all the reportList where moreHelp equals to UPDATED_MORE_HELP
        defaultReportShouldNotBeFound("moreHelp.in=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllReportsByMoreHelpIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where moreHelp is not null
        defaultReportShouldBeFound("moreHelp.specified=true");

        // Get all the reportList where moreHelp is null
        defaultReportShouldNotBeFound("moreHelp.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByMoreHelpContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where moreHelp contains DEFAULT_MORE_HELP
        defaultReportShouldBeFound("moreHelp.contains=" + DEFAULT_MORE_HELP);

        // Get all the reportList where moreHelp contains UPDATED_MORE_HELP
        defaultReportShouldNotBeFound("moreHelp.contains=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllReportsByMoreHelpNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where moreHelp does not contain DEFAULT_MORE_HELP
        defaultReportShouldNotBeFound("moreHelp.doesNotContain=" + DEFAULT_MORE_HELP);

        // Get all the reportList where moreHelp does not contain UPDATED_MORE_HELP
        defaultReportShouldBeFound("moreHelp.doesNotContain=" + UPDATED_MORE_HELP);
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction equals to DEFAULT_SATISFACTION
        defaultReportShouldBeFound("satisfaction.equals=" + DEFAULT_SATISFACTION);

        // Get all the reportList where satisfaction equals to UPDATED_SATISFACTION
        defaultReportShouldNotBeFound("satisfaction.equals=" + UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction in DEFAULT_SATISFACTION or UPDATED_SATISFACTION
        defaultReportShouldBeFound("satisfaction.in=" + DEFAULT_SATISFACTION + "," + UPDATED_SATISFACTION);

        // Get all the reportList where satisfaction equals to UPDATED_SATISFACTION
        defaultReportShouldNotBeFound("satisfaction.in=" + UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction is not null
        defaultReportShouldBeFound("satisfaction.specified=true");

        // Get all the reportList where satisfaction is null
        defaultReportShouldNotBeFound("satisfaction.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction is greater than or equal to DEFAULT_SATISFACTION
        defaultReportShouldBeFound("satisfaction.greaterThanOrEqual=" + DEFAULT_SATISFACTION);

        // Get all the reportList where satisfaction is greater than or equal to UPDATED_SATISFACTION
        defaultReportShouldNotBeFound("satisfaction.greaterThanOrEqual=" + UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction is less than or equal to DEFAULT_SATISFACTION
        defaultReportShouldBeFound("satisfaction.lessThanOrEqual=" + DEFAULT_SATISFACTION);

        // Get all the reportList where satisfaction is less than or equal to SMALLER_SATISFACTION
        defaultReportShouldNotBeFound("satisfaction.lessThanOrEqual=" + SMALLER_SATISFACTION);
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction is less than DEFAULT_SATISFACTION
        defaultReportShouldNotBeFound("satisfaction.lessThan=" + DEFAULT_SATISFACTION);

        // Get all the reportList where satisfaction is less than UPDATED_SATISFACTION
        defaultReportShouldBeFound("satisfaction.lessThan=" + UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void getAllReportsBySatisfactionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where satisfaction is greater than DEFAULT_SATISFACTION
        defaultReportShouldNotBeFound("satisfaction.greaterThan=" + DEFAULT_SATISFACTION);

        // Get all the reportList where satisfaction is greater than SMALLER_SATISFACTION
        defaultReportShouldBeFound("satisfaction.greaterThan=" + SMALLER_SATISFACTION);
    }

    @Test
    @Transactional
    void getAllReportsByEndIsEqualToSomething() throws Exception {
        End end;
        if (TestUtil.findAll(em, End.class).isEmpty()) {
            reportRepository.saveAndFlush(report);
            end = EndResourceIT.createEntity(em);
        } else {
            end = TestUtil.findAll(em, End.class).get(0);
        }
        em.persist(end);
        em.flush();
        report.setEnd(end);
        end.setReport(report);
        reportRepository.saveAndFlush(report);
        UUID endId = end.getId();

        // Get all the reportList where end equals to endId
        defaultReportShouldBeFound("endId.equals=" + endId);

        // Get all the reportList where end equals to UUID.randomUUID()
        defaultReportShouldNotBeFound("endId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].reportNumber").value(hasItem(DEFAULT_REPORT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].moreHelp").value(hasItem(DEFAULT_MORE_HELP)))
            .andExpect(jsonPath("$.[*].satisfaction").value(hasItem(DEFAULT_SATISFACTION)));

        // Check, that the count call also returns 1
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport.time(UPDATED_TIME).reportNumber(UPDATED_REPORT_NUMBER).moreHelp(UPDATED_MORE_HELP).satisfaction(UPDATED_SATISFACTION);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testReport.getReportNumber()).isEqualTo(UPDATED_REPORT_NUMBER);
        assertThat(testReport.getMoreHelp()).isEqualTo(UPDATED_MORE_HELP);
        assertThat(testReport.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void putNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(UUID.randomUUID());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(UUID.randomUUID());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(UUID.randomUUID());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.reportNumber(UPDATED_REPORT_NUMBER).moreHelp(UPDATED_MORE_HELP).satisfaction(UPDATED_SATISFACTION);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testReport.getReportNumber()).isEqualTo(UPDATED_REPORT_NUMBER);
        assertThat(testReport.getMoreHelp()).isEqualTo(UPDATED_MORE_HELP);
        assertThat(testReport.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport
            .time(UPDATED_TIME)
            .reportNumber(UPDATED_REPORT_NUMBER)
            .moreHelp(UPDATED_MORE_HELP)
            .satisfaction(UPDATED_SATISFACTION);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testReport.getReportNumber()).isEqualTo(UPDATED_REPORT_NUMBER);
        assertThat(testReport.getMoreHelp()).isEqualTo(UPDATED_MORE_HELP);
        assertThat(testReport.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
    }

    @Test
    @Transactional
    void patchNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(UUID.randomUUID());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(UUID.randomUUID());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(UUID.randomUUID());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Delete the report
        restReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, report.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
