/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public final class ResourceBindError {
  public final static ResourceBindError RbErrorUnknownError = new ResourceBindError("RbErrorUnknownError");
  public final static ResourceBindError RbErrorBadRequest = new ResourceBindError("RbErrorBadRequest");
  public final static ResourceBindError RbErrorNotAllowed = new ResourceBindError("RbErrorNotAllowed");
  public final static ResourceBindError RbErrorConflict = new ResourceBindError("RbErrorConflict");

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static ResourceBindError swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + ResourceBindError.class + " with value " + swigValue);
  }

  private ResourceBindError(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private ResourceBindError(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private ResourceBindError(String swigName, ResourceBindError swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static ResourceBindError[] swigValues = { RbErrorUnknownError, RbErrorBadRequest, RbErrorNotAllowed, RbErrorConflict };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}

