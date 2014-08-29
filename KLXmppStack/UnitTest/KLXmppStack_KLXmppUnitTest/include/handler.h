#ifndef HANDLER_H__
#define HANDLER_H__

#include "../../KLXmpp/include/kl_xmppstack.h"
#include "../../KLXmpp/include/kl_xmppcallback.h"

class Handler
{
public:
	Handler() {}
	virtual ~Handler() {}

	virtual void handleLogined( const gloox::JID& jid_ ) {}
	virtual void handleLogouted( const gloox::JID& jid_ ) {}

	virtual void handleBecameNewFriend( const gloox::JID& a_, const gloox::JID& b_ ) {}
	virtual void handleContactRemoved( const gloox::JID& a_, const gloox::JID& b_ ) {}
	
	virtual void handleCreatedMUCRoom( const gloox::JID& jid_, const gloox::JID& mucroom_ ) {}
	virtual void handleEnteredMUCRoom( const gloox::JID& mucroom_, 
		                               const std::string& nickname_ ) {}
	virtual void handleExitedMUCRoom( const gloox::JID& mucroom_, const std::string& nickname_ ) {}
	
	virtual void handleGrantedVoice( const gloox::JID& mucroom_, const std::string& nickname_ ) {}
	virtual void handleRevokedVoice( const gloox::JID& mucroom_, const std::string& nickname_ ) {}

	virtual void handleKickedOutMUCRoom( const gloox::JID& mucroom_, const std::string& nickname_ ) {}
	virtual void handleBanedOutMUCRoom( const gloox::JID& mucroom_, const std::string& nickname_ ) {}
};

#endif // HANDLER_H__