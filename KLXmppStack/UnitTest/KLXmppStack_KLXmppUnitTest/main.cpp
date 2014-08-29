/** 
 * @brief KLXmppģ�鵥Ԫ���ԣ�����gtest��Դ���Կ�ܣ���
 * 
 * testcase�����涨��[����]Test
 * test�����涨��test[������]_handleInput[����˵��]
 * 
 * @author JI Yixuan
 */
#include "logintest.h"
#include "rostertest.h"
#include "rostertestmt.h"
#include "messagetest.h"
#include "personalinfotest.h"
#include "newstest.h"
#include "muctest.h"
#include "muctestmt.h"
#include <gtest/gtest.h>
#include <string>
#include <stdio.h>

#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>

#define DEBUG_NEW new(_NORMAL_BLOCK, __FILE__, __LINE__)
#define new DEBUG_NEW

std::string g_username1 = "";
std::string g_username2 = "";
std::string g_username3 = "";
std::string g_server = "";
std::string g_resource = "";
std::string g_password = "";
std::string g_host = "";
int g_port = -1;
std::string g_muc_service = "";
std::string g_instantmucroom_id = "";
std::string g_huddle_id = "";

#pragma region BaseTest
// ������������(BaseTest)������XmppStack����Ĺ����Ƿ���ȷ���ò����������ʺ������Ƿ���ȷ�޹ء�
// ����kl::XmppStack���캯��
TEST( BaseTest, testNew )
{
	kl::XmppStack* xs = new kl::XmppStack( gloox::JID(), "" );

	ASSERT_TRUE( xs != 0 );
	// ����Ա�����Ƿ���ȷ��ʼ��
	EXPECT_TRUE( xs->m_client != 0 );
	EXPECT_TRUE( xs->m_registration != 0 );
	EXPECT_TRUE( xs->m_pubSubManager != 0 );
	EXPECT_TRUE( xs->m_pubSubEventManager != 0 );
	EXPECT_TRUE( xs->m_sIProfileFT != 0 );
	EXPECT_TRUE( xs->m_sOCKS5BytestreamServer != 0 );
	EXPECT_EQ( 0, xs->m_xmppCallbacks.size() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::jid()�����������ʽ������JID
TEST( BaseTest, testJid_handleInputNormalJID )
{
	const std::string username = "a";
	const std::string server = "b";
	const std::string resource = "c";
	const std::string full = "a@b/c";
	const std::string bare = "a@b";
	gloox::JID jid( username + "@" + server + "/" + resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, "" );

	EXPECT_TRUE( jid == xs->jid() );
	EXPECT_STREQ( username.c_str(), xs->jid().username().c_str() );
	EXPECT_STREQ( server.c_str(), xs->jid().server().c_str() );
	EXPECT_STREQ( resource.c_str(), xs->jid().resource().c_str() );
	EXPECT_STREQ( full.c_str(), xs->jid().full().c_str() );
	EXPECT_STREQ( bare.c_str(), xs->jid().bare().c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::jid()�����������������'@'���ŵ�JID
TEST( BaseTest, testJid_handleInputTwoAtJID )
{
	const std::string full = "a@a@b/c";
	const std::string bare = "a@a@b";
	gloox::JID jid( full );
	kl::XmppStack* xs = new kl::XmppStack( jid, "" );
	
	EXPECT_TRUE( jid == xs->jid() );
	EXPECT_STREQ( "a", xs->jid().username().c_str() );
	EXPECT_STREQ( "a@b", xs->jid().server().c_str() ); // ��һ��'@'�͵�һ��'/'֮��Ĳ���Ϊserver
	EXPECT_STREQ( "c", xs->jid().resource().c_str() );
	EXPECT_STREQ( full.c_str(), xs->jid().full().c_str() );
	EXPECT_STREQ( bare.c_str(), xs->jid().bare().c_str() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::jid()�����������������'/'���ŵ�JID
TEST( BaseTest, testJid_handleInputTwoSpritJID )
{
	const std::string full = "a@b/c/d";
	const std::string bare = "a@b";
	gloox::JID jid( full );
	kl::XmppStack* xs = new kl::XmppStack( jid, "" );
	
	EXPECT_TRUE( jid == xs->jid() );
	EXPECT_STREQ( "a", xs->jid().username().c_str() );
	EXPECT_STREQ( "b", xs->jid().server().c_str() );
	EXPECT_STREQ( "c/d", xs->jid().resource().c_str() ); // ��һ��'/'��Ĳ���Ϊresource
	EXPECT_STREQ( full.c_str(), xs->jid().full().c_str() );
	EXPECT_STREQ( bare.c_str(), xs->jid().bare().c_str() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::password()����
TEST( BaseTest, testPassword )
{
	gloox::JID jid( "a@b/c" );
	const std::string password = "000000";
	kl::XmppStack* xs = new kl::XmppStack( jid, password );
	
	EXPECT_STREQ( password.c_str(), xs->password().c_str() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::host()����������Ĭ�ϲ�������ȡJID��server�Ĳ�����Ϊ����������/��ַ
TEST( BaseTest, testHost_handleInputDefault )
{
	gloox::JID jid( "a@b/c" );
	const std::string password = "000000";
	kl::XmppStack* xs = new kl::XmppStack( jid, password );
	
	EXPECT_STREQ( "b", xs->host().c_str() );
	EXPECT_STREQ( jid.server().c_str(), xs->host().c_str() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::host()�����������û��Զ������������/��ַ
TEST( BaseTest, testHost_handleInputCustom )
{
	gloox::JID jid( "a@b/c" );
	const std::string password = "000000";
	const std::string host = "172.30.3.78";
	kl::XmppStack* xs = new kl::XmppStack( jid, password, host );
	
	EXPECT_STREQ( host.c_str(), xs->host().c_str() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::port()����������Ĭ�ϲ�������5222�˿�
TEST( BaseTest, testPort_handleInputDefault )
{
	gloox::JID jid( "a@b/c" );
	const std::string password = "000000";
	const std::string host = "172.30.3.78";
	kl::XmppStack* xs = new kl::XmppStack( jid, password, host );
	
	EXPECT_EQ( 5222, xs->port() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::port()�����������û��Զ���˿�
TEST( BaseTest, testPort_handleInputCustom )
{
	gloox::JID jid( "a@b/c" );
	const std::string password = "000000";
	const std::string host = "172.30.3.78";
	const int port = 52220;
	kl::XmppStack* xs = new kl::XmppStack( jid, password, host, port );
	
	EXPECT_EQ( port, xs->port() );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::registerXmppCallback()����
TEST( BaseTest, testRegisterXmppCallback )
{
	kl::XmppStack* xs = new kl::XmppStack( gloox::JID(), "" );
	kl::XmppCallback* xc0 = new kl::XmppCallback();
	kl::XmppCallback* xc1 = new kl::XmppCallback();
	
	// ��ֹ�����ָ�룬����Ӧ�÷���false
	EXPECT_FALSE( xs->registerXmppCallback( 0 ) );
	EXPECT_EQ( 0, xs->m_xmppCallbacks.size() );
	
	// ��������ǿն���ָ��1
	EXPECT_TRUE( xs->registerXmppCallback( xc0 ) );
	EXPECT_EQ( 1, xs->m_xmppCallbacks.size() );

	// ��ֹ�����ظ�����ָ�룬����Ӧ�÷���false
	EXPECT_FALSE( xs->registerXmppCallback( xc0 ) );
	EXPECT_EQ( 1, xs->m_xmppCallbacks.size() );
	
	// ��������ǿշ��ظ�����ָ��2
	EXPECT_TRUE( xs->registerXmppCallback( xc1 ) );
	EXPECT_EQ( 2, xs->m_xmppCallbacks.size() );
	
	delete xc1;
	xc1 = 0;
	delete xc0;
	xc0 = 0;
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::removeXmppCallback()����
TEST( BaseTest, testRemoveXmppCallback )
{
	kl::XmppStack* xs = new kl::XmppStack( gloox::JID(), "" );
	kl::XmppCallback* xc0 = new kl::XmppCallback();
	kl::XmppCallback* xc1 = new kl::XmppCallback();
	xs->registerXmppCallback( xc0 );
	xs->registerXmppCallback( xc1 );
	// �����Ƴ�ָ��1
	xs->removeXmppCallback( xc0 );
	EXPECT_EQ( 1, xs->m_xmppCallbacks.size() );
	// �����Ƴ�ָ��2
	xs->removeXmppCallback( xc1 );
	EXPECT_EQ( 0, xs->m_xmppCallbacks.size() );
	
	delete xc1;
	xc1 = 0;
	delete xc0;
	xc0 = 0;
	delete xs;
	xs = 0;
}
#pragma endregion

#pragma region LoginTest
// ��¼��������(LoginTest)������XmppStack�Ƿ������ȷ��¼XMPP������������Ƿ���JID�ʺš����롢����������/��ַ�Ͷ˿����롣
// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ������Ƿ�JID��usernameΪ�գ�
TEST_F( LoginTest, testLogin_handleInputNullUsernameJID )
{
	// e.g. test jid = @localhost/kl
	gloox::JID jid( "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );
	
	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleNullUsernameJID ) );
	EXPECT_EQ( 1, m_onInvaildJID );
	EXPECT_EQ( 0, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ������Ƿ�JID��serverΪ�գ�
TEST_F( LoginTest, testLogin_handleInputNullServerJID )
{
	// e.g. test jid = test@/kl
	gloox::JID jid( g_username1 + "@/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleNullServerJID ) );
	EXPECT_EQ( 1, m_onInvaildJID );
	EXPECT_EQ( 0, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ������JID��resourceΪ�գ�
TEST_F( LoginTest, testLogin_handleInputNullResourceJID )
{
	// e.g. test jid = test@localhost
	gloox::JID jid( g_username1 + "@" + g_server + "/" );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleNullServerJID ) );
	EXPECT_EQ( 0, m_onInvaildJID );
	EXPECT_EQ( 1, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ��������Ч���루����Ϊ�գ�
TEST_F( LoginTest, testLogin_handleInputNullPassword )
{
	// e.g. test jid = test@localhost/kl, password = null
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, "", g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleNullPassword ) );
	EXPECT_EQ( 0, m_onInvaildJID );
	EXPECT_EQ( 1, m_onInvaildPassword );
	EXPECT_EQ( 0, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ������������������/��ַ
TEST_F( LoginTest, testLogin_handleInputIncorrectHost )
{
	// e.g. test host = 0.0.0.0
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, "000000", "0.0.0.0", g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleIncorrectHost ) );
	EXPECT_EQ( 0, m_onInvaildJID );
	EXPECT_EQ( 0, m_onInvaildPassword );
	EXPECT_EQ( 0, m_onTcpConnSuccess );
	EXPECT_EQ( 1, m_onTcpConnFailed );
	EXPECT_EQ( 0, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ���������˿�
TEST_F( LoginTest, testLogin_handleInputIncorrectPort )
{
	// e.g. test port = 0
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, "000000", g_host, 0 );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleIncorrectPort ) );
	EXPECT_EQ( 0, m_onInvaildJID );
	EXPECT_EQ( 0, m_onInvaildPassword );
	EXPECT_EQ( 0, m_onTcpConnSuccess );
	EXPECT_EQ( 1, m_onTcpConnFailed );
	EXPECT_EQ( 0, m_onLoginSuccess );
	
	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼ʧ�ܣ�ԭ�������������
TEST_F( LoginTest, testOnAuthFailed_handleInputIncorrectPassword )
{
	// e.g. test jid = test@localhost/kl, password = 0
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, "0", g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::HandleIncorrectPassword ) );
	EXPECT_EQ( 0, m_onInvaildJID );
	EXPECT_EQ( 0, m_onInvaildPassword );
	EXPECT_EQ( 1, m_onTcpConnSuccess );
	EXPECT_EQ( 0, m_onTcpConnFailed );
	EXPECT_EQ( 1, m_onAuthenticating );
	EXPECT_EQ( 1, m_onAuthFailed );
	EXPECT_EQ( 0, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::login()��������¼�ɹ�
TEST_F( LoginTest, testLogin_handleLoginSucccess )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::Null ) );
	EXPECT_EQ( 0, m_onInvaildJID );
	EXPECT_EQ( 0, m_onInvaildPassword );
	EXPECT_EQ( 1, m_onTcpConnSuccess );
	EXPECT_EQ( 0, m_onTcpConnFailed );
	EXPECT_EQ( 1, m_onAuthenticating );
	EXPECT_EQ( 0, m_onAuthFailed );
	EXPECT_EQ( 1, m_onLoginSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::logout()�������ǳ��ɹ�
TEST_F( LoginTest, testLogin_handleLogoutSucccess )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( LoginTest::Context::Null ) );
	EXPECT_EQ( 1, m_onLoginSuccess );
	EXPECT_TRUE( m_invokeLogoutSuccess );
	EXPECT_EQ( 1, m_onLogoutSuccess );

	delete xs;
	xs = 0;
}
#pragma endregion

#pragma region RosterTest
// ����kl::XmppStack::logout()�������ǳ��ɹ����Ƿ��ȡ��������
TEST_F( RosterTest, testLogin_handleOnRecvRoster )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( RosterTest::Context::Null ) );
	EXPECT_EQ( 1, m_onLoginSuccess );
	EXPECT_EQ( 1, m_onRecvRoster );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::addContact
// 1. A��B��¼
// 2. A��BΪ����
// 3. A�Ƴ�����B
TEST_F( RosterTestMt, testAddContact )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( RosterTestMt::Context::HandleAddContact ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isALogined );
	EXPECT_TRUE( isBLogined );
	EXPECT_TRUE( isCLogined );
	EXPECT_TRUE( isABecameBFriend );
	EXPECT_TRUE( isBBecameAFriend );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

#pragma endregion

#pragma region MessageTest
// ��Ϣ��������(MessageTest)������XmppStack��¼�ڣ��Ƿ������ȷ�շ�������״̬����Ϣ�������ı���Ϣ��Xhtml��ʽ���ı���Ϣ����
// ����kl::XmppStack::sendAttention()�����������������Լ�
// K-UT-KLXmpp-��Ϣ����(MessageTest)-jyx001
TEST_F( MessageTest, testSendAttention_handleSendAttentionToSelf )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );
	
	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MessageTest::Context::HandleSendAttentionToSelf ) );
	EXPECT_TRUE( m_invokeSendAttentionSuccess );
	EXPECT_EQ( 1, m_onRecvAttention );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::sendPresence()����������״̬
// K-UT-KLXmpp-��Ϣ����(MessageTest)-jyx002
TEST_F( MessageTest, testSendPresence )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );
	
	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MessageTest::Context::HandleSendPresence ) );
	EXPECT_TRUE( m_invokeSendPresenceSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::sendChatMessage()�����������ı���Ϣ���Լ�
TEST_F( MessageTest, testSendChatMessage_handleSendTextChatMessageToSelf )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );
	
	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MessageTest::Context::HandleSendTextChatMessageToSelf ) );
	EXPECT_TRUE( m_invokeSendChatMessage );
	EXPECT_EQ( 1, m_onRecvChatMessage );
	EXPECT_STREQ( m_outTextChatMessage.c_str(), m_inTextChatMessage.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::sendChatMessage()���������͸��ı���Ϣ����ʽ��ȷ��XHtml���ģ����Լ�
TEST_F( MessageTest, testSendChatMessage_handleSendRichChatMessageWithVaildXhtmlToSelf )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );
	
	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MessageTest::Context::HandleSendRichChatMessageWithVaildXhtmlToSelf ) );
	EXPECT_TRUE( m_invokeSendChatMessage );
	EXPECT_EQ( 1, m_onRecvChatMessage );
	EXPECT_STREQ( m_outTextChatMessage.c_str(), m_inTextChatMessage.c_str() );
	EXPECT_STREQ( m_outVaildXhtml.c_str(), m_inXhtml.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::sendChatMessage()���������͸��ı���Ϣ����ʽ�����XHtml���ģ����Լ�
TEST_F( MessageTest, testSendChatMessage_handleSendRichChatMessageWithInvaildXhtmlToSelf )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );
	
	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MessageTest::Context::HandleSendRichChatMessageWithInvaildXhtmlToSelf ) );
	EXPECT_TRUE( m_invokeSendChatMessage );
	EXPECT_EQ( 1, m_onRecvChatMessage );
	EXPECT_STREQ( m_outTextChatMessage.c_str(), m_inTextChatMessage.c_str() );
	EXPECT_STREQ( "", m_inXhtml.c_str() );

	delete xs;
	xs = 0;
}
#pragma endregion

#pragma region �������ϲ�������(PersonalInfoTest)
TEST_F( PersonalInfoTest, testOnRetrieveRegistrationInfo )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 0 );
	EXPECT_EQ( 1, m_onRetrieveRegistrationInfo );
}

TEST_F( PersonalInfoTest, testPublishNickname )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 1 ); // invoke publishNickname when login success
	EXPECT_EQ( 1, m_onPublishNicknameSuccess );
	EXPECT_EQ( 0, m_onPublishNicknameFailed );
	EXPECT_EQ( 1, m_onRecvNicknamePublishedNotification );
	EXPECT_STREQ( m_outNickname.c_str(), m_inNickname.c_str() );
	EXPECT_STREQ( m_publishNicknameOutUid.c_str(), m_publishNicknameInUid.c_str() );
}

TEST_F( PersonalInfoTest, testPublishAvatar )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 2 ); // invoke publishAvatar when login success
	EXPECT_EQ( 1, m_onPublishAvatarDataSuccess );
	EXPECT_EQ( 0, m_onPublishAvatarDataFailed );
	EXPECT_EQ( 1, m_onPublishingAvatarMetadata );
	EXPECT_EQ( 1, m_onPublishAvatarMetadataSuccess );
	EXPECT_EQ( 0, m_onPublishAvatarMetadataFailed );
	EXPECT_EQ( 1, m_onRecvAvatarMetadataPublishedNotification );
	EXPECT_EQ( 1, m_onLoadAvatarSuccess );
	EXPECT_EQ( 0, m_onLoadAvatarFailed );
	EXPECT_STREQ( m_publishAvatarOutUid.c_str(), m_publishAvatarInUid.c_str() );
	EXPECT_STREQ( m_publishingAvatarMetadataOutUid.c_str(), m_publishingAvatarMetadataInUid.c_str() );
	EXPECT_STREQ( m_outAvatarData.c_str(), m_inAvatarData.c_str() );
	EXPECT_EQ( m_outAvatarDataBytes, m_inAvatarDataBytes );
	EXPECT_STREQ( m_outAvatarType.c_str(), m_inAvatarType.c_str() );
	EXPECT_EQ( m_outAvatarHeight, m_inAvatarHeight );
	EXPECT_EQ( m_outAvatarWidth, m_inAvatarWidth );
	EXPECT_STREQ( m_outAvatarId.c_str(), m_inAvatarId.c_str() );
}

TEST_F( PersonalInfoTest, testPublishPersonalInfoChangeNotification )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 3 ); // invoke publishPersonalInfoChangeNotification when login success
	EXPECT_EQ( 1, m_onPublishPersonalInfoChangeNotificationSuccess );
	EXPECT_EQ( 0, m_onPublishPersonalInfoChangeNotificationFailed );
	EXPECT_EQ( 1, m_onRecvPersonalInfoChangeNotification );
	EXPECT_STREQ( m_publishPersonalInfoChangeNotificationOutUid.c_str(), m_publishPersonalInfoChangeNotificationInUid.c_str() );
}

TEST_F( PersonalInfoTest, testPublishAddressBookChangeNotification )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 4 ); // invoke publishAddressBookChangeNotification when login success
	EXPECT_EQ( 1, m_onPublishAddressBookChangeNotificationSuccess );
	EXPECT_EQ( 0, m_onPublishAddressBookChangeNotificationFailed );
	EXPECT_EQ( 1, m_onRecvAddressBookChangeNotification );
	EXPECT_STREQ( m_publishAddressBookChangeNotificationOutUid.c_str(), m_publishAddressBookChangeNotificationInUid.c_str() );
}
#pragma endregion

#pragma region ��̬��������(NewsTest)
// ��̬��������(NewsTest)�����Զ�̬��ؽӿڡ�
TEST_F( NewsTest, testPublishNews )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 1 ); // invoke publishNews when login success
	EXPECT_EQ( 1, m_onPublishNewsSuccess );
	EXPECT_EQ( 0, m_onPublishNewsFailed );
	EXPECT_EQ( 1, m_onRecvNewsPublishedNotification );
	EXPECT_EQ( 1, m_onDeleteNewsSuccess );
	EXPECT_EQ( 0, m_onDeleteNewsFailed );
	EXPECT_EQ( 1, m_onRecvNewsDeletedNotification );
	EXPECT_STREQ( m_publishNewsOutUid.c_str(), m_publishNewsInUid.c_str() );
	EXPECT_TRUE( m_outNewsContentType == m_inNewsContentType );
	EXPECT_STREQ( m_publishNewsOutUid.c_str(), m_publishNewsInUid.c_str() );
	EXPECT_STREQ( m_outNewsTime.c_str(), m_inNewsTime.c_str() );
	EXPECT_STREQ( m_outNewsGeoloc.c_str(), m_inNewsGeoloc.c_str() );
	EXPECT_STREQ( m_outNewsAuthor.c_str(), m_inNewsAuthor.c_str() );
}

TEST_F( NewsTest, testCommentNews )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 2 ); // invoke commentNews when login success
	EXPECT_EQ( 1, m_onCommentNewsSuccess );
	EXPECT_EQ( 0, m_onCommentNewsFailed );
	EXPECT_STREQ( m_commentNewsOutUid.c_str(), m_commentNewsInUid.c_str() );
}

TEST_F( NewsTest, testRepeatNews )
{
	run( g_username1, g_server, g_resource , g_password, g_host, g_port, 3 ); // invoke repeatNews when login success
	EXPECT_EQ( 1, m_onRepeatNewsSuccess );
	EXPECT_EQ( 0, m_onRepeatNewsFailed );
	EXPECT_STREQ( m_publishNewsOutUid.c_str(), m_publishNewsInUid.c_str() );
	EXPECT_STREQ( m_sourceNewsId.c_str(), m_inSourceNewsId.c_str() );
	EXPECT_STREQ( m_outNewsAuthor.c_str(), m_inRepeatNewsAuthor.c_str() );
	EXPECT_TRUE( m_outNewsContentType == m_inRepeatNewsContentType );
	EXPECT_STREQ( m_outNewsContent.c_str(), m_inRepeatNewsContent.c_str() );
	EXPECT_STREQ( m_outNewsTime.c_str(), m_inRepeatNewsTime.c_str() );
	EXPECT_STREQ( m_outNewsGeoloc.c_str(), m_inRepeatNewsGeoloc.c_str() );
}
#pragma endregion

/*
#pragma region MucTest
// ����kl::XmppStack::createInstantMUCRoom()������������ʱȺ�ķ���
TEST_F( MucTest, testCreateInstantMUCRoom )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleCreateInstantMUCRoom, instantmucroom ) );
	EXPECT_EQ( 1, m_onCreateMUCRoomSuccess );
	EXPECT_TRUE( xs->findMUCRoom( instantmucroom ) != 0 );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::sendMUCRoomPresence()����������״̬��Ⱥ�ķ���
TEST_F( MucTest, testSendMUCRoomPresence )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleSendMUCRoomPresence, instantmucroom ) );
	EXPECT_EQ( 2, m_onRecvMUCRoomPresence ); // online --> away
	EXPECT_TRUE( m_outPres == m_inPres );
	EXPECT_STREQ( m_outPresStatus.c_str(), m_inPresStatus.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::sendMUCRoomMessage()����������1���ı���Ϣ��Ⱥ�ķ���
TEST_F( MucTest, testSendMUCRoomMessage_HandleSendTextMUCRoomMessage )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleSendTextMUCRoomMessage, instantmucroom ) );
	EXPECT_EQ( 1, m_onRecvMUCRoomMessage );
	EXPECT_STREQ( m_outTextMUCRoomMessage.c_str(), m_inTextMUCRoomMessage.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::changeMUCRoomNickname()�������޸�Ⱥ�ķ������ǳƣ���������ݣ�
TEST_F( MucTest, testChangeMUCRoomNickname )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleChangeMUCRoomNickname, instantmucroom ) );
	EXPECT_EQ( 3, m_onRecvMUCRoomPresence ); // 1. old nick --> online 2. old nick --> offline 3. new nick --> online 
	EXPECT_STREQ( m_outNewMUCroomNickname.c_str(), m_inNewMUCroomNickname.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::changeMUCRoomSubject()�������޸�Ⱥ�ķ������⣨��������ݣ�
TEST_F( MucTest, testChangeMUCRoomSubject )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleChangeMUCRoomSubject, instantmucroom ) );
	EXPECT_EQ( 1, m_onMUCRoomSubjectChanged );
	EXPECT_STREQ( m_outNewMUCroomSubject.c_str(), m_inNewMUCroomSubject.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::queryMUCRoomInfo()��������ѯȺ�ķ�����Ϣ����������ݣ�
TEST_F( MucTest, testQueryMUCRoomInfo )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleQueryMUCRoomInfo, instantmucroom ) );
	EXPECT_EQ( 1, m_onRetrieveMUCRoomInfo );
	EXPECT_FALSE( m_inMUCRoomInfo.isPersistent );
	EXPECT_FALSE( m_inMUCRoomInfo.isMembersOnly );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::queryMUCRoomConfig()��������ѯȺ�ķ��䵱ǰ���ã���������ݣ�
TEST_F( MucTest, testQueryMUCRoomConfig )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleQueryMUCRoomConfig, instantmucroom ) );
	EXPECT_EQ( 1, m_onRetrieveMUCRoomConfig );
	EXPECT_FALSE( m_inMUCRoomConfig.isPersistent );
	EXPECT_STREQ( g_instantmucroom_id.c_str(), m_inMUCRoomConfig.roomname.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::configMUCRoom()�������޸�Ⱥ�ķ������ã��޸ķ������ƺͷ����飨��������ݣ�
TEST_F( MucTest, testConfigMUCRoom_HandleChangeMUCRoomNameAndDesc )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleChangeMUCRoomNameAndDesc, instantmucroom ) );
	EXPECT_EQ( 1, m_onConfigMUCRoomSuccess );
	EXPECT_EQ( 0, m_onConfigMUCRoomFailed );
	EXPECT_STREQ( m_outNewMUCRoomName.c_str(), m_inMUCRoomConfig.roomname.c_str() );
	EXPECT_STREQ( m_outNewMUCRoomDesc.c_str(), m_inMUCRoomConfig.roomdesc.c_str() );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::createHuddle()����������Ⱥ
TEST_F( MucTest, testCreateHuddle )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID huddle( g_huddle_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleCreateHuddle, huddle ) );
	EXPECT_EQ( 1, m_onCreateMUCRoomSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::destroyMUCRoom()����������Ⱥ�ķ��䣬ֻ�г־÷�����Ҫ�ֶ����٣�����������ݣ�
TEST_F( MucTest, testDestroyMUCRoom_AsNonOwner )
{
	gloox::JID jid( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID huddle( g_huddle_id + "@" + g_muc_service + "/" + g_username2 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleDestroyMUCRoom, huddle ) );
	EXPECT_EQ( 1, m_onDestroyMUCRoomFailed );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::destroyMUCRoom()����������Ⱥ�ķ��䣬ֻ�г־÷�����Ҫ�ֶ����٣���������ݣ�
TEST_F( MucTest, testDestroyMUCRoom_AsOwner )
{
	gloox::JID jid( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID huddle( g_huddle_id + "@" + g_muc_service + "/" + g_username1 );
	kl::XmppStack* xs = new kl::XmppStack( jid, g_password, g_host, g_port );

	EXPECT_TRUE( init( xs ) );
	EXPECT_TRUE( run( MucTest::Context::HandleDestroyMUCRoom, huddle ) );
	EXPECT_EQ( 1, m_onDestroyMUCRoomSuccess );

	delete xs;
	xs = 0;
}

// ����kl::XmppStack::enterMUCRoom()����
// 1. A������ʱȺ�ķ��� 
// 2. B��C���뷿��
// 3. B��C�뿪����
TEST_F( MucTestMt, testEnterMUCRoom )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( MucTestMt::Context::HandleEnterMUCRoom, instantmucroom ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isALogined );
	EXPECT_TRUE( isBLogined );
	EXPECT_TRUE( isCLogined );

	EXPECT_TRUE( isACreateMUCRoom );
	EXPECT_TRUE( isBEnterMUCRooom );
	EXPECT_TRUE( isCEnterMUCRooom );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

// ����kl::XmppStack::inviteIntoMUCRoom()����
// 1. A������ʱȺ�ķ���
// 2. A����B��C���뷿�� 
// 3. B��C��������
TEST_F( MucTestMt, testInviteIntoMUCRoom )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( MucTestMt::Context::HandleInviteIntoMUCRoom, instantmucroom ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isACreateMUCRoom );
	EXPECT_TRUE( isBEnterMUCRooom );
	EXPECT_TRUE( isCEnterMUCRooom );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

// ����kl::XmppStack::revokeMUCRoomVoice()����
// 1. A������ʱȺ�ķ���
// 2. B���뷿��
// 3. A����B
TEST_F( MucTestMt, testRevokeMUCRoomVoice )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( MucTestMt::Context::HandleRevokeMUCRoomVoice, instantmucroom ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isACreateMUCRoom );
	EXPECT_TRUE( isBEnterMUCRooom );
	EXPECT_TRUE( isBRevokedVoice );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

// ����kl::XmppStack::grantMUCRoomVoice()����
// 1. A��������ʱȺ�ķ���
// 2. B���뷿��
// 3. A����B
// 4. Aȡ����B�Ľ���
TEST_F( MucTestMt, testGrantMUCRoomVoice )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( MucTestMt::Context::HandleGrantMUCRoomVoice, instantmucroom ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isACreateMUCRoom );
	EXPECT_TRUE( isBEnterMUCRooom );
	EXPECT_TRUE( isBGrantedVoice );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

// ����kl::XmppStack::kickOutMUCRoom()����
// 1. A��������ʱȺ�ķ���
// 2. B���뷿��
// 3. A�߳�B 
TEST_F( MucTestMt, testKickOutMUCRoom )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( MucTestMt::Context::HandleKickOutMUCRoom, instantmucroom ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isACreateMUCRoom );
	EXPECT_TRUE( isBEnterMUCRooom );
	EXPECT_TRUE( isBKickedOutMUCRoom );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

// ����kl::XmppStack::banOutMUCRoom()����
// 1. A��������ʱȺ�ķ���
// 2. B���뷿��
// 3. A��B���뷿������� 
TEST_F( MucTestMt, testBanOutMUCRoom )
{
	gloox::JID jid1( g_username1 + "@" + g_server + "/" + g_resource );
	gloox::JID jid2( g_username2 + "@" + g_server + "/" + g_resource );
	gloox::JID jid3( g_username3 + "@" + g_server + "/" + g_resource );
	gloox::JID instantmucroom( g_instantmucroom_id + "@" + g_muc_service + "/" + g_username1 );
	XmppStackProxy* xsp1 = new XmppStackProxy( jid1, g_password, g_host, g_port );
	XmppStackProxy* xsp2 = new XmppStackProxy( jid2, g_password, g_host, g_port );
	XmppStackProxy* xsp3 = new XmppStackProxy( jid3, g_password, g_host, g_port );

	init( xsp1, xsp2, xsp3 );
	run( MucTestMt::Context::HandleBanOutMUCRoom, instantmucroom ); // create threads and loop

	::Sleep( 1000 );

	EXPECT_TRUE( isACreateMUCRoom );
	EXPECT_TRUE( isAEnterMUCRooom );
	EXPECT_TRUE( isBBanedOutMUCRoom );

	delete xsp1;
	xsp1 = 0;
	delete xsp2;
	xsp2 = 0;
	delete xsp3;
	xsp3 = 0;
}

#pragma endregion
*/

// �������ڶ�ȡ�����ļ�
class KLXmppUnitTestEnvironment : public testing::Environment,
	                              public gloox::TagHandler
{
public:
	KLXmppUnitTestEnvironment() : m_parser( 0 )
	{
		m_parser = new gloox::Parser( this );
	}

	virtual ~KLXmppUnitTestEnvironment() 
	{
		delete m_parser;
		m_parser = 0;
	}
	
	virtual void handleTag( gloox::Tag* tag )
	{
		gloox::Tag* tag_username1 = tag->findChild( "username1" );
		if ( tag_username1 )
		{
			g_username1 = tag_username1->cdata();
		}
		gloox::Tag* tag_username2 = tag->findChild( "username2" );
		if ( tag_username2 )
		{
			g_username2 = tag_username2->cdata();
		}
		gloox::Tag* tag_username3 = tag->findChild( "username3" );
		if ( tag_username3 )
		{
			g_username3 = tag_username3->cdata();
		}
		gloox::Tag* tag_server = tag->findChild( "server" );
		if ( tag_server )
		{
			g_server = tag_server->cdata();
		}
		gloox::Tag* tag_resource = tag->findChild( "resource" );
		if ( tag_resource )
		{
			g_resource = tag_resource->cdata();
		}
		gloox::Tag* tag_password = tag->findChild( "password" );
		if ( tag_password )
		{
			g_password = tag_password->cdata();
		}
		gloox::Tag* tag_host = tag->findChild( "host" );
		if ( tag_host )
		{
			g_host = tag_host->cdata();
		}
		gloox::Tag* tag_port = tag->findChild( "port" );
		if ( tag_port )
		{
			g_port = atoi( tag_port->cdata().c_str() );
		}
		// ��ȡȺ�Ĳ�������
		gloox::Tag* tag_muc_service = tag->findChild( "muc-service" );
		if ( tag_muc_service )
		{
			g_muc_service = tag_muc_service->cdata();
		}
		gloox::Tag* tag_instantmucroom_id = tag->findChild( "instantmucroom-id" );
		if ( tag_instantmucroom_id )
		{
			g_instantmucroom_id = tag_instantmucroom_id->cdata();
		}
		gloox::Tag* tag_huddle_id = tag->findChild( "huddle-id" );
		if ( tag_huddle_id )
		{
			g_huddle_id = tag_huddle_id->cdata();
		}
	}
	
	virtual void SetUp() 
	{
		FILE* file = fopen( "klxmppunittest_config.xml", "rb" );
		if ( file )
		{
			fseek( file, 0, SEEK_END );
			int size = ftell( file );
			fseek( file, 0, 0 );
			char* buf = new char[size];
			memset( buf, 0, size );
			fread( buf, 1, size, file );
			if ( m_parser )
			{
				m_parser->feed( std::string( buf ) );
			}
			printf( "load config success ... \n\n" );
			printf( "tcp: host = %s, port = %d\n\n", g_host.c_str(), g_port );
			printf( "jid1 = %s@%s/%s, password = %s\n\n", g_username1.c_str(), g_server.c_str(), g_resource.c_str(), g_password.c_str() );
			printf( "jid2 = %s@%s/%s, password = %s\n\n", g_username2.c_str(), g_server.c_str(), g_resource.c_str(), g_password.c_str() );
			printf( "jid3 = %s@%s/%s, password = %s\n\n", g_username3.c_str(), g_server.c_str(), g_resource.c_str(), g_password.c_str() );
			
			delete buf;
			buf = 0;
			fclose( file );
		}
		else
		{
			printf( "load config failed ... \n" );
			system( "PAUSE" );
			exit( 0 );
		}
	}

	virtual void TearDown() 
	{
	}

private:
	gloox::Parser* m_parser;
};

GTEST_API_ int main( int argc, char** argv )
{
	//_CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF );
	//_CrtSetBreakAlloc(4069); // �ж���?���ڴ���䴦
	printf( "Running KLXmppUnitTest ...\n" );
	testing::InitGoogleTest( &argc, argv );
	KLXmppUnitTestEnvironment* en = new KLXmppUnitTestEnvironment();
	testing::Environment* const global_env = testing::AddGlobalTestEnvironment( en );
	RUN_ALL_TESTS();
	system( "PAUSE" );
}