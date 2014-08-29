#ifndef NEWSTEST_H__
#define NEWSTEST_H__

#ifndef KL_XMPPSTACK_TEST
#define KL_XMPPSTACK_TEST
#endif

#include "../../KLXmpp/include/kl_xmppstack.h"
#include "../../KLXmpp/include/kl_news.h"
#include <gtest/gtest.h>

class NewsTest : public ::testing::Test,
	             public kl::XmppCallback
{
public:
	void onLoginSuccess() 
	{
		if ( 0 == m_context )
		{
			m_xs->logout();
		}
		else if ( 1 == m_context || 2 == m_context || 3 == m_context )
		{
			m_outNewsAuthor = m_xs->jid().bare();
			m_outNewsContentType = kl::NewsContentType::Text;
			m_outNewsContent = "newstest";
			m_outNewsTime = "2008-05-08T18:30:02Z";
			m_outNewsGeoloc = "ShangHai";
			m_publishNewsOutUid = m_xs->publishNews( m_outNewsContentType, m_outNewsContent, m_outNewsTime, m_outNewsGeoloc );
		}
	}

	void onPublishNewsSuccess( const std::string& uid_ ) 
	{
		m_onPublishNewsSuccess++;
		m_publishNewsInUid = uid_;
	}

	void onPublishNewsFailed( const std::string& uid_ ) 
	{
		m_onPublishNewsFailed++;
		m_publishNewsInUid = uid_;
		m_xs->logout();
	}
	
	void onRecvNewsPublishedNotification( kl::News* news_ ) 
	{
		m_onRecvNewsPublishedNotification++;
		if ( kl::NewsType::Source == news_->type() )
		{
			m_sourceNewsId = news_->id();
		} 
		else if ( kl::NewsType::Repeat == news_->type() )
		{
			m_repeatNewsId = news_->id();
		}
		m_inNewsAuthor = news_->author().full();
		m_inNewsContentType = news_->contentType();
		m_inNewsContent = news_->content();
		m_inNewsTime =  news_->publishTime();
		m_inNewsGeoloc = news_->geoloc();
		if ( 1 == m_context )
		{
			m_xs->deleteNews( m_sourceNewsId );
		}
		else if ( 2 == m_context )
		{
			m_commentNewsOutUid = m_xs->commentNews( m_xs->jid().bare(), m_sourceNewsId, "", "" );
		}
		else if ( 3 == m_context && kl::NewsType::Source == news_->type() )
		{
			m_repeatNewsOutUid = m_xs->repeatNews( m_xs->jid().bare(), m_sourceNewsId, "", "" );
		}
		else if ( 3 == m_context && kl::NewsType::Repeat == news_->type() )
		{
			m_inSourceNewsId = news_->source()->id();
			m_inRepeatNewsAuthor = news_->source()->author().full();
			m_inRepeatNewsContentType = news_->source()->contentType();
			m_inRepeatNewsContent = news_->source()->content();
			m_inRepeatNewsTime = news_->source()->publishTime();
			m_inRepeatNewsGeoloc = news_->source()->geoloc();
			m_xs->deleteNews( m_repeatNewsId );
		}
	}

	void onDeleteNewsSuccess( const std::string& uid_ ) 
	{
		m_onDeleteNewsSuccess++;
	}

	void onDeleteNewsFailed( const std::string& uid_ ) 
	{
		m_onDeleteNewsFailed++;
		m_xs->logout();
	}

	void onRecvNewsDeletedNotification( const gloox::JID& from_, const std::string& news_id_ ) 
	{
		m_onRecvNewsDeletedNotification++;
		if ( 1 == m_context || 2 == m_context )
		{
			m_xs->logout();
		}
		else if ( 3 == m_context || news_id_ == m_repeatNewsId )
		{
			m_xs->deleteNews( m_sourceNewsId );
		}
		else if ( 3 == m_context || news_id_ == m_sourceNewsId )
		{
			m_xs->logout();
		}
	}

	void onCommentNewsSuccess( const std::string& uid_ ) 
	{
		m_onCommentNewsSuccess++;
		m_commentNewsInUid = uid_;
		if ( 2 == m_context )
		{
			m_xs->deleteNews( m_sourceNewsId );
		}
	}
		
	void onCommentNewsFailed( const std::string& uid_ ) 
	{
		m_onCommentNewsFailed++;
		m_commentNewsInUid = uid_;
		m_xs->logout();
	}

	void onRepeatNewsSuccess( const std::string& uid_ ) 
	{
		m_onRepeatNewsSuccess++;
		m_repeatNewsInUid = uid_;
	}

	void onRepeatNewsFailed( const std::string& uid_ ) 
	{
		m_onRepeatNewsFailed++;
		m_repeatNewsInUid = uid_;
		m_xs->logout();
	}

protected:
	NewsTest() 
		: m_context( 0 ),
		  m_onPublishNewsSuccess( 0 ),
		  m_onPublishNewsFailed( 0 ),
		  m_onRecvNewsPublishedNotification( 0 ),
		  m_onDeleteNewsSuccess( 0 ),
		  m_onDeleteNewsFailed( 0 ),
		  m_onRecvNewsDeletedNotification( 0 ),
		  m_onCommentNewsSuccess( 0 ),
		  m_onCommentNewsFailed( 0 ),
		  m_onRepeatNewsSuccess( 0 ),
		  m_onRepeatNewsFailed( 0 ),
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
	// 
	int m_onPublishNewsSuccess;
	int m_onPublishNewsFailed;
	int m_onRecvNewsPublishedNotification;
	int m_onDeleteNewsSuccess;
	int m_onDeleteNewsFailed;
	int m_onRecvNewsDeletedNotification;
	int m_onCommentNewsSuccess;
	int m_onCommentNewsFailed;
	int m_onRepeatNewsSuccess;
	int m_onRepeatNewsFailed;
	std::string m_publishNewsOutUid;
	std::string m_publishNewsInUid;
	kl::NewsContentType m_outNewsContentType;
	kl::NewsContentType m_inNewsContentType;
	kl::NewsContentType m_inRepeatNewsContentType;
	std::string m_outNewsContent;
	std::string m_inNewsContent;
	std::string m_inRepeatNewsContent;
	std::string m_outNewsTime;
	std::string m_inNewsTime;
	std::string m_inRepeatNewsTime;
	std::string m_outNewsGeoloc;
	std::string m_inNewsGeoloc;
	std::string m_inRepeatNewsGeoloc;
	std::string m_outNewsAuthor;
	std::string m_inNewsAuthor;
	std::string m_inRepeatNewsAuthor;
	std::string m_sourceNewsId;
	std::string m_repeatNewsId;
	std::string m_inSourceNewsId;
	std::string m_commentNewsOutUid;
	std::string m_commentNewsInUid;
	std::string m_repeatNewsOutUid;
	std::string m_repeatNewsInUid;
	kl::XmppStack* m_xs;
};

#endif // NEWSTEST_H__