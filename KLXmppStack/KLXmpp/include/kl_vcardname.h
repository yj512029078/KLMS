#ifndef KL_VCARDNAME_H__
#define KL_VCARDNAME_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardName
	{
	public:
		VCardName();

#ifndef SWIG // SWIG过滤以下接口

		VCardName( const gloox::VCard::Name& name_ );

#endif // SWIG

		virtual ~VCardName();
		
		// getter
		// 姓氏
		const std::string& family() const;
		// 名字
		const std::string& given() const;

		const std::string& middle() const;

		const std::string& prefix() const;

		const std::string& suffix() const;

		// setter
		void setFamily( const std::string& family_ );

		void setGiven( const std::string& given_ );

		void setMiddle( const std::string& middle_ );

		void setPrefix( const std::string& prefix_ );

		void setSuffix( const std::string& suffix_ );
		
	private:
		gloox::VCard::Name* m_name;
	};
}

#endif // KL_VCARDNAME_H__
