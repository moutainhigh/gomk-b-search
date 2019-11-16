package io.gomk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public class CustomUserDetailsService

        //实现AuthenticationUserDetailsService，实现loadUserDetails方法
        implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);


    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        // 结合具体的逻辑去实现用户认证，并返回继承UserDetails的用户对象;
        System.out.println("当前的用户名是："+token.getName());

        //获取用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(token.getName());
        Map<String, Object> userAttributess = token.getAssertion().getPrincipal().getAttributes();
        //System.out.println(userAttributess.toString());
        if (userAttributess != null) {
//            userInfo.setId( String.valueOf(userAttributess.get("id")));
        }

        System.out.println(userInfo.toString());
        return userInfo;

    }

}

