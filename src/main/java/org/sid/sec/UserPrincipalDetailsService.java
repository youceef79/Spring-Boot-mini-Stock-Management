package org.sid.sec;

import java.util.Collection;
import java.util.Map;

import org.sid.dao.UserRepository;
import org.sid.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
@Controller
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    
    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
        User user = this.userRepository.findByUsername(s);
        user.setPassword(user.getPassword());
    //    getModel().addAttribute("user", user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
 
        return userPrincipal;
    }
    
    public User getUser() {
    	UserPrincipal myUserDetails = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return myUserDetails.getUser();
     }
    
    public void setUser(User u) {
    	UserPrincipal myUserDetails = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         myUserDetails.setUser(u);
     }
       
}