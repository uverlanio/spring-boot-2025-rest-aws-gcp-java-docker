package br.com.sbrwgjd.integrationtests.swagger;

import br.com.sbrwgjd.config.TestConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //pega a porta do properties de teste
public class SwaggerIntegrationTest {

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
