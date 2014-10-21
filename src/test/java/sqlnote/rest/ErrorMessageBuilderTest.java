package sqlnote.rest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorMessageBuilderTest {
    
    @Test
    public void testBuild() throws Exception {
        // exercise
        String jsonString = ErrorMessageBuilder.build("error message");
        
        // verify
        assertThat(jsonString, is("{\"message\":\"error message\"}"));
    }
}
