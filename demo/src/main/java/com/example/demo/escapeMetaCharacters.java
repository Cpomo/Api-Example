package com.example.demo;

//Class that removes special character, returns string
public class escapeMetaCharacters {
	public static String EscapeMetaCharacters(String inputString) {
		final String[] metaCharacters = { "\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<",
				">", "-", "&", "%", ";" };

		for (int i = 0; i < metaCharacters.length; i++) {
			if (inputString.contains(metaCharacters[i])) {
				inputString = inputString.replace(metaCharacters[i], "");
			}
		}
		return inputString;
	}
}
