package dsj.dvmManager.swissChessImport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * SwissChess-Data is imported in two steps
 * 1) Import of all players including their access-tokens as LST-file
 * 2) Import of the round's games
 */
@Service
public class SwissChessImportFacade {

    private final SwissChessLstImportService swissChessLstImportService;
    private final SwissChessPgnImportService swissChessPgnImportService;

    @Autowired
    public SwissChessImportFacade(SwissChessLstImportService swissChessLstImportService,
                                  SwissChessPgnImportService swissChessPgnImportService) {
        this.swissChessLstImportService = swissChessLstImportService;
        this.swissChessPgnImportService = swissChessPgnImportService;
    }

    public SwissChessLstResult createSwissChessLstResult(InputStream inputStream) {
        return swissChessLstImportService.importSwissChessLst(inputStream);
    }

    public SwissChessPgnResult createSwissChessPgnResult(InputStream inputStream) throws IOException {
        return swissChessPgnImportService.importSwissChessPgn(inputStream);
    }

}
