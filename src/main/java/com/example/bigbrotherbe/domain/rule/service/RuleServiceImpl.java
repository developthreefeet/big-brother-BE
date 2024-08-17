package com.example.bigbrotherbe.domain.rule.service;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleRegisterRequest;
import com.example.bigbrotherbe.domain.rule.dto.request.RuleUpdateRequest;
import com.example.bigbrotherbe.domain.rule.dto.response.RuleResponse;
import com.example.bigbrotherbe.domain.rule.entity.Rule;
import com.example.bigbrotherbe.domain.rule.repository.RuleRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.file.dto.FileDeleteDTO;
import com.example.bigbrotherbe.global.file.dto.FileResponse;
import com.example.bigbrotherbe.global.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.global.file.dto.FileUpdateDTO;
import com.example.bigbrotherbe.global.file.entity.File;
import com.example.bigbrotherbe.global.file.enums.FileType;
import com.example.bigbrotherbe.global.file.service.FileService;
import com.example.bigbrotherbe.global.file.util.FileUtil;
import com.example.bigbrotherbe.global.jwt.component.AuthUtil;
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
    private final AffiliationService affiliationService;

    private final AuthUtil authUtil;
    private final FileUtil fileUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerRule(RuleRegisterRequest ruleRegisterRequest, List<MultipartFile> multipartFiles) {
        if (!affiliationService.checkExistAffiliationById(ruleRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (authUtil.checkPresidentRole(ruleRegisterRequest.getAffiliationId())) {
            throw new BusinessException(NOT_PRESIDENT_MEMBER);
        }

        fileUtil.checkPdfFiles(multipartFiles);

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

        fileUtil.checkPdfFiles(multipartFiles);

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

        List<FileResponse> fileInfo = rule.getFiles().stream()
                .map(file -> FileResponse.of(file.getFileName(), file.getUrl()))
                .toList();

        return RuleResponse.fromRuleResponse(rule, fileInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rule> getRules(String affiliation, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return ruleRepository.findByAffiliationId(affiliationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rule> searchRules(String affiliation, String title, Pageable pageable) {
        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        return ruleRepository.findByAffiliationIdAndTitleContaining(affiliationId, title, pageable);
    }
}
