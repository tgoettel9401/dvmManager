package dsj.dvmManager.pgnParser;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgnParserService {
	
	public Pgn createPgnFromString(String pgnString)  {
		return transformPgnRawToPgn(createPgnRawFromString(pgnString));
	}

	PgnRaw createPgnRawFromString(String pgnString) {

		PgnRaw pgnRaw = new PgnRaw();

		List<String> lines = divideIntoLines(pgnString);

		for (String line : lines) {

			// Check if field is relevant for PGN.
			boolean isRelevantField = false;
			PgnField thisField = null;
			for (PgnField field : PgnField.values()) {
				if (line.contains("[" + field.getFieldString())) {
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

	Pgn transformPgnRawToPgn(PgnRaw pgnRaw) {
		Pgn pgn = new Pgn();
		pgn.setResult(PgnResult.fromResultString(pgnRaw.getResult()));
		return pgn;
	}

	List<String> divideIntoLines(String pgnString) {
		List<String> lines = Lists.newArrayList();

		String[] linesArray = pgnString.split("\\n");

		for (String line : linesArray) {
			lines.add(line);
		}

		return lines;
	}

	PgnRaw updatePgnRaw(PgnRaw pgnRaw, PgnField field, String value) {
		switch (field) {
			case RESULT:
				pgnRaw.setResult(value);
		}
		return pgnRaw;
	}

}
