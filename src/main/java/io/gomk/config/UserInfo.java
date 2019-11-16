package io.gomk.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserInfo implements UserDetails {
    private static final long serialVersionUID = -1041327031937199938L;

    private String id;
    private String username;
    private String password;

    private boolean isAccountNonExpired = true; //是否过期

    private boolean isAccountNonLocked = true;//账户未锁定为true

    private boolean isCredentialsNonExpired = true;//证书不过期为true

    private boolean isEnabled = true;//是否可用

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        if(this.getRole() == null || this.getRole().length() <1){
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
//        }
//        else{
//            return AuthorityUtils.commaSeparatedStringToAuthorityList(this.getRole());
//        }
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }





    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }


    public void setAccountNonExpired(boolean isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }


    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }


    public void setAccountNonLocked(boolean isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }


    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }


    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }


    public boolean isEnabled() {
        return isEnabled;
    }


    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


}
