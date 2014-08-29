%include "../../swig/Lib/java/std_string.i"
//%include "../../swig/Lib/java/std_map.i"
%include "../../swig/Lib/java/std_vector.i"
%include "kl_std_map.i"
%include "kl_std_list.i"

// Mapping void* as byte[]
%typemap(jni) void * "void *"
%typemap(jtype) void * "byte[]"
%typemap(jstype) void * "byte[]"
%typemap(javain) void * "$javainput"

%include "../common/klcppwrap.i"