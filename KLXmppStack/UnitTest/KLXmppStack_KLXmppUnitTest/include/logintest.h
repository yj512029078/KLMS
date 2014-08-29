#ifndef LOGINTEST_H__
#define LOGINTEST_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include "../../KLXmpp/include/kl_xmppstack.h"
#include <gtest/gtest.h>

class LoginTest : public ::testing::Test,
	              public kl::XmppCallback
{
public:
	enum Context
	{
		Null,
		HandleNullUsernameJID,
		HandleNullServerJID,
		HandleNullResourceJID,
		HandleNullPassword,
		HandleIncorrectHost,
		HandleIncorrectPort,
		HandleIncorrectPassword
	};
public:
	void onInvaildJID() 
	{
		m_onInvaildJID++;
	}

	void onInvaildPassword() 
	{
		m_onInvaildPassword++;
	}

	void onTcpConnSuccess() 
	{
		m_onTcpConnSuccess++;
	}

	void onTcpConnFailed( gloox::ConnectionError error_ ) 
	{
		m_onTcpConnFailed++;
	}

	void onNegotiatingEncryption() 
	{
		m_onNegotiatingEncryption++;
	}

	void onNegotiatingCompression() 
	{
		m_onNegotiatingCompression++;
	}

	void onAuthenticating() 
	{
		m_onAuthenticating++;
	}

	void onAuthFailed() 
	{
		m_onAuthFailed++;
	}

	void onBindingResource() 
	{
		m_onBindingResource++;
	}

	void onCreatingSession() 
	{
		m_onCreatingSession++;
	}

	void onLoadingRoster() 
	{
		m_onLoadingRoster++;
	}

	void onLoginSuccess() 
	{
		m_onLoginSuccess++;

		switch ( m_context )
		{
		default:
			m_invokeLogoutSuccess = m_xs->logout();
		}
	}

	void onLogoutSuccess() 
	{
		m_onLogoutSuccess++;
	}

protected:
	LoginTest() 
		: m_onInvaildJID( 0 ),
		  m_onInvaildPassword( 0 ),
		  m_onTcpConnSuccess( 0 ),
		  m_onTcpConnFailed( 0 ),
		  m_onNegotiatingEncryption( 0 ),
		  m_onNegotiatingCompression( 0 ),
		  m_onAuthenticating( 0 ),
		  m_onAuthFailed( 0 ),
		  m_onBindingResource( 0 ),
		  m_onCreatingSession( 0 ), 
		  m_onLoadingRoster( 0 ),
		  m_onLoginSuccess( 0 ),
		  m_onLogoutSuccess( 0 ),
		  m_xs( 0 ),
		  m_context( Context::Null ),
		  m_invokeLogoutSuccess( false )
	{
	}

	~LoginTest()
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
	int m_onInvaildJID;
	int m_onInvaildPassword;
	int m_onTcpConnSuccess;
	int m_onTcpConnFailed;
	int m_onNegotiatingEncryption;
	int m_onNegotiatingCompression;
	int m_onAuthenticating;
	int m_onAuthFailed;
	int m_onBindingResource;
	int m_onCreatingSession;
	int m_onLoadingRoster;
	int m_onLoginSuccess;
	int m_onLogoutSuccess;

	bool m_invokeLogoutSuccess;
	
	kl::XmppStack* m_xs;
	Context m_context;
};

#endif // LOGINTEST_H__