#ifndef MUCTESTMT_H__
#define MUCTESTMT_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include <process.h>
#include "xmppstackproxy.h"
//#include "../../KLXmpp/include/kl_xmppstack.h"
#include "../../KLXmpp/include/kl_mucroominfo.h"
#include "../../KLXmpp/include/kl_mucroomconfig.h"
#include <gtest/gtest.h>

class MucTestMt : public ::testing::Test,
	              public Handler
{
public:
	enum Context
	{
		Null,
		HandleEnterMUCRoom,
		HandleInviteIntoMUCRoom,
		HandleKickOutMUCRoom,
		HandleBanOutMUCRoom,
		HandleRevokeMUCRoomVoice,
		HandleGrantMUCRoomVoice
	};
protected:
	MucTestMt() 
		: m_context( Null ),
		  isALogined( false ),
		  isBLogined( false ),
		  isCLogined( false ),
		  isACreateMUCRoom( false ),
		  isBCreateMUCRoom( false ),
		  isCCreateMUCRoom( false ),
		  isAEnterMUCRooom( false ),
		  isBEnterMUCRooom( false ),
		  isCEnterMUCRooom( false ),
		  isAExitMUCRooom( false ),
		  isBExitMUCRooom( false ),
		  isCExitMUCRooom( false ),
		  isARevokedVoice( false ),
		  isBRevokedVoice( false ),
		  isCRevokedVoice( false ),
		  isAGrantedVoice( false ),
		  isBGrantedVoice( false ),
		  isCGrantedVoice( false ),
		  isAKickedOutMUCRoom( false ),
		  isBKickedOutMUCRoom( false ),
		  isCKickedOutMUCRoom( false ),
		  isABanedOutMUCRoom( false ),
		  isBBanedOutMUCRoom( false ),
		  isCBanedOutMUCRoom( false )
	{
	}

	void handleLogined( const gloox::JID& jid_ ) 
	{
		if ( jid_ == m_xsp1->jid() )
		{
			isALogined = true;
		}
		else if ( jid_ == m_xsp2->jid() )
		{
			isBLogined = true;
		}
		else if ( jid_ == m_xsp3->jid() )
		{
			isCLogined = true;
		}
	}

	virtual void handleLogouted( const gloox::JID& jid_ ) 
	{
		if ( jid_ == m_xsp1->jid() )
		{
			isALogouted = true;
		}
		else if ( jid_ == m_xsp2->jid() )
		{
			isBLogouted = true;
		}
		else if ( jid_ == m_xsp3->jid() )
		{
			isCLogouted = true;
		}
	}

	void handleCreatedMUCRoom( const gloox::JID& jid_, const gloox::JID& mucroom_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( jid_ == m_xsp1->jid() )
		{
			isACreateMUCRoom = true;
		}
		else if ( jid_ == m_xsp2->jid() )
		{
			isBCreateMUCRoom = true;
		}
		else if ( jid_ == m_xsp3->jid() )
		{
			isCCreateMUCRoom = true;
		}
	}

	void handleEnteredMUCRoom( const gloox::JID& mucroom_, 
		                       const std::string& nickname_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( nickname_ == m_xsp1->jid().username() )
		{
			isAEnterMUCRooom = true;
		}
		else if ( nickname_ == m_xsp2->jid().username() )
		{
			isBEnterMUCRooom = true;
		}
		else if ( nickname_ == m_xsp3->jid().username() )
		{
			isCEnterMUCRooom = true;
		}
	}

	virtual void handleExitedMUCRoom( const gloox::JID& mucroom_, const std::string& nickname_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( nickname_ == m_xsp1->jid().username() )
		{
			isAExitMUCRooom = true;
		}
		else if ( nickname_ == m_xsp2->jid().username() )
		{
			isBExitMUCRooom = true;
		}
		else if ( nickname_ == m_xsp3->jid().username() )
		{
			isCExitMUCRooom = true;
		}
	}

	void handleGrantedVoice( const gloox::JID& mucroom_, const std::string& nickname_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( nickname_ == m_xsp1->jid().username() )
		{
			isAGrantedVoice = true;
		}
		else if ( nickname_ == m_xsp2->jid().username() )
		{
			isBGrantedVoice = true;
		}
		else if ( nickname_ == m_xsp3->jid().username() )
		{
			isCGrantedVoice = true;
		}
	}

	void handleRevokedVoice( const gloox::JID& mucroom_, const std::string& nickname_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( nickname_ == m_xsp1->jid().username() )
		{
			isARevokedVoice = true;
		}
		else if ( nickname_ == m_xsp2->jid().username() )
		{
			isBRevokedVoice = true;
		}
		else if ( nickname_ == m_xsp3->jid().username() )
		{
			isCRevokedVoice = true;
		}
	}

	void handleKickedOutMUCRoom( const gloox::JID& mucroom_, const std::string& nickname_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( nickname_ == m_xsp1->jid().username() )
		{
			isAKickedOutMUCRoom = true;
		}
		else if ( nickname_ == m_xsp2->jid().username() )
		{
			isBKickedOutMUCRoom = true;
		}
		else if ( nickname_ == m_xsp3->jid().username() )
		{
			isCKickedOutMUCRoom = true;
		}
	}

	void handleBanedOutMUCRoom( const gloox::JID& mucroom_, const std::string& nickname_ ) 
	{
		if ( mucroom_.bare()  != m_mucroom.bare() )
		{
			return;
		}

		if ( nickname_ == m_xsp1->jid().username() )
		{
			isABanedOutMUCRoom = true;
		}
		else if ( nickname_ == m_xsp2->jid().username() )
		{
			isBBanedOutMUCRoom = true;
		}
		else if ( nickname_ == m_xsp3->jid().username() )
		{
			isCBanedOutMUCRoom = true;
		}
	}

	virtual void init( XmppStackProxy* xsp1_, XmppStackProxy* xsp2_, XmppStackProxy* xsp3_ )
	{
		if ( xsp1_ )
		{
			m_xsp1 = xsp1_;
			m_xsp1->registerXmppCallback( m_xsp1 );
			m_xsp1->registerHandler( this );
		}
		if ( xsp2_ )
		{
			m_xsp2 = xsp2_;
			m_xsp2->registerXmppCallback( m_xsp2 );
			m_xsp2->registerHandler( this );
		}
		if ( xsp3_ )
		{
			m_xsp3 = xsp3_;
			m_xsp3->registerXmppCallback( m_xsp3 );
			m_xsp3->registerHandler( this );
		}
	}

	virtual void run( Context context_, const gloox::JID& mucroom_ )
	{
		m_context = context_;
		m_mucroom = mucroom_;

		if ( m_xsp1 )
		{
			_beginthread( XmppStackProxy::run, 0, m_xsp1 );
		}
		if ( m_xsp2 )
		{
			_beginthread( XmppStackProxy::run, 0, m_xsp2 );
		}
		if ( m_xsp3 )
		{
			_beginthread( XmppStackProxy::run, 0, m_xsp3 );
		}
		
		int index = 0;
		bool isQuit = false;
		while ( !isQuit )
		{
			printf( "%ds\r", ++index );
			if ( index >= 30 )
			{
				break;
			}

			// 
			switch( m_context )
			{
			case HandleEnterMUCRoom:
				if ( isALogined && !isACreateMUCRoom && !isAEnterMUCRooom ) // A还未创建房间
				{
					//m_xsp1->createInstantMUCRoom( m_mucroom );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isBEnterMUCRooom ) // A创建了房间，B还未进入房间
				{
					m_xsp2->enterMUCRoom( gloox::JID( m_mucroom.bare() + "/" + m_xsp2->jid().username() ) );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isCEnterMUCRooom ) // A创建了房间，C还未进入房间
				{
					m_xsp3->enterMUCRoom( gloox::JID( m_mucroom.bare() + "/" + m_xsp3->jid().username() ) );
				}
				else if ( isALogined && isBLogined && isCLogined && isACreateMUCRoom && isBEnterMUCRooom && isCEnterMUCRooom )
				{
					isQuit = true;
				}
				break;
			case HandleInviteIntoMUCRoom:
				if ( isALogined && !isACreateMUCRoom && !isAEnterMUCRooom ) // A还未创建房间
				{
					//m_xsp1->createInstantMUCRoom( m_mucroom );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isBEnterMUCRooom) // A、B已登录，A已经创建了房间，B还未进入房间
				{
					m_xsp1->inviteIntoMUCRoom( m_mucroom, m_xsp2->jid() );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isCEnterMUCRooom) // A、C已登录，A已经创建了房间，C还未进入房间
				{
					m_xsp1->inviteIntoMUCRoom( m_mucroom, m_xsp3->jid() );
				}
				else if ( isALogined && isBLogined && isCLogined && isACreateMUCRoom && isBEnterMUCRooom && isCEnterMUCRooom )
				{
					isQuit = true;
				}
				break;
			case HandleRevokeMUCRoomVoice:
				if ( isALogined && !isACreateMUCRoom && !isAEnterMUCRooom ) // A还未创建房间
				{
					//m_xsp1->createInstantMUCRoom( m_mucroom );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isBEnterMUCRooom ) // A创建了房间，B还未进入房间
				{
					m_xsp2->enterMUCRoom( gloox::JID( m_mucroom.bare() + "/" + m_xsp2->jid().username() ) );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && !isBRevokedVoice )
				{
					m_xsp1->revokeMUCRoomVoice( m_mucroom, m_xsp2->jid().username() );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && isBRevokedVoice )
				{
					isQuit = true;
				}
				break;
			case HandleGrantMUCRoomVoice:
				if ( isALogined && !isACreateMUCRoom && !isAEnterMUCRooom ) // A还未创建房间
				{
					//m_xsp1->createInstantMUCRoom( m_mucroom );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isBEnterMUCRooom ) // A创建了房间，B还未进入房间
				{
					m_xsp2->enterMUCRoom( gloox::JID( m_mucroom.bare() + "/" + m_xsp2->jid().username() ) );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && !isBRevokedVoice )
				{
					m_xsp1->revokeMUCRoomVoice( m_mucroom, m_xsp2->jid().username() );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && isBRevokedVoice && !isBGrantedVoice )
				{
					m_xsp1->grantMUCRoomVoice( m_mucroom, m_xsp2->jid().username() );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && isBRevokedVoice && isBGrantedVoice )
				{
					isQuit = true;
				}
				break;
			case HandleKickOutMUCRoom:
				if ( isALogined && !isACreateMUCRoom && !isAEnterMUCRooom ) // A还未创建房间
				{
					//m_xsp1->createInstantMUCRoom( m_mucroom );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isBEnterMUCRooom ) // A创建了房间，B还未进入房间
				{
					m_xsp2->enterMUCRoom( gloox::JID( m_mucroom.bare() + "/" + m_xsp2->jid().username() ) );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && !isBKickedOutMUCRoom )
				{
					m_xsp1->kickOutMUCRoom( m_mucroom, m_xsp2->jid().username() );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && isBKickedOutMUCRoom )
				{
					isQuit = true;
				}
				break;
			case HandleBanOutMUCRoom:
				if ( isALogined && !isACreateMUCRoom && !isAEnterMUCRooom ) // A还未创建房间
				{
					//m_xsp1->createInstantMUCRoom( m_mucroom );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && !isBEnterMUCRooom ) // A创建了房间，B还未进入房间
				{
					m_xsp2->enterMUCRoom( gloox::JID( m_mucroom.bare() + "/" + m_xsp2->jid().username() ) );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && !isBBanedOutMUCRoom )
				{
					m_xsp1->banOutMUCRoom( m_mucroom, m_xsp2->jid().username() );
				}
				else if ( isALogined && isBLogined && isACreateMUCRoom && isBEnterMUCRooom && isBBanedOutMUCRoom )
				{
					isQuit = true;
				}
				break;
			}

			::Sleep( 1000 );
		}

		// 超过30s，A、B、C自动登出
		//m_xsp1->exitMUCRoom( m_mucroom );
		if ( isBEnterMUCRooom )
		{
			m_xsp2->exitMUCRoom( m_mucroom );
		}
		if ( isCEnterMUCRooom )
		{
			m_xsp3->exitMUCRoom( m_mucroom );
		}
		
		printf( "\n" );
		index = 0;
		while ( true )
		{
			printf( "%ds\r", ++index );
			if ( index >= 5 )
			{
				break;
			}
			::Sleep( 1000 );
		}
		printf( "\n" );
		m_xsp1->logout();
		m_xsp2->logout();
		m_xsp3->logout();

		while ( true )
		{
			if ( isALogouted && isBLogouted && isCLogouted )
			{
				break;
			}

			::Sleep( 1000 );
		}
	}

	virtual void SetUp() 
	{
		// TODO: Code here will be called immediately after the constructor (right before each test).
	}

	virtual void TearDown() 
	{
		// TODO: Code here will be called immediately after each test (right before the destructor).
	}
	
	// 
	XmppStackProxy* m_xsp1;
	XmppStackProxy* m_xsp2;
	XmppStackProxy* m_xsp3;
	Context m_context;
	gloox::JID m_mucroom;

	// flags
	bool isALogined; // A 是否成功登录
	bool isBLogined;
	bool isCLogined;

	bool isALogouted; // A 是否成功登出
	bool isBLogouted;
	bool isCLogouted;
	
	bool isACreateMUCRoom; // A 是否创建了房间
	bool isBCreateMUCRoom; // B 是否创建了房间
	bool isCCreateMUCRoom; // C 是否创建了房间

	bool isAEnterMUCRooom; // A 是否进入了房间
	bool isBEnterMUCRooom; // B 是否进入了房间
	bool isCEnterMUCRooom; // C 是否进入了房间

	bool isAExitMUCRooom; // A 是否离开了房间
	bool isBExitMUCRooom; // B 是否离开了房间
	bool isCExitMUCRooom; // C 是否进离开了房间
	
	bool isARevokedVoice;
	bool isBRevokedVoice;
	bool isCRevokedVoice;

	bool isAGrantedVoice;
	bool isBGrantedVoice;
	bool isCGrantedVoice;

	bool isAKickedOutMUCRoom;
	bool isBKickedOutMUCRoom;
	bool isCKickedOutMUCRoom;

	bool isABanedOutMUCRoom;
	bool isBBanedOutMUCRoom;
	bool isCBanedOutMUCRoom;
};

#endif // MUCTEST_H__