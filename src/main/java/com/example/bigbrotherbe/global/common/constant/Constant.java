package com.example.bigbrotherbe.global.common.constant;

public class Constant {
    public static class GetContent {
        public static final String PAGE_DEFAULT_VALUE = "0";
        public static final String SIZE_DEFAULT_VALUE = "10";
    }

    public static class Lambda{
        public static final String FUNCTION_NAME = "campusNoticeCrawler";
        public static final String BASE_URL = "https://www.mju.ac.kr";
    }

    public static class Cron{
        public static final String ONE_DAY_INTERVAL = "0 0 0 * * ?";
    }

    public static class Url{
        public static final String DOMAIN_URL = "https://api.mju-bigbrother.xyz";
    }

    public static class Entity{
        public static final String NOTICE = "notice";
        public static final String FAQ = "faq";
        public static final String MEETINGS = "meetings";
        public static final String EVENT = "event";
        public static final String RULE = "rule";
        public static final String AFFILIATION = "affiliation";
        public static final String CAMPUS_NOTICE = "campusNotice";
        public static final String TRANSACTIONS = "transactions";
    }
}
