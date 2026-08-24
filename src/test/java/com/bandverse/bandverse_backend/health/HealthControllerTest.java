package com.bandverse.bandverse_backend.health;

import com.bandverse.bandverse_backend.business.health.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.bandverse.bandverse_backend.util.response_builders.failure.FailureResponseBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FailureResponseBuilder failureResponseBuilder;

    @Test
    void shouldReturnHealthyStatus() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                      "status": "UP"
                    }
                    """));
    }
}