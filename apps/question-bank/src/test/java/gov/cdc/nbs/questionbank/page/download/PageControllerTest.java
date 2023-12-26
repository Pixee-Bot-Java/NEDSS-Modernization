package gov.cdc.nbs.questionbank.page.download;

import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.questionbank.page.PageController;
import gov.cdc.nbs.questionbank.page.PageCreator;
import gov.cdc.nbs.questionbank.page.PageDownloader;
import gov.cdc.nbs.questionbank.page.PageStateChanger;
import gov.cdc.nbs.questionbank.page.model.PageHistory;
import gov.cdc.nbs.questionbank.page.service.PageHistoryFinder;
import org.junit.jupiter.api.BeforeEach;
import gov.cdc.nbs.questionbank.page.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

  @Mock
  private PageCreator creator;
  @Mock
  private PageStateChanger stateChange;
  @Mock
  private PageDownloader pageDownloader;
  @Mock
  private UserDetailsProvider userDetailsProvider;
  @Mock
  private PageDeletor pageDeletor;
  @Mock
  private PageMetaDataDownloader pageMetaDataDownloader;


  @Mock
  private PageHistoryFinder pageHistoryFinder;

  private PageController pageController;

  @BeforeEach
  void setUp() {
    pageController = new PageController(creator, stateChange,
        pageDownloader, userDetailsProvider, pageDeletor, pageMetaDataDownloader, pageHistoryFinder);
  }

  @Test
  void downloadPageLibraryPDFTest() throws Exception {

    byte[] resp = "pagedownloader".getBytes();
    Mockito.when(pageDownloader.downloadLibraryPDF())
        .thenReturn(resp);
    ResponseEntity<byte[]> actual = pageController.downloadPageLibraryPDF();
    assertNotNull(actual);
  }

  @Test
  void downloadPageMetadataTest() throws IOException {
    Long waTemplateUid = 1L;
    when(pageMetaDataDownloader.downloadPageMetadataByWaTemplateUid(waTemplateUid))
        .thenReturn(new ByteArrayInputStream("test,csv,data".getBytes()));

    ResponseEntity<Resource> response = pageController.downloadPageMetadata(waTemplateUid);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("attachment; filename=PageMetadata.xlsx",
        response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    assertEquals(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        response.getHeaders().getContentType());
  }

  @Test
  void downloadPageMetadataExceptionTest() throws IOException {
    Long waTemplateUid = 1L;
    when(pageMetaDataDownloader.downloadPageMetadataByWaTemplateUid(waTemplateUid))
        .thenReturn(new ByteArrayInputStream("test,csv,data".getBytes()));

    when(pageMetaDataDownloader.downloadPageMetadataByWaTemplateUid(waTemplateUid))
        .thenThrow(new IOException("Error Downloading Page History"));
    var exception = assertThrows(IOException.class, () -> pageController.downloadPageMetadata(waTemplateUid));
    assertTrue(exception.getMessage().contains("Error Downloading Page History"));

  }

  void getPageHistoryTest() {
    List<PageHistory> expectedPageHistory = Arrays.asList(
        new PageHistory("1", "09/25/2019", "User1", "Note1"),
        new PageHistory("2", "09/25/2019", "User2", "Note2")
    );
    when(pageHistoryFinder.getPageHistory(100l)).thenReturn(expectedPageHistory);
    List<PageHistory> actualPageHistory = pageController.getPageHistory(100l);
    assertEquals(expectedPageHistory, actualPageHistory);
  }

  @Test
  void getPageHistoryException() {
    when(pageHistoryFinder.getPageHistory(100l)).
        thenThrow(new RuntimeException("Error Fetching Page-History by Template_nm From the Database"));
    var exception = assertThrows(RuntimeException.class, () -> pageController.getPageHistory(100l));
    assertTrue(exception.getMessage().contains("Error Fetching Page-History by Template_nm From the Database"));
  }


}
