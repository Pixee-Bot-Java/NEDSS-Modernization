package gov.cdc.nbs.questionbank.page.content.tab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import gov.cdc.nbs.questionbank.entity.WaUiMetadata;

@Repository
public interface WaUiMetaDataRepository extends JpaRepository<WaUiMetadata, Long> {

    @Query(value = "SELECT MAX(w.orderNbr) FROM WaUiMetadata w WHERE w.waTemplateUid.id =:page")
    Long findMaxOrderNumberByTemplateUid(@Param("page") Long page);

    @Query(value = "select w.waTemplateUid from WaUiMetadata w where waUiMetadataUid = :waUiMetadataUid")
    Long findPageNumber(@Param("waUiMetadataUid") Long waUiMetadataUid);

    @Query(value = "select top 1 nbs_ui_component_uid from WA_UI_metadata where order_nbr = ?1  and wa_template_uid = ?2", nativeQuery = true)
    Long findNextNbsUiComponentUid(Integer orderNbr, Long page);
    @Query(value = "SELECT" +
            "    COALESCE((SELECT" +
            "            MIN(order_nbr)" +
            "            FROM WA_UI_metadata" +
            "        WHERE" +
            "            wa_template_uid = ?2" +
            "            AND nbs_ui_component_uid = ?3" +
            "            AND order_nbr > (" +
            "                SELECT" +
            "                    order_nbr FROM WA_UI_metadata" +
            "                WHERE" +
            "                    wa_ui_metadata_uid = ?1)), (" +
            "                SELECT" +
            "                    MAX(order_nbr) + 1" +
            "                    FROM WA_UI_metadata" +
            "                WHERE" +
            "                    wa_template_uid = ?2))", nativeQuery = true)
    Long findOrderNbr_2(Long waUiMetadataUid, Long waTemplateId, Long nbsComponentUid);

    @Query(value = "SELECT" +
            "    MIN(order_nbr)" +
            "    FROM" +
            "            WA_UI_metadata" +
            "    WHERE" +
            "            wa_template_uid = ?2" +
            "    AND nbs_ui_component_uid = ?3" +
            "    AND order_nbr > (" +
            "    SELECT" +
            "            order_nbr" +
            "    FROM" +
            "            WA_UI_metadata" +
            "    WHERE" +
            "            wa_ui_metadata_uid = ?1)", nativeQuery = true)
    Long findOrderNbr(Long waUiMetadataUid, Long waTemplateId, Long nbsComponentUid);


    /**
     * Increments the orderNbr for all entries for a page beginning with the given orderNbr. This is typically used when
     * inserting a new entry into the page
     * 
     * @param orderNbr
     * @param page
     */
    @Modifying
    @Query("UPDATE WaUiMetadata m set m.orderNbr = m.orderNbr + 1 where m.orderNbr >= :orderNbr and m.waTemplateUid.id =:page")
    void incrementOrderNumbers(@Param("orderNbr") Integer orderNbr, @Param("page") Long page);


    /**
     * Finds the orderNbr of the next section or tab within a page given the starting orderNbr
     * 
     * @param orderNbr
     * @param page
     * @return
     */
    @Query("SELECT MIN(m.orderNbr) FROM WaUiMetadata m WHERE m.waTemplateUid.id =:page AND m.orderNbr > :orderNbr AND (m.nbsUiComponentUid = 1015 OR m.nbsUiComponentUid = 1010)")
    Long findOrderNbrOfNextSectionOrTab(
            @Param("orderNbr") Integer orderNbr,
            @Param("page") long page);

    @Modifying
    @Query(value = "update WaUiMetadata w set w.questionLabel = :questionLabel, w.displayInd = :displayInd  where waUiMetadataUid= :waUiMetadataUid")
    void updateQuestionLabelAndVisibility(@Param("questionLabel") String questionLabel, @Param("displayInd")String visibility, @Param("waUiMetadataUid") Long waUiMetadataUid);

    @Modifying
    @Query(value = "update WaUiMetadata w set w.orderNbr = w.orderNbr - 1 where w.orderNbr >= :orderNbr and w.id != :waUiMetadataUid")
    void updateOrderNumberByDecreasing(@Param("orderNbr") Integer orderNbr, @Param("waUiMetadataUid")  Long waUiMetadataUid);

    @Query(value = "SELECT w.orderNbr FROM WaUiMetadata w WHERE waUiMetadataUid = :waUiMetadataUid")
    Integer getOrderNumber(@Param("waUiMetadataUid") Long waUiMetadataUid);

}

