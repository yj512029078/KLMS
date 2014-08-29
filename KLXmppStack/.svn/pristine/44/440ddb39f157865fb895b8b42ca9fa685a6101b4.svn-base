%{
#include "../../../gloox/src/mucroom.h"
%}

namespace gloox
{
	enum MUCOperation
  {
    RequestUniqueName,              /**< Request a unique room name. */
    CreateInstantRoom,              /**< Create an instant room. */
    CancelRoomCreation,             /**< Cancel room creation process. */
    RequestRoomConfig,              /**< Request room configuration form. */
    SendRoomConfig,                 /**< Send room configuration */
    DestroyRoom,                    /**< Destroy room. */
    GetRoomInfo,                    /**< Fetch room info. */
    GetRoomItems,                   /**< Fetch room items (e.g., current occupants). */
    SetRNone,                       /**< Set a user's role to None. */
    SetVisitor,                     /**< Set a user's role to Visitor. */
    SetParticipant,                 /**< Set a user's role to Participant. */
    SetModerator,                   /**< Set a user's role to Moderator. */
    SetANone,                       /**< Set a user's affiliation to None. */
    SetOutcast,                     /**< Set a user's affiliation to Outcast. */
    SetMember,                      /**< Set a user's affiliation to Member. */
    SetAdmin,                       /**< Set a user's affiliation to Admin. */
    SetOwner,                       /**< Set a user's affiliation to Owner. */
    RequestVoiceList,               /**< Request the room's Voice List. */
    StoreVoiceList,                 /**< Store the room's Voice List. */
    RequestBanList,                 /**< Request the room's Ban List. */
    StoreBanList,                   /**< Store the room's Ban List. */
    RequestMemberList,              /**< Request the room's Member List. */
    StoreMemberList,                /**< Store the room's Member List. */
    RequestModeratorList,           /**< Request the room's Moderator List. */
    StoreModeratorList,             /**< Store the room's Moderator List. */
    RequestOwnerList,               /**< Request the room's Owner List. */
    StoreOwnerList,                 /**< Store the room's Owner List. */
    RequestAdminList,               /**< Request the room's Admin List. */
    StoreAdminList,                 /**< Store the room's Admin List. */
    InvalidOperation                /**< Invalid operation. */
  };


	class ClientBase;
	class MUCRoomHandler;
	class MUCRoomConfigHandler;
	
	class GLOOX_API MUCRoom : private DiscoHandler, 
							  private PresenceHandler,
							  public  IqHandler, 
							  private MessageHandler, 
							  private DiscoNodeHandler
	{
	public:
		MUCRoom( ClientBase* parent, const JID& nick, MUCRoomHandler* mrh, MUCRoomConfigHandler* mrch = 0 );
		virtual ~MUCRoom();
		const std::string name() const { return m_nick.username(); }
		const std::string service() const { return m_nick.server(); }
		const std::string nick() const { return m_nick.resource(); }
		virtual void join( Presence::PresenceType type = Presence::Available, const std::string& status = EmptyString, int priority = 0 );
		void leave( const std::string& msg = EmptyString );
		void send( const std::string& message );
		void setSubject( const std::string& subject );
		MUCRoomAffiliation affiliation() const { return m_affiliation; }
		MUCRoomRole role() const { return m_role; }
		void setNick( const std::string& nick );
		void setPresence( Presence::PresenceType presence, const std::string& msg = EmptyString );
		void invite( const JID& invitee, const std::string& reason, const std::string& thread = EmptyString );
		void kick( const std::string& nick, const std::string& reason = EmptyString );
        void ban( const std::string& nick, const std::string& reason );
		void getRoomInfo();
		void getRoomItems();
		//void setPublish( bool publish, bool publishNick );
		void addHistory( const std::string& message, const JID& from, const std::string& stamp );
		void requestVoice();
		void requestRoomConfig();
		void grantVoice( const std::string& nick, const std::string& reason );
		void revokeVoice( const std::string& nick, const std::string& reason );
		static Message* declineInvitation( const JID& room, const JID& invitor, const std::string& reason = EmptyString);
		void acknowledgeInstantRoom();
		void requestList( MUCOperation operation );
	};
}
