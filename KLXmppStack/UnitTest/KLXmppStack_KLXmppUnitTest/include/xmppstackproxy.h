#ifndef XMPPSTACKPROXY_H__
#define XMPPSTACKPROXY_H__

#include "handler.h"
#include "../../KLXmpp/include/kl_xmppstack.h"
#include "../../KLXmpp/include/kl_xmppcallback.h"

class XmppStackProxy : public kl::XmppStack,
	                   public kl::XmppCallback
{
public:
	XmppStackProxy( const gloox::JID& jid_, const std::string& password_, const std::string& host_ = "", int port_ = 5222 )
		: kl::XmppStack( jid_, password_, host_, port_ ),
		  m_handler( 0 ),
		  m_pres( gloox::Presence::PresenceType::Unavailable ),
		  m_role( gloox::MUCRoomRole::RoleNone )
	{
	}

	virtual ~XmppStackProxy()
	{
	}

	void registerHandler( Handler* handler_ ) { m_handler = handler_; }

	static void _cdecl run( void * arg_ )
    {
		( ( kl::XmppStack* ) arg_ )->login();
    }

	void login()
	{
		printf( "[%s]: i try to login xmppserver\n", this->jid().username().c_str() );
		return kl::XmppStack::login();
	}

	bool addContact( const gloox::JID& contact_, const std::string& nickname_ = "", const gloox::StringList& groups_ = gloox::StringList() )
	{
		printf( "[%s]: i try to add [%s] as contact\n", this->jid().username().c_str(), contact_.username().c_str() );
		return kl::XmppStack::addContact( contact_, nickname_, groups_ );
	}

	bool removeContact( const gloox::JID& contact_ )
	{
		printf( "[%s]: i try to remove contact [%s]\n", this->jid().username().c_str(), contact_.username().c_str() );
		return kl::XmppStack::removeContact( contact_ );
	}

	void enterMUCRoom( const gloox::JID& room_,
		               const std::string& password_ = "",
					   gloox::Presence::PresenceType presType_ = gloox::Presence::Available,
					   const std::string& presStatus_ = "",
					   int presPriority_ = 0 )
	{
		printf( "[%s]: i try to enter the mucroom [%s]\n", this->jid().username().c_str(), room_.bare().c_str() );
		kl::XmppStack::enterMUCRoom( room_ );
	}

	void inviteIntoMUCRoom( const gloox::JID& room_, const gloox::JID& invitee_, const std::string& reason_ = "" )
	{
		printf( "[%s]: i try to invite [%s] into mucroom: [%s]\n", this->jid().username().c_str(), invitee_.username().c_str(), room_.bare().c_str() );
		kl::XmppStack::inviteIntoMUCRoom( room_, invitee_, reason_ );
	}

	void rejectMUCRoomInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ = "" )
	{
		printf( "[%s]: i reject the mucroom [%s] invitation from [%s]\n", this->jid().username().c_str(), room_.bare().c_str(), invitor_.username().c_str() );
		kl::XmppStack::rejectMUCRoomInvitation( room_, invitor_, reason_ );
	}

	void grantMUCRoomVoice( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" )
	{
		printf( "[%s]: i try to grant the voice to [%s]\n", this->jid().username().c_str(), occupantNickname_.c_str() );
		kl::XmppStack::grantMUCRoomVoice( room_, occupantNickname_, reason_ );
	}

	void revokeMUCRoomVoice( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" )
	{
		printf( "[%s]: i try to revoke the voice from [%s]\n", this->jid().username().c_str(), occupantNickname_.c_str() );
		kl::XmppStack::revokeMUCRoomVoice( room_, occupantNickname_, reason_ );
	}

	void kickOutMUCRoom( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" )
	{
		printf( "[%s]: i try to kick [%s] out of the mucroom [%s]\n", this->jid().username().c_str(), occupantNickname_.c_str(), room_.bare().c_str() );
		kl::XmppStack::kickOutMUCRoom( room_, occupantNickname_, reason_ );
	}

	void onInvaildJID() 
	{
		printf( "[%s]: invaild jid\n", this->jid().username().c_str() );
	}
	
	void onLoginSuccess() 
	{
		printf( "[%s]: i am logined\n", this->jid().username().c_str() );

		if ( m_handler )
		{
			m_handler->handleLogined( this->jid() );
		}
	}

	void onLogoutSuccess() 
	{
		printf( "[%s]: i am logouted\n", this->jid().username().c_str() );

		if ( m_handler )
		{
			m_handler->handleLogouted( this->jid() );
		}
	}

	void onRecvRosterPresence( const gloox::RosterItem& item_, 
                               const std::string& resource_, 
							   gloox::Presence::PresenceType type_, 
							   const std::string& msg_ ) 
	{
		std::string pres = "";
		switch( type_ )
		{
		case gloox::Presence::PresenceType::Available:
			pres = "Available";
			break;
		case gloox::Presence::PresenceType::Unavailable:
			pres = "Unavailable";
			break;
		default:
			pres = "??";
			break;
		}
		printf( "[%s]: i recv contact[%s]'s pres [%s]\n", this->jid().username().c_str(), item_.jid().c_str(), pres.c_str() );
	}

	void onContactAdded( const gloox::JID& jid_, const std::string& nickname_, const gloox::StringList& groups_ ) 
	{
		printf( "[%s]: i added contact [%s] to roster\n", this->jid().username().c_str(), jid_.username().c_str() );
	}

	void onContactRemoved( const gloox::JID& jid_ ) 
	{
		printf( "[%s]: i removed contact [%s] from roster\n", this->jid().username().c_str(), jid_.username().c_str() );
		if ( m_handler )
		{
			m_handler->handleContactRemoved( this->jid(), jid_ );
		}
	}

	void onContactUpdated( const gloox::JID& jid_, 
		                   const std::string& nickname_, 
						   const gloox::StringList& groups_, 
						   gloox::SubscriptionType type_ ) 
	{
		std::string type = "";
		switch( type_ )
		{
		case gloox::SubscriptionType::S10nBoth:
			type = "S10nBoth";
			break;
		case gloox::SubscriptionType::S10nFrom:
			type = "S10nFrom";
			break;
		case gloox::SubscriptionType::S10nFromOut:
			type = "S10nFromOut";
			break;
		case gloox::SubscriptionType::S10nNone:
			type = "S10nNone";
			break;
		case gloox::SubscriptionType::S10nNoneIn:
			type = "S10nNoneIn";
			break;
		case gloox::SubscriptionType::S10nNoneOut:
			type = "S10nNoneOut";
			break;
		case gloox::SubscriptionType::S10nNoneOutIn:
			type = "S10nNoneOutIn";
			break;
		case gloox::SubscriptionType::S10nTo:
			type = "S10nTo";
			break;
		case gloox::SubscriptionType::S10nToIn:
			type = "S10nToIn";
			break;
		default:
			type = "??";
			break;
		}
		printf( "[%s]: i updated contact [%s] subscriptiontype->[%s]\n", this->jid().username().c_str(), jid_.username().c_str(), type.c_str() );
	
		if ( m_subscriptionTypeMap[jid_.bare()] != gloox::SubscriptionType::S10nBoth 
			&& gloox::SubscriptionType::S10nBoth == type_ )
		{
			printf( "[%s]: [%s] became my new friend\n", this->jid().username().c_str(), jid_.username().c_str() );
			if ( m_handler )
			{
				m_handler->handleBecameNewFriend( jid_, this->jid() );
			}
		}
		m_subscriptionTypeMap.insert( std::make_pair( jid_.bare(), type_ ) );
	}

	void onSubscriptionAccepted( const gloox::JID& jid_ ) 
	{
		printf( "[%s]: [%s] accepted the subscription request\n", this->jid().username().c_str(), jid_.username().c_str() );
	}

	bool onRecvSubscriptionRequest( const gloox::JID& jid_, const std::string& msg_ ) 
	{ 
		printf( "[%s]: i recv and accept the subscription request from [%s]\n", this->jid().username().c_str(), jid_.username().c_str() );
		return true; 
	}

	void onCreateMUCRoomSuccess( const gloox::JID& room_ ) 
	{
		printf( "[%s]: i created the mucroom [%s]\n", this->jid().username().c_str(), room_.bare().c_str() );

		if ( m_handler )
		{
			m_handler->handleCreatedMUCRoom( this->jid(), room_.bare() );
		}
	}

	void onRecvMUCRoomPresence( const gloox::JID& room_, 
		                        const std::string& participantOldNickname_, 
								const std::string& participantNewNickname_,
								const gloox::JID& participantJid_,
								const gloox::Presence& pres_, 
								gloox::MUCRoomAffiliation affi_, 
								gloox::MUCRoomRole role_ ) 
	{
		std::string pres = "";
		std::string affi = "";
		std::string role = "";

		switch ( affi_ )
		{
		case gloox::MUCRoomAffiliation::AffiliationOwner:
			affi = "AffiliationOwner";
			break;
		case gloox::MUCRoomAffiliation::AffiliationAdmin:
			affi = "AffiliationAdmin";
			break;
		case gloox::MUCRoomAffiliation::AffiliationMember:
			affi = "AffiliationMember";
			break;
		case gloox::MUCRoomAffiliation::AffiliationNone:
			affi = "AffiliationNone";
			break;
		case gloox::MUCRoomAffiliation::AffiliationOutcast:
			affi = "AffiliationOutcast";
			break;
		}

		switch ( role_ )
		{
		case gloox::MUCRoomRole::RoleModerator:
			role = "RoleModerator";
			break;
		case gloox::MUCRoomRole::RoleParticipant:
			role = "RoleParticipant";
			break;
		case gloox::MUCRoomRole::RoleVisitor:
			role = "RoleVisitor";
			break;
		case gloox::MUCRoomRole::RoleNone:
			role = "RoleNone";
			break;
		}
		switch( pres_.subtype() )
		{
		case gloox::Presence::PresenceType::Available:
			pres = "Available";
			break;
		case gloox::Presence::PresenceType::Unavailable:
			pres = "Unavailable";
			break;
		default:
			pres = "?";
			break;
		}
		printf( "[%s]: [%s] [%s, %s, %s] in mucroom [%s]\n", 
			this->jid().username().c_str(), 
			participantOldNickname_.c_str(), 
			pres.c_str(), affi.c_str(), role.c_str(), 
			room_.bare().c_str() );

		if ( participantOldNickname_ == this->jid().username() && m_handler ) // is self
		{
			if ( gloox::Presence::PresenceType::Unavailable == m_pres && gloox::Presence::PresenceType::Available == pres_.subtype() )
			{
				printf( "[%s]: i entered the mucroom [%s]\n", participantOldNickname_.c_str(), room_.bare().c_str() );
				m_handler->handleEnteredMUCRoom( room_, participantOldNickname_ );
			}

			if ( gloox::Presence::PresenceType::Available == m_pres 
				&& gloox::Presence::PresenceType::Unavailable == pres_.subtype()
				&& gloox::MUCRoomRole::RoleNone == role_
				&& gloox::MUCRoomAffiliation::AffiliationNone == affi_ )
			{
				printf( "[%s]: i has been kicked out of the mucroom [%s]\n", participantOldNickname_.c_str(), room_.bare().c_str() );
				m_handler->handleKickedOutMUCRoom( room_, participantOldNickname_ );
			}

			if ( gloox::MUCRoomAffiliation::AffiliationOutcast != m_affi && gloox::MUCRoomAffiliation::AffiliationOutcast == affi_ )
			{
				printf( "[%s]: i has been baned out of the mucroom [%s]\n", participantOldNickname_.c_str(), room_.bare().c_str() );
				m_handler->handleBanedOutMUCRoom( room_, participantOldNickname_ );
			}

			if ( gloox::MUCRoomRole::RoleParticipant == m_role && gloox::MUCRoomRole::RoleVisitor == role_ )
			{
				printf( "[%s]: i has been revoked the voice in the mucroom [%s]\n", participantOldNickname_.c_str(), room_.bare().c_str() );
				m_handler->handleRevokedVoice( room_, participantOldNickname_ );
			}

			if ( gloox::MUCRoomRole::RoleVisitor == m_role && gloox::MUCRoomRole::RoleParticipant == role_ )
			{
				printf( "[%s]: i has been granted the voice in the mucroom [%s]\n", participantOldNickname_.c_str(), room_.bare().c_str() );
				m_handler->handleGrantedVoice( room_, participantOldNickname_ );
			} 

			m_pres = pres_.subtype();
			m_role = role_;
			m_affi = affi_;
		}
	}

	void onRecvMUCRoomInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_, const std::string& password_ ) 
	{
		printf( "[%s]: i recv and accept the mucroom [%s] invitation from [%s]\n", this->jid().username().c_str(), room_.bare().c_str(), invitor_.username().c_str() );
		//this->acceptMUCRoomInvitation( room_, this->jid().username() );
		//this->rejectMUCRoomInvitation( room_, invitor_ );
	}

private:
	std::map<std::string, gloox::SubscriptionType> m_subscriptionTypeMap;
	gloox::Presence::PresenceType m_pres;
	gloox::MUCRoomRole m_role;
	gloox::MUCRoomAffiliation m_affi;
	Handler* m_handler;

};

#endif // XMPPSTACKPROXY_H__