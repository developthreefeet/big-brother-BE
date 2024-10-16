package com.example.bigbrotherbe.domain.campusNotice.util;

import com.example.bigbrotherbe.domain.campusNotice.entity.CampusNoticeType;
import com.example.bigbrotherbe.domain.campusNotice.service.CampusNoticeService;
import com.example.bigbrotherbe.global.common.constant.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerUtil {

    private final CampusNoticeService campusNoticeService;

    @Scheduled(cron = Constant.Cron.ONE_DAY_INTERVAL)
    public void invokeLambda() {
        for (CampusNoticeType noticeType : CampusNoticeType.values()){
            String payload = "{\"queryStringParameters\": {" +
                    "\"url\": \"" + noticeType.getUrl() + "\"," +
                    "\"base_url\": \"" + Constant.Crawler.BASE_URL + "\"" +
                    "}}";
            campusNoticeService.invokeLambda(Constant.Crawler.FUNCTION_NAME, payload, noticeType);
        }
    }
}
