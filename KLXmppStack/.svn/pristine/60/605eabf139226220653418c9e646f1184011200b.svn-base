#ifndef KL_VCARDADDRESS_H__
#define KL_VCARDADDRESS_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardAddress
	{
	public:
		VCardAddress();

#ifndef SWIG // SWIG过滤以下接口

		VCardAddress( const gloox::VCard::Address& address_ );

#endif // SWIG

		virtual ~VCardAddress();
		
		void setPobox( const std::string& pobox_ );

		const std::string& pobox() const;

		void setExtadd( const std::string& extadd_ );

		const std::string& extadd() const;

		void setStreet( const std::string& street_ );

		const std::string& street() const;

		void setLocality( const std::string& locality_ );

		const std::string& locality() const;

		void setRegion( const std::string& region_ );

		const std::string& region() const;

		void setPcode( const std::string& pcode_ );

		const std::string& pcode() const;

		void setCtry( const std::string& ctry_ );

		const std::string& ctry() const;

		bool isHome() const;

		bool isWork() const;
		
		bool isPostal() const;
		
		bool isParcel() const;
		
		bool isPref() const;

		bool isDom() const;

		bool isIntl() const;

		void setType( int type_ );
		
	private:
		gloox::VCard::Address* m_address;
	};
}

#endif // KL_VCARDEMAIL_H__
