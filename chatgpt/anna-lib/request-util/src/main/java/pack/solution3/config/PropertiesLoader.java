package pack.solution3.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static final APIConfig config;

    static {
        config = new APIConfig();
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.setBaseUrl(properties.getProperty("api.url"));
        config.setEndpoint(properties.getProperty("api.endpoint"));
    }

    public static APIConfig getAPIConfig() {
        return config;
    }
}
