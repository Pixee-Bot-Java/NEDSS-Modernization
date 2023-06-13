package gov.cdc.nbs.codes;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gov.cdc.nbs.entity.srte.QStateCountyCodeValue;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class CountyCodedValueFinder {

    private final JPAQueryFactory factory;
    private final QStateCountyCodeValue values;

    CountyCodedValueFinder(final JPAQueryFactory factory) {
        this.factory = factory;
        this.values = QStateCountyCodeValue.stateCountyCodeValue;
    }

    Collection<GroupedCodedValue> all() {
        return this.factory.select(
                values.id,
                values.codeDescTxt,
                values.parentIsCd
            ).from(values)
            .fetch()
            .stream()
            .map(this::map)
            .toList();
    }

    Collection<GroupedCodedValue> all(final String category) {
        return this.factory.select(
                values.id,
                values.codeDescTxt,
                values.parentIsCd
            ).from(values)
            .where(values.parentIsCd.eq(category))
            .fetch()
            .stream()
            .map(this::map)
            .toList();
    }

    private GroupedCodedValue map(final Tuple tuple) {
        String value = tuple.get(values.id);
        String name = StandardNameFormatter.formatted(tuple.get(values.codeDescTxt));
        String group = tuple.get(values.parentIsCd);
        return new GroupedCodedValue(value, name, group);
    }
}
