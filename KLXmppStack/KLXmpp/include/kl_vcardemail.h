#ifndef KL_VCARDEMAIL_H__
#define KL_VCARDEMAIL_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardEmail
	{
	public:
		VCardEmail();

#ifndef SWIG // SWIG过滤以下接口

		VCardEmail( const gloox::VCard::Email& email_ );

#endif // SWIG

		virtual ~VCardEmail();
		
		// <USERID>
		void setUserid( const std::string& userid_ );

		const std::string& userid() const;

		bool isHome() const;

		bool isWork() const;
		
		bool isInternet() const;
		
		bool isPref() const;
		
		bool isX400() const;

		void setType( int type_ );
		
	private:
		gloox::VCard::Email* m_email;
	};
}

#endif // KL_VCARDEMAIL_H__
