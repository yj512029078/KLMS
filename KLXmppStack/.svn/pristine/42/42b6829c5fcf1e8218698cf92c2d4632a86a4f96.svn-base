%include "../../swig/Lib/csharp/std_wstring.i"
%include "../../swig/Lib/csharp/std_map.i"
%include "kl_std_string.i"
%include "kl_std_list.i"

// Mapping void* as byte[]
%typemap(ctype) void * "void *"
%typemap(imtype) void * "byte[]"
%typemap(cstype) void * "byte[]"
%typemap(csin) void * "$csinput"
%typemap(csout) void * { return $imcall; }
%typemap(in) void * %{ $1 = $input; %}
%typemap(out) void * %{ $result = $1; %}
%typemap(csdirectorin) void * "$iminput"


%typemap(cscode) kl::BytestreamData %{
  public byte[] GetBytes() {
    uint clen = this.length();
    if(clen>0) {
        byte[] bytes = new byte[clen];
        this.copy(bytes, clen);
        return bytes;
    }
    return null;
  }
%}

%include "../common/klcppwrap.i"

