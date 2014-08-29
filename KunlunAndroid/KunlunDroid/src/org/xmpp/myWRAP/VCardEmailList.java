/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class VCardEmailList {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected VCardEmailList(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(VCardEmailList obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_VCardEmailList(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VCardEmailList() {
    this(klcppwrapJNI.new_VCardEmailList(), true);
  }

  public long size() {
    return klcppwrapJNI.VCardEmailList_size(swigCPtr, this);
  }

  public boolean isEmpty() {
    return klcppwrapJNI.VCardEmailList_isEmpty(swigCPtr, this);
  }

  public void clear() {
    klcppwrapJNI.VCardEmailList_clear(swigCPtr, this);
  }

  public void add(VCardEmail x) {
    klcppwrapJNI.VCardEmailList_add(swigCPtr, this, VCardEmail.getCPtr(x), x);
  }

  public VCardEmail get(int index) {
    long cPtr = klcppwrapJNI.VCardEmailList_get(swigCPtr, this, index);
    return (cPtr == 0) ? null : new VCardEmail(cPtr, false);
  }

}