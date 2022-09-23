package com.enquizit.nbs.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.enquizit.nbs.model.graphql.GraphQLPage;
import com.enquizit.nbs.model.graphql.PatientFilter;
import com.enquizit.nbs.model.patient.Patient;
import com.enquizit.nbs.model.patient.QPatient;
import com.enquizit.nbs.repository.PatientRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PatientService {
    private final int MAX_PAGE_SIZE = 50;

    @PersistenceContext
    private final EntityManager entityManager;
    private final PatientRepository patientRepository;

    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Page<Patient> findAllPatients(GraphQLPage page) {
        if (page == null) {
            page = new GraphQLPage(MAX_PAGE_SIZE, 0);
        }
        var pageable = PageRequest.of(page.getPageNumber(), Math.min(page.getPageSize(), MAX_PAGE_SIZE));
        return patientRepository.findAll(pageable);
    }

    public List<Patient> findPatientsByFilter(PatientFilter filter) {
        // limit page size
        if (filter.getPage().getPageSize() == 0 || filter.getPage().getPageSize() > MAX_PAGE_SIZE) {
            filter.getPage().setPageSize(MAX_PAGE_SIZE);
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        var patient = QPatient.patient;
        var query = queryFactory.selectFrom(patient);
        query = applyIfFilterNotNull(query, patient.personUid::eq, filter.getId());
        query = applyIfFilterNotNull(query, patient.lastNm::likeIgnoreCase, filter.getLastName());
        query = applyIfFilterNotNull(query, patient.firstNm::likeIgnoreCase, filter.getFirstName());
        query = applyIfFilterNotNull(query, patient.ssn::eq, filter.getSsn());
        if (filter.getPhoneNumber() != null) {
            query = query.where(
                    patient.hmPhoneNbr.eq(filter.getPhoneNumber())
                            .or(patient.wkPhoneNbr.eq(filter.getPhoneNumber()))
                            .or(patient.cellPhoneNbr.eq(filter.getPhoneNumber())));
        }
        query = query
                .where(getDateOfBirthExpression(patient, filter.getDateOfBirth(), filter.getDateOfBirthOperator()));
        query = applyIfFilterNotNull(query, patient.birthGenderCd::eq, filter.getGender());
        query = applyIfFilterNotNull(query, patient.deceasedIndCd::eq, filter.getDeceasedDataValue());
        query = applyIfFilterNotNull(query, patient.hmStreetAddr1::eq, filter.getAddress());
        query = applyIfFilterNotNull(query, patient.hmCityCd::eq, filter.getCity());
        query = applyIfFilterNotNull(query, patient.hmStateCd::eq, filter.getState());
        query = applyIfFilterNotNull(query, patient.hmCntryCd::eq, filter.getCountry());
        query = applyIfFilterNotNull(query, patient.ethnicityGroupCd::eq, filter.getEthnicity());
        query = applyIfFilterNotNull(query, patient.recordStatusCd::eq, filter.getRecordStatus());
        return query.limit(filter.getPage().getPageSize())
                .offset(filter.getPage().getOffset()).fetch();

    }

    private <T> JPAQuery<Patient> applyIfFilterNotNull(JPAQuery<Patient> query,
            Function<T, BooleanExpression> expression, T parameter) {
        if (parameter != null) {
            return query.where(expression.apply(parameter));
        } else {
            return query;
        }
    }

    private BooleanExpression getDateOfBirthExpression(QPatient patient, LocalDateTime dob, String dobOperator) {
        if (dob == null) {
            return null;
        }
        if (dobOperator == null) {
            return patient.birthTime.eq(dob);
        } else if (dobOperator.toLowerCase().equals("equal")) {
            return patient.birthTime.eq(dob);
        } else if (dobOperator.toLowerCase().equals("before")) {
            return patient.birthTime.after(dob);
        } else if (dobOperator.toLowerCase().equals("after")) {
            return patient.birthTime.before(dob);
        } else {
            throw new IllegalArgumentException("Invalid value for Date of Birth operator");
        }
    }

}
