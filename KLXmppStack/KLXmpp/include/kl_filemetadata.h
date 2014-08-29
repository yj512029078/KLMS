#ifndef KL_FILEMETADATA_H__
#define KL_FILEMETADATA_H__

#include <string>

namespace kl
{
	/**
	 * 描述文件元数据。
	 */ 
	struct FileMetadata
	{
		/* 文件名 */
		std::string name;

		/* 文件大小，最大支持2G */
		long size;

		std::string hash;
									 
		std::string date; 

		/* 文件类型 */
		std::string mimetype;
		
		/* 文件描述*/
		std::string desc;
	};
}

#endif // KL_FILEMETADATA_H__