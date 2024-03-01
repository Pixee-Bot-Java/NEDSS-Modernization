package gov.cdc.nbs.gateway;

public enum RouteOrdering {

  PATIENT_PROFILE(0),
  NBS_6(1000);

  private final int order;

  RouteOrdering(int order) {
    this.order = order;
  }

  public int order() {
    return order;
  }

  public int before() {
    return order - 1;
  }

  public int after() {
    return order + 1;
  }
}
