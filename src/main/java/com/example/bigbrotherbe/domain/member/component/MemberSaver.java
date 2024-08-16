package com.example.bigbrotherbe.domain.member.component;


import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSaver {
    private final MemberRepository memberRepository;
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
}
