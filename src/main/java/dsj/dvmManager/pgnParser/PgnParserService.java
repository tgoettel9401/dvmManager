package dsj.dvmManager.pgnParser;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PgnParserService {

	public List<PgnGame> createPgnListFromString(String pgnStringMultipleGames) {
		List<PgnGame> pgnGames = Lists.newArrayList();
		List<String> pgnDividedIntoLines = divideIntoLines(pgnStringMultipleGames);
		List<List<String>> gameStringsWithLines = divideIntoGames(pgnDividedIntoLines);
		gameStringsWithLines.forEach(gameLines -> pgnGames.add(createPgnFromPgnString(gameLines)));
		return pgnGames;
	}

	public PgnGame createPgnFromPgnString(List<String> pgnStringLines) {
		return transformPgnRawToPgn(createPgnRawFromString(pgnStringLines));
	}

	PgnGameRaw createPgnRawFromString(List<String> pgnStringLines) {

		PgnGameRaw pgnRaw = new PgnGameRaw();

		for (String line : pgnStringLines) {

			// Check if field is relevant for PGN.
			boolean isRelevantField = false;
			PgnField thisField = null;
			for (PgnField field : PgnField.values()) {
				if (line.contains(field.getFieldString())) {
					isRelevantField = true;
					thisField = field;
				}
			}

			if (isRelevantField) {
				int startOfValue = line.indexOf("\"")+1;
				int endOfValue = line.lastIndexOf("\"");
				String value = line.substring(startOfValue, endOfValue);
				updatePgnRaw(pgnRaw, thisField, value);
			}

		}

		return pgnRaw;
	}

	PgnGame transformPgnRawToPgn(PgnGameRaw pgnRaw) {
		PgnGame pgn = new PgnGame();
		pgn.setResult(PgnResult.fromResultString(pgnRaw.getResult()));
		pgn.setPlayerNameWhite(pgnRaw.getPlayerNameWhite());
		pgn.setPlayerNameBlack(pgnRaw.getPlayerNameBlack());
		pgn.setTeamNameWhite(pgnRaw.getTeamNameWhite());
		pgn.setTeamNameBlack(pgnRaw.getTeamNameBlack());
		if (!pgnRaw.getBoardNumber().isEmpty()) // BoardNumber may be not existing for importing pgn from Lichess.
			pgn.setBoardNumber(Integer.parseInt(pgnRaw.getBoardNumber()));
		return pgn;
	}

	public List<List<String>> divideIntoGames(List<String> pgnLinesMulipleGames) {
		List<List<String>> gameStrings = Lists.newArrayList();

		List<String> singleGameLines = Lists.newArrayList();
		boolean isStarted = false;
		for (String line : pgnLinesMulipleGames) {

			// Find the "border" lines that contain the string [Event
			if (line.contains(PgnField.EVENT.getFieldString())) {

				// If it is the first event, then set isStarted to true and add the line to the gameLines.
				if (!isStarted) {
					isStarted = true;
					singleGameLines.add(line);
				}

				// If isStarted is already set, then this is not the first event. This means a new game starts here.
				// Save the singleGameLines (previous game) and empty the list. Add this line as first line of new game.
				else {
					gameStrings.add(Lists.newArrayList(singleGameLines));
					singleGameLines.clear();
					singleGameLines.add(line);
				}
			}

			// Lines not at "border" (not containing "[Event") are added to singleGameLines if isStarted is already set.
			else {
				if (isStarted) {
					singleGameLines.add(line);
				}
			}
		}

		// After all lines have been processed, the last singleGame has to be added as well because it does not end with
		// an Event.
		gameStrings.add(singleGameLines);

		return gameStrings;
	}

	List<String> divideIntoLines(String pgnString) {
		List<String> lines = Lists.newArrayList();

		String[] linesArray = pgnString.split("\\n");

		lines.addAll(Arrays.asList(linesArray));

		return lines;
	}

	PgnGameRaw updatePgnRaw(PgnGameRaw pgnRaw, PgnField field, String value) {
		switch (field) {
			case RESULT:
				pgnRaw.setResult(value);
				return pgnRaw;
			case PLAYER_WHITE:
				pgnRaw.setPlayerNameWhite(value);
				return pgnRaw;
			case PLAYER_BLACK:
				pgnRaw.setPlayerNameBlack(value);
				return pgnRaw;
			case TEAM_WHITE:
				pgnRaw.setTeamNameWhite(value);
				return pgnRaw;
			case TEAM_BLACK:
				pgnRaw.setTeamNameBlack(value);
				return pgnRaw;
			case BOARD:
				pgnRaw.setBoardNumber(value);
				return pgnRaw;
			default:
				return pgnRaw;
		}
	}

}
