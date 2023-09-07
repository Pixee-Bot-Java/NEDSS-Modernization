package gov.cdc.nbs.questionbank.page.content.addsection;

import gov.cdc.nbs.questionbank.page.content.section.SectionCreator;
import gov.cdc.nbs.questionbank.page.content.section.exception.AddSectionException;
import gov.cdc.nbs.questionbank.page.content.section.exception.DeleteSectionException;
import gov.cdc.nbs.questionbank.page.content.section.exception.UpdateSectionException;
import gov.cdc.nbs.questionbank.page.content.section.request.CreateSectionRequest;
import gov.cdc.nbs.questionbank.page.content.section.request.UpdateSectionRequest;
import gov.cdc.nbs.questionbank.page.content.section.response.CreateSectionResponse;
import gov.cdc.nbs.questionbank.page.content.section.response.DeleteSectionResponse;
import gov.cdc.nbs.questionbank.page.content.section.response.UpdateSectionResponse;
import gov.cdc.nbs.questionbank.page.content.tab.repository.WaUiMetaDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import javax.persistence.EntityManager;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AddSectionServiceTest {

    @InjectMocks
    private SectionCreator createSectionService;

    @Mock
    private WaUiMetaDataRepository waUiMetaDataRepository;

    @Mock
    private EntityManager entityManager;

    @Test
    void createSectionServiceTest() {

        CreateSectionRequest createSectionRequest =
                new CreateSectionRequest(10L, "Local", true);

        CreateSectionResponse createSectionResponse =
                createSectionService.createSection(10L, 123L, createSectionRequest);
        assertEquals("Section Created Successfully", createSectionResponse.message());
    }


    @Test
    void updateSectionServiceTest() {

        UpdateSectionRequest updateSectionRequest =
                new UpdateSectionRequest(  "Local", "T");

        UpdateSectionResponse updateSectionResponse =
                createSectionService.updateSection(123L, updateSectionRequest);
        assertEquals("Section updated successfully", updateSectionResponse.message());
    }


    @Test
    void updateSectionServiceNoLabelOrVisibilityTest() {

        UpdateSectionRequest updateSectionRequest =
                new UpdateSectionRequest(  null, null);

                assertThrows(UpdateSectionException.class, () ->createSectionService.updateSection(123L, updateSectionRequest));

    }

    @Test
    void deleteSectionTest() {

        Mockito.when(waUiMetaDataRepository.getOrderNumber(123L))
                .thenReturn(1);

        Mockito.when(waUiMetaDataRepository.findNextNbsUiComponentUid( 2, 1234L))
                .thenReturn(Optional.of(1015L));

        DeleteSectionResponse deleteSectionResponse =
                createSectionService.deleteSection(1234L, 123L);
        assertEquals("Section deleted successfully", deleteSectionResponse.message());
    }


    @Test
    void deleteSectionTestExceptionInElse() {


        Mockito.when(waUiMetaDataRepository.getOrderNumber(123L))
                .thenReturn(1);

        Mockito.when(waUiMetaDataRepository.findNextNbsUiComponentUid( 2, 1234L))
                .thenReturn(Optional.of(10L));

        assertThrows(DeleteSectionException.class, () ->
                createSectionService.deleteSection(1234L, 123L));

    }

    @Test
    void deleteSectionTestInElse() {

        Mockito.when(waUiMetaDataRepository.getOrderNumber(123L))
                .thenReturn(1);

        Mockito.when(waUiMetaDataRepository.findNextNbsUiComponentUid( 2, 1234L))
                .thenReturn(Optional.empty());

        DeleteSectionResponse deleteSectionResponse =
                createSectionService.deleteSection(1234L, 123L);
        assertEquals("Section deleted successfully", deleteSectionResponse.message());

    }

    @Test
    void createSectionServiceTestException() {

        assertThrows(AddSectionException.class, () -> createSectionService.createSection(10L, 123L, null));

    }

    @Test
    void updateSectionServiceTestException() {
        assertThrows(UpdateSectionException.class, () -> createSectionService.updateSection(123L, null));

    }
    @Test
    void deleteSectionServiceTestException() {
        Mockito.when(waUiMetaDataRepository.findNextNbsUiComponentUid( 2, 1234L))
            .thenThrow(new DeleteSectionException(""));

        assertThrows(DeleteSectionException.class, () ->
                createSectionService.deleteSection(100L, 123L));

    }
}
