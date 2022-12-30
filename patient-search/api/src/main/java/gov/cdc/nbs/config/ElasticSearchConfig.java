package gov.cdc.nbs.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
@SuppressWarnings("deprecation")
public class ElasticSearchConfig {

    @Value("${nbs.elasticsearch.url:http://localhost:9200}")
    private String elasticSearchUrl;
    public static final String DATE_PATTERN = "uuuu-MM-dd HH:mm:ss.SSS||uuuu-MM-dd HH:mm:ss.S||uuuu-MM-dd HH:mm:ss.SS";

    /**
     * RestHighLevelClient is deprecated but no viable alternatives exist for
     * spring-data-elasticsearch until Spring Boot 3.0.0 due to Jakarta EE 9 APIs.
     * 
     * Links:
     * https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#preface.versions
     * https://github.com/spring-projects/spring-boot/issues/28598
     * 
     * @throws MalformedURLException
     * 
     */
    @Bean
    public RestHighLevelClient client() throws MalformedURLException {
        URL url = new URL(elasticSearchUrl);
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(url.getHost() + ":" + url.getPort())
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws MalformedURLException {
        return new ElasticsearchRestTemplate(client());
    }

}