/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class FileMetadata {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected FileMetadata(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(FileMetadata obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_FileMetadata(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setName(String value) {
    klcppwrapJNI.FileMetadata_name_set(swigCPtr, this, value);
  }

  public String getName() {
    return klcppwrapJNI.FileMetadata_name_get(swigCPtr, this);
  }

  public void setSize(int value) {
    klcppwrapJNI.FileMetadata_size_set(swigCPtr, this, value);
  }

  public int getSize() {
    return klcppwrapJNI.FileMetadata_size_get(swigCPtr, this);
  }

  public void setHash(String value) {
    klcppwrapJNI.FileMetadata_hash_set(swigCPtr, this, value);
  }

  public String getHash() {
    return klcppwrapJNI.FileMetadata_hash_get(swigCPtr, this);
  }

  public void setDate(String value) {
    klcppwrapJNI.FileMetadata_date_set(swigCPtr, this, value);
  }

  public String getDate() {
    return klcppwrapJNI.FileMetadata_date_get(swigCPtr, this);
  }

  public void setMimetype(String value) {
    klcppwrapJNI.FileMetadata_mimetype_set(swigCPtr, this, value);
  }

  public String getMimetype() {
    return klcppwrapJNI.FileMetadata_mimetype_get(swigCPtr, this);
  }

  public void setDesc(String value) {
    klcppwrapJNI.FileMetadata_desc_set(swigCPtr, this, value);
  }

  public String getDesc() {
    return klcppwrapJNI.FileMetadata_desc_get(swigCPtr, this);
  }

  public FileMetadata() {
    this(klcppwrapJNI.new_FileMetadata(), true);
  }

}
