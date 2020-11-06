package dsj.dvmManager.pgnParser;

import lombok.Data;

@Data
public class PgnRaw {

	// PGN consists of many more information, but only Result is saved here.
	private String result;

}
