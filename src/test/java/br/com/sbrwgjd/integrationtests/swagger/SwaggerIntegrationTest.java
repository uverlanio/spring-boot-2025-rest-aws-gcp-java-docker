package br.com.sbrwgjd.integrationtests.swagger;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.integrationtests.testcontainers.*;
import org.junit.*;
import org.springframework.boot.test.context.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //pega a porta do properties de teste
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void shouldDisplaySwaggerUIPage(){
            var content = given()
                .basePath("/swagger-ui/index.html")
                    .port(TestConfigs.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

            assertTrue(content.contains("Swagger UI"));
    }
}
