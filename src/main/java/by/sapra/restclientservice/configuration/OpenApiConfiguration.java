package by.sapra.restclientservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Value(value = "${server.port}")
    private Integer port;
    @Value(value = "${server.host}")
    private String host;
    @Bean
    public OpenAPI openApiDescription() {
        Server localhost = new Server();
        localhost.setUrl(host + ":" + port);
        localhost.setDescription("local env");

        Server prodServer = new Server();
        prodServer.setUrl("http://som.prod.url");
        prodServer.setDescription("Production env");

        Contact contact = new Contact();
        contact.setEmail("test@email.test");
        contact.setName("Test Name");

        License mitLicense = new License().name("GNU AGPLv3").url("https://choosealicense/licenses/agpl-3.0");

        Info info = new Info()
                .title("Client orders API")
                .contact(contact)
                .version("1.0")
                .license(mitLicense)
                .description("API for client orders")
                .termsOfService("http://some.terms.url");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localhost, prodServer));
    }
}
