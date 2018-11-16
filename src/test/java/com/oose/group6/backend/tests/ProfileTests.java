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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProfileTests {

    @Autowired
    private MockMvc mvc;

    /**
     * Session ID obtained during tests
     */
    private static String sessionId;
    private static String urlProfileP = "/api/v1/profile/patient/";
    private static String urlProfileS = "/api/v1/profile/public/staff?username=%s";
    private static String urlStaffPCP = "/api/v1/profile/staff/pcp";
    private static String urlProfilePUpdate = "/api/v1/profile/patient/update";
    private static String urlProfileSUpdate = "/api/v1/profile/staff/update";
    private static String urlDepartmentList = "/api/v1/profile/department/all";
    private static String urlMedicalCenterList = "/api/v1/profile/medicalcenter/all";

    @Test
    public void t1_01_contextLoads() throws Exception {
    }

    @Test
    public void t2_01_getPatientProfile() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(get(urlProfileP)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().isOk());
    }

    @Test
    public void t2_02_getPatientProfile_expiredSession() throws Exception {
        TestsUtil.logout(mvc, sessionId);
        mvc.perform(get(urlProfileP)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t2_03_getPatientProfile_missingSessionId() throws Exception {
        mvc.perform(get(urlProfileP))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t2_04_getPatientProfile_fakeSessionId() throws Exception {
        mvc.perform(get(urlProfileP)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, "233"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_01_getStaffProfile() throws Exception {
        mvc.perform(get(String.format(urlProfileS, "staff")))
                .andExpect(status().isOk());
    }

    @Test
    public void t3_02_getStaffProfile_fakeUsername() throws Exception {
        mvc.perform(get(String.format(urlProfileS, "staffffffffffff")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_03_getStaffProfile_emptyUsername() throws Exception {
        mvc.perform(get(String.format(urlProfileS, "")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_01_getStaffPCPList_withoutLogIn() throws Exception {
        TestsUtil.logout(mvc, sessionId);
        mvc.perform(get(urlStaffPCP).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_02_getStaffPCPList_fakeSessionId() throws Exception {
        mvc.perform(get(urlStaffPCP).header(FusionConfig.HEADER_FIELD_SESSION_ID, "233"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_03_getStaffPCPList() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(get(urlStaffPCP).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId))
                .andExpect(status().isOk());
    }

    @Test
    public void t5_01_updatePatientProfile() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"new name\"," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":1512526008000," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"F\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"staff\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void t5_02_updatePatientProfile_malformedJSON() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_03_updatePatientProfile_emptyName() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\" \"," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":1512526008000," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"F\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"staff\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_04_updatePatientProfile_nullName() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":null," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":1512526008000," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"F\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"staff\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_05_updatePatientProfile_StringDoB() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"new name\"," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":\"str\"," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"F\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"staff\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_06_updatePatientProfile_UnidentifiedGender() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"new name\"," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":\"str\"," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"f\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"staff\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_07_updatePatientProfile_invalidPcpUsername() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"new name\"," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":\"str\"," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"f\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"a\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_08_updatePatientProfile_pcpUsernamePatient() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlProfilePUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"new name\"," +
                        "\"address\":null," +
                        "\"race\":null," +
                        "\"dob\":\"str\"," +
                        "\"emergencyContactName\":null," +
                        "\"emergencyContactPhone\":null," +
                        "\"communicationPref\":null," +
                        "\"medicalPref\":null," +
                        "\"staffPref\":null," +
                        "\"email\":null," +
                        "\"phone\":\"13800000000\"," +
                        "\"gender\":\"f\"," +
                        "\"photoUrl\":null," +
                        "\"pcpUsername\":\"patient\"" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_01_updateStaffProfile() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\":\"new name\",\n" +
                        "\t\"speciality\":\"cancer\",\n" +
                        "\t\"email\":null,\n" +
                        "\t\"phone\":\"13800000000\",\n" +
                        "\t\"gender\":\"F\",\n" +
                        "\t\"photoUrl\":null,\n" +
                        "\t\"departmentId\":1,\n" +
                        "\t\"medicalCenterId\":1\n" +
                        "}\n"))
                .andExpect(status().isOk());
    }

    @Test
    public void t6_02_updateStaffProfile_malformedJSON() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_03_updateStaffProfile_emptyName() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\":\" \",\n" +
                        "\t\"speciality\":\"cancer\",\n" +
                        "\t\"email\":null,\n" +
                        "\t\"phone\":\"13800000000\",\n" +
                        "\t\"gender\":\"F\",\n" +
                        "\t\"photoUrl\":null,\n" +
                        "\t\"departmentId\":1,\n" +
                        "\t\"medicalCenterId\":1\n" +
                        "}\n"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_04_updateStaffProfile_nullName() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\":null,\n" +
                        "\t\"speciality\":\"cancer\",\n" +
                        "\t\"email\":null,\n" +
                        "\t\"phone\":\"13800000000\",\n" +
                        "\t\"gender\":\"F\",\n" +
                        "\t\"photoUrl\":null,\n" +
                        "\t\"departmentId\":1,\n" +
                        "\t\"medicalCenterId\":1\n" +
                        "}\n"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_05_updateStaffProfile_UnidentifiedGender() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\":\"new name\",\n" +
                        "\t\"speciality\":\"cancer\",\n" +
                        "\t\"email\":null,\n" +
                        "\t\"phone\":\"13800000000\",\n" +
                        "\t\"gender\":\"f\",\n" +
                        "\t\"photoUrl\":null,\n" +
                        "\t\"departmentId\":1,\n" +
                        "\t\"medicalCenterId\":1\n" +
                        "}\n"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_06_updateStaffProfile_invalidDepartmentId() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\":\"new name\",\n" +
                        "\t\"speciality\":\"cancer\",\n" +
                        "\t\"email\":null,\n" +
                        "\t\"phone\":\"13800000000\",\n" +
                        "\t\"gender\":\"F\",\n" +
                        "\t\"photoUrl\":null,\n" +
                        "\t\"departmentId\":10000,\n" +
                        "\t\"medicalCenterId\":1\n" +
                        "}\n"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t6_07_updateStaffProfile_invalidMedicalCenterId() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc, "staff", "123456"));
        mvc.perform(post(urlProfileSUpdate).header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"name\":\"new name\",\n" +
                        "\t\"speciality\":\"cancer\",\n" +
                        "\t\"email\":null,\n" +
                        "\t\"phone\":\"13800000000\",\n" +
                        "\t\"gender\":\"F\",\n" +
                        "\t\"photoUrl\":null,\n" +
                        "\t\"departmentId\":1,\n" +
                        "\t\"medicalCenterId\":10000\n" +
                        "}\n"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t7_01_getDepartmentList() throws Exception {
        mvc.perform(get(urlDepartmentList))
                .andExpect(status().isOk());
    }

    @Test
    public void t8_01_getDepartmentList() throws Exception {
        mvc.perform(get(urlMedicalCenterList))
                .andExpect(status().isOk());
    }

}
