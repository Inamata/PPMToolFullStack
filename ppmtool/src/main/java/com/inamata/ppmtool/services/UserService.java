package com.inamata.ppmtool.services;

import com.inamata.ppmtool.domain.User;
import com.inamata.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        newUser.setConfirmPassword("BLANK_AFTER_CHECK");
        //Username has to be unique(exception)
        //Make sure pwd and confirm pwd match
        //we dont persist or show the confirm pwd
        return userRepository.save(newUser);
        /*
        try{

        }catch (Exception e){

        }

         */
    }
}
