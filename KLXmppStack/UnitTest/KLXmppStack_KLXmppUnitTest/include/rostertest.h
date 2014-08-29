#ifndef ROSTERTEST_H__
#define ROSTERTEST_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include "../../KLXmpp/include/kl_xmppstack.h"
#include <gtest/gtest.h>

class RosterTest : public ::testing::Test,
	               public kl::XmppCallback
{
public:
	enum Context
	{
		Null
	};

public:
	void onLoginSuccess() 
	{
		m_onLoginSuccess++;
	}

	void onRecvRoster( const std::map<std::string, gloox::RosterItem*>& roster_ ) 
	{
		m_onRecvRoster++;
		m_inRoster = roster_;
		m_xs->logout();
	}

protected:
	RosterTest() 
		: m_xs( 0 ), 
		  m_onLoginSuccess( 0 ), 
		  m_onRecvRoster( 0 )
	{
	}

	virtual ~RosterTest()
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

	virtual bool run( Context context_ )
	{
		m_context = context_;
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
	}

	virtual void TearDown() 
	{
		// TODO: Code here will be called immediately after each test (right before the destructor).
	}

	// 统计回调函数被调用次数
	int m_onLoginSuccess;
	int m_onRecvRoster;
	// 
	std::map<std::string, gloox::RosterItem*> m_inRoster;

	kl::XmppStack* m_xs;
	Context m_context;
};

#endif // ROSTERTEST_H__