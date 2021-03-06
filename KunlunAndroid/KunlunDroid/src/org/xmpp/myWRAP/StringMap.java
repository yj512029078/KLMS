/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class StringMap {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected StringMap(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(StringMap obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_StringMap(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public StringMap() {
    this(klcppwrapJNI.new_StringMap__SWIG_0(), true);
  }

  public StringMap(StringMap arg0) {
    this(klcppwrapJNI.new_StringMap__SWIG_1(StringMap.getCPtr(arg0), arg0), true);
  }

  public long size() {
    return klcppwrapJNI.StringMap_size(swigCPtr, this);
  }

  public boolean empty() {
    return klcppwrapJNI.StringMap_empty(swigCPtr, this);
  }

  public void clear() {
    klcppwrapJNI.StringMap_clear(swigCPtr, this);
  }

  public StringList keys() {
    return new StringList(klcppwrapJNI.StringMap_keys(swigCPtr, this), true);
  }

  public String get(String key) {
    return klcppwrapJNI.StringMap_get(swigCPtr, this, key);
  }

  public void set(String key, String x) {
    klcppwrapJNI.StringMap_set(swigCPtr, this, key, x);
  }

  public void del(String key) {
    klcppwrapJNI.StringMap_del(swigCPtr, this, key);
  }

  public boolean has_key(String key) {
    return klcppwrapJNI.StringMap_has_key(swigCPtr, this, key);
  }

}
