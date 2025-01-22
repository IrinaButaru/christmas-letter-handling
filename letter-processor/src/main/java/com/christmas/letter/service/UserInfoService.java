package com.christmas.letter.service;

import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.exception.UnauthorizedException;
import com.christmas.letter.model.dto.UserInfoDetails;
import com.christmas.letter.model.entity.UserEntity;
import com.christmas.letter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null)
            throw new UnauthorizedException(GlobalExceptionHandler.UNAUTHORIZED_MESSAGE);

        Optional<UserEntity> userEntity = userRepository.findById(username);

        return userEntity.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public List<String> getRoles(String username) {
        UserEntity userEntity = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));


        return userEntity.getRoles();
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
