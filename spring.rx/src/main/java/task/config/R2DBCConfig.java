package task.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories
public class R2DBCConfig extends AbstractR2dbcConfiguration {

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        try {

//            return new MariadbConnectionFactory(MariadbConnectionConfiguration.builder()
//                    .host("127.0.0.1")
//                    .port(3308)
//                    .username("appuser")
//                    .password("password")
//                    .database("springdb")
//                    .connectTimeout(Duration.ofSeconds(30))
//                    .build());
            ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                    .option(DRIVER, "pool")
                    .option(PROTOCOL, "mariadb")
                    .option(HOST, "tododb2")
                    .option(PORT, 3306)
                    .option(USER, "appuser")
                    .option(PASSWORD, "password")
                    .option(DATABASE, "tododb")
                    .option(MAX_SIZE, 10)
                    .build());
            return connectionFactory;
        }
        catch (Exception e) {
            System.out.println("unable connect to db");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
