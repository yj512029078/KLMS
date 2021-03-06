/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public class XmppCallback {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected XmppCallback(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(XmppCallback obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        klcppwrapJNI.delete_XmppCallback(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    klcppwrapJNI.XmppCallback_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    klcppwrapJNI.XmppCallback_change_ownership(this, swigCPtr, true);
  }

  public void onLog(LogLevel level_, LogArea area_, String log_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onLog(swigCPtr, this, level_.swigValue(), area_.swigValue(), log_); else klcppwrapJNI.XmppCallback_onLogSwigExplicitXmppCallback(swigCPtr, this, level_.swigValue(), area_.swigValue(), log_);
  }

  public void onInvaildJID() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onInvaildJID(swigCPtr, this); else klcppwrapJNI.XmppCallback_onInvaildJIDSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onInvaildPassword() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onInvaildPassword(swigCPtr, this); else klcppwrapJNI.XmppCallback_onInvaildPasswordSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onTcpConnSuccess() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onTcpConnSuccess(swigCPtr, this); else klcppwrapJNI.XmppCallback_onTcpConnSuccessSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onTcpConnFailed(ConnectionError error_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onTcpConnFailed(swigCPtr, this, error_.swigValue()); else klcppwrapJNI.XmppCallback_onTcpConnFailedSwigExplicitXmppCallback(swigCPtr, this, error_.swigValue());
  }

  public void onNegotiatingEncryption() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onNegotiatingEncryption(swigCPtr, this); else klcppwrapJNI.XmppCallback_onNegotiatingEncryptionSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onNegotiatingCompression() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onNegotiatingCompression(swigCPtr, this); else klcppwrapJNI.XmppCallback_onNegotiatingCompressionSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onAuthenticating() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onAuthenticating(swigCPtr, this); else klcppwrapJNI.XmppCallback_onAuthenticatingSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onAuthFailed() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onAuthFailed(swigCPtr, this); else klcppwrapJNI.XmppCallback_onAuthFailedSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onBindingResource() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onBindingResource(swigCPtr, this); else klcppwrapJNI.XmppCallback_onBindingResourceSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onCreatingSession() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onCreatingSession(swigCPtr, this); else klcppwrapJNI.XmppCallback_onCreatingSessionSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onLoadingRoster() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onLoadingRoster(swigCPtr, this); else klcppwrapJNI.XmppCallback_onLoadingRosterSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onLoginSuccess() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onLoginSuccess(swigCPtr, this); else klcppwrapJNI.XmppCallback_onLoginSuccessSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onDiscoMucService(JID mucService_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDiscoMucService(swigCPtr, this, JID.getCPtr(mucService_), mucService_); else klcppwrapJNI.XmppCallback_onDiscoMucServiceSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(mucService_), mucService_);
  }

  public void onDiscoS5BService(JID s5BService_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDiscoS5BService(swigCPtr, this, JID.getCPtr(s5BService_), s5BService_); else klcppwrapJNI.XmppCallback_onDiscoS5BServiceSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(s5BService_), s5BService_);
  }

  public void onRetrieveEntityInfo(JID jid_, EntityInfo info_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRetrieveEntityInfo(swigCPtr, this, JID.getCPtr(jid_), jid_, EntityInfo.getCPtr(info_), info_); else klcppwrapJNI.XmppCallback_onRetrieveEntityInfoSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(jid_), jid_, EntityInfo.getCPtr(info_), info_);
  }

  public void onLogoutSuccess() {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onLogoutSuccess(swigCPtr, this); else klcppwrapJNI.XmppCallback_onLogoutSuccessSwigExplicitXmppCallback(swigCPtr, this);
  }

  public void onRecvSelfPresence(RosterItem item_, String resource_, Presence.PresenceType type_, String msg_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvSelfPresence(swigCPtr, this, RosterItem.getCPtr(item_), item_, resource_, type_.swigValue(), msg_); else klcppwrapJNI.XmppCallback_onRecvSelfPresenceSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(item_), item_, resource_, type_.swigValue(), msg_);
  }

  public void onRecvRosterPresence(RosterItem self_, String resource_, Presence.PresenceType type_, String msg_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvRosterPresence(swigCPtr, this, RosterItem.getCPtr(self_), self_, resource_, type_.swigValue(), msg_); else klcppwrapJNI.XmppCallback_onRecvRosterPresenceSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(self_), self_, resource_, type_.swigValue(), msg_);
  }

  public void onRecvChatMessage(Message msg_, String xhtml_, boolean receipts_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvChatMessage(swigCPtr, this, Message.getCPtr(msg_), msg_, xhtml_, receipts_); else klcppwrapJNI.XmppCallback_onRecvChatMessageSwigExplicitXmppCallback(swigCPtr, this, Message.getCPtr(msg_), msg_, xhtml_, receipts_);
  }

  public void onRecvAttentionMessage(String id_, JID from_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvAttentionMessage(swigCPtr, this, id_, JID.getCPtr(from_), from_); else klcppwrapJNI.XmppCallback_onRecvAttentionMessageSwigExplicitXmppCallback(swigCPtr, this, id_, JID.getCPtr(from_), from_);
  }

  public void onRecvIMailMessage(String id_, JID from_, IMailSMTPInfo iMailSMTPInfo_, boolean receipts_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvIMailMessage(swigCPtr, this, id_, JID.getCPtr(from_), from_, IMailSMTPInfo.getCPtr(iMailSMTPInfo_), iMailSMTPInfo_, receipts_); else klcppwrapJNI.XmppCallback_onRecvIMailMessageSwigExplicitXmppCallback(swigCPtr, this, id_, JID.getCPtr(from_), from_, IMailSMTPInfo.getCPtr(iMailSMTPInfo_), iMailSMTPInfo_, receipts_);
  }

  public void onRecvLocationMessage(String id_, JID from_, Geoloc geoloc_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvLocationMessage(swigCPtr, this, id_, JID.getCPtr(from_), from_, Geoloc.getCPtr(geoloc_), geoloc_); else klcppwrapJNI.XmppCallback_onRecvLocationMessageSwigExplicitXmppCallback(swigCPtr, this, id_, JID.getCPtr(from_), from_, Geoloc.getCPtr(geoloc_), geoloc_);
  }

  public void onRecvReceiptMessage(String id_, JID from_, String receiptId_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvReceiptMessage(swigCPtr, this, id_, JID.getCPtr(from_), from_, receiptId_); else klcppwrapJNI.XmppCallback_onRecvReceiptMessageSwigExplicitXmppCallback(swigCPtr, this, id_, JID.getCPtr(from_), from_, receiptId_);
  }

  public void onRecvRoster(Roster roster_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvRoster(swigCPtr, this, Roster.getCPtr(roster_), roster_); else klcppwrapJNI.XmppCallback_onRecvRosterSwigExplicitXmppCallback(swigCPtr, this, Roster.getCPtr(roster_), roster_);
  }

  public void onRosterItemAdded(RosterItem item_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRosterItemAdded(swigCPtr, this, RosterItem.getCPtr(item_), item_); else klcppwrapJNI.XmppCallback_onRosterItemAddedSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(item_), item_);
  }

  public void onRosterItemUpdated(RosterItem item_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRosterItemUpdated(swigCPtr, this, RosterItem.getCPtr(item_), item_); else klcppwrapJNI.XmppCallback_onRosterItemUpdatedSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(item_), item_);
  }

  public void onRosterItemRemoved(RosterItem item_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRosterItemRemoved(swigCPtr, this, RosterItem.getCPtr(item_), item_); else klcppwrapJNI.XmppCallback_onRosterItemRemovedSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(item_), item_);
  }

  public void onRosterItemSubscribed(RosterItem item_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRosterItemSubscribed(swigCPtr, this, RosterItem.getCPtr(item_), item_); else klcppwrapJNI.XmppCallback_onRosterItemSubscribedSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(item_), item_);
  }

  public void onRosterItemUnsubscribed(RosterItem item_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRosterItemUnsubscribed(swigCPtr, this, RosterItem.getCPtr(item_), item_); else klcppwrapJNI.XmppCallback_onRosterItemUnsubscribedSwigExplicitXmppCallback(swigCPtr, this, RosterItem.getCPtr(item_), item_);
  }

  public void onRecvSubscriptionRequest(JID jid_, String msg_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvSubscriptionRequest(swigCPtr, this, JID.getCPtr(jid_), jid_, msg_); else klcppwrapJNI.XmppCallback_onRecvSubscriptionRequestSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(jid_), jid_, msg_);
  }

  public void onRetrieveRegistrationInfo(String username_, String name_, String email_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRetrieveRegistrationInfo(swigCPtr, this, username_, name_, email_); else klcppwrapJNI.XmppCallback_onRetrieveRegistrationInfoSwigExplicitXmppCallback(swigCPtr, this, username_, name_, email_);
  }

  public void onPublishNicknameSuccess(String nickname_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onPublishNicknameSuccess(swigCPtr, this, nickname_); else klcppwrapJNI.XmppCallback_onPublishNicknameSuccessSwigExplicitXmppCallback(swigCPtr, this, nickname_);
  }

  public void onPublishNicknameFailed(String nickname_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onPublishNicknameFailed(swigCPtr, this, nickname_); else klcppwrapJNI.XmppCallback_onPublishNicknameFailedSwigExplicitXmppCallback(swigCPtr, this, nickname_);
  }

  public void onRecvNickname(JID from_, String nickname_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvNickname(swigCPtr, this, JID.getCPtr(from_), from_, nickname_); else klcppwrapJNI.XmppCallback_onRecvNicknameSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, nickname_);
  }

  public void onPublishAvatarSuccess(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onPublishAvatarSuccess(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onPublishAvatarSuccessSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onPublishAvatarFailed(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onPublishAvatarFailed(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onPublishAvatarFailedSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onRecvAvatarMetadata(JID from_, AvatarMetadata metadata_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvAvatarMetadata(swigCPtr, this, JID.getCPtr(from_), from_, AvatarMetadata.getCPtr(metadata_), metadata_); else klcppwrapJNI.XmppCallback_onRecvAvatarMetadataSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, AvatarMetadata.getCPtr(metadata_), metadata_);
  }

  public void onRecvAvatarData(JID from_, String id_, String base64_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvAvatarData(swigCPtr, this, JID.getCPtr(from_), from_, id_, base64_); else klcppwrapJNI.XmppCallback_onRecvAvatarDataSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, id_, base64_);
  }

  public void onLoadAvatarSuccess(JID from_, String id_, String base64_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onLoadAvatarSuccess(swigCPtr, this, JID.getCPtr(from_), from_, id_, base64_); else klcppwrapJNI.XmppCallback_onLoadAvatarSuccessSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, id_, base64_);
  }

  public void onLoadAvatarFailed(JID from_, String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onLoadAvatarFailed(swigCPtr, this, JID.getCPtr(from_), from_, id_); else klcppwrapJNI.XmppCallback_onLoadAvatarFailedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, id_);
  }

  public void onNotifyPersonalInfoChangedSuccess(String uid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onNotifyPersonalInfoChangedSuccess(swigCPtr, this, uid_); else klcppwrapJNI.XmppCallback_onNotifyPersonalInfoChangedSuccessSwigExplicitXmppCallback(swigCPtr, this, uid_);
  }

  public void onNotifyPersonalInfoChangedFailed(String uid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onNotifyPersonalInfoChangedFailed(swigCPtr, this, uid_); else klcppwrapJNI.XmppCallback_onNotifyPersonalInfoChangedFailedSwigExplicitXmppCallback(swigCPtr, this, uid_);
  }

  public void onRecvPersonalInfoChangedNotification(JID from_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvPersonalInfoChangedNotification(swigCPtr, this, JID.getCPtr(from_), from_); else klcppwrapJNI.XmppCallback_onRecvPersonalInfoChangedNotificationSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_);
  }

  public void onNotifyAddressBookChangedSuccess(String uid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onNotifyAddressBookChangedSuccess(swigCPtr, this, uid_); else klcppwrapJNI.XmppCallback_onNotifyAddressBookChangedSuccessSwigExplicitXmppCallback(swigCPtr, this, uid_);
  }

  public void onNotifyAddressBookChangedFailed(String uid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onNotifyAddressBookChangedFailed(swigCPtr, this, uid_); else klcppwrapJNI.XmppCallback_onNotifyAddressBookChangedFailedSwigExplicitXmppCallback(swigCPtr, this, uid_);
  }

  public void onRecvAddressBookChangedNotification(JID from_, AddressBookInfo addressBookInfo_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvAddressBookChangedNotification(swigCPtr, this, JID.getCPtr(from_), from_, AddressBookInfo.getCPtr(addressBookInfo_), addressBookInfo_); else klcppwrapJNI.XmppCallback_onRecvAddressBookChangedNotificationSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, AddressBookInfo.getCPtr(addressBookInfo_), addressBookInfo_);
  }

  public void onRecvBackgroundChangedNotification(JID from_, BackgroundInfo backgroundInfo_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvBackgroundChangedNotification(swigCPtr, this, JID.getCPtr(from_), from_, BackgroundInfo.getCPtr(backgroundInfo_), backgroundInfo_); else klcppwrapJNI.XmppCallback_onRecvBackgroundChangedNotificationSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(from_), from_, BackgroundInfo.getCPtr(backgroundInfo_), backgroundInfo_);
  }

  public void onPublishMicroblogSuccess(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onPublishMicroblogSuccess(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onPublishMicroblogSuccessSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onPublishMicroblogFailed(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onPublishMicroblogFailed(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onPublishMicroblogFailedSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onDeleteMicroblogSuccess(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDeleteMicroblogSuccess(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onDeleteMicroblogSuccessSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onDeleteMicroblogFailed(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDeleteMicroblogFailed(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onDeleteMicroblogFailedSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onRecvMicroblog(Microblog microblog_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMicroblog(swigCPtr, this, Microblog.getCPtr(microblog_), microblog_); else klcppwrapJNI.XmppCallback_onRecvMicroblogSwigExplicitXmppCallback(swigCPtr, this, Microblog.getCPtr(microblog_), microblog_);
  }

  public void onMicroblogDeleted(String id_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onMicroblogDeleted(swigCPtr, this, id_); else klcppwrapJNI.XmppCallback_onMicroblogDeletedSwigExplicitXmppCallback(swigCPtr, this, id_);
  }

  public void onCreateMUCRoomSuccess(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onCreateMUCRoomSuccess(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onCreateMUCRoomSuccessSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onCreateMUCRoomFailed(JID room_, CreateMUCRoomError error_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onCreateMUCRoomFailed(swigCPtr, this, JID.getCPtr(room_), room_, error_.swigValue()); else klcppwrapJNI.XmppCallback_onCreateMUCRoomFailedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, error_.swigValue());
  }

  public void onConfigMUCRoomSuccess(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onConfigMUCRoomSuccess(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onConfigMUCRoomSuccessSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onConfigMUCRoomFailed(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onConfigMUCRoomFailed(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onConfigMUCRoomFailedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onDestroyMUCRoomSuccess(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDestroyMUCRoomSuccess(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onDestroyMUCRoomSuccessSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onDestroyMUCRoomFailed(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDestroyMUCRoomFailed(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onDestroyMUCRoomFailedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onRecvMUCRoomPresence(JID room_, String participantOldNickname_, String participantNewNickname_, JID participantJid_, Presence pres_, MUCRoomAffiliation affi_, MUCRoomRole role_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMUCRoomPresence(swigCPtr, this, JID.getCPtr(room_), room_, participantOldNickname_, participantNewNickname_, JID.getCPtr(participantJid_), participantJid_, Presence.getCPtr(pres_), pres_, affi_.swigValue(), role_.swigValue()); else klcppwrapJNI.XmppCallback_onRecvMUCRoomPresenceSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, participantOldNickname_, participantNewNickname_, JID.getCPtr(participantJid_), participantJid_, Presence.getCPtr(pres_), pres_, affi_.swigValue(), role_.swigValue());
  }

  public void onRecvMUCRoomChatMessage(JID room_, String nickname_, Message msg_, String xhtml_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMUCRoomChatMessage(swigCPtr, this, JID.getCPtr(room_), room_, nickname_, Message.getCPtr(msg_), msg_, xhtml_); else klcppwrapJNI.XmppCallback_onRecvMUCRoomChatMessageSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, nickname_, Message.getCPtr(msg_), msg_, xhtml_);
  }

  public void onRecvMUCRoomLocationMessage(String id_, JID room_, String nickname_, Geoloc geoloc_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMUCRoomLocationMessage(swigCPtr, this, id_, JID.getCPtr(room_), room_, nickname_, Geoloc.getCPtr(geoloc_), geoloc_); else klcppwrapJNI.XmppCallback_onRecvMUCRoomLocationMessageSwigExplicitXmppCallback(swigCPtr, this, id_, JID.getCPtr(room_), room_, nickname_, Geoloc.getCPtr(geoloc_), geoloc_);
  }

  public void onRecvMUCRoomAttentionMessage(String id_, JID room_, String nickname_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMUCRoomAttentionMessage(swigCPtr, this, id_, JID.getCPtr(room_), room_, nickname_); else klcppwrapJNI.XmppCallback_onRecvMUCRoomAttentionMessageSwigExplicitXmppCallback(swigCPtr, this, id_, JID.getCPtr(room_), room_, nickname_);
  }

  public void onModifyMUCRoomMemberListSuccess(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onModifyMUCRoomMemberListSuccess(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onModifyMUCRoomMemberListSuccessSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onModifyMUCRoomMemberListFailed(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onModifyMUCRoomMemberListFailed(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onModifyMUCRoomMemberListFailedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onModifyMUCRoomOwnerListSuccess(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onModifyMUCRoomOwnerListSuccess(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onModifyMUCRoomOwnerListSuccessSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onModifyMUCRoomOwnerListFailed(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onModifyMUCRoomOwnerListFailed(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onModifyMUCRoomOwnerListFailedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onRecvMUCRoomDirectInvitation(JID room_, JID invitor_, String reason_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMUCRoomDirectInvitation(swigCPtr, this, JID.getCPtr(room_), room_, JID.getCPtr(invitor_), invitor_, reason_); else klcppwrapJNI.XmppCallback_onRecvMUCRoomDirectInvitationSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, JID.getCPtr(invitor_), invitor_, reason_);
  }

  public void onRecvMUCRoomMediatedInvitation(JID room_, JID invitor_, String reason_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvMUCRoomMediatedInvitation(swigCPtr, this, JID.getCPtr(room_), room_, JID.getCPtr(invitor_), invitor_, reason_); else klcppwrapJNI.XmppCallback_onRecvMUCRoomMediatedInvitationSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, JID.getCPtr(invitor_), invitor_, reason_);
  }

  public void onRetrieveMUCRoomMemberList(JID room_, StringList members_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRetrieveMUCRoomMemberList(swigCPtr, this, JID.getCPtr(room_), room_, StringList.getCPtr(members_), members_); else klcppwrapJNI.XmppCallback_onRetrieveMUCRoomMemberListSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, StringList.getCPtr(members_), members_);
  }

  public void onRetrieveMUCRoomOwnerList(JID room_, StringList owners_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRetrieveMUCRoomOwnerList(swigCPtr, this, JID.getCPtr(room_), room_, StringList.getCPtr(owners_), owners_); else klcppwrapJNI.XmppCallback_onRetrieveMUCRoomOwnerListSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, StringList.getCPtr(owners_), owners_);
  }

  public void onMUCRoomSubjectChanged(JID room_, String nickname_, String subject_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onMUCRoomSubjectChanged(swigCPtr, this, JID.getCPtr(room_), room_, nickname_, subject_); else klcppwrapJNI.XmppCallback_onMUCRoomSubjectChangedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, nickname_, subject_);
  }

  public void onRetrieveMUCRoomInfo(JID room_, MUCRoomInfo info_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRetrieveMUCRoomInfo(swigCPtr, this, JID.getCPtr(room_), room_, MUCRoomInfo.getCPtr(info_), info_); else klcppwrapJNI.XmppCallback_onRetrieveMUCRoomInfoSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, MUCRoomInfo.getCPtr(info_), info_);
  }

  public void onRetrieveMUCRoomConfig(JID room_, MUCRoomConfig config_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRetrieveMUCRoomConfig(swigCPtr, this, JID.getCPtr(room_), room_, MUCRoomConfig.getCPtr(config_), config_); else klcppwrapJNI.XmppCallback_onRetrieveMUCRoomConfigSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, MUCRoomConfig.getCPtr(config_), config_);
  }

  public void onMUCRoomInvitationRejected(JID room_, JID invitee_, String reason_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onMUCRoomInvitationRejected(swigCPtr, this, JID.getCPtr(room_), room_, JID.getCPtr(invitee_), invitee_, reason_); else klcppwrapJNI.XmppCallback_onMUCRoomInvitationRejectedSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_, JID.getCPtr(invitee_), invitee_, reason_);
  }

  public void onChangeMUCNicknameConflict(JID room_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onChangeMUCNicknameConflict(swigCPtr, this, JID.getCPtr(room_), room_); else klcppwrapJNI.XmppCallback_onChangeMUCNicknameConflictSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(room_), room_);
  }

  public void onFtRequest(JID initiator_, String sid_, FileMetadata file_, boolean supportS5b_, boolean supportIbb_, boolean supportOob_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtRequest(swigCPtr, this, JID.getCPtr(initiator_), initiator_, sid_, FileMetadata.getCPtr(file_), file_, supportS5b_, supportIbb_, supportOob_); else klcppwrapJNI.XmppCallback_onFtRequestSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(initiator_), initiator_, sid_, FileMetadata.getCPtr(file_), file_, supportS5b_, supportIbb_, supportOob_);
  }

  public void onFtRquestRejected(String sid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtRquestRejected(swigCPtr, this, sid_); else klcppwrapJNI.XmppCallback_onFtRquestRejectedSwigExplicitXmppCallback(swigCPtr, this, sid_);
  }

  public void onFtRquestError(String sid_, StanzaErrorType type_, StanzaError error_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtRquestError(swigCPtr, this, sid_, type_.swigValue(), error_.swigValue()); else klcppwrapJNI.XmppCallback_onFtRquestErrorSwigExplicitXmppCallback(swigCPtr, this, sid_, type_.swigValue(), error_.swigValue());
  }

  public void onFtIncomingBytestreamCreated(String sid_, Bytestream.StreamType type_, JID initiator_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtIncomingBytestreamCreated(swigCPtr, this, sid_, type_.swigValue(), JID.getCPtr(initiator_), initiator_); else klcppwrapJNI.XmppCallback_onFtIncomingBytestreamCreatedSwigExplicitXmppCallback(swigCPtr, this, sid_, type_.swigValue(), JID.getCPtr(initiator_), initiator_);
  }

  public void onFtOutgoingBytestreamCreated(String sid_, Bytestream.StreamType type_, JID target_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtOutgoingBytestreamCreated(swigCPtr, this, sid_, type_.swigValue(), JID.getCPtr(target_), target_); else klcppwrapJNI.XmppCallback_onFtOutgoingBytestreamCreatedSwigExplicitXmppCallback(swigCPtr, this, sid_, type_.swigValue(), JID.getCPtr(target_), target_);
  }

  public void onFtIncomingBytestreamOpened(String sid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtIncomingBytestreamOpened(swigCPtr, this, sid_); else klcppwrapJNI.XmppCallback_onFtIncomingBytestreamOpenedSwigExplicitXmppCallback(swigCPtr, this, sid_);
  }

  public void onFtOutgoingBytestreamOpened(String sid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtOutgoingBytestreamOpened(swigCPtr, this, sid_); else klcppwrapJNI.XmppCallback_onFtOutgoingBytestreamOpenedSwigExplicitXmppCallback(swigCPtr, this, sid_);
  }

  public void onRecvFtBytestreamData(String sid_, BytestreamData data_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvFtBytestreamData__SWIG_0(swigCPtr, this, sid_, BytestreamData.getCPtr(data_), data_); else klcppwrapJNI.XmppCallback_onRecvFtBytestreamDataSwigExplicitXmppCallback__SWIG_0(swigCPtr, this, sid_, BytestreamData.getCPtr(data_), data_);
  }

  public void onRecvFtBytestreamData(SID sid_, BytestreamData data_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onRecvFtBytestreamData__SWIG_1(swigCPtr, this, SID.getCPtr(sid_), sid_, BytestreamData.getCPtr(data_), data_); else klcppwrapJNI.XmppCallback_onRecvFtBytestreamDataSwigExplicitXmppCallback__SWIG_1(swigCPtr, this, SID.getCPtr(sid_), sid_, BytestreamData.getCPtr(data_), data_);
  }

  public void onFtBytestreamClosed(String sid_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onFtBytestreamClosed(swigCPtr, this, sid_); else klcppwrapJNI.XmppCallback_onFtBytestreamClosedSwigExplicitXmppCallback(swigCPtr, this, sid_);
  }

  public void onDiscoItem(JID parent_, JID jid_, String node_, String name_) {
    if (getClass() == XmppCallback.class) klcppwrapJNI.XmppCallback_onDiscoItem(swigCPtr, this, JID.getCPtr(parent_), parent_, JID.getCPtr(jid_), jid_, node_, name_); else klcppwrapJNI.XmppCallback_onDiscoItemSwigExplicitXmppCallback(swigCPtr, this, JID.getCPtr(parent_), parent_, JID.getCPtr(jid_), jid_, node_, name_);
  }

  public XmppCallback() {
    this(klcppwrapJNI.new_XmppCallback(), true);
    klcppwrapJNI.XmppCallback_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

}
