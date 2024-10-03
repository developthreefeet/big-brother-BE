package com.example.bigbrotherbe.domain.transactions.service;

import com.example.bigbrotherbe.domain.affiliation.service.AffiliationService;
import com.example.bigbrotherbe.domain.member.entity.Member;
import com.example.bigbrotherbe.domain.transactions.dto.request.TransactionsUpdateRequest;
import com.example.bigbrotherbe.domain.transactions.dto.response.TransactionsResponse;
import com.example.bigbrotherbe.domain.transactions.entity.Transactions;
import com.example.bigbrotherbe.domain.transactions.repository.TransactionsRepository;
import com.example.bigbrotherbe.common.exception.BusinessException;
import com.example.bigbrotherbe.file.dto.FileSaveDTO;
import com.example.bigbrotherbe.file.entity.File;
import com.example.bigbrotherbe.file.enums.FileType;
import com.example.bigbrotherbe.file.service.FileService;
import com.example.bigbrotherbe.auth.util.AuthUtil;
import com.example.bigbrotherbe.ocr.dto.OcrDTO;
import com.example.bigbrotherbe.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;

    private final AffiliationService affiliationService;
    private final OcrService ocrService;
    private final FileService fileService;

    private final AuthUtil authUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(MultipartFile multipartFile, Long affiliationId) {
        if (!affiliationService.checkExistAffiliationById(affiliationId)) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(EMPTY_FILE);
        }

        if (authUtil.checkCouncilRole(affiliationId)) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        OcrDTO ocrDTO = ocrService.extractText(multipartFile);

        List<String[]> parseTransactions = ocrDTO.getParseTransactions();
        String parseAccountNumber = ocrDTO.getParseAccountNumber();

        parseTransactions.forEach(parseTransaction -> {
            Transactions transactions = Transactions.builder()
                    .date(parseDateTime(parseTransaction[0]))
                    .type(parseTransaction[1])
                    .amount(parseLong(parseTransaction[2]))
                    .balance(parseLong(parseTransaction[3]))
                    .description(parseTransaction[4])
                    .accountNumber(parseAccountNumber)
                    .affiliationId(affiliationId)
                    .build();

            transactionsRepository.save(transactions);
        });

        // pdf 저장
        FileSaveDTO fileSaveDTO = FileSaveDTO.builder()
                .fileType(FileType.TRANSACTIONS.getType())
                .multipartFile(multipartFile)
                .build();

        File file = fileService.saveFile(fileSaveDTO);

        Transactions transactions = Transactions.builder()
                .affiliationId(affiliationId)
                .accountNumber(parseAccountNumber)
                .file(file)
                .note("PDF 파일 저장")
                .build();

        transactionsRepository.save(transactions);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long transactionsId, TransactionsUpdateRequest transactionsUpdateRequest) {
        Transactions transactions = transactionsRepository.findById(transactionsId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_MEETINGS));

        if (authUtil.checkCouncilRole(transactions.getAffiliationId())) {
            throw new BusinessException(NOT_COUNCIL_MEMBER);
        }

        transactions.update(transactionsUpdateRequest.getNote());
    }

    public List<TransactionsResponse> getTransactionsWithMonth(int year, int month, String affiliation) {
        String yearMonth = String.format("%d-%02d", year, month);

        Member member = authUtil.getLoginMember();
        Long affiliationId = authUtil.getAffiliationIdByMemberId(member.getId(), affiliation);
        List<Transactions> transactions = transactionsRepository.findAllByYearMonthAndAffiliationId(yearMonth, affiliationId);

        return transactions.stream()
                .map(TransactionsResponse::fromTransactionsResponse)
                .collect(Collectors.toList());
    }


    private LocalDateTime parseDateTime(String dateTimeString) {
        String DATE_TIME_REGEX = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_REGEX);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private long parseLong(String longString) {
        try {
            NumberFormat format = NumberFormat.getInstance();
            Number number = format.parse(longString);
            return number.longValue();
        } catch (ParseException e) {
            throw new BusinessException(FAIL_TO_LONG_PARSING);
        }
    }
}
