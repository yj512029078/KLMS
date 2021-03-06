/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class Presence extends Stanza {
  private long swigCPtr;

  protected Presence(long cPtr, boolean cMemoryOwn) {
    super(klcppwrapJNI.Presence_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Presence obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_Presence(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Presence(Presence.PresenceType type, JID to, String status, int priority, String xmllang) {
    this(klcppwrapJNI.new_Presence__SWIG_0(type.swigValue(), JID.getCPtr(to), to, status, priority, xmllang), true);
  }

  public Presence(Presence.PresenceType type, JID to, String status, int priority) {
    this(klcppwrapJNI.new_Presence__SWIG_1(type.swigValue(), JID.getCPtr(to), to, status, priority), true);
  }

  public Presence(Presence.PresenceType type, JID to, String status) {
    this(klcppwrapJNI.new_Presence__SWIG_2(type.swigValue(), JID.getCPtr(to), to, status), true);
  }

  public Presence(Presence.PresenceType type, JID to) {
    this(klcppwrapJNI.new_Presence__SWIG_3(type.swigValue(), JID.getCPtr(to), to), true);
  }

  public Presence.PresenceType subtype() {
    return Presence.PresenceType.swigToEnum(klcppwrapJNI.Presence_subtype(swigCPtr, this));
  }

  public Presence.PresenceType presence() {
    return Presence.PresenceType.swigToEnum(klcppwrapJNI.Presence_presence(swigCPtr, this));
  }

  public void setPresence(Presence.PresenceType type) {
    klcppwrapJNI.Presence_setPresence(swigCPtr, this, type.swigValue());
  }

  public String status(String lang) {
    return klcppwrapJNI.Presence_status__SWIG_0(swigCPtr, this, lang);
  }

  public String status() {
    return klcppwrapJNI.Presence_status__SWIG_1(swigCPtr, this);
  }

  public void addStatus(String status, String lang) {
    klcppwrapJNI.Presence_addStatus__SWIG_0(swigCPtr, this, status, lang);
  }

  public void addStatus(String status) {
    klcppwrapJNI.Presence_addStatus__SWIG_1(swigCPtr, this, status);
  }

  public void resetStatus() {
    klcppwrapJNI.Presence_resetStatus(swigCPtr, this);
  }

  public int priority() {
    return klcppwrapJNI.Presence_priority(swigCPtr, this);
  }

  public void setPriority(int priority) {
    klcppwrapJNI.Presence_setPriority(swigCPtr, this, priority);
  }

  public final static class PresenceType {
    public final static Presence.PresenceType Available = new Presence.PresenceType("Available");
    public final static Presence.PresenceType Chat = new Presence.PresenceType("Chat");
    public final static Presence.PresenceType Away = new Presence.PresenceType("Away");
    public final static Presence.PresenceType DND = new Presence.PresenceType("DND");
    public final static Presence.PresenceType XA = new Presence.PresenceType("XA");
    public final static Presence.PresenceType Unavailable = new Presence.PresenceType("Unavailable");
    public final static Presence.PresenceType Probe = new Presence.PresenceType("Probe");
    public final static Presence.PresenceType Error = new Presence.PresenceType("Error");
    public final static Presence.PresenceType Invalid = new Presence.PresenceType("Invalid");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static PresenceType swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + PresenceType.class + " with value " + swigValue);
    }

    private PresenceType(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private PresenceType(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private PresenceType(String swigName, PresenceType swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static PresenceType[] swigValues = { Available, Chat, Away, DND, XA, Unavailable, Probe, Error, Invalid };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
