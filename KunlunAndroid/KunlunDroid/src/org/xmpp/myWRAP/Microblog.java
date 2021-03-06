/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class Microblog {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Microblog(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Microblog obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_Microblog(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public Microblog(String id_) {
    this(klcppwrapJNI.new_Microblog(id_), true);
  }

  public String id() {
    return klcppwrapJNI.Microblog_id(swigCPtr, this);
  }

  public String author() {
    return klcppwrapJNI.Microblog_author(swigCPtr, this);
  }

  public Microblog.Type type() {
    return Microblog.Type.swigToEnum(klcppwrapJNI.Microblog_type(swigCPtr, this));
  }

  public String content() {
    return klcppwrapJNI.Microblog_content(swigCPtr, this);
  }

  public String published() {
    return klcppwrapJNI.Microblog_published(swigCPtr, this);
  }

  public String geoloc() {
    return klcppwrapJNI.Microblog_geoloc(swigCPtr, this);
  }

  public String device() {
    return klcppwrapJNI.Microblog_device(swigCPtr, this);
  }

  public String commentLink() {
    return klcppwrapJNI.Microblog_commentLink(swigCPtr, this);
  }

  public void setAuthor(String author_) {
    klcppwrapJNI.Microblog_setAuthor(swigCPtr, this, author_);
  }

  public void setType(Microblog.Type type_) {
    klcppwrapJNI.Microblog_setType(swigCPtr, this, type_.swigValue());
  }

  public void setContent(String content_) {
    klcppwrapJNI.Microblog_setContent(swigCPtr, this, content_);
  }

  public void setPublished(String published_) {
    klcppwrapJNI.Microblog_setPublished(swigCPtr, this, published_);
  }

  public void setGeoloc(String geoloc_) {
    klcppwrapJNI.Microblog_setGeoloc(swigCPtr, this, geoloc_);
  }

  public void setDevice(String device_) {
    klcppwrapJNI.Microblog_setDevice(swigCPtr, this, device_);
  }

  public void setCommentLink(String commentLink_) {
    klcppwrapJNI.Microblog_setCommentLink(swigCPtr, this, commentLink_);
  }

  public final static class Type {
    public final static Microblog.Type Text = new Microblog.Type("Text");
    public final static Microblog.Type Xhtml = new Microblog.Type("Xhtml");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static Type swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + Type.class + " with value " + swigValue);
    }

    private Type(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private Type(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private Type(String swigName, Type swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static Type[] swigValues = { Text, Xhtml };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
