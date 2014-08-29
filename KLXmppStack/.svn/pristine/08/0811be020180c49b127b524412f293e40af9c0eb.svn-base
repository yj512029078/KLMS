#ifndef KL_VCARDTELEPHONE_H__
#define KL_VCARDTELEPHONE_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardTelephone
	{
	public:
		VCardTelephone();

#ifndef SWIG // SWIG过滤以下接口

		VCardTelephone( const gloox::VCard::Telephone& telephone_ );

#endif // SWIG

		virtual ~VCardTelephone();
		
		void setNumber( const std::string& number_ );

		const std::string& number() const;

		bool isHome() const;

		bool isWork() const;

		bool isVoice() const;
		
		bool isFax() const;
		
		bool isPager() const;
		
		bool isMsg() const;

		bool isCell() const;

		bool isVideo() const;

		bool isBBS() const;

		bool isModem() const;

		bool isISDN() const;

		bool isPCS() const;

		bool isPref() const;

		void setType( int type_ );
		
	private:
		gloox::VCard::Telephone* m_telephone;
	};
}

#endif // KL_VCARDEMAIL_H__
