package io.gomk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/1
 */
@Configuration
public class HdfsConfig {
    @Value("${hdfs.path}")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
