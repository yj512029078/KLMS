#include "../include/kl_geoloc.h"

namespace kl
{
	const char* TAG_NAME_GEOLOC      = "geoloc";
	const char* TAG_NAME_ACCURACY    = "accuracy";
	const char* TAG_NAME_ALT         = "alt";
	const char* TAG_NAME_AREA        = "area";
	const char* TAG_NAME_BEARING     = "bearing";
	const char* TAG_NAME_BUILDING    = "building";
	const char* TAG_NAME_COUNTRY     = "country";
	const char* TAG_NAME_COUNTRYCODE = "countrycode";
	const char* TAG_NAME_DATUM       = "datum";
	const char* TAG_NAME_DESCRIPTION = "description";
	const char* TAG_NAME_ERROR       = "error";
	const char* TAG_NAME_FLOOR       = "floor";
	const char* TAG_NAME_LAT         = "lat";
	const char* TAG_NAME_LOCALITY    = "locality";
	const char* TAG_NAME_LON         = "lon";
	const char* TAG_NAME_POSTALCODE  = "postalcode";
	const char* TAG_NAME_REGION      = "region";
	const char* TAG_NAME_ROOM        = "room";
	const char* TAG_NAME_SPEED       = "speed";
	const char* TAG_NAME_STREET      = "street";
	const char* TAG_NAME_TEXT        = "text";
	const char* TAG_NAME_TIMESTAMP   = "timestamp";
	const char* TAG_NAME_TZO         = "tzo";
	const char* TAG_NAME_URI         = "uri";

	Geoloc::Geoloc()
		: m_accuracy( 0 ),
		  m_alt( 0 ), 
		  m_bearing( 0 ),
		  m_error( 0 ),
		  m_lat( 0 ), 
		  m_lon( 0 ), 
		  m_speed( 0 )
	{
	}

	Geoloc::Geoloc( const gloox::Tag* tag_ )
		: m_accuracy( 0 ),
		  m_alt( 0 ), 
		  m_bearing( 0 ),
		  m_error( 0 ),
		  m_lat( 0 ), 
		  m_lon( 0 ), 
		  m_speed( 0 )
	{
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_ACCURACY, &m_accuracy );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_ALT, &m_alt );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_AREA, m_area );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_BEARING, &m_bearing );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_BUILDING, m_building );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_COUNTRY, m_country );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_COUNTRYCODE, m_countrycode );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_DATUM, m_datum );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_DESCRIPTION, m_description );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_ERROR, &m_error );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_FLOOR, m_floor );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_LAT, &m_lat );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_LOCALITY, m_locality );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_LON, &m_lon );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_POSTALCODE, m_postalcode );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_REGION, m_region );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_ROOM, m_room );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_SPEED, &m_speed );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_STREET, m_street );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_TEXT, m_text );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_TIMESTAMP, m_timestamp );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_TZO, m_tzo );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_URI, m_uri );
	}

	gloox::Tag* Geoloc::newTag() const
	{
		gloox::Tag* tag_geoloc = new gloox::Tag( kl::TAG_NAME_GEOLOC );
		tag_geoloc->setXmlns( kl::XMLNS_GEOLOC );
		
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_ACCURACY, 0 == m_accuracy ? "" : double2string( m_accuracy ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_ACCURACY, 0 == m_alt ? "" : double2string( m_alt ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_AREA, m_area );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_BEARING, 0 == m_bearing ? "" : double2string( m_bearing ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_BUILDING, m_building );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_COUNTRY, m_country );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_COUNTRYCODE, m_countrycode );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_DATUM, m_datum );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_DESCRIPTION, m_description );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_ERROR, 0 == m_error ? "" : double2string( m_error ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_FLOOR, m_floor );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_LAT, 0 == m_lat ? "" : double2string( m_lat ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_LOCALITY, m_locality );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_LON, 0 == m_lon ? "" : double2string( m_lon ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_POSTALCODE, m_postalcode );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_REGION, m_region );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_ROOM, m_room );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_SPEED, 0 == m_speed ? "" : double2string( m_speed ) );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_STREET, m_street );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_TEXT, m_text );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_TIMESTAMP, m_timestamp );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_TZO, m_tzo );
		kl::util::TagUtil::insertField( tag_geoloc, kl::TAG_NAME_URI, m_uri );
		
		return tag_geoloc;
	}
}