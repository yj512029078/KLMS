/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class VCardAddressList {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected VCardAddressList(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(VCardAddressList obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_VCardAddressList(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VCardAddressList() {
    this(klcppwrapJNI.new_VCardAddressList(), true);
  }

  public long size() {
    return klcppwrapJNI.VCardAddressList_size(swigCPtr, this);
  }

  public boolean isEmpty() {
    return klcppwrapJNI.VCardAddressList_isEmpty(swigCPtr, this);
  }

  public void clear() {
    klcppwrapJNI.VCardAddressList_clear(swigCPtr, this);
  }

  public void add(VCardAddress x) {
    klcppwrapJNI.VCardAddressList_add(swigCPtr, this, VCardAddress.getCPtr(x), x);
  }

  public VCardAddress get(int index) {
    long cPtr = klcppwrapJNI.VCardAddressList_get(swigCPtr, this, index);
    return (cPtr == 0) ? null : new VCardAddress(cPtr, false);
  }

}
