package com.example.jimmy_fturgunov;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JimmyFturgunovApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {

    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }


    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper() {{
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            registerModule(new JavaTimeModule());
        }};
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper() {{
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            registerModule(new JavaTimeModule());
        }};
        return objectMapper.readValue(json, clazz);
    }
    protected <T> T mapFromJson(String json, TypeReference<T> typeReference) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper() {{
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            registerModule(new JavaTimeModule());
        }};
        objectMapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
        return objectMapper.readValue(json, typeReference);
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract class IgnoreHibernatePropertiesInJackson{ };

    public void executeScript(String resource) {
        try {
            InputStream is = AbstractTest.class.getResourceAsStream(resource);
            String script = IOUtils.toString(is);
            Connection conn = dataSource.getConnection();
            conn.createStatement().execute(script);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
