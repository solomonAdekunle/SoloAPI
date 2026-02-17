package config;

public class PropsUtils {

    protected static String escapeString(String str) {
        StringBuilder result = new StringBuilder();

        int length = str.length();
        for (int i = 0; i < length; i++) {
            char character = str.charAt(i);
            if (character <= 0x007e) {
                result.append(character);
                continue;
            }

            result.append('\\');
            result.append('u');
            result.append(hexDigit(character, 12));
            result.append(hexDigit(character, 8));
            result.append(hexDigit(character, 4));
            result.append(hexDigit(character, 0));
        }
        return result.toString();
    }

    private static char hexDigit(char ch, int offset) {
        int value = (ch >> offset) & 0xF;

        if (value <= 9)
            return (char) ('0' + value);

        return (char) ('A' + value - 10);
    }
}
