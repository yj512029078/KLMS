#ifndef MUCTEST_H__
#define MUCTEST_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include "../../KLXmpp/include/kl_xmppstack.h"
#include "../../KLXmpp/include/kl_mucroominfo.h"
#include "../../KLXmpp/include/kl_mucroomconfig.h"
#include <gtest/gtest.h>

class MucTest : public ::testing::Test,
	            public kl::XmppCallback
{
public:
	enum Context
	{
		Null,
		HandleCreateInstantMUCRoom,
		HandleSendMUCRoomPresence,
		HandleSendTextMUCRoomMessage,
		HandleChangeMUCRoomNickname,
		HandleChangeMUCRoomSubject,
		HandleQueryMUCRoomInfo,
		HandleQueryMUCRoomConfig,
		HandleChangeMUCRoomNameAndDesc,
		HandleCreateHuddle,
		HandleDestroyMUCRoom
	};
public:
	void onLoginSuccess() 
	{
		switch ( m_context )
		{
		case HandleCreateInstantMUCRoom:
		case HandleSendMUCRoomPresence:
		case HandleSendTextMUCRoomMessage:
		case HandleChangeMUCRoomNickname:
		case HandleChangeMUCRoomSubject:
		case HandleQueryMUCRoomInfo:
		case HandleQueryMUCRoomConfig:
		case HandleCreateHuddle:
			//m_xs->createHuddle( m_mucroom, kl::MUCRoomConfig() );
			break;
		case HandleDestroyMUCRoom:
			m_xs->destroyMUCRoom( m_mucroom );
			break;
		default:
			m_xs->logout();
		}
	}

	void onCreateMUCRoomSuccess( const gloox::JID& room_ ) 
	{
		m_onCreateMUCRoomSuccess++;

		switch ( m_context )
		{
		case HandleCreateInstantMUCRoom:
		case HandleCreateHuddle:
			m_xs->logout();
			break;
		case HandleSendMUCRoomPresence:
			m_xs->sendMUCRoomPresence( room_, m_outPres, m_outPresStatus );
			break;
		case HandleSendTextMUCRoomMessage:
			m_xs->sendMUCRoomMessage( room_, m_outTextMUCRoomMessage );
			break;
		case HandleChangeMUCRoomNickname:
			m_xs->changeMUCRoomNickname( room_, m_outNewMUCroomNickname );
			break;
		case HandleChangeMUCRoomSubject:
			m_xs->changeMUCRoomSubject( room_, m_outNewMUCroomSubject );
			break;
		case HandleQueryMUCRoomInfo:
			m_xs->queryMUCRoomInfo( room_ );
			break;
		case HandleQueryMUCRoomConfig:
		case HandleChangeMUCRoomNameAndDesc:
			m_xs->queryMUCRoomConfig( room_ );
			break;
		}
	}

	void onCreateMUCRoomFailed( const gloox::JID& room_ ) 
	{
		m_onCreateMUCRoomFailed++;
	}

	void onDestroyMUCRoomSuccess( const gloox::JID& room_ ) 
	{
		m_onDestroyMUCRoomSuccess++;

		switch ( m_context )
		{
		case HandleDestroyMUCRoom:
			m_xs->logout();
			break;
		}
	}

	void onDestroyMUCRoomFailed( const gloox::JID& room_ ) 
	{
		m_onDestroyMUCRoomFailed++;

		switch ( m_context )
		{
		case HandleDestroyMUCRoom:
			m_xs->logout();
			break;
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
		m_onRecvMUCRoomPresence++;
		
		m_inPres = pres_.subtype();
		m_inPresStatus = pres_.status();

		m_inNewMUCroomNickname = participantOldNickname_;

		switch ( m_context )
		{
		case HandleSendMUCRoomPresence:
			if ( pres_.subtype() != gloox::Presence::PresenceType::Available )
			{
				m_xs->logout();
			}
			break;
		case HandleChangeMUCRoomNickname:
			if ( participantNewNickname_ != "" )
			{
				m_xs->logout();
			}
			break;
		}
	}

	void onRecvMUCRoomMessage( const gloox::JID& room_, const gloox::Message& msg_, const std::string& xhtml_ ) 
	{
		if ( msg_.from().resource() != "" )
		{
			m_onRecvMUCRoomMessage++;
			m_inTextMUCRoomMessage = msg_.body();

			switch ( m_context )
			{
			case HandleSendTextMUCRoomMessage:
				m_xs->logout();
				break;
			}
		}
	}

	void onMUCRoomSubjectChanged( const gloox::JID& room_, const std::string& nickname_, const std::string& subject_ ) 
	{
		m_onMUCRoomSubjectChanged++;

		m_inNewMUCroomSubject = subject_;

		switch ( m_context )
		{
		case HandleChangeMUCRoomSubject:
			m_xs->logout();
			break;
		}
	}

	void onRetrieveMUCRoomInfo( const gloox::JID& room_, const kl::MUCRoomInfo& info_ ) 
	{
		m_onRetrieveMUCRoomInfo++;

		m_inMUCRoomInfo = info_;

		switch ( m_context )
		{
		case HandleQueryMUCRoomInfo:
			m_xs->logout();
			break;
		}
	}

	void onRetrieveMUCRoomConfig( const gloox::JID& room_, const kl::MUCRoomConfig& config_ ) 
	{
		m_onRetrieveMUCRoomConfig++;

		m_inMUCRoomConfig = config_;

		switch ( m_context )
		{
		case HandleQueryMUCRoomConfig:
			m_xs->logout();
			break;
		case HandleChangeMUCRoomNameAndDesc:
			if ( 0 == m_onConfigMUCRoomSuccess ) 
			{
				m_inMUCRoomConfig.roomname = m_outNewMUCRoomName;
				m_inMUCRoomConfig.roomdesc = m_outNewMUCRoomDesc;
				m_xs->configMUCRoom( room_, m_inMUCRoomConfig );
			}
			else if ( 1 == m_onConfigMUCRoomSuccess ) 
			{
				m_xs->logout();
			}
			break;
		}
	}

	void onConfigMUCRoomSuccess( const gloox::JID& room_ ) 
	{
		m_onConfigMUCRoomSuccess++;

		switch ( m_context )
		{
		case HandleChangeMUCRoomNameAndDesc:
			m_xs->queryMUCRoomConfig( room_ );
			break;
		}
	}

	void onConfigMUCRoomFailed( const gloox::JID& room_ ) 
	{
		m_onConfigMUCRoomFailed++;

		switch ( m_context )
		{
		case HandleChangeMUCRoomNameAndDesc:
			m_xs->logout();
			break;
		}
	}

protected:
	MucTest() 
		: m_onCreateMUCRoomSuccess( 0 ),
		  m_onCreateMUCRoomFailed( 0 ),
		  m_onDestroyMUCRoomSuccess( 0 ),
		  m_onDestroyMUCRoomFailed( 0 ),
		  m_onRecvMUCRoomMessage( 0 ),
		  m_onRecvMUCRoomPresence( 0 ),
		  m_onMUCRoomSubjectChanged( 0 ),
		  m_onRetrieveMUCRoomInfo( 0 ),
		  m_onRetrieveMUCRoomConfig( 0 ),
		  m_onConfigMUCRoomSuccess( 0 ),
		  m_onConfigMUCRoomFailed( 0 ),
	      m_xs( 0 ),
		  m_context( Null )
	{
	}

	virtual bool init( kl::XmppStack* xs_ )
	{
		if ( xs_ )
		{
			m_xs = xs_;
			m_xs->registerXmppCallback( this );
			return true;
		}
		return false;
	}

	virtual bool run( Context context_, const gloox::JID& mucroom_ )
	{
		m_context = context_;
		m_mucroom = mucroom_;
		if ( m_xs )
		{
			m_xs->login();
			return true;
		}
		return false;
	}

	virtual void SetUp() 
	{
		// TODO: Code here will be called immediately after the constructor (right before each test).
		m_outPres = gloox::Presence::PresenceType::Away;
		m_outPresStatus = "testaway";
		m_outTextMUCRoomMessage = "testtextmucroommessage";
		m_outNewMUCroomNickname = "testnickname";
		m_outNewMUCroomSubject = "testsubject";
		m_outNewMUCRoomName = "testmucroomname";
		m_outNewMUCRoomDesc = "testmucroomdesc";
	}

	virtual void TearDown() 
	{
		// TODO: Code here will be called immediately after each test (right before the destructor).
	}
	
	int m_onCreateMUCRoomSuccess;
	int m_onCreateMUCRoomFailed;
	int m_onDestroyMUCRoomSuccess;
	int m_onDestroyMUCRoomFailed;
	int m_onRecvMUCRoomMessage;
	int m_onRecvMUCRoomPresence;
	int m_onMUCRoomSubjectChanged;
	int m_onRetrieveMUCRoomInfo;
	int m_onRetrieveMUCRoomConfig;
	int m_onConfigMUCRoomSuccess;
	int m_onConfigMUCRoomFailed;
	
	gloox::Presence::PresenceType m_outPres;
	gloox::Presence::PresenceType m_inPres;
	std::string m_outPresStatus;
	std::string m_inPresStatus;

	std::string m_outTextMUCRoomMessage;
	std::string m_inTextMUCRoomMessage;

	std::string m_outNewMUCroomNickname;
	std::string m_inNewMUCroomNickname;
	
	std::string m_outNewMUCroomSubject;
	std::string m_inNewMUCroomSubject;

	std::string m_outNewMUCRoomName;

	std::string m_outNewMUCRoomDesc;
	
	kl::MUCRoomInfo m_inMUCRoomInfo;
	kl::MUCRoomConfig m_inMUCRoomConfig;

	kl::XmppStack* m_xs;
	Context m_context;

	gloox::JID m_mucroom;
};

#endif // MUCTEST_H__