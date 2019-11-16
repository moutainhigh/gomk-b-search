package io.gomk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: ZHG
 * @Date: 2019-07-11 9:11
 * @Description: CAS 配置参数信息
 */
@Component
@Data
public class CasProperties {

    @Value("${security.cas.casServerUrl}")
    private String casServerUrl;

    @Value("${security.cas.casServerLoginUrl}")
    private String casServerLoginUrl;

    @Value("${security.cas.casServerLogoutUrl}")
    private String casServerLogoutUrl;

    @Value("${security.cas.appServerUrl}")
    private String appServerUrl;

    @Value("${security.cas.appLoginUrl}")
    private String appLoginUrl;

    @Value("${security.cas.appLogoutUrl}")
    private String appLogoutUrl;
}
