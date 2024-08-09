package com.example.bigbrotherbe.domain.transactions.service;

import com.example.bigbrotherbe.domain.member.repository.AffiliationRepository;
import com.example.bigbrotherbe.domain.member.service.MemberService;
import com.example.bigbrotherbe.domain.transactions.entity.Transactions;
import com.example.bigbrotherbe.domain.transactions.repository.TransactionsRepository;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.ocr.dto.OcrDTO;
import com.example.bigbrotherbe.global.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.FAIL_TO_PARSE_TO_LONG;
import static com.example.bigbrotherbe.global.exception.enums.ErrorCode.NO_EXIST_AFFILIATION;


@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final MemberService memberService;

    private final OcrService ocrService;

    private static final String DATE_TIME_REGEX = "yyyy-MM-dd HH:mm:ss";

    public void register(MultipartFile multipartFile, Long affiliationId) {
        if (!memberService.checkExistAffiliationById(affiliationId)) {
            throw new BusinessException(NO_EXIST_AFFILIATION);
        }

        if (multipartFile == null || multipartFile.isEmpty()) {
            System.out.println("파일이 안들어와요요용");
            // 파일 없을 때 예외 처리
//            throw new BusinessException("파일이 없슴둥");
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

    private LocalDateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_REGEX);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private long parseLong(String longString) {
        try {
            NumberFormat format = NumberFormat.getInstance();
            Number number = format.parse(longString);
            return number.longValue();
        } catch (ParseException e) {
            throw new BusinessException(FAIL_TO_PARSE_TO_LONG);
        }
    }

}
