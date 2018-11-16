package com.oose.group6.backend.util;

import com.oose.group6.backend.common.FusionConfig;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestsUtil {

    public static ResultActions login(MockMvc mvc) throws Exception {
        return login(mvc, "patient", "123456");
    }

    public static ResultActions login(MockMvc mvc, String username, String password) throws Exception {
        return mvc.perform(post("/api/v1/user/login/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\""+username+"\",\"password\":\""+password+"\"}"));
    }

    public static String getLoginSessionId(ResultActions resultActions) throws Exception {
        String resultStr = resultActions.andReturn().getResponse().getContentAsString();
        Pattern p = Pattern.compile("(.*)(\"sessionId\" *: *\")(.*)(\",)(.*)(\",)(.*)");
        Matcher m = p.matcher(resultStr);
        if (m.find()) {
            return m.replaceFirst("$3");
        } else throw new IllegalArgumentException("Cannot parse session_id");
    }

    public static ResultActions logout(MockMvc mvc, String sessionId) throws Exception {
        return mvc.perform(get("/api/v1/user/logout/")
                .header(FusionConfig.HEADER_FIELD_SESSION_ID, sessionId));
    }

}
