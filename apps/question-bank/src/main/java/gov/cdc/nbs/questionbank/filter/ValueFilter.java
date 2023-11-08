package gov.cdc.nbs.questionbank.filter;

public sealed interface ValueFilter extends Filter permits MultiValueFilter, SingleValueFilter {

  Operator operator();

  enum Operator {
    EQUALS("equals"),
    NOT_EQUAL_TO("not equal to"),
    STARTS_WITH("starts with"),
    CONTAINS("contains");

    private final String display;

    Operator(final String display) {
      this.display = display;
    }

    public String display() {
      return display;
    }
  }

}
