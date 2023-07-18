package gov.cdc.nbs.questionbank.pagerules.repository;

import gov.cdc.nbs.questionbank.entity.pagerule.WaRuleMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaRuleMetaDataRepository extends JpaRepository<WaRuleMetadata, Long> {

}
