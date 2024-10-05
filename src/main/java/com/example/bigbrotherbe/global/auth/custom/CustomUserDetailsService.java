package com.example.bigbrotherbe.global.auth.custom;

import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import com.example.bigbrotherbe.global.common.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(()
                -> new BusinessException(ErrorCode.NO_EXIST_EMAIL));
        return new CustomerDetails(member);
    }
}
