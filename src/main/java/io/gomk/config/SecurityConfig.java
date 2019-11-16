package io.gomk.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * @Auther: ZHG
 * @Date: 2019-07-01 9:58
 * @Description: 单点登录配置
 */

@Configuration
@EnableWebSecurity //启用web权限
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用方法验证*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CasProperties casProperties;

    /**
     * 定义认证用户信息获取来源，密码校验规则等
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.info("定义认证用户信息获取来源");
        super.configure(auth);
        auth.authenticationProvider(casAuthenticationProvider());
    }

    /**
     * 定义安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("定义安全策略");
        http.authorizeRequests()//配置安全策略
                //.antMatchers("/","/login").permitAll()//定义/请求不需要验证
                .antMatchers("/checkfile/**","/static/**","/swagger-ui.html","/webjars/**","/swagger-resources/**","/v2/**").permitAll()//graph 不需要认证
//                .antMatchers(HttpMethod.POST,"graph").permitAll()
                .anyRequest().authenticated()//其余的所有请求都需要验证
                .and().logout().permitAll()//定义logout不需要验证
                .and().formLogin();//使用form表单登录

        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint()).and().addFilter(casAuthenticationFilter()).addFilterBefore(casLogoutFilter(), LogoutFilter.class).addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    /**
     * 认证的入口
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
       // LOGGER.info("进入认证");
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * 指定service相关信息
     */
    @Bean
    public ServiceProperties serviceProperties() {
       // LOGGER.info("ServiceProperties信息");
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casProperties.getAppServerUrl() + casProperties.getAppLoginUrl());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * CAS认证过滤器
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
       // LOGGER.info("CasAuthenticationFilter");
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());
        return casAuthenticationFilter;
    }

    /**
     * cas 认证 Provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        //LOGGER.info("CasAuthenticationProvider");
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        return casAuthenticationProvider;
    }

/*@Bean
	public UserDetailsService customUserDetailsService(){
		return new CustomUserDetailsService();
	}*/

    /**
     * 用户自定义的AuthenticationUserDetailsService
     */
    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
       // LOGGER.info("customUserDetailsService");
        return new CustomUserDetailsService();
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
    }

    /**
     * 单点登出过滤器
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        //LOGGER.info("单点登出过滤器");
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casProperties.getCasServerUrl());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }
    /**
     * 请求单点退出过滤器
     */
    @Bean
    public LogoutFilter casLogoutFilter() {
        //LOGGER.info("请求单点退出过滤器");
        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());
        return logoutFilter;
        }
    }

