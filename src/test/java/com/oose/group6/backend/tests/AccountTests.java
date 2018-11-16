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
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTests {

    @Autowired
    private MockMvc mvc;

    /**
     * Session ID obtained during tests
     */
    private static String sessionId;
    private static String urlLogin = "/api/v1/user/login/";
    private static String urlLogout = "/api/v1/user/logout/";
    private static String urlRegister = "/api/v1/user/register/";
    private static String urlChangePassword = "/api/v1/user/changemm/";

    @Test
    public void t1_01_contextLoads() throws Exception {
    }

    @Test
    public void t1_02_requestUnmappedEndpoint() throws Exception {
        mvc.perform(post(urlLogin+"v2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"o\",\"password\":\"123456\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void t2_01_loginWithCorrectCredential() throws Exception {
        ResultActions resultActions = TestsUtil.login(mvc);
        resultActions.andExpect(status().isOk());
        sessionId = TestsUtil.getLoginSessionId(resultActions);
    }

    @Test
    public void t2_02_loginWithInvalidUsername() throws Exception {
        TestsUtil.login(mvc, "patienttttttt", "123456")
                .andExpect(status().isNotFound());
    }

    @Test
    public void t2_02_loginWithWrongPassword() throws Exception {
        TestsUtil.login(mvc, "patient", "12345")
                .andExpect(status().isNotFound());
    }

    @Test
    public void t2_03_loginWithMalformedRequest_jsonMissingType() throws Exception {
        mvc.perform(post(urlLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{username:patient,password:12345}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t2_03_loginWithMalformedRequest_jsonGarbageContent() throws Exception {
        mvc.perform(post(urlLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{nocontent}"))
                .andExpect(status().is4xxClientError());
        mvc.perform(post(urlLogin))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t2_03_loginWithMalformedRequest_nonJsonContentType() throws Exception {
        mvc.perform(post(urlLogin))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_01_logoutWithoutSessionId() throws Exception {
        mvc.perform(get(urlLogout))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t3_02_logout() throws Exception {
        TestsUtil.logout(mvc, sessionId)
                .andExpect(status().isOk());
    }

    @Test
    public void t4_01_newUserRegistration() throws Exception {
        mvc.perform(post(urlRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"ggggggt2\",\"password\":\"123456\",\"userType\":\"P\",\"name\":\"aaa\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void t4_02_newUserRegistration_existingUsername() throws Exception {
        mvc.perform(post(urlRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"ggggggt2\",\"password\":\"123456\",\"userType\":\"P\",\"name\":\"aaa\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_03_newUserRegistration_shortUsername() throws Exception {
        mvc.perform(post(urlRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"a\",\"password\":\"123456\",\"userType\":\"P\",\"name\":\"aaa\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_04_newUserRegistration_emptyPassword() throws Exception {
        mvc.perform(post(urlRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"a\",\"password\":\"\",\"userType\":\"P\",\"name\":\"aaa\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_05_newUserRegistration_invalidUserType() throws Exception {
        mvc.perform(post(urlRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"a\",\"password\":\"\",\"userType\":\"\",\"name\":\"aaa\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t4_06_newUserRegistration_malformedRequest() throws Exception {
        mvc.perform(post(urlRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"a\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_01_changePassword() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlChangePassword)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\":\"123456\", \"newPassword\":\"1234567\"}"))
                .andExpect(status().isOk());
        mvc.perform(post(urlChangePassword)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\":\"1234567\", \"newPassword\":\"123456\"}"));
    }

    @Test
    public void t5_02_changePassword_fakeSessionId() throws Exception {
        sessionId = "null";
        mvc.perform(post(urlChangePassword)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\":\"123456\", \"newPassword\":\"1234567\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_03_changePassword_invalidOldPassword() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlChangePassword)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\":\"1234567\", \"newPassword\":\"1234567\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_04_changePassword_newPasswordTooShort() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlChangePassword)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPassword\":\"123456\", \"newPassword\":\"12345\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_05_changePassword_malformedJSON() throws Exception {
        sessionId = TestsUtil.getLoginSessionId(TestsUtil.login(mvc));
        mvc.perform(post(urlChangePassword)
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPasswor\":\"123456\", \"newPassword\":\"12345\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void t5_06_changePassword_malformedHeader() throws Exception {
        mvc.perform(post(urlChangePassword)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oldPasswor\":\"123456\", \"newPassword\":\"12345\"}"))
                .andExpect(status().is4xxClientError());
    }
}
