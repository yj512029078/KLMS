#ifndef KL_MICROBLOG_H__
#define KL_MICROBLOG_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/pubsubevent.h"
#include <string>

namespace kl
{
	/**
	 * @brief 微博实体。
	 *
	 * @author JI Yixuan
	 */
	class KLXMPP_API Microblog
	{
	public:
		enum Type
		{
			Text,           /**< 纯文本。 */
			Xhtml           /**< 富文本。 */
		};

		/**
		 * 构造函数。
		 * 
		 * @param id_ 微博ID。
		 */
		Microblog( const std::string& id_ );

#ifndef SWIG // SWIG过滤以下接口
		Microblog( const gloox::PubSub::Event* event_ );
#endif // end #ifndef SWIG

		virtual ~Microblog();

		/**
		 * 返回微博ID。
		 */
		const std::string id() const { return m_id; }

		/**
		 * 返回微博作者。
		 */
		const std::string author() const { return m_author; }
		
		/**
		 * 返回微博类型。
		 */
		Type type() const { return m_type; }
		
		/**
		 * 返回微博内容。
		 */
		const std::string content() const { return m_content; }
		
		/**
		 * 返回微博发布时间。
		 */
		const std::string published() const { return m_published; }
		
		/**
		 * 返回微博发布地理位置。
		 */
		const std::string geoloc() const { return m_geoloc; }
		
		/**
		 * 返回微博发布设备。
		 */
		const std::string device() const { return m_device; }
		
		/**
		 * 返回微博评论链接。
		 */
		const std::string commentLink() const { return m_commentLink; }
		
		/**
		 * 设置微博作者。
		 */
		void setAuthor( const std::string& author_ ) { m_author = author_; } 
		
		/**
		 * 设置微博类型。
		 */
		void setType( Microblog::Type type_ ) { m_type = type_; }
		
		/**
		 * 设置微博内容。
		 */
		void setContent( const std::string& content_ ) { m_content = content_; }
		
		/**
		 * 设置微博发布时间。
		 */
		void setPublished( const std::string& published_ ) { m_published = published_; }

		/**
		 * 设置微博发布地理位置。
		 */
		void setGeoloc( const std::string& geoloc_ ) { m_geoloc = geoloc_; }

		/**
		 * 设置微博发布设备。
		 */
		void setDevice( const std::string& device_ ) { m_device = device_; }

		/**
		 * 设置微博评论链接。
		 */
		void setCommentLink( const std::string& commentLink_ ) { m_commentLink = commentLink_; }

	private:
		// ID
		std::string m_id;
		// 作者
		std::string m_author;
		// 内容类型
		Type m_type;
		// 内容
		std::string m_content;
		// 发布时间
		std::string m_published;
		// 地理位置
		std::string m_geoloc;
		// 设备
		std::string m_device;
		// 评论链接
		std::string m_commentLink;
	};
}

#endif // KL_MICROBLOG_H__