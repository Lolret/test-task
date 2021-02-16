import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.skoltech.sensors.development.Application;
import com.skoltech.sensors.development.domain.entity.Measure;
import com.skoltech.sensors.development.repository.MeasureRepo;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@WebMvcTest(MeasureController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {SkoltechIntegrationTest.Initializer.class})
class SkoltechIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeasureRepo measureRepo;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")
            .withDatabaseName("sensor")
            .withUsername("user1")
            .withPassword("1qaz2wsx!!");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            postgreSQLContainer.start();
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=create-drop"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @BeforeEach
    public void eachSetup() {
        objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        Measure m1 = new Measure();
        m1.setId(1L);
        m1.setValue(1.0);
        m1.setTime(10L);

        measureRepo.saveAll(Arrays.asList(m1));
    }

    @Test
    void testGetMeasuresWithValue_LessThanEqualClause() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/history?time=10"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetMeasures() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/history?time=lt(10)"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }
}
