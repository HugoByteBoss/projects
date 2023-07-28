package br.com.timetracker.auth.configurations.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.timetracker.auth")
public class MongoTransactionConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String dbName;

    @Value("${spring.data.mongodb.uri}")
    private String mongoURI;

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new ZonedDateTimeReadConverter(),
                new ZonedDateTimeWriteConverter(),
                new LocalDateTimeReadConverter(),
                new LocalDateTimeWriteConverter()));
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(mongoURI);
        return new MongoClient(uri);
    }
}
