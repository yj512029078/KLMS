#ifndef KL_VCARDORG_H__
#define KL_VCARDORG_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardOrg
	{
	public:
		VCardOrg();

#ifndef SWIG // SWIG过滤以下接口

		VCardOrg( const gloox::VCard::Org& org_ );

#endif // SWIG

		virtual ~VCardOrg();

		void setName( const std::string& name_ );
		
		const std::string& name() const;

		void setUnits( const std::list<std::string>& units_ );

		const std::list<std::string>& units() const;

	private:
		gloox::VCard::Org* m_org;
	};
}

#endif // KL_VCARDORG_H__
