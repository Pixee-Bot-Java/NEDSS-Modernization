package gov.cdc.nbs.search;

public class WildCards {

  public static String startsWith(final String value) {
    return isValid(value)
        ? value.toLowerCase().trim() + "*"
        : null;
  }

  public static String contains(final String value) {
    return isValid(value)
        ? "*" + value.toLowerCase().trim() + "*"
        : null;
  }

  private static boolean isValid(final String value) {
    return value != null && !value.isEmpty();
  }

  private WildCards() {
  }
}
