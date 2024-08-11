package com.example.bigbrotherbe.domain.member.entity;

import com.example.bigbrotherbe.domain.BaseTimeEntity;
import com.example.bigbrotherbe.domain.member.entity.role.AffiliationMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, name = "name")
    private String username;

    @Column(nullable = false, name = "member_password")
    private String password;

    @Column
    private String email;

    @Column
    private String is_active;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AffiliationMember> affiliations = new ArrayList<>();

    // 정적 필드로 정의된 정규식 패턴 객체
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.affiliations.stream()
                .map(affiliationMember -> new SimpleGrantedAuthority(affiliationMember.getRole()))
                .collect(Collectors.toList());
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

    @Builder
    public Member(String username, String password, String email, String is_active, LocalDateTime create_at, LocalDateTime update_at) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }
        this.username = username;
        this.password = password;
        this.email = email;
        this.is_active = is_active;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    public void changePassword(String memberPass) {
        this.password = memberPass;
    }

    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
