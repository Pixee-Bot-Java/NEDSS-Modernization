package gov.cdc.nbs.authorization.permission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gov.cdc.nbs.authentication.entity.AuthBusObjType;
import gov.cdc.nbs.authentication.entity.QAuthBusObjType;
import org.springframework.stereotype.Component;

@Component
class AuthObjectTypeFinder {

    private final JPAQueryFactory factory;
    private final QAuthBusObjType objectType;

    AuthObjectTypeFinder(final JPAQueryFactory factory) {
        this.factory = factory;
        this.objectType = QAuthBusObjType.authBusObjType;
    }

    AuthBusObjType find(final String name) {
        return this.factory.select(objectType).from(objectType)
            .where(objectType.busObjNm.eq(name.toUpperCase()))
            .limit(1)
            .fetchFirst();
    }

}
