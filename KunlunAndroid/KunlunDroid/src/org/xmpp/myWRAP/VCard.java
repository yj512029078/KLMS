/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class VCard {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected VCard(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(VCard obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_VCard(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public VCard() {
    this(klcppwrapJNI.new_VCard__SWIG_0(), true);
  }

  public VCard(String xml_) {
    this(klcppwrapJNI.new_VCard__SWIG_1(xml_), true);
  }

  public boolean isValid() {
    return klcppwrapJNI.VCard_isValid(swigCPtr, this);
  }

  public String xml() {
    return klcppwrapJNI.VCard_xml(swigCPtr, this);
  }

  public void setFormattedname(String name_) {
    klcppwrapJNI.VCard_setFormattedname(swigCPtr, this, name_);
  }

  public String formattedname() {
    return klcppwrapJNI.VCard_formattedname(swigCPtr, this);
  }

  public void setName(String family_, String given_) {
    klcppwrapJNI.VCard_setName(swigCPtr, this, family_, given_);
  }

  public VCardName name() {
    long cPtr = klcppwrapJNI.VCard_name(swigCPtr, this);
    return (cPtr == 0) ? null : new VCardName(cPtr, false);
  }

  public void setNickname(String nickname_) {
    klcppwrapJNI.VCard_setNickname(swigCPtr, this, nickname_);
  }

  public String nickname() {
    return klcppwrapJNI.VCard_nickname(swigCPtr, this);
  }

  public void setUrl(String url_) {
    klcppwrapJNI.VCard_setUrl(swigCPtr, this, url_);
  }

  public String url() {
    return klcppwrapJNI.VCard_url(swigCPtr, this);
  }

  public void setBday(String bday_) {
    klcppwrapJNI.VCard_setBday(swigCPtr, this, bday_);
  }

  public String bday() {
    return klcppwrapJNI.VCard_bday(swigCPtr, this);
  }

  public void setJabberid(String jabberid_) {
    klcppwrapJNI.VCard_setJabberid(swigCPtr, this, jabberid_);
  }

  public String jabberid() {
    return klcppwrapJNI.VCard_jabberid(swigCPtr, this);
  }

  public void setTitle(String title_) {
    klcppwrapJNI.VCard_setTitle(swigCPtr, this, title_);
  }

  public String title() {
    return klcppwrapJNI.VCard_title(swigCPtr, this);
  }

  public void setRole(String role_) {
    klcppwrapJNI.VCard_setRole(swigCPtr, this, role_);
  }

  public String role() {
    return klcppwrapJNI.VCard_role(swigCPtr, this);
  }

  public void setNote(String note_) {
    klcppwrapJNI.VCard_setNote(swigCPtr, this, note_);
  }

  public String note() {
    return klcppwrapJNI.VCard_note(swigCPtr, this);
  }

  public void setDesc(String desc_) {
    klcppwrapJNI.VCard_setDesc(swigCPtr, this, desc_);
  }

  public String desc() {
    return klcppwrapJNI.VCard_desc(swigCPtr, this);
  }

  public void setMailer(String mailer_) {
    klcppwrapJNI.VCard_setMailer(swigCPtr, this, mailer_);
  }

  public String mailer() {
    return klcppwrapJNI.VCard_mailer(swigCPtr, this);
  }

  public void setRev(String rev_) {
    klcppwrapJNI.VCard_setRev(swigCPtr, this, rev_);
  }

  public String rev() {
    return klcppwrapJNI.VCard_rev(swigCPtr, this);
  }

  public void setUid(String uid_) {
    klcppwrapJNI.VCard_setUid(swigCPtr, this, uid_);
  }

  public String uid() {
    return klcppwrapJNI.VCard_uid(swigCPtr, this);
  }

  public void setTz(String tz_) {
    klcppwrapJNI.VCard_setTz(swigCPtr, this, tz_);
  }

  public String tz() {
    return klcppwrapJNI.VCard_tz(swigCPtr, this);
  }

  public void setProdid(String prodid_) {
    klcppwrapJNI.VCard_setProdid(swigCPtr, this, prodid_);
  }

  public String prodid() {
    return klcppwrapJNI.VCard_prodid(swigCPtr, this);
  }

  public void setSortstring(String sortstring_) {
    klcppwrapJNI.VCard_setSortstring(swigCPtr, this, sortstring_);
  }

  public String sortstring() {
    return klcppwrapJNI.VCard_sortstring(swigCPtr, this);
  }

  public void setPhoto(String type_, String binval_) {
    klcppwrapJNI.VCard_setPhoto(swigCPtr, this, type_, binval_);
  }

  public VCardPhoto photo() {
    long cPtr = klcppwrapJNI.VCard_photo(swigCPtr, this);
    return (cPtr == 0) ? null : new VCardPhoto(cPtr, false);
  }

  public void setLogo(String type_, String binval_) {
    klcppwrapJNI.VCard_setLogo(swigCPtr, this, type_, binval_);
  }

  public VCardPhoto logo() {
    long cPtr = klcppwrapJNI.VCard_logo(swigCPtr, this);
    return (cPtr == 0) ? null : new VCardPhoto(cPtr, false);
  }

  public void addEmail(String userid_, int type_) {
    klcppwrapJNI.VCard_addEmail(swigCPtr, this, userid_, type_);
  }

  public VCardEmailList emailList() {
    return new VCardEmailList(klcppwrapJNI.VCard_emailList(swigCPtr, this), false);
  }

  public void addAddress(String pobox_, String extadd_, String street_, String locality_, String region_, String pcode_, String ctry_, int type_) {
    klcppwrapJNI.VCard_addAddress(swigCPtr, this, pobox_, extadd_, street_, locality_, region_, pcode_, ctry_, type_);
  }

  public VCardAddressList addressList() {
    return new VCardAddressList(klcppwrapJNI.VCard_addressList(swigCPtr, this), false);
  }

  public void addLabel(StringList lines_, int type_) {
    klcppwrapJNI.VCard_addLabel(swigCPtr, this, StringList.getCPtr(lines_), lines_, type_);
  }

  public VCardLabelList labelList() {
    return new VCardLabelList(klcppwrapJNI.VCard_labelList(swigCPtr, this), false);
  }

  public void addTelephone(String number_, int type_) {
    klcppwrapJNI.VCard_addTelephone(swigCPtr, this, number_, type_);
  }

  public VCardTelephponeList telephoneList() {
    return new VCardTelephponeList(klcppwrapJNI.VCard_telephoneList(swigCPtr, this), false);
  }

  public void setGeo(String lat_, String lon_) {
    klcppwrapJNI.VCard_setGeo(swigCPtr, this, lat_, lon_);
  }

  public VCardGeo geo() {
    long cPtr = klcppwrapJNI.VCard_geo(swigCPtr, this);
    return (cPtr == 0) ? null : new VCardGeo(cPtr, false);
  }

  public void setOrg(String name_, StringList units_) {
    klcppwrapJNI.VCard_setOrg(swigCPtr, this, name_, StringList.getCPtr(units_), units_);
  }

  public VCardOrg org() {
    long cPtr = klcppwrapJNI.VCard_org(swigCPtr, this);
    return (cPtr == 0) ? null : new VCardOrg(cPtr, false);
  }

}
