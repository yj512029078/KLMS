#ifndef KL_VCARDGEO_H__
#define KL_VCARDGEO_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardGeo
	{
	public:
		VCardGeo();

#ifndef SWIG // SWIG过滤以下接口

		VCardGeo( const gloox::VCard::Geo& geo_ );

#endif // SWIG

		virtual ~VCardGeo();

		void setLatitude( const std::string& latitude_ );
		
		const std::string& latitude() const;

		void setLongitude( const std::string& longitude_ );

		const std::string& longitude() const;

	private:
		gloox::VCard::Geo* m_geo;
	};
}

#endif // KL_VCARDGEO_H__
