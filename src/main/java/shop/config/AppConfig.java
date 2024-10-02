package shop.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "shop")
public class AppConfig {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment
                .getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment
                .getProperty("spring.datasource.url"));
        dataSource.setUsername(environment
                .getProperty("spring.datasource.username"));
        dataSource.setPassword(environment
                .getProperty("spring.datasource.password"));
        return dataSource;
    }

    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(getDataSource());
        Properties props = new Properties();
        props.put("spring.jpa.show-sql",
                environment.getProperty("spring.jpa.show-sql"));
        props.put("spring.jpa.hibernate.ddl-auto",
                environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        sessionFactory.setHibernateProperties(props);
        sessionFactory.setPackagesToScan("shop.entity");
        return sessionFactory;
    }
}
