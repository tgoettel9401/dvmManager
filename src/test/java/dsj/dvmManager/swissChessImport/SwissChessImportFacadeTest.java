package dsj.dvmManager.swissChessImport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class SwissChessImportFacadeTest {

    @InjectMocks
    private SwissChessImportFacade swissChessImportFacade;

    @Mock
    private SwissChessLstImportService swissChessLstImportService;

    @Mock
    private SwissChessPgnImportService swissChessPgnImportService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void importSwissChessLst() {
        InputStream inputStream = mock(InputStream.class);
        SwissChessLstResult correctLstResult = mock(SwissChessLstResult.class);
        when(swissChessLstImportService.importSwissChessLst(inputStream)).thenReturn(correctLstResult);
        SwissChessLstResult returnedLstResult = swissChessImportFacade.createSwissChessLstResult(inputStream);
        assertThat(returnedLstResult).isEqualTo(correctLstResult);
    }

    @Test
    void importSwissChessPgn() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        SwissChessPgnResult correctPgnResult = mock(SwissChessPgnResult.class);
        when(swissChessPgnImportService.importSwissChessPgn(inputStream)).thenReturn(correctPgnResult);
        SwissChessPgnResult returnedPgnResult = swissChessImportFacade.createSwissChessPgnResult(inputStream);
        assertThat(returnedPgnResult).isEqualTo(correctPgnResult);
    }

}