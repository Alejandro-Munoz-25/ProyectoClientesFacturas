package com.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IUserDao;
import com.springboot.app.models.entity.Role;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService {
	@Autowired
	private IUserDao userDao;

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailService.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		com.springboot.app.models.entity.User user = userDao.findByUsername(username);

		if (user == null) {
			logger.error("User	".concat(username).concat(" not Found"));
			throw new UsernameNotFoundException("User	".concat(username).concat(" not Found"));
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		if (authorities.isEmpty()) {
			logger.error("User	".concat(username).concat(" has assigned roles"));
			throw new UsernameNotFoundException("User	".concat(username).concat(" not Found"));
		}
		return new User(user.getUsername(), user.getPassword(), user.getEnable(), true, true, true, authorities);
	}

}
