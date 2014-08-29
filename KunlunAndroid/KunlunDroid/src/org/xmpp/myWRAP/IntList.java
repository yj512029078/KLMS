/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class IntList {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IntList(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IntList obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_IntList(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public IntList() {
    this(klcppwrapJNI.new_IntList(), true);
  }

  public long size() {
    return klcppwrapJNI.IntList_size(swigCPtr, this);
  }

  public boolean isEmpty() {
    return klcppwrapJNI.IntList_isEmpty(swigCPtr, this);
  }

  public void clear() {
    klcppwrapJNI.IntList_clear(swigCPtr, this);
  }

  public void add(int x) {
    klcppwrapJNI.IntList_add(swigCPtr, this, x);
  }

  public int get(int index) {
    return klcppwrapJNI.IntList_get(swigCPtr, this, index);
  }

}
