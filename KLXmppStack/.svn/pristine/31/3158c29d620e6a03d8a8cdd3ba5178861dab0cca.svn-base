#ifndef KL_MUCROOM_H__
#define KL_MUCROOM_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/mucroom.h"

namespace kl
{
	class KLXMPP_API MUCRoom : public gloox::MUCRoom
	{
	public:
		MUCRoom( gloox::ClientBase* parent_, const gloox::JID& nick_, gloox::MUCRoomHandler* mrh_, gloox::MUCRoomConfigHandler* mrch_ = 0 );

		virtual ~MUCRoom();

		virtual void handleIqID( const gloox::IQ& iq_, int context_ );
	
	private:
		void handleIqResult( const gloox::IQ& iq_, int context_ );
		void handleIqError( const gloox::IQ& iq_, int context_ );

	private:
		gloox::MUCRoomConfigHandler*  m_rch;
	};
}

#endif // KL_MUCROOM_H__