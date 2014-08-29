#include "../../gloox/src/connectiontcpclient.h"

class MyConnectionDataHandler : public gloox::ConnectionDataHandler
{
	virtual void handleReceivedData( const gloox::ConnectionBase* connection, const std::string& data )
	{
	}

	virtual void handleConnect( const gloox::ConnectionBase* connection )
	{
	}

	virtual void handleDisconnect( const gloox::ConnectionBase* connection, gloox::ConnectionError reason )
	{
	}
};

int main()
{
	gloox::LogSink log;
	MyConnectionDataHandler* mcdh = new MyConnectionDataHandler();
	gloox::ConnectionBase* conn = new gloox::ConnectionTCPClient( log, "172.30.3.67", 5222 );
	conn->registerConnectionDataHandler( mcdh );
	gloox::ConnectionError ce = conn->connect();
	bool ret = true;
	while ( ret & ce ==  gloox::ConnectionError::ConnNoError)
	{
		conn->recv( 10 );
	}
	return 0;
}