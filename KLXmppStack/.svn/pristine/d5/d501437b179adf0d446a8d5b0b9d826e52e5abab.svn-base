#ifndef KL_GEOLOC_H__
#define KL_GEOLOC_H__

#include "kl_common.h"
#include "kl_convert.h"
#include "kl_util.h"
#include "../../gloox/src/tag.h"

namespace kl
{
	/**
	 * @brief 地理位置信息实体。
	 *
	 * @author JI Yixuan
	 */
	class KLXMPP_API Geoloc
	{	
	public:
		Geoloc();
#ifndef SWIG // SWIG过滤以下接口
		Geoloc( const gloox::Tag* tag_ );
#endif // end #ifndef SWIG
		virtual ~Geoloc() {}

		void setAccuracy( double accuracy_ ) { m_accuracy = accuracy_; }

		double accuracy() { return m_accuracy; }

		void setAlt( double alt_ ) { m_alt = alt_; }

		double alt() { return m_alt; }

		void setArea( const std::string& area_ ) { m_area = area_; }

		const std::string& area() const { return m_area; }

		void setBearing( double bearing_ ) { m_bearing = bearing_; }

		double bearing() { return m_bearing; }

		void setBuilding( const std::string& building_ ) { m_building = building_; }

		const std::string& building() const { return m_building; }

		void setCountry( const std::string& country_ ) { m_country = country_; }

		const std::string& country() const { return m_country; }

		void setCountrycode( const std::string& countrycode_ ) { m_countrycode = countrycode_; }

		const std::string& countrycode() const { return m_countrycode; }

		void setDatum( const std::string& datum_ ) { m_datum = datum_; }

		const std::string& datum() const { return m_datum; }

		void setDescription( const std::string& description_ ) { m_description = description_; }

		const std::string& description() const { return m_description; }

		void setError( double error_ ) { m_error = error_; }

		double error() { return m_error; }

		void setFloor( const std::string& floor_ ) { m_floor = floor_; }

		const std::string& floor() const { return m_floor; }	

		void setLat( double lat_ ) { m_lat = lat_; }

		double lat() { return m_lat; }
	
		void setLocality( const std::string& locality_ ) { m_locality = locality_; }

		const std::string& locality() const { return m_locality; }

		void setLon( double lon_ ) { m_lon = lon_; }

		double lon() { return m_lon; }

		void setPostalcode( const std::string& postalcode_ ) { m_postalcode = postalcode_; }

		const std::string& postalcode() const { return m_postalcode; }
	
		void setRegion( const std::string& region_ ) { m_region = region_; }

		const std::string& region() const { return m_region; }

		void setRoom( const std::string& room_ ) { m_room = room_; }

		const std::string& room() const { return m_room; }

		void setSpeed( double speed_ ) { m_speed = speed_; }

		double speed() { return m_speed; }
	
		void setStreet( const std::string& street_ ) { m_street = street_; }

		const std::string& street() const { return m_street; }

		void setText( const std::string& text_ ) { m_text = text_; }

		const std::string& text() const { return m_text; }

		void setTimestamp( const std::string& timestamp_ ) { m_timestamp = timestamp_; }

		const std::string& timestamp() const { return m_timestamp; }

		void setTzo( const std::string& tzo_ ) { m_tzo = tzo_; }

		const std::string& tzo() const { return m_tzo; }

		void setUri( const std::string& uri_ ) { m_uri = uri_; }

		const std::string& uri() const { return m_uri; }
		
#ifndef SWIG // SWIG过滤以下接口
		gloox::Tag* newTag() const;
#endif // end #ifndef SWIG
	
	private:
		
		/**
		 * 精度
		 * Horizontal GPS error in meters; this element obsoletes the <error/> element, e.g. 10
		 */
		double m_accuracy;

		/**
		 * 海拔
		 * Altitude in meters above or below sea level, e.g. 1609
		 */
		double m_alt;

		/**
		 * 地区
		 * A named area such as a campus or neighborhood, e.g. Central Park
		 */
		std::string m_area;

		/**
		 * GPS bearing (direction in which the entity is heading to reach its next waypoint), measured in decimal degrees relative to true north
		 */
		double m_bearing;

		/**
		 * 建筑
		 * A specific building on a street or in an area, e.g. The Empire State Building
		 */
		std::string m_building;
		
		/**
		 * 国家
		 * The nation where the user is located, e.g. United States
		 */
		std::string m_country;

		/**
		 * 国家代码
		 * The ISO 3166 two-letter country code, e.g. US
		 */
		std::string m_countrycode;

		/**
		 * GPS datum
		 */
		std::string m_datum;
		
		/**
		 * 描述
		 * A natural-language name for or description of the location, e.g. Bill's house
		 */
		std::string m_description;

		/**
		 * Horizontal GPS error in arc minutes; this element is deprecated in favor of <accuracy/>, e.g. 290.8882087
		 */
		double m_error;

		/**
		 * A particular floor in a building, e.g. 102
		 */
		std::string m_floor;
		
		/**
		 * 纬度
		 * Latitude in decimal degrees North, e.g. 39.75
		 */
		double m_lat;

		/**
		 * 地点
		 * A locality within the administrative region, such as a town or city, e.g. New York City
		 */
		std::string m_locality;

		/**
		 * 经度
		 * Longitude in decimal degrees East, e.g. -104.99
		 */
		double m_lon;
		
		/**
		 * A code used for postal delivery, e.g. 10118
		 */
		std::string m_postalcode;
		
		/**
		 * 省份
		 * An administrative region of the nation, such as a state or province, e.g. New York
		 */
		std::string m_region;
		
		/**
		 * 房间
		 * A particular room in a building, e.g. Observatory
		 */		
		std::string m_room;
		
		/**
		 * The speed at which the entity is moving, in meters per second
		 */	
		double m_speed;
		
		/**
		 * 房间
		 * A particular room in a building, e.g. Observatory
		 */	
		std::string m_street;
		
		/**
		 * A thoroughfare within the locality, or a crossing of two thoroughfares, e.g. 350 Fifth Avenue / 34th and Broadway
		 */	
		std::string m_text;
		
		/**
		 * 时间戳
		 * UTC timestamp specifying the moment when the reading was taken (MUST conform to the DateTime profile of XMPP Date and Time Profiles (XEP-0082)), e.g. 2004-02-19T21:12Z
		 */	
		std::string m_timestamp;
		
		/**
		 * The time zone offset from UTC for the current location (MUST adhere to the Time Zone Definition (TZD) specified in XMPP Date and Time Profiles (XEP-0082)), e.g. -07:00
		 */	
		std::string m_tzo;
		
		/**
		 * A URI or URL pointing to information about the location
		 */	
		std::string m_uri;
	};
}

#endif // KL_GEOLOCINFO_H__