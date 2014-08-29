#include "../include/kl_util.h"

namespace kl
{
	namespace util
	{
		void TagUtil::insertField( gloox::Tag* tag_, const char* field_, const std::string& var_ )
		{
			if( field_ && !var_.empty() )
			{
				new gloox::Tag( tag_, field_, var_ );
			}
		}

		void TagUtil::checkField( const gloox::Tag* tag_, const char* field_, std::string& var_ )
		{
			if( field_ )
			{
				gloox::Tag* child = tag_->findChild( field_ );
				if( child )
				{
					var_ = child->cdata();
				}
			}
		}

		void TagUtil::checkField( const gloox::Tag* tag_, const char* field_, int* var_ )
		{
			if( field_ )
			{
				gloox::Tag* child = tag_->findChild( field_ );
				if( child )
				{
					(*var_) = atoi( child->cdata().c_str() );
				}
			}
		}

		void TagUtil::checkField( const gloox::Tag* tag_, const char* field_, double* var_ )
		{
			if( field_ )
			{
				gloox::Tag* child = tag_->findChild( field_ );
				if( child )
				{
					(*var_) = atof( child->cdata().c_str() );
				}
			}
		}
	}
}