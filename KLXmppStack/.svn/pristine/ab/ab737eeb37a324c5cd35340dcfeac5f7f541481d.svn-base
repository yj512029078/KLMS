#include "../include/kl_mucroom.h"

namespace kl
{
	MUCRoom::MUCRoom( gloox::ClientBase* parent_, const gloox::JID& nick_, gloox::MUCRoomHandler* mrh_, gloox::MUCRoomConfigHandler* mrch_ )
		: gloox::MUCRoom( parent_, nick_, mrh_, mrch_ )
	{
		m_rch = mrch_;
	}

	MUCRoom::~MUCRoom()
	{
	}

	void MUCRoom::handleIqID( const gloox::IQ& iq_, int context_ )
	{
		if( !m_rch )
			return;

		switch( iq_.subtype() )
		{
			case gloox::IQ::Result:
				handleIqResult( iq_, context_ );
				break;
			case  gloox::IQ::Error:
				handleIqError( iq_, context_ );
				break;
			default:
				break;
		}

		gloox::MUCRoom::handleIqID( iq_, context_ );
	}

	void MUCRoom::handleIqResult( const gloox::IQ& iq_, int context_ )
	{
		switch( context_ )
		{
			case gloox::MUCOperation::SendRoomConfig:
			case gloox::MUCOperation::StoreOwnerList:
				m_rch->handleMUCConfigResult( this, true, (gloox::MUCOperation) context_ );
				break;
			default:
				break;
		}
	}

	void MUCRoom::handleIqError( const gloox::IQ& iq_, int context_ )
	{
		switch( context_ )
		{
			case gloox::MUCOperation::SendRoomConfig:
				m_rch->handleMUCConfigResult( this, false, (gloox::MUCOperation) context_ );
				break;
			default:
				break;
		}
	}
}