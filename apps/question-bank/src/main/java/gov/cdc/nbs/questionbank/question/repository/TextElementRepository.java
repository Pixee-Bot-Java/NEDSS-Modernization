package gov.cdc.nbs.questionbank.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gov.cdc.nbs.questionbank.entities.VersionId;
import gov.cdc.nbs.questionbank.entities.TextEntity;

public interface TextElementRepository extends JpaRepository<TextEntity, VersionId> {

}
