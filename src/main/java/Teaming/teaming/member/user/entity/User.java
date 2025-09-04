package teaming.teaming.member.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
@SuperBuilder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(length = 25, unique = true, nullable = false)
	private String username; // 사용자명

	@Column(length = 4, nullable = false)
	private String name; // 실명

	@Column(length = 100, nullable = false)
	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private int grade;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private Major mainMajor; // 주전공

	@Enumerated(EnumType.STRING)
	private Major subMajor;
}
