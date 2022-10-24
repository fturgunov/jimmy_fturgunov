package com.example.jimmy_fturgunov.config;

import com.example.jimmy_fturgunov.AbstractTest;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;

@Component()
public class Config implements InitializingBean {

    @Autowired
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is = AbstractTest.class.getResourceAsStream("/initial-data-set/common-data-set.sql");
        String script = IOUtils.toString(is);
        Connection conn = dataSource.getConnection();
        conn.createStatement().execute(script);
    }
}
