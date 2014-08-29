#ifndef KL_VCARDLABEL_H__
#define KL_VCARDLABEL_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardLabel
	{
	public:
		VCardLabel();

#ifndef SWIG // SWIG过滤以下接口

		VCardLabel( const gloox::VCard::Label& label_ );

#endif // SWIG

		virtual ~VCardLabel();
		
		void setLines( const std::list<std::string>& lines_ );

		std::list<std::string>& lines() const;

		bool isHome() const;

		bool isWork() const;
		
		bool isPostal() const;
		
		bool isParcel() const;
		
		bool isPref() const;

		bool isDom() const;

		bool isIntl() const;

		void setType( int type_ );
		
	private:
		gloox::VCard::Label* m_label;
	};
}

#endif // KL_VCARDEMAIL_H__
