package com.projet.gestion_panneau.configuration;

import com.projet.gestion_panneau.entity.User;
import com.projet.gestion_panneau.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);

		if (!user.isPresent()) {
			throw new UsernameNotFoundException("Username not found");
		}

		return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
				user.get().getState().equals("Active"), true, true, true, getGrantedAuthorities(user.get()));
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		return user.getRoles()
				.stream()
				.flatMap(role -> {
					Stream<GrantedAuthority> roleAuthority = Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getLibelle()));
					Stream<GrantedAuthority> permissionAuthorities = role.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getResource().name() + "_" + permission.getAction().name()));
					return Stream.concat(roleAuthority, permissionAuthorities);})
				.distinct()
				.toList();
	}
}
