#ifndef KL_COMMON_H__
#define KL_COMMON_H__

#include <string>
#include <cstdlib>

#if defined( _WIN32 ) && !defined( __SYMBIAN32__ )
#  if defined( KLXMPP_EXPORTS ) || defined( DLL_EXPORT )
#    define KLXMPP_API __declspec( dllexport )
#  else
#    if defined( KLXMPP_IMPORTS ) || defined( DLL_IMPORT )
#      define KLXMPP_API __declspec( dllimport )
#    endif
#  endif
#endif

#ifndef KLXMPP_API
#  define KLXMPP_API
#endif

namespace kl
{
	const std::string XMLNS_X_CONFERENCE        = "jabber:x:conference";
	const std::string XMLNS_AVATAR_DATA         = "urn:xmpp:avatar:data";
	const std::string XMLNS_AVATAR_METADATA     = "urn:xmpp:avatar:metadata";
	const std::string XMLNS_MICROBLOG           = "urn:xmpp:microblog:0";
	const std::string XMLNS_ATOM                = "http://www.w3.org/2005/Atom";
	const std::string XMLNS_GEOLOC              = "http://jabber.org/protocol/geoloc";
	const std::string XMLNS_NEEKLE_PERSONALINFO = "http://neekle.com/xmpp/protocol/personalinfo";
	const std::string XMLNS_NEEKLE_ADDRESSBOOK  = "http://neekle.com/xmpp/protocol/addressbook";
	const std::string XMLNS_NEEKLE_BACKGROUND   = "http://neekle.com/xmpp/protocol/background";

	enum CreateMUCRoomError
	{
		Unknown,
		NotAllowed,                /**< Inform user that room creation is restricted */
		RemoteServerNotFound       /**< Inform user that muc service does not exist */
	};

	// gloox::StanzaExtensionType的扩展
	enum StanzaExtensionType
	{
		ExtDirectMUCInvitation = 47
	};

	enum MUCRoomInvitationType
	{
		Mediated, // 间接
		Direct // 直接
	};
}

#endif // KL_COMMON_H__