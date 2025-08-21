package Teaming.teaming.member.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.core.io.support.SpringFactoriesLoader;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
@SuperBuilder
public class User {

	@Id
	@Column(length = 25)
	private String username; // 사용자명

	@Column(length = 4)
	private String name; // 실명

	@Column(length = 100)
	private String password;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private int grade;

	private String role;

	@Enumerated(EnumType.STRING)
	private Major mainMajor; // 주전공

	@Enumerated(EnumType.STRING)
	private Major subMajor;
}
