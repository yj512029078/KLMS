%include <std_common.i>

%{
#include <list>
#include <stdexcept>
%}


namespace std {
    
    template<class T> class list {
      public:
        typedef size_t size_type;
        typedef T value_type;
        typedef const value_type& const_reference;
        list();
        //list(size_type n);
        size_type size() const;
        //size_type capacity() const;
        //void reserve(size_type n);
        %rename(isEmpty) empty;
        bool empty() const;
        void clear();
        %rename(add) push_back;
        void push_back(const value_type& x);
        
        %extend 
		{
			const_reference get(int index) throw (std::out_of_range) 
			{
				if (index>=0 && index<(int)$self->size())
				{
					std::list<T>::const_iterator it = (*self).begin();
					while(index--)
					{
						it++;
					}
					return (*it);
				}
				else
				{
					throw std::out_of_range("index");
				}
			}
		}
    };
    
   
}