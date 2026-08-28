public enum CommandType {
    TODO,
    DEADLINE,
    EVENT,
    LIST,
    ON,
    MARK,
    UNMARK,
    DELETE,
    BYE,
    UNKNOWN;

    public static CommandType from(String input) {
        String trimmedInput = input.trim();

        if (trimmedInput.isEmpty()) {
            return UNKNOWN;
        }

        String keyword = trimmedInput.split("\\s+", 2)[0].toUpperCase();

        try {
            return valueOf(keyword);
        } catch (IllegalArgumentException exception) {
            return UNKNOWN;
        }
    }
}
