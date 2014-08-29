#ifndef ROSTERTESTMT_H__
#define ROSTERTESTMT_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include <process.h>
#include "xmppstackproxy.h"
//#include "../../KLXmpp/include/kl_xmppstack.h"
#include "../../KLXmpp/include/kl_mucroominfo.h"
#include "../../KLXmpp/include/kl_mucroomconfig.h"
#include <gtest/gtest.h>

class RosterTestMt : public ::testing::Test,
	                 public Handler
{
public:
	enum Context
	{
		Null,
		HandleAddContact
	};
protected:
	RosterTestMt() 
		: m_context( Null ),
		  isALogined( false ),
		  isBLogined( false ),
		  isCLogined( false ),
		  isABecameBFriend( false ),
		  isBBecameAFriend( false ),
		  isARemovedContactB( false ),
		  isBRemovedContactA( false )
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

	virtual void handleBecameNewFriend( const gloox::JID& a_, const gloox::JID& b_ ) 
	{
		if ( a_.bare() == m_xsp1->jid().bare() && b_.bare() == m_xsp2->jid().bare() )
		{
			isABecameBFriend = true;
		}
		else if ( b_.bare() == m_xsp1->jid().bare() && a_.bare() == m_xsp2->jid().bare() )
		{
			isBBecameAFriend = true;
		}
	}

	virtual void handleContactRemoved( const gloox::JID& a_, const gloox::JID& b_ ) 
	{
		if ( a_.bare() == m_xsp1->jid().bare() && b_.bare() == m_xsp2->jid().bare() )
		{
			isARemovedContactB = true;
		}
		else if ( b_.bare() == m_xsp1->jid().bare() && a_.bare() == m_xsp2->jid().bare() )
		{
			isBRemovedContactA = true;
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

	virtual void run( Context context_ )
	{
		m_context = context_;

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
		bool isInvokeAddContact = false;
		bool isInvokeRemoveContact = false;
		while ( !isQuit )
		{
			printf( "%ds\r", ++index );
			if ( index >= 15 )
			{
				break;
			}
			// 
			switch( m_context )
			{
			case HandleAddContact:
				if ( isALogined && isBLogined && !isInvokeAddContact ) // A、B已经登录
				{
					m_xsp1->addContact( m_xsp2->jid(), m_xsp2->jid().username() );
					isInvokeAddContact = true;
				}
				else if ( isALogined && isBLogined && isABecameBFriend && isBBecameAFriend && !isInvokeRemoveContact )
				{
					m_xsp1->removeContact( m_xsp2->jid() );
					isInvokeRemoveContact = true;
				}
				else if ( isALogined && isBLogined && isABecameBFriend && isBBecameAFriend && isARemovedContactB && isBRemovedContactA )
				{
					isQuit = true;
				}
				break;
			}

			::Sleep( 1000 );
		}

		// 超过30s，A、B、C自动登出
		
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

	// flags
	bool isALogined; // A 是否成功登录
	bool isBLogined;
	bool isCLogined;

	bool isALogouted; // A 是否成功登出
	bool isBLogouted;
	bool isCLogouted;

	bool isABecameBFriend; // A成为B的好友
	bool isBBecameAFriend; // B成为A的好友

	bool isARemovedContactB;
	bool isBRemovedContactA;
};

#endif // MUCTEST_H__