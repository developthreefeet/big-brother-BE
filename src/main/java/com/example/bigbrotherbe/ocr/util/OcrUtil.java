package com.example.bigbrotherbe.ocr.util;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class OcrUtil {

    private static final String TRANSACTION_REGEX =
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+([가-힣]+)\\s+([-,\\d]+)\\s+([-,\\d]+)\\s+(.*)";

    private static final String ACCOUNT_NUMBER_REGEX = "계좌번호\\s+([\\d-]+)";



    public String extractTextFromPDF(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();

            return pdfStripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
            // 에외처리
        }
    }

    public List<String[]> parseTransactions(String text) {
        Pattern pattern = Pattern.compile(TRANSACTION_REGEX);
        Matcher matcher = pattern.matcher(text);
        List<String[]> transactions = new ArrayList<>();

        while (matcher.find()) {
            String[] transaction = new String[5];
            transaction[0] = matcher.group(1); // 거래일자
            transaction[1] = matcher.group(2); // 구분
            transaction[2] = matcher.group(3); // 거래금액
            transaction[3] = matcher.group(4); // 거래 후 잔액
            transaction[4] = matcher.group(5); // 거래내용
            transactions.add(transaction);
        }

        return transactions;
    }

    public String parseAccountNumber(String text) {
        Pattern pattern = Pattern.compile(ACCOUNT_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 캡처 그룹 (계좌번호)을 반환
        }

        return null; // 계좌번호가 없을 경우 null 반환
    }
}
