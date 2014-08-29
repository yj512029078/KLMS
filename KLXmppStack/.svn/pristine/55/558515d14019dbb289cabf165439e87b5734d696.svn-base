#include "../../gloox/src/disco.h"
#include "../../gloox/src/client.h"
#include "../../gloox/src/message.h"
#include "../../gloox/src/inbandbytestream.h"
#include "../../gloox/src/loghandler.h"
#include "../../gloox/src/connectionlistener.h"
#include "../../gloox/src/messagehandler.h"
#include "../../gloox/src/siprofileft.h"
#include "../../gloox/src/siprofilefthandler.h"
#include "../../gloox/src/bytestreamdatahandler.h"
#include <string>
#include <list>

class XmppFtService : public gloox::LogHandler,
	                  public gloox::ConnectionListener,
					  public gloox::MessageHandler,
					  public gloox::SIProfileFTHandler,
					  public gloox::BytestreamDataHandler
{
public:
	XmppFtService() : m_client( 0 )
	{
		m_client = new gloox::Client( gloox::JID( "jyx@localhost/kl" ), "000000", 52220 );
		m_sIProfileFT = new gloox::SIProfileFT( m_client, this );
		
		m_client->logInstance().registerLogHandler( gloox::LogLevelDebug, gloox::LogAreaAll, this );
		m_client->registerMessageHandler( this );
		m_client->registerConnectionListener( this );
		
		m_client->disco()->addFeature( "http://jabber.org/protocol/bytestreams" ); // support S5B
	}

	virtual ~XmppFtService()
	{
		delete m_client;
		m_client = 0;
	}

	void login()
	{
		if ( m_client )
		{
			// 尝试TCP连接，非阻塞模型
			if ( m_client->connect( false ) )
			{
				gloox::ConnectionError ce = gloox::ConnNoError;
				
				while ( ce == gloox::ConnNoError )
				{
					// 从SOCKET接收数据
					ce = m_client->recv(); 
				}
			}
		}
	}

	/* override gloox::LogHandler */
	void handleLog( gloox::LogLevel level_, gloox::LogArea area_, const std::string& message_ )
	{
		if ( gloox::LogArea::LogAreaXmlIncoming == area_ )
		{
			printf( "recv <<<<<<<<\r\n%s\r\n\r\n", message_.c_str() );
		}
		else if ( gloox::LogArea::LogAreaXmlOutgoing == area_ )
		{
			printf( "send >>>>>>>>\r\n%s\r\n\r\n", message_.c_str() );
		}
	}

	/* override gloox::ConnectionListener */
	void onConnect()
	{
		printf( "onConnect() ... \r\n\r\n" );
	}

	void onDisconnect( gloox::ConnectionError e_ )
	{
		printf( "onDisconnect() ... \r\n\r\n" );
	}

	bool onTLSConnect( const gloox::CertInfo& info_ )
	{
		printf( "onTLSConnect() ... \r\n\r\n" );
		return true;
	}

	/* override gloox::MessageHandler */
	void handleMessage( const gloox::Message& msg_, gloox::MessageSession* session_ )
	{
		printf( "handleMessage() ... \r\n\r\n" );
		if ( msg_.body() == "send file" )
		{
			m_sIProfileFT->requestFT( msg_.from(), "ft.txt", 4, "", "", "", "text", gloox::SIProfileFT::StreamType::FTTypeIBB, msg_.to(), "sid_01" );
		}
	}

	/* override gloox::SIProfileFTHandler */
	void handleFTRequest( const gloox::JID& from_, 
		                  const gloox::JID& to_, 
						  const std::string& sid_, // <si id='' ... >
						  const std::string& name_, // <file name='' ... >
						  long size_, // <file size='' ... >
						  const std::string& hash_,
						  const std::string& date_, 
						  const std::string& mimetype_, // <si mime-type='' ... >
						  const std::string& desc_, // <file><desc>...<>
						  int stypes_ )
	{
		printf( "handleFTRequest() ... %s\r\n\r\n", sid_.c_str() );
		bool supportS5B = ( ( stypes_ & gloox::SIProfileFT::StreamType::FTTypeS5B ) == 0 ) ? false : true;
		bool supportIBB = ( ( stypes_ & gloox::SIProfileFT::StreamType::FTTypeIBB ) == 0 ) ? false : true;
		bool supportOOB = ( ( stypes_ & gloox::SIProfileFT::StreamType::FTTypeOOB ) == 0 ) ? false : true;
		if (  size_ <= 4 * 1024 && supportIBB ) // should not create thread
		{
			m_sIProfileFT->acceptFT( from_, sid_, gloox::SIProfileFT::StreamType::FTTypeIBB );
		}
		else if ( supportS5B ) // should create thread
		{
			m_sIProfileFT->acceptFT( from_, sid_, gloox::SIProfileFT::StreamType::FTTypeS5B );
		}
	}

	void handleFTRequestError( const gloox::IQ& iq_, const std::string& sid_ )
	{
		printf( "handleFTRequestError() ... \r\n\r\n" );
	}

	void handleFTBytestream( gloox::Bytestream* bs_ )
	{
		printf( "handleFTBytestream() ... %s\r\n\r\n", bs_->sid().c_str() );
		bs_->registerBytestreamDataHandler( this );
		if ( bs_->initiator() != m_client->jid() && gloox::Bytestream::StreamType::S5B == bs_->type() )
		{
			bool ret = bs_->connect();
			if ( ret )
			{
				gloox::ConnectionError ce = gloox::ConnNoError;
				while ( ce == gloox::ConnNoError )
				{
					// 从SOCKET接收数据
					ce = bs_->recv(); 
				}
			}
		}
		else if ( bs_->initiator() == m_client->jid() )
		{
			bool ret = bs_->connect();
		}
	}
	
	const std::string handleOOBRequestResult( const gloox::JID& from_, const gloox::JID& to_, const std::string& sid_ )
	{
		printf( "handleOOBRequestResult() ... \r\n\r\n" );
		return false;
	}

	/* override gloox::BytestreamDataHandler */
	void handleBytestreamData( gloox::Bytestream* bs_, const std::string& data_ )
	{
		printf( "handleBytestreamData() ... %s, size=%d\r\n\r\n", bs_->sid().c_str(), data_.size() ); // S5B 1KB/次
	}

	void handleBytestreamError( gloox::Bytestream* bs_, const gloox::IQ& iq_ )
	{
		printf( "handleBytestreamError() ... \r\n\r\n" );
	}

	void handleBytestreamOpen( gloox::Bytestream* bs_ )
	{
		printf( "handleBytestreamOpen() ... %s\r\n\r\n", bs_->sid().c_str() );

		if ( bs_->initiator() == m_client->jid() && gloox::Bytestream::StreamType::IBB == bs_->type() )
		{
			bs_->send( "1234" );
			bs_->close();
		}
	}

	void handleBytestreamClose( gloox::Bytestream* bs_ )
	{
		printf( "handleBytestreamClose() ... %s\r\n\r\n", bs_->sid().c_str() );
		bs_->removeBytestreamDataHandler();
	}

private:
	gloox::Client* m_client;
	// FT
	gloox::SIProfileFT* m_sIProfileFT;
};

int main()
{
	//long long size = atol( "32547886080" );
	//unsigned long size_ = strtoul( "2547886080", 0, 0 );
	XmppFtService* xfs = new XmppFtService();
	xfs->login();
	
	delete xfs;
	xfs = 0;
	
	return 0;
}