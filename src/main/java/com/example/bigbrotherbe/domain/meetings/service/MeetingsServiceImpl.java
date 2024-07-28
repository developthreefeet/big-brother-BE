package com.example.bigbrotherbe.domain.meetings.service;

import com.example.bigbrotherbe.domain.meetings.dto.MeetingsRegisterRequest;
import com.example.bigbrotherbe.domain.meetings.dto.MeetingsUpdateRequest;
import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.meetings.repository.MeetingsRepository;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.global.jwt.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MeetingsServiceImpl implements MeetingsService{

    private final MeetingsRepository meetingsRepository;
    private final AuthUtil authUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerMeetings(MeetingsRegisterRequest meetingsRegisterRequest) {
//
        Member member = authUtil.getLoginMember();

        // 해당 소속이 있는지 필터링 affiliation _id 없으면 exception
        // role에 따라 권한있는지 필터링 없으면 exception
        // save 해서 meetings pk(Mettings_id) 받기

        meetingsRepository.save(meetingsRegisterRequest.toMeetingsEntity());

        // s3에 파일 저장 url 반환 -> 뭐 씌워야 한다고 했음 -> pre-signed 이거 나중에
        // 그거 가지고 meetingsPk(targetPk) & file_type으로 저장 -> files에

        // meetings에서 조회할 때 FILE TABLE에서 targetPk와 type에 맞는 해당 url
        // 버디에 담아서 res

        // res 할때 boardInfo에 이미지 파일 있으면 넣어서

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeetings(Long meetingsId, MeetingsUpdateRequest meetingsUpdateRequest) {
        Meetings meetings = meetingsRepository.findById(meetingsId)
                .orElseThrow(() -> new NoSuchElementException("없어요~~"));

        meetings.update(meetingsUpdateRequest.getTitle(), meetingsUpdateRequest.getContent(), meetingsUpdateRequest.isPublic());
    }
}
