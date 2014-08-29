%module(directors="1") klcppwrap

%rename(PresenceType)    gloox::PresenceType;
%rename(MessageType)     gloox::MessageType;
%rename(LogLevel)        gloox::LogLevel;
%rename(LogArea)         gloox::LogArea;
%rename(DiscoIdentity)   kl::disco::Identity;

%{
#include "../../../gloox/src/macros.h"
#include "../../../KLXmpp/include/kl_common.h"
#include "../../../KLXmpp/include/kl_xmppstack.h"
#include "../../../KLXmpp/include/kl_xmppcallback.h"
#include "../../../KLXmpp/include/kl_disco_identity.h"
#include "../../../KLXmpp/include/kl_service.h"
#include "../../../KLXmpp/include/kl_avatarmetadata.h"
#include "../../../KLXmpp/include/kl_mucroominfo.h"
#include "../../../KLXmpp/include/kl_mucroomconfig.h"
#include "../../../KLXmpp/include/kl_filemetadata.h"
#include "../../../KLXmpp/include/kl_bytestreamdata.h"
#include "../../../KLXmpp/include/kl_microblog.h"
#include "../../../KLXmpp/include/kl_vcard.h"
#include "../../../KLXmpp/include/kl_vcardname.h"
#include "../../../KLXmpp/include/kl_vcardphoto.h"
#include "../../../KLXmpp/include/kl_vcardemail.h"
#include "../../../KLXmpp/include/kl_vcardaddress.h"
#include "../../../KLXmpp/include/kl_vcardlabel.h"
#include "../../../KLXmpp/include/kl_vcardtelephone.h"
#include "../../../KLXmpp/include/kl_vcardgeo.h"
#include "../../../KLXmpp/include/kl_vcardorg.h"
#include "../../../KLXmpp/include/kl_addressbookinfo.h"
#include "../../../KLXmpp/include/kl_imailsmtpinfo.h"
#include "../../../KLXmpp/include/kl_geoloc.h"
#include "../../../KLXmpp/include/kl_backgroundinfo.h"
%}

%feature("director") XmppCallback;

// gloox定义数据类型映射
%include "../../../gloox/src/macros.h"
%include "gloox.i"
%include "gloox_logsink.i"
%include "gloox_jid.i"
%include "gloox_stanza.i"
%include "gloox_message.i"
%include "gloox_presence.i"
%include "gloox_rosteritem.i"
%include "gloox_bytestream.i"
%include "gloox_siprofileft.i"

// KunLun自定义数据类型映射
%include "../../../KLXmpp/include/kl_common.h"
%include "../../../KLXmpp/include/kl_xmppstack.h"
%include "../../../KLXmpp/include/kl_xmppcallback.h"
%include "../../../KLXmpp/include/kl_disco_identity.h"
%include "../../../KLXmpp/include/kl_service.h"
%include "../../../KLXmpp/include/kl_avatarmetadata.h"
%include "../../../KLXmpp/include/kl_mucroominfo.h"
%include "../../../KLXmpp/include/kl_mucroomconfig.h"
%include "../../../KLXmpp/include/kl_filemetadata.h"
%include "../../../KLXmpp/include/kl_bytestreamdata.h"
%include "../../../KLXmpp/include/kl_microblog.h"
%include "../../../KLXmpp/include/kl_vcard.h"
%include "../../../KLXmpp/include/kl_vcardname.h"
%include "../../../KLXmpp/include/kl_vcardphoto.h"
%include "../../../KLXmpp/include/kl_vcardemail.h"
%include "../../../KLXmpp/include/kl_vcardaddress.h"
%include "../../../KLXmpp/include/kl_vcardlabel.h"
%include "../../../KLXmpp/include/kl_vcardtelephone.h"
%include "../../../KLXmpp/include/kl_vcardgeo.h"
%include "../../../KLXmpp/include/kl_vcardorg.h"
%include "../../../KLXmpp/include/kl_addressbookinfo.h"
%include "../../../KLXmpp/include/kl_imailsmtpinfo.h"
%include "../../../KLXmpp/include/kl_geoloc.h"
%include "../../../KLXmpp/include/kl_backgroundinfo.h"

// stl模板映射
%template(StringList) std::list<std::string>;
%template(IntList) std::list<int>;
%template(StringMap) std::map<std::string, std::string>;
%template(Roster) std::map<std::string, gloox::RosterItem*>;
%template(DiscoIdentityList) std::list<kl::disco::Identity*>;
%template(ServiceMap) std::map<std::string, kl::Service*>;

%template(VCardEmailList) std::list<kl::VCardEmail*>;
%template(VCardAddressList) std::list<kl::VCardAddress*>;
%template(VCardLabelList) std::list<kl::VCardLabel*>;
%template(VCardTelephponeList) std::list<kl::VCardTelephone*>;
