package org.sid.sec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sid.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SuppressWarnings("serial")
public class UserPrincipal implements UserDetails{
    private User user;

    public UserPrincipal(User user){
        this.user = user;
    }
    
    public User getUser() {
    	return this.user;
    }
    
    public void setUser(User u) {
    	 this.user = u;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Extract list of roles (ROLE_name)
        this.user.getRoleList().forEach(r -> {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + r);
            authorities.add(authority);
        });

        return authorities;
    }

    @Override
    public String getUsername(){
        return this.user.getUsername();
    }
    
    
    @Override
    public String getPassword(){
        return this.user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}