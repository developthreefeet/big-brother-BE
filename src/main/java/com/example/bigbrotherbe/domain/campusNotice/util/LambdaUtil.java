package com.example.bigbrotherbe.domain.campusNotice.util;

import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.domain.campusNotice.service.CampusNoticeService;
import com.example.bigbrotherbe.global.constant.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LambdaUtil {

    private final CampusNoticeService campusNoticeService;

    @Scheduled(cron = Constant.Cron.ONE_DAY_INTERVAL)
    public void invokeLambda() {
        for (CampusNoticeType noticeType : CampusNoticeType.values()){
            String payload = "{\"queryStringParameters\": {" +
                    "\"url\": \"" + noticeType.getUrl() + "\"," +
                    "\"base_url\": \"" + Constant.Lambda.BASE_URL + "\"" +
                    "}}";
            campusNoticeService.invokeLambda(Constant.Lambda.FUNCTION_NAME, payload, noticeType);
        }
    }
}
