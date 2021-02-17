import com.skoltech.sensors.development.controller.MeasureController;
import com.skoltech.sensors.development.domain.entity.Measure;
import com.skoltech.sensors.development.service.MeasureService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

class ScoltechControllerUnitTest {

    private MeasureService measureService =
            mock(MeasureService.class);

    private MeasureController measureController =
            new MeasureController(measureService, null);

    @Test
    void testSaveMeasures_httpStatusIs_204() {
        // given
        Measure m1 = new Measure();
        Measure m2 = new Measure();
        m1.setId(1L);       m2.setId(2L);
        m1.setValue(10.0);  m2.setValue(20.0);
        m1.setTime(100L);   m2.setTime(200L);
        // when
        List<Measure> measures = Arrays.asList(m1, m2);
        given(measureService
                .createSensor(eq(measures)))
                .willReturn(measures);

        ResponseEntity response = measureController
                .createResources(measures);

        // then
        then(measureService)
                .should()
                .createSensor(eq(measures));

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }
}
