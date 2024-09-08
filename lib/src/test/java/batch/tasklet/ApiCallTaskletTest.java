package batch.tasklet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApiCallTaskletTest {

    @InjectMocks
    private ApiCallTasklet apiCallTasklet;

    @Mock
    private HttpURLConnection connection;

    @Mock
    private StepContribution stepContribution;

    @Mock
    private ChunkContext chunkContext;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // URL 및 연결 설정을 Mock
        URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        when(connection.getURL()).thenReturn(url);
        when(connection.getInputStream()).thenReturn(mock(InputStream.class));
    }

    @Test
    public void testApiCallSuccess() throws Exception {
        // 실행
        RepeatStatus status = apiCallTasklet.execute(stepContribution, chunkContext);

        // 검증
        assertEquals(RepeatStatus.FINISHED, status);
    }

    @Test
    public void testApiCallFailure() throws Exception {
        // Expect an IOException to be thrown
        thrown.expect(IOException.class);
        thrown.expectMessage("Network error");

        // Simulate IOException
        when(connection.getInputStream()).thenThrow(new IOException("Network error"));

        // Execute
        apiCallTasklet.execute(stepContribution, chunkContext);
    }

}
