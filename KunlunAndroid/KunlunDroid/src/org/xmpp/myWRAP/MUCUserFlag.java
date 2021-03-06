/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public final class MUCUserFlag {
  public final static MUCUserFlag UserSelf = new MUCUserFlag("UserSelf", klcppwrapJNI.UserSelf_get());
  public final static MUCUserFlag UserNickChanged = new MUCUserFlag("UserNickChanged", klcppwrapJNI.UserNickChanged_get());
  public final static MUCUserFlag UserKicked = new MUCUserFlag("UserKicked", klcppwrapJNI.UserKicked_get());
  public final static MUCUserFlag UserBanned = new MUCUserFlag("UserBanned", klcppwrapJNI.UserBanned_get());
  public final static MUCUserFlag UserAffiliationChanged = new MUCUserFlag("UserAffiliationChanged", klcppwrapJNI.UserAffiliationChanged_get());
  public final static MUCUserFlag UserRoomDestroyed = new MUCUserFlag("UserRoomDestroyed", klcppwrapJNI.UserRoomDestroyed_get());
  public final static MUCUserFlag UserNickAssigned = new MUCUserFlag("UserNickAssigned", klcppwrapJNI.UserNickAssigned_get());
  public final static MUCUserFlag UserNewRoom = new MUCUserFlag("UserNewRoom", klcppwrapJNI.UserNewRoom_get());
  public final static MUCUserFlag UserMembershipRequired = new MUCUserFlag("UserMembershipRequired", klcppwrapJNI.UserMembershipRequired_get());
  public final static MUCUserFlag UserRoomShutdown = new MUCUserFlag("UserRoomShutdown", klcppwrapJNI.UserRoomShutdown_get());
  public final static MUCUserFlag UserAffiliationChangedWNR = new MUCUserFlag("UserAffiliationChangedWNR", klcppwrapJNI.UserAffiliationChangedWNR_get());

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static MUCUserFlag swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + MUCUserFlag.class + " with value " + swigValue);
  }

  private MUCUserFlag(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private MUCUserFlag(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private MUCUserFlag(String swigName, MUCUserFlag swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static MUCUserFlag[] swigValues = { UserSelf, UserNickChanged, UserKicked, UserBanned, UserAffiliationChanged, UserRoomDestroyed, UserNickAssigned, UserNewRoom, UserMembershipRequired, UserRoomShutdown, UserAffiliationChangedWNR };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}

