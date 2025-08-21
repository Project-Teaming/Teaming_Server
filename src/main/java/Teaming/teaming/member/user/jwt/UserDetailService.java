package Teaming.teaming.member.user.jwt;

import Teaming.teaming.member.user.entity.User;
import Teaming.teaming.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(()
		-> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

		return new UserDetail(user);
	}
}
