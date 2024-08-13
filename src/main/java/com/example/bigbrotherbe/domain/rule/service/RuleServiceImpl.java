package com.example.bigbrotherbe.domain.rule.service;

import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleRegisterRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleUpdateRequest;
import com.example.bigbrotherbe.domain.rule.dto.response.RuleResponse;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import com.example.bigbrotherbe.domain.rule.repository.RuleRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;

    private final FileService fileService;
    private final MemberService memberService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerRule(RuleRegisterRequest ruleRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!memberService.checkExistAffiliationById(ruleRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

//                Member member = authUtil.getLoginMember();
        // role에 따라 권한있는지 필터링 없으면 exception

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                    .fileType(FileType.RULE.getType())
                    .multipartFileList(multipartFiles)
                    .build();

            files = fileService.saveFiles(fileSaveDTO);
        }
        Rule rule = ruleRegisterRequest.toRuleEntity(files);

        if (files != null) {
            files.forEach(file -> {
                file.linkRule(rule);
            });
        }

        ruleRepository.save(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRule(Long ruleId, RuleUpdateRequest ruleUpdateRequest, List<MultipartFile> multipartFiles) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_RULE));

        List<File> files = null;
        if (fileService.checkExistRequestFile(multipartFiles)) {
            FileUpdateDTO fileUpdateDTO = FileUpdateDTO.builder()
                    .fileType(FileType.RULE.getType())
                    .multipartFileList(multipartFiles)
                    .files(rule.getFiles())
                    .build();

            files = fileService.updateFile(fileUpdateDTO);
        }

        rule.update(ruleUpdateRequest.getTitle(), files);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(Long ruleId) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_RULE));

        FileDeleteDTO fileDeleteDTO = FileDeleteDTO.builder()
                .fileType(FileType.RULE.getType())
                .files(rule.getFiles())
                .build();

        fileService.deleteFile(fileDeleteDTO);
        ruleRepository.delete(rule);
    }

    @Override
    @Transactional(readOnly = true)
    public RuleResponse getRuleById(Long ruleId) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_RULE));

        List<String> urlList = rule.getFiles().stream()
                .map(File::getUrl)
                .toList();

        return RuleResponse.fromRuleResponse(rule, urlList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rule> getRules(Long affiliationId, Pageable pageable) {
        return ruleRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rule> searchRules(Long affiliationId, String title, Pageable pageable) {
        return ruleRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }
}
