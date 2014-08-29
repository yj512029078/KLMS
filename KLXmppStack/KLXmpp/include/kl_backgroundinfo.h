#ifndef KL_BACKGROUNDINFO_H__
#define KL_BACKGROUNDINFO_H__

#include "kl_common.h"
#include "kl_convert.h"
#include "kl_util.h"
#include "../../gloox/src/tag.h"

namespace kl
{
	/**
	 * @brief 背景信息实体。
	 *
	 * @author JI Yixuan
	 */
	class KLXMPP_API BackgroundInfo
	{
	public:
		BackgroundInfo();

#ifndef SWIG // SWIG过滤以下接口
		BackgroundInfo( const gloox::Tag* tag_ );
#endif // end #ifndef SWIG
		
		virtual ~BackgroundInfo() {}

		void setId( const std::string& id_ ) { m_id = id_; }

		const std::string& id() const { return m_id; }

		void setType( const std::string& type_ ) { m_type = type_; }

		const std::string& type() const { return m_type; }

		void setUrl( const std::string& url_ ) { m_url = url_; }

		const std::string& url() const { return m_url; }

		void setBytes( int bytes_ ) { m_bytes = bytes_; }

		int bytes() { return m_bytes; }

		void setHeight( int height_ ) { m_height = height_; }

		int height() { return m_height; }

		void setWidth( int width_ ) { m_width = width_; }

		int width() { return m_width; }

#ifndef SWIG // SWIG过滤以下接口
		gloox::Tag* newTag() const;
#endif // end #ifndef SWIG

	private:
		std::string m_id;
		std::string m_type;
		std::string m_url;
		int m_bytes;
		int m_height;
		int m_width;
	};
}

#endif // KL_BACKGROUNDINFO_H__
