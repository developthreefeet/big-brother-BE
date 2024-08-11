package com.example.bigbrotherbe.domain.transactions.service;

import com.example.bigbrotherbe.domain.meetings.entity.Meetings;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.domain.transactions.dto.request.TransactionsUpdateRequest;
import com.example.bigbrotherbe.domain.transactions.entity.Transactions;
import com.example.bigbrotherbe.domain.transactions.repository.TransactionsRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.ocr.dto.OcrDTO;
import com.example.bigbrotherbe.global.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;

    private final MemberService memberService;
    private final OcrService ocrService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(MultipartFile multipartFile, Long affiliationId) {
        if (!memberService.checkExistAffiliationById(affiliationId)) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (!memberService.checkExistAffiliationById(affiliationId)) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(EMPTY_FILE);
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
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long transactionsId, TransactionsUpdateRequest transactionsUpdateRequest) {
        Transactions transactions = transactionsRepository.findById(transactionsId)
                .orElseThrow(() -> new BusinessException(NO_EXIST_MEETINGS));

        transactions.update(transactionsUpdateRequest.getNote());
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
