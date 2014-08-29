%include <std_common.i>

%define SWIG_STD_LIST_MINIMUM_INTERNAL(CSINTERFACE, CONST_REFERENCE, CTYPE...)

%typemap(csinterfaces) std::list< CTYPE > "IDisposable, System.Collections.IEnumerable\n#if !SWIG_DOTNET_1\n    , System.Collections.Generic.CSINTERFACE<$typemap(cstype, CTYPE)>\n#endif\n";

%typemap(cscode) std::list< CTYPE > 
%{

//
/*
public $csclassname(System.Collections.ICollection c) : this() 
{
    if (c == null)
	{
      throw new ArgumentNullException("c");
	}
    foreach ($typemap(cstype, CTYPE) element in c) 
	{
      this.Add(element);
    }
}
*/

//
/*
public bool IsFixedSize 
{
	get { return false; }
}
*/

/* [override] ICollection<T> */
public bool IsReadOnly 
{
	get { return false; }
}

/* [override] IList<T> 以[index]方式获取index位置上的元素, 已测试 */
public $typemap(cstype, CTYPE) this[int index]  
{
	get
	{
		return getitem(index);
	}
	set 
	{
		setitem(index, value);
	}
}

//
/*
public int Capacity 
{
	get 
	{
		return (int)capacity();
	}
	set 
	{
		if (value < size())
		{
			throw new ArgumentOutOfRangeException("Capacity");
		}
		//reserve((uint)value);
	}
}
*/

/* [override] ICollection<T> 计算元素总数 */
public int Count 
{
	get 
	{
		return (int)size();
	}
}

/*
public bool IsSynchronized 
{
	get { return false; }
}
*/

/* [override] ICollection<T> */
#if SWIG_DOTNET_1
  public void CopyTo(System.Array array)
#else
  public void CopyTo($typemap(cstype, CTYPE)[] array)
#endif
{
	CopyTo(0, array, 0, this.Count);
}


#if SWIG_DOTNET_1
  public void CopyTo(System.Array array, int arrayIndex)
#else
  public void CopyTo($typemap(cstype, CTYPE)[] array, int arrayIndex)
#endif
{
	CopyTo(0, array, arrayIndex, this.Count);
}

#if SWIG_DOTNET_1
  public void CopyTo(int index, System.Array array, int arrayIndex, int count)
#else
  public void CopyTo(int index, $typemap(cstype, CTYPE)[] array, int arrayIndex, int count)
#endif
{
	if (array == null)
	{
		throw new ArgumentNullException("array");
	}
	if (index < 0)
	{
		throw new ArgumentOutOfRangeException("index", "Value is less than zero");
	}
    if (arrayIndex < 0)
	{
		throw new ArgumentOutOfRangeException("arrayIndex", "Value is less than zero");
	}
	if (count < 0)
	{
		throw new ArgumentOutOfRangeException("count", "Value is less than zero");
	}
	if (array.Rank > 1)
	{
		throw new ArgumentException("Multi dimensional array.", "array");
	}
    if (index+count > this.Count || arrayIndex+count > array.Length)
	{
		throw new ArgumentException("Number of elements to copy is too large.");
	}
    
	for (int i=0; i<count; i++)
	{
		array.SetValue(getitemcopy(index+i), arrayIndex+i);
	}
}

//
#if !SWIG_DOTNET_1
System.Collections.Generic.IEnumerator<$typemap(cstype, CTYPE)> System.Collections.Generic.IEnumerable<$typemap(cstype, CTYPE)>.GetEnumerator() 
{
return new $csclassnameEnumerator(this);
}
#endif

//
System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator() 
{
	return new $csclassnameEnumerator(this);
}

//
public $csclassnameEnumerator GetEnumerator() 
{
	return new $csclassnameEnumerator(this);
}

public sealed class $csclassnameEnumerator : System.Collections.IEnumerator
#if !SWIG_DOTNET_1
    , System.Collections.Generic.IEnumerator<$typemap(cstype, CTYPE)>
#endif
{

private $csclassname collectionRef;
private int currentIndex;
private object currentObject;
private int currentSize;

public $csclassnameEnumerator($csclassname collection) 
{
    collectionRef = collection;
    currentIndex = -1;
    currentObject = null;
    currentSize = collectionRef.Count;
}

public $typemap(cstype, CTYPE) Current 
{
    get 
	{
		if (currentIndex == -1)
		{
			throw new InvalidOperationException("Enumeration not started.");
		}
        
		if (currentIndex > currentSize - 1)
		{
			throw new InvalidOperationException("Enumeration finished.");
		}
       
		if (currentObject == null)
		{
			throw new InvalidOperationException("Collection modified.");
		}
        
		return ($typemap(cstype, CTYPE))currentObject;
    }
}

/* [override] IEnumerator */
object System.Collections.IEnumerator.Current 
{
    get 
	{
		return Current;
    }
}

/* [override] IEnumerator */
public bool MoveNext() 
{
    int size = collectionRef.Count;
    bool moveOkay = (currentIndex+1 < size) && (size == currentSize);
    if (moveOkay) 
	{
		currentIndex++;
		currentObject = collectionRef[currentIndex];
    } 
	else 
	{
		currentObject = null;
    }
    return moveOkay;
}

/* [override] IEnumerator */
public void Reset() 
{
    currentIndex = -1;
    currentObject = null;
    if (collectionRef.Count != currentSize)
	{
		throw new InvalidOperationException("Collection modified.");
    }
}

#if !SWIG_DOTNET_1
    public void Dispose() {
        currentIndex = -1;
        currentObject = null;
    }
#endif

}

%}


public:
	typedef size_t size_type;
    typedef CTYPE value_type;
    typedef CONST_REFERENCE const_reference;
    %rename(Clear) clear;
    void clear();
	// 添加一个元素, 已测试
    %rename(Add) push_back;
    void push_back(CTYPE const& x);
    size_type size() const;
    //size_type capacity() const;
    //void reserve(size_type n);
    %newobject GetRange(int index, int count);
    %newobject Repeat(CTYPE const& value, int count);
    list();
    list(const list &other);

	%extend 
	{
		//
		/* 
		list(int capacity) throw (std::out_of_range) 
		{
			std::list< CTYPE >* pv = 0;
			if (capacity >= 0) 
			{
				pv = new std::list< CTYPE >();
				pv->reserve(capacity);
			} 
			else {
				throw std::out_of_range("capacity");
			}
			return pv;
		}
		*/

		//
		
		CTYPE getitemcopy(int index) throw (std::out_of_range) 
		{
			if (index>=0 && index<(int)$self->size())
			{
				return (*$self).front()/*[index]*/;
			}
			else
			{
				throw std::out_of_range("index");
			}
		}
		

		//
		
		const_reference getitem(int index) throw (std::out_of_range) 
		{
			if (index>=0 && index<(int)$self->size())
			{
				std::list<CTYPE>::const_iterator it = (*self).begin();
				while(index--)
				{
					it++;
				}
				return (*it);
				//return (*$self)[index];
			}
			else
			{
				throw std::out_of_range("index");
			}
				
		}
		

		//
		
		void setitem(int index, CTYPE const& val) throw (std::out_of_range) 
		{
			if (index>=0 && index<(int)$self->size())
			{
				std::list<CTYPE>::iterator it = (*self).begin();
				while(index--)
				{
					it++;
				}
				(*it) = val;
			}
			else
			{
				throw std::out_of_range("index");
			}
			
		}
		

		// Takes a deep copy of the elements unlike ArrayList.AddRange
		void AddRange(const std::list< CTYPE >& values)
		{
			$self->insert($self->end(), values.begin(), values.end());
		}

		// Takes a deep copy of the elements unlike ArrayList.GetRange
		/*
		std::list< CTYPE > *GetRange(int index, int count) throw (std::out_of_range, std::invalid_argument) 
		{
			if (index < 0)
			{
				throw std::out_of_range("index");
			}
			if (count < 0)
			{
				throw std::out_of_range("count");
			}
			if (index >= (int)$self->size()+1 || index+count > (int)$self->size())
			{
				throw std::invalid_argument("invalid range");
			}
			return new std::list< CTYPE >($self->begin()+index, $self->begin()+index+count);
		}
		*/

		/* [override] IList<T> 在index位置插入元素, 已测试 */
		void Insert(int index, CTYPE const& x) throw (std::out_of_range) 
		{
			if (index>=0 && index<(int)self->size()+1)
			{
				std::list<CTYPE>::iterator it = self->begin();
				while(index--)
				{
					it++;
				}
				self->insert(it, x);
			}
			else
			{
				throw std::out_of_range("index");
			}
		}
		

		// Takes a deep copy of the elements unlike ArrayList.InsertRange
		/*
		void InsertRange(int index, const std::list< CTYPE >& values) throw (std::out_of_range) 
		{
			if (index>=0 && index<(int)$self->size()+1)
			{
				$self->insert($self->begin()+index, values.begin(), values.end());
			}
			else
			{
				throw std::out_of_range("index");
			}
			
		}
		*/

		/* 删除index位置上的元素, 已测试 */
		void RemoveAt(int index) throw (std::out_of_range) 
		{
			
			if (index>=0 && index<(int)$self->size())
			{
				std::list<CTYPE>::iterator it = (*self).begin();
				while(index--)
				{
					it++;
				}
				$self->erase(it);
			}
			else
			{
				throw std::out_of_range("index");
			}
			
		}
		

		/*
		void RemoveRange(int index, int count) throw (std::out_of_range, std::invalid_argument) 
		{
			if (index < 0)
			{
				throw std::out_of_range("index");
			}
			if (count < 0)
			{
				throw std::out_of_range("count");
			}
			if (index >= (int)$self->size()+1 || index+count > (int)$self->size())
			{
				throw std::invalid_argument("invalid range");
			}
			$self->erase($self->begin()+index, $self->begin()+index+count);
		}
		*/

		/*
		static std::list< CTYPE > *Repeat(CTYPE const& value, int count) throw (std::out_of_range) 
		{
			if (count < 0)
			{
				throw std::out_of_range("count");
			}
			return new std::list< CTYPE >(count, value);
		}
		*/

		void Reverse() {
			std::reverse($self->begin(), $self->end());
		}

		/*
		void Reverse(int index, int count) throw (std::out_of_range, std::invalid_argument) 
		{
			if (index < 0)
			{
				throw std::out_of_range("index");
			}
			if (count < 0)
			{
				throw std::out_of_range("count");
			}
		
			if (index >= (int)$self->size()+1 || index+count > (int)$self->size())
			{
				throw std::invalid_argument("invalid range");
			}
		
			std::reverse($self->begin()+index, $self->begin()+index+count);
		}
		*/

		// Takes a deep copy of the elements unlike ArrayList.SetRange
		/*
		void SetRange(int index, const std::list< CTYPE >& values) throw (std::out_of_range) 
		{
			if (index < 0)
			{
				throw std::out_of_range("index");
			}
		
			if (index+values.size() > $self->size())
			{
				throw std::out_of_range("index");
			}
		
			std::copy(values.begin(), values.end(), $self->begin()+index);
		}
		*/

	} // %extend
%enddef // %define SWIG_STD_LIST_MINIMUM_INTERNAL(CSINTERFACE, CONST_REFERENCE, CTYPE...)


%define SWIG_STD_LIST_EXTRA_OP_EQUALS_EQUALS(CTYPE...)
%extend 
{
	bool Contains(CTYPE const& value) 
	{
		return std::find($self->begin(), $self->end(), value) != $self->end();
	}

	/* [override] IList<T> 获取指定值的元素位置 已测试 */
	int IndexOf(CTYPE const& value) 
	{
		int index = -1;
		std::list< CTYPE >::const_iterator it = $self->begin();
		while (it != $self->end())
		{
			index++;
			if ( value == (*it) ) // 必须重载==操作符才能生效
			{
				break;
			}
			it++;
		}
		return index;
	}
	

	/*
	int LastIndexOf(CTYPE const& value) 
	{
		int index = -1;
		std::list< CTYPE >::reverse_iterator rit = std::find($self->rbegin(), $self->rend(), value);
		if (rit != $self->rend())
		{
			index = (int)($self->rend() - 1 - rit);
		}
		return index;
	}
	*/

	bool Remove(CTYPE const& value) 
	{
		std::list< CTYPE >::iterator it = std::find($self->begin(), $self->end(), value);
		if (it != $self->end()) 
		{
			$self->erase(it);
			return true;
		}
		return false;
	}

} // %extend
%enddef // %define SWIG_STD_VECTOR_EXTRA_OP_EQUALS_EQUALS(CTYPE...)

%define SWIG_STD_LIST_ENHANCED(CTYPE...)
namespace std {
  template<> class list< CTYPE > {
    SWIG_STD_LIST_MINIMUM_INTERNAL(IList, %arg(CTYPE const&), %arg(CTYPE))
    SWIG_STD_LIST_EXTRA_OP_EQUALS_EQUALS(CTYPE)
  };
}
%enddef

%define SWIG_STD_LIST_SPECIALIZE(CSTYPE, CTYPE...)
#warning SWIG_STD_LIST_SPECIALIZE macro deprecated, please see csharp/std_list.i and switch to SWIG_STD_LIST_ENHANCED
SWIG_STD_LIST_ENHANCED(CTYPE)
%enddef

%define SWIG_STD_LIST_SPECIALIZE_MINIMUM(CSTYPE, CTYPE...)
#warning SWIG_STD_LIST_SPECIALIZE_MINIMUM macro deprecated, it is no longer required
%enddef

%{
#include <list>
#include <algorithm>
#include <stdexcept>
%}

%csmethodmodifiers std::list::getitemcopy "private"
%csmethodmodifiers std::list::getitem "private"
%csmethodmodifiers std::list::setitem "private"
%csmethodmodifiers std::list::size "private"
//%csmethodmodifiers std::list::capacity "private"
//%csmethodmodifiers std::list::reserve "private"

namespace std {
  template<class T> class list {
    SWIG_STD_LIST_MINIMUM_INTERNAL(IEnumerable, T const&, T)
  };
  template<class T> class list<T *> {
    SWIG_STD_LIST_MINIMUM_INTERNAL(IList, T *const&, T *)
    SWIG_STD_LIST_EXTRA_OP_EQUALS_EQUALS(T *)
  };
  // bool is specialized in the C++ standard - const_reference in particular
  template<> class list<bool> {
    SWIG_STD_LIST_MINIMUM_INTERNAL(IList, bool, bool)
    SWIG_STD_LIST_EXTRA_OP_EQUALS_EQUALS(bool)
  };
}

SWIG_STD_LIST_ENHANCED(char)
SWIG_STD_LIST_ENHANCED(signed char)
SWIG_STD_LIST_ENHANCED(unsigned char)
SWIG_STD_LIST_ENHANCED(short)
SWIG_STD_LIST_ENHANCED(unsigned short)
SWIG_STD_LIST_ENHANCED(int)
SWIG_STD_LIST_ENHANCED(unsigned int)
SWIG_STD_LIST_ENHANCED(long)
SWIG_STD_LIST_ENHANCED(unsigned long)
SWIG_STD_LIST_ENHANCED(long long)
SWIG_STD_LIST_ENHANCED(unsigned long long)
SWIG_STD_LIST_ENHANCED(float)
SWIG_STD_LIST_ENHANCED(double)
SWIG_STD_LIST_ENHANCED(std::string)


