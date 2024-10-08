package com.example.bigbrotherbe.global.common.enums;

import com.example.bigbrotherbe.global.common.constant.Constant;
import com.example.bigbrotherbe.global.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.bigbrotherbe.global.common.exception.enums.ErrorCode.NO_ENTITY_TYPE;

@Getter
@AllArgsConstructor
public enum EntityType {
    NOTICE_TYPE(1, Constant.Entity.NOTICE),
    FAQ_TYPE(2, Constant.Entity.FAQ),
    MEETINGS_TYPE(3, Constant.Entity.MEETINGS),
    EVENT_TYPE(4, Constant.Entity.EVENT),
    RULE_TYPE(5, Constant.Entity.RULE),
    AFFILIATION_TYPE(6, Constant.Entity.AFFILIATION),
    CAMPUS_NOTICE_TYPE(7, Constant.Entity.CAMPUS_NOTICE),
    TRANSACTIONS_TYPE(8, Constant.Entity.TRANSACTIONS);

    private final int val;
    private final String type;

    public static EntityType getEntityType(String entity) {
        switch (entity) {
            case Constant.Entity.NOTICE:
                return NOTICE_TYPE;
            case Constant.Entity.FAQ:
                return FAQ_TYPE;
            case Constant.Entity.MEETINGS:
                return MEETINGS_TYPE;
            case Constant.Entity.EVENT:
                return EVENT_TYPE;
            case Constant.Entity.RULE:
                return RULE_TYPE;
            case Constant.Entity.AFFILIATION:
                return AFFILIATION_TYPE;
            case Constant.Entity.CAMPUS_NOTICE:
                return CAMPUS_NOTICE_TYPE;
            case Constant.Entity.TRANSACTIONS:
                return TRANSACTIONS_TYPE;
            default:
                throw new BusinessException(NO_ENTITY_TYPE);
        }
    }
}
