package Teaming.teaming.member.user.repository;

import Teaming.teaming.member.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
	Optional <User> findByUsername(String username);

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
