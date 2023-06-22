package gov.cdc.nbs.questionbank.valueset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.cdc.nbs.questionbank.entity.Codeset;
import gov.cdc.nbs.questionbank.entity.CodesetId;


public interface ValueSetRepository extends JpaRepository <Codeset,CodesetId> {
	
	
 @Query("SELECT count(*) FROM Codeset WHERE codeSetGroup.codeSetNm =:name AND id.classCd = 'code_value_general'")
 long  checkValueSetName(@Param("name")String name);
 
 
 @Query("SELECT count(codeSetGroup.id) FROM Codeset WHERE codeSetGroup.id > 99900")
 int getCodeSetGroupCeilID();

 @Modifying
 @Transactional
 @Query("UPDATE Codeset SET statusCd='I' WHERE  id.codeSetNm =:codeSetNm")
 int inActivateValueSet(@Param("codeSetNm") String codeSetNm);
 
 
 @Modifying
 @Transactional
 @Query("UPDATE Codeset SET statusCd='A' WHERE  id.codeSetNm =:codeSetNm")
 int activateValueSet(@Param("codeSetNm") String codeSetNm);
 

}
