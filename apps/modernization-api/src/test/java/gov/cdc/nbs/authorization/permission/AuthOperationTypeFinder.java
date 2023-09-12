package gov.cdc.nbs.authorization.permission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gov.cdc.nbs.authentication.entity.AuthBusOpType;
import gov.cdc.nbs.authentication.entity.QAuthBusOpType;
import org.springframework.stereotype.Component;

@Component
class AuthOperationTypeFinder {

    private final JPAQueryFactory factory;
    private final QAuthBusOpType operationType;

    AuthOperationTypeFinder(final JPAQueryFactory factory) {
        this.factory = factory;
        this.operationType = QAuthBusOpType.authBusOpType;
    }

    AuthBusOpType find(final String name) {
        return this.factory.select(operationType).from(operationType)
            .where(operationType.busOpNm.eq(name.toUpperCase()))
            .limit(1)
            .fetchFirst();
    }

}
