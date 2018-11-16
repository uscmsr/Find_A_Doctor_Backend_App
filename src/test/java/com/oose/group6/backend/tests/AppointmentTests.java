package com.oose.group6.backend.tests;

import com.oose.group6.backend.common.FusionConfig;
import com.oose.group6.backend.util.TestsUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentTests {

    @Autowired
    private MockMvc mvc;

    /**
     * Session ID obtained during tests
     */
    private static String sessionId;
    private static String urlApptDetail = "/api/v1/appointment/detail?id=%d";
    private static String urlNewAppt = "/api/v1/appointment/schedule";
    private static String urlFindDoctor = "/api/v1/appointment/finddoctor";
    private static String urlPastAppt = "/api/v1/appointment/past";
    private static String urlFutureAppt = "/api/v1/appointment/future";


    @Test
    public void t1_01_contextLoads() throws Exception {
    }

    @Test
    public void t2_01_getAppointmentDetail_withoutSessionId() throws Exception {
        mvc.perform(get(String.format(urlApptDetail, 1)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t2_02_getAppointmentDetail_fakeSessionId() throws Exception {
        mvc.perform(get(String.format(urlApptDetail, 1))
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, "233"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t2_03_getAppointmentDetail_asPatient() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(get(String.format(urlApptDetail, 1))
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().isOk());
    }

    @Test
    public void t2_04_getAppointmentDetail_asStaff() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(get(String.format(urlApptDetail, 1))
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().isOk());
    }

    @Test
    public void t2_05_getAppointmentDetail_nonParticipant() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "StevenRQuimby", "password"));
        mvc.perform(get(String.format(urlApptDetail, 1))
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_01_newAppointment_missingSessionId() throws Exception {
        mvc.perform(put(urlNewAppt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515700000\"," +
                        "\"endTime\":\"507515800000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_02_newAppointment_fakeSessionId() throws Exception {
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, "233")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515700000\"," +
                        "\"endTime\":\"507515800000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_03_newAppointment_malformedRequest() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"aaa\":\"staff\""+
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_04_newAppointment_asPatient() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515700000\"," +
                        "\"endTime\":\"507515800000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void t3_05_newAppointment_asDoctor() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515000000\"," +
                        "\"endTime\":\"507515100000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_06_newAppointment_timeConflict() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515700000\"," +
                        "\"endTime\":\"507515190000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_07_newAppointment_timeOverlapEnd() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515600000\"," +
                        "\"endTime\":\"507515700000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().isOk());
    }


    @Test
    public void t3_08_newAppointment_timeOverlapStart() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515400000\"," +
                        "\"endTime\":\"507515500000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().isOk());
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"staff\"," +
                        "\"startTime\":\"507515500000\"," +
                        "\"endTime\":\"507515600000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void t3_09_newAppointment_SamePatientSameTimeDifferentDoctor() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"StevenRQuimby\"," +
                        "\"startTime\":\"507515600000\"," +
                        "\"endTime\":\"507515700000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_10_newAppointment_DifferentPatientSameTimeDifferentDoctor() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "patient2", "123456"));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"StevenRQuimby\"," +
                        "\"startTime\":\"507515500000\"," +
                        "\"endTime\":\"507515600000\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void t3_11_newAppointment_timeTooShort() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "patient2", "123456"));
        mvc.perform(put(urlNewAppt)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"staffUsername\":\"StevenRQuimby\"," +
                        "\"startTime\":\"507515600000\"," +
                        "\"endTime\":\"507515600010\"," +
                        "\"location\":\"\"," +
                        "\"symptom\":\"dehydrated\"," +
                        "\"allergies\":\"no known allergy\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_01_findDoctor() throws Exception {
        mvc.perform(post(urlFindDoctor)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"symptom\":\"My hand joints hurt after playing cellphone\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void t4_02_findDoctor_malformedJson() throws Exception {
        mvc.perform(post(urlFindDoctor)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"symptommmm\":\"My hand joints hurt after playing cellphone\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_03_findDoctor_nonJson() throws Exception {
        mvc.perform(post(urlFindDoctor))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_01_viewPastAppt() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(get(urlPastAppt).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().isOk());
    }

    @Test
    public void t5_02_viewPastAppt_fakeSessionId() throws Exception {
        sessionId = "null";
        mvc.perform(get(urlPastAppt).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_01_viewFutureAppt() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(get(urlFutureAppt).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().isOk());
    }

    @Test
    public void t6_02_viewFutureAppt_fakeSessionId() throws Exception {
        sessionId = "null";
        mvc.perform(get(urlFutureAppt).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().is4xxClientError());
    }

}
