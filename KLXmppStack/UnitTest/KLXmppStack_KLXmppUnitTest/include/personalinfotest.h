#ifndef PERSONALINFOTEST_H__
#define PERSONALINFOTEST_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include "../../KLXmpp/include/kl_xmppstack.h"
#include <gtest/gtest.h>

class PersonalInfoTest : public ::testing::Test,
	                     public kl::XmppCallback
{
public:
	void onLoginSuccess() 
	{
		if ( 1 == m_context )
		{
			m_outNickname = "nicknametest";
			m_publishNicknameOutUid = m_xs->publishNickname( m_outNickname );
		}
		else if ( 2 == m_context ) 
		{
			m_outAvatarData = "qANQR1DBwU4DX7jmYZnncm...";
			m_outAvatarDataBytes = 12345;
			m_outAvatarType = "image/png";
			m_outAvatarHeight = 90;
			m_outAvatarWidth = 90;
			m_publishAvatarOutUid = m_xs->publishAvatar( m_outAvatarData, m_outAvatarDataBytes, m_outAvatarType, m_outAvatarHeight, m_outAvatarWidth );
		}
		else if( 3 == m_context )
		{
			m_publishPersonalInfoChangeNotificationOutUid = m_xs->publishPersonalInfoChangeNotification();
		}
		else if ( 4 == m_context )
		{
			m_publishAddressBookChangeNotificationOutUid = m_xs->publishAddressBookChangeNotification();
		}
	}

	void onRetrieveRegistrationInfo( const std::string& username_, const std::string& name_, const std::string& email_ ) 
	{
		m_onRetrieveRegistrationInfo++;
		if ( 0 == m_context )
		{
			m_xs->logout();
		}
	}

	void onPublishNicknameSuccess( const std::string& uid_ ) 
	{
		m_onPublishNicknameSuccess++;
		m_publishNicknameInUid = uid_;
	}

	void onPublishNicknameFailed( const std::string& uid_ ) 
	{
		m_onPublishNicknameFailed++;
		m_publishNicknameInUid = uid_;
		if ( 1 == m_context )
		{
			m_xs->logout();
		}
	}

	void onRecvNicknamePublishedNotification( const gloox::JID& from_, const std::string& nickname_ ) 
	{
		m_onRecvNicknamePublishedNotification++;
		m_inNickname = nickname_;
		if ( 1 == m_context )
		{
			m_xs->logout();
		}
	}

	void onPublishAvatarDataSuccess( const std::string& uid_, const std::string& avatarId_ ) 
	{
		m_onPublishAvatarDataSuccess++;
		m_publishAvatarInUid = uid_;
		m_outAvatarId = avatarId_;
	}

	void onPublishAvatarDataFailed( const std::string& uid_, const std::string& avatarId_ ) 
	{
		m_onPublishAvatarDataFailed++;
		m_publishAvatarInUid = uid_;
		m_outAvatarId = avatarId_;
		if ( 2 == m_context )
		{
			m_xs->logout();
		}
	}

	void onPublishingAvatarMetadata( const std::string& uid_, const std::string& avatarId_ ) 
	{
		m_onPublishingAvatarMetadata++;
		m_publishingAvatarMetadataOutUid = uid_;
	}

	void onPublishAvatarMetadataSuccess( const std::string& uid_ ) 
	{
		m_onPublishAvatarMetadataSuccess++;
		m_publishingAvatarMetadataInUid = uid_;
	}

	void onPublishAvatarMetadataFailed( const std::string& uid_ ) 
	{
		m_onPublishAvatarMetadataFailed++;
		m_publishingAvatarMetadataInUid = uid_;
		if ( 2 == m_context )
		{
			m_xs->logout();
		}
	}

	void onRecvAvatarMetadataPublishedNotification( const gloox::JID& from_, 
		                                            const std::string& avatarId_, 
													int bytes_, 
													const std::string& type_,
													int height_, 
													int width_,
													const std::string& url_ ) 
	{
		m_onRecvAvatarMetadataPublishedNotification++;
		m_inAvatarId = avatarId_;
		m_inAvatarDataBytes = bytes_;
		m_inAvatarType = type_;
		m_inAvatarHeight = height_;
		m_inAvatarWidth = width_;
		if ( 2 == m_context )
		{
			m_loadAvatarOutUid = m_xs->loadAvatar( from_, avatarId_ );
		}
	}

	void onLoadAvatarSuccess( const std::string& uid_, const gloox::JID& from_, const std::string& avatarId_, const std::string& data_ ) 
	{
		m_onLoadAvatarSuccess++;
		m_loadAvatarInUid = uid_;
		m_inAvatarData = data_;
		if ( 2 == m_context )
		{
			m_xs->logout();
		}
	}

	void onLoadAvatarFailed( const std::string& uid_, const std::string& avatarId_ ) 
	{
		m_onLoadAvatarFailed++;
		m_loadAvatarInUid = uid_;
		if ( 2 == m_context )
		{
			m_xs->logout();
		}
	}

	void onPublishPersonalInfoChangeNotificationSuccess( const std::string& uid_ ) 
	{
		m_onPublishPersonalInfoChangeNotificationSuccess++;
		m_publishPersonalInfoChangeNotificationInUid = uid_;
	}

	void onPublishPersonalInfoChangeNotificationFailed( const std::string& uid_ ) 
	{
		m_onPublishPersonalInfoChangeNotificationFailed++;
		m_publishPersonalInfoChangeNotificationInUid = uid_;
		if ( 3 == m_context )
		{
			m_xs->logout();
		}
	}

	void onRecvPersonalInfoChangeNotification( const gloox::JID& from_ ) 
	{
		m_onRecvPersonalInfoChangeNotification++;
		if ( 3 == m_context )
		{
			m_xs->logout();
		}
	}

	void onPublishAddressBookChangeNotificationSuccess( const std::string& uid_ ) 
	{
		m_onPublishAddressBookChangeNotificationSuccess++;
		m_publishAddressBookChangeNotificationInUid = uid_;
	}
	
	void onPublishAddressBookChangeNotificationFailed( const std::string& uid_ ) 
	{
		m_onPublishAddressBookChangeNotificationFailed++;
		m_publishAddressBookChangeNotificationInUid = uid_;
		if ( 4 == m_context )
		{
			m_xs->logout();
		}
	}
	
	void onRecvAddressBookChangeNotification( const gloox::JID& from_ ) 
	{
		m_onRecvAddressBookChangeNotification++;
		if ( 4 == m_context )
		{
			m_xs->logout();
		}
	}

protected:
	PersonalInfoTest() 
		: m_context( 0 ), 
		  m_onRetrieveRegistrationInfo( 0 ),
		  m_onPublishNicknameSuccess( 0 ),
		  m_onPublishNicknameFailed( 0 ),
		  m_onRecvNicknamePublishedNotification( 0 ),
		  m_onPublishAvatarDataSuccess( 0 ),
		  m_onPublishAvatarDataFailed( 0 ),
		  m_onPublishingAvatarMetadata( 0 ),
		  m_onPublishAvatarMetadataSuccess( 0 ),
		  m_onPublishAvatarMetadataFailed( 0 ),
		  m_onRecvAvatarMetadataPublishedNotification( 0 ),
		  m_onLoadAvatarSuccess( 0 ),
		  m_onLoadAvatarFailed( 0 ),
		  m_onPublishPersonalInfoChangeNotificationSuccess( 0 ),
		  m_onPublishPersonalInfoChangeNotificationFailed( 0 ),
		  m_onRecvPersonalInfoChangeNotification( 0 ),
		  m_onPublishAddressBookChangeNotificationSuccess( 0 ),
		  m_onPublishAddressBookChangeNotificationFailed( 0 ),
		  m_onRecvAddressBookChangeNotification( 0 ),
		  m_xs( 0 )
	{
	}

	virtual void run( const std::string& username_, 
	                  const std::string& server_, 
				      const std::string& resource_, 
				      const std::string& password_, 
				      const std::string& host_, 
				      int port_,
					  int context_ )
	{
		m_context = context_;
		m_xs = new kl::XmppStack( gloox::JID( username_ + "@" + server_ + "/" + resource_ ), password_, host_, port_ );
		m_xs->registerXmppCallback( this );
		m_xs->login();
	}

	virtual void SetUp() 
	{
		// TODO: Code here will be called immediately after the constructor (right before each test).
	}

	virtual void TearDown() 
	{
		// TODO: Code here will be called immediately after each test (right before the destructor).
		delete m_xs;
		m_xs = 0;
	}

	int m_context;
	int m_onRetrieveRegistrationInfo;
	// nickname
	int m_onPublishNicknameSuccess;
	int m_onPublishNicknameFailed;
	int m_onRecvNicknamePublishedNotification;
	std::string m_outNickname;
	std::string m_inNickname;
	std::string m_publishNicknameOutUid;
	std::string m_publishNicknameInUid;
	// avatar
	int m_onPublishAvatarDataSuccess;
	int m_onPublishAvatarDataFailed;
	int m_onPublishingAvatarMetadata;
	int m_onPublishAvatarMetadataSuccess;
	int m_onPublishAvatarMetadataFailed;
	int m_onRecvAvatarMetadataPublishedNotification;
	int m_onLoadAvatarSuccess;
	int m_onLoadAvatarFailed;
	std::string m_publishAvatarOutUid;
	std::string m_publishAvatarInUid;
	std::string m_publishingAvatarMetadataOutUid;
	std::string m_publishingAvatarMetadataInUid;
	std::string m_outAvatarData;
	std::string m_inAvatarData;
	int m_outAvatarDataBytes;
	int m_inAvatarDataBytes;
	std::string m_outAvatarType;
	std::string m_inAvatarType;
	int m_outAvatarHeight;
	int m_inAvatarHeight;
	int m_outAvatarWidth;
	int m_inAvatarWidth;
	std::string m_outAvatarId;
	std::string m_inAvatarId;
	std::string m_loadAvatarOutUid;
	std::string m_loadAvatarInUid;
	// personalinfochange
	int m_onPublishPersonalInfoChangeNotificationSuccess;
	int m_onPublishPersonalInfoChangeNotificationFailed;
	int m_onRecvPersonalInfoChangeNotification;
	std::string m_publishPersonalInfoChangeNotificationOutUid;
	std::string m_publishPersonalInfoChangeNotificationInUid;
	// addressbookchange
	int m_onPublishAddressBookChangeNotificationSuccess;
	int m_onPublishAddressBookChangeNotificationFailed;
	int m_onRecvAddressBookChangeNotification;
	std::string m_publishAddressBookChangeNotificationOutUid;
	std::string m_publishAddressBookChangeNotificationInUid;

	kl::XmppStack* m_xs;
};

#endif // PERSONALINFOTEST_H__