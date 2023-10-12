package gov.cdc.nbs.questionbank.page.download;

import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.questionbank.page.PageController;
import gov.cdc.nbs.questionbank.page.PageCreator;
import gov.cdc.nbs.questionbank.page.PageDownloader;
import gov.cdc.nbs.questionbank.page.PageFinder;
import gov.cdc.nbs.questionbank.page.PageStateChanger;
import gov.cdc.nbs.questionbank.page.services.PageSummaryFinder;
import gov.cdc.nbs.questionbank.page.services.PageUpdater;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {

    @Mock
    private PageUpdater pageUpdater;
    @Mock
    private PageSummaryFinder finder;
    @Mock
    private PageFinder pageFinder;
    @Mock
    private PageCreator creator;
    @Mock
    private PageStateChanger stateChange;
    @Mock
    private PageDownloader pageDownloader;
    @Mock
    private UserDetailsProvider userDetailsProvider;

    @Test
    void downloadPageLibraryPDFTest() throws Exception {

        PageController pageController = new PageController(pageUpdater, finder, pageFinder, creator, stateChange,
                pageDownloader, userDetailsProvider);

        byte[] resp = "pagedownloader".getBytes();
        Mockito.when(pageDownloader.downloadLibraryPDF())
                .thenReturn(resp);
        ResponseEntity<byte[]> actual = pageController.downloadPageLibraryPDF();
        assertNotNull(actual);
    }
}