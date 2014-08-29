#ifndef MESSAGETEST_H__
#define MESSAGETEST_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include "../../KLXmpp/include/kl_xmppstack.h"
#include <gtest/gtest.h>

class MessageTest : public ::testing::Test,
	                public kl::XmppCallback
{
public:
	enum Context
	{
		Null,
		HandleSendAttentionToSelf,
		HandleSendPresence,
		HandleSendTextChatMessageToSelf,
		HandleSendRichChatMessageWithVaildXhtmlToSelf,
		HandleSendRichChatMessageWithInvaildXhtmlToSelf
	};
public:
	void onLoginSuccess() 
	{
		switch ( m_context )
		{
		case HandleSendAttentionToSelf:
			m_invokeSendAttentionSuccess = m_xs->sendAttention( m_xs->jid() );
			break;
		case HandleSendPresence:
			m_invokeSendPresenceSuccess = m_xs->sendPresence( gloox::Presence::PresenceType::Away, "away" );
			m_xs->logout();
			break;
		case HandleSendTextChatMessageToSelf:
			m_invokeSendChatMessage = m_xs->sendChatMessage( m_xs->jid(), m_outTextChatMessage );
			break;
		case HandleSendRichChatMessageWithVaildXhtmlToSelf:
			m_invokeSendChatMessage = m_xs->sendChatMessage( m_xs->jid(), m_outTextChatMessage, m_outVaildXhtml );
			break;
		case HandleSendRichChatMessageWithInvaildXhtmlToSelf:
			m_invokeSendChatMessage = m_xs->sendChatMessage( m_xs->jid(), m_outTextChatMessage, m_outInvaildXhtml );
			break;
		default:
			m_xs->logout();
		}
	}

	void onRecvAttention( const gloox::JID& from_ ) 
	{
		m_onRecvAttention++;

		m_xs->logout();
	}

	void onRecvChatMessage( const gloox::Message& msg_, const std::string& xhtml_ ) 
	{
		m_onRecvChatMessage++;
		m_inTextChatMessage = msg_.body();
		m_inXhtml = xhtml_;

		m_xs->logout();
	}

protected:
	MessageTest() 
		: m_xs( 0 ), 
		  m_context( Null ), 
		  m_onRecvAttention( 0 ), 
		  m_onRecvChatMessage( 0 ),
		  m_invokeSendAttentionSuccess( false ),
		  m_invokeSendPresenceSuccess( false ),
		  m_invokeSendChatMessage( false )
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
		m_outTextChatMessage = "klxmppstack_klxmppunittest_textchatmessage";
		m_outVaildXhtml = "<html xmlns='http://jabber.org/protocol/xhtml-im'>"\
			              "<body xmlns='http://www.w3.org/1999/xhtml'>"\
						  "<p><img src='http://172.30.3.8:5557/kunlunMedia/upload/1000/c4424bba-c8f3-40d4-976d-8962dba0b8e3.png' alt='A License to Jabber' height='261' width='537'/>"\
						  "</p></body></html>";
		m_outInvaildXhtml = "<html xmlns='http://jabber.org/protocol/xhtml-im'>"\
			                "<body xmlns='http://www.w3.org/1999/xhtml'>"\
						    "<p><img src='http://172.30.3.8:5557/kunlunMedia/upload/1000/c4424bba-c8f3-40d4-976d-8962dba0b8e3.png' alt='A License to Jabber' height='261' width='537'/>"\
						    "</p></body></html"; // error end tag of <html>
	}

	virtual void TearDown() 
	{
		// TODO: Code here will be called immediately after each test (right before the destructor).
	}

	int m_onRecvAttention;
	int m_onRecvChatMessage;

	std::string m_outTextChatMessage;
	std::string m_inTextChatMessage;

	std::string m_outVaildXhtml;
	std::string m_outInvaildXhtml;
	std::string m_inXhtml;

	kl::XmppStack* m_xs;
	Context m_context;

	bool m_invokeSendAttentionSuccess;
	bool m_invokeSendPresenceSuccess;
	bool m_invokeSendChatMessage;
};

#endif // MESSAGETEST_H__