/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public final class MUCRoomFlag {
  public final static MUCRoomFlag FlagPasswordProtected = new MUCRoomFlag("FlagPasswordProtected", klcppwrapJNI.FlagPasswordProtected_get());
  public final static MUCRoomFlag FlagPublicLogging = new MUCRoomFlag("FlagPublicLogging", klcppwrapJNI.FlagPublicLogging_get());
  public final static MUCRoomFlag FlagPublicLoggingOff = new MUCRoomFlag("FlagPublicLoggingOff", klcppwrapJNI.FlagPublicLoggingOff_get());
  public final static MUCRoomFlag FlagHidden = new MUCRoomFlag("FlagHidden", klcppwrapJNI.FlagHidden_get());
  public final static MUCRoomFlag FlagMembersOnly = new MUCRoomFlag("FlagMembersOnly", klcppwrapJNI.FlagMembersOnly_get());
  public final static MUCRoomFlag FlagModerated = new MUCRoomFlag("FlagModerated", klcppwrapJNI.FlagModerated_get());
  public final static MUCRoomFlag FlagNonAnonymous = new MUCRoomFlag("FlagNonAnonymous", klcppwrapJNI.FlagNonAnonymous_get());
  public final static MUCRoomFlag FlagOpen = new MUCRoomFlag("FlagOpen", klcppwrapJNI.FlagOpen_get());
  public final static MUCRoomFlag FlagPersistent = new MUCRoomFlag("FlagPersistent", klcppwrapJNI.FlagPersistent_get());
  public final static MUCRoomFlag FlagPublic = new MUCRoomFlag("FlagPublic", klcppwrapJNI.FlagPublic_get());
  public final static MUCRoomFlag FlagSemiAnonymous = new MUCRoomFlag("FlagSemiAnonymous", klcppwrapJNI.FlagSemiAnonymous_get());
  public final static MUCRoomFlag FlagTemporary = new MUCRoomFlag("FlagTemporary", klcppwrapJNI.FlagTemporary_get());
  public final static MUCRoomFlag FlagUnmoderated = new MUCRoomFlag("FlagUnmoderated", klcppwrapJNI.FlagUnmoderated_get());
  public final static MUCRoomFlag FlagUnsecured = new MUCRoomFlag("FlagUnsecured", klcppwrapJNI.FlagUnsecured_get());
  public final static MUCRoomFlag FlagFullyAnonymous = new MUCRoomFlag("FlagFullyAnonymous", klcppwrapJNI.FlagFullyAnonymous_get());

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static MUCRoomFlag swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + MUCRoomFlag.class + " with value " + swigValue);
  }

  private MUCRoomFlag(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private MUCRoomFlag(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private MUCRoomFlag(String swigName, MUCRoomFlag swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static MUCRoomFlag[] swigValues = { FlagPasswordProtected, FlagPublicLogging, FlagPublicLoggingOff, FlagHidden, FlagMembersOnly, FlagModerated, FlagNonAnonymous, FlagOpen, FlagPersistent, FlagPublic, FlagSemiAnonymous, FlagTemporary, FlagUnmoderated, FlagUnsecured, FlagFullyAnonymous };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}
