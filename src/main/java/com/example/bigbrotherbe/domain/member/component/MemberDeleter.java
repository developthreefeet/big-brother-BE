package com.example.bigbrotherbe.domain.member.component;


import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDeleter {
    private final MemberRepository memberRepository;
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }
}
