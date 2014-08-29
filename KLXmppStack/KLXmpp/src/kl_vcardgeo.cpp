#include "../include/kl_vcardgeo.h"

namespace kl
{
	VCardGeo::VCardGeo()
	{
		m_geo = new gloox::VCard::Geo();
	}

	VCardGeo::VCardGeo( const gloox::VCard::Geo& geo_ )
	{
		m_geo = new gloox::VCard::Geo();

		m_geo->latitude = geo_.latitude;
		m_geo->longitude = geo_.longitude;
	}

	VCardGeo::~VCardGeo()
	{
		delete m_geo;
	}	

	void VCardGeo::setLatitude( const std::string& latitude_ )
	{
		m_geo->latitude = latitude_;
	}

	const std::string& VCardGeo::latitude() const
	{
		return m_geo->latitude;
	}

	void VCardGeo::setLongitude( const std::string& longitude_ )
	{
		m_geo->longitude = longitude_;
	}

	const std::string& VCardGeo::longitude() const
	{
		return m_geo->longitude;
	}
}