/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class StringList {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected StringList(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(StringList obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_StringList(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public StringList() {
    this(klcppwrapJNI.new_StringList(), true);
  }

  public long size() {
    return klcppwrapJNI.StringList_size(swigCPtr, this);
  }

  public boolean isEmpty() {
    return klcppwrapJNI.StringList_isEmpty(swigCPtr, this);
  }

  public void clear() {
    klcppwrapJNI.StringList_clear(swigCPtr, this);
  }

  public void add(String x) {
    klcppwrapJNI.StringList_add(swigCPtr, this, x);
  }

  public String get(int index) {
    return klcppwrapJNI.StringList_get(swigCPtr, this, index);
  }

}
