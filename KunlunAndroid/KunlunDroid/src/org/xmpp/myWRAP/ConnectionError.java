/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xmpp.myWRAP;

public final class ConnectionError {
  public final static ConnectionError ConnNoError = new ConnectionError("ConnNoError");
  public final static ConnectionError ConnStreamError = new ConnectionError("ConnStreamError");
  public final static ConnectionError ConnStreamVersionError = new ConnectionError("ConnStreamVersionError");
  public final static ConnectionError ConnStreamClosed = new ConnectionError("ConnStreamClosed");
  public final static ConnectionError ConnProxyAuthRequired = new ConnectionError("ConnProxyAuthRequired");
  public final static ConnectionError ConnProxyAuthFailed = new ConnectionError("ConnProxyAuthFailed");
  public final static ConnectionError ConnProxyNoSupportedAuth = new ConnectionError("ConnProxyNoSupportedAuth");
  public final static ConnectionError ConnIoError = new ConnectionError("ConnIoError");
  public final static ConnectionError ConnParseError = new ConnectionError("ConnParseError");
  public final static ConnectionError ConnConnectionRefused = new ConnectionError("ConnConnectionRefused");
  public final static ConnectionError ConnDnsError = new ConnectionError("ConnDnsError");
  public final static ConnectionError ConnOutOfMemory = new ConnectionError("ConnOutOfMemory");
  public final static ConnectionError ConnNoSupportedAuth = new ConnectionError("ConnNoSupportedAuth");
  public final static ConnectionError ConnTlsFailed = new ConnectionError("ConnTlsFailed");
  public final static ConnectionError ConnTlsNotAvailable = new ConnectionError("ConnTlsNotAvailable");
  public final static ConnectionError ConnCompressionFailed = new ConnectionError("ConnCompressionFailed");
  public final static ConnectionError ConnAuthenticationFailed = new ConnectionError("ConnAuthenticationFailed");
  public final static ConnectionError ConnUserDisconnected = new ConnectionError("ConnUserDisconnected");
  public final static ConnectionError ConnNotConnected = new ConnectionError("ConnNotConnected");

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static ConnectionError swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + ConnectionError.class + " with value " + swigValue);
  }

  private ConnectionError(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private ConnectionError(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private ConnectionError(String swigName, ConnectionError swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static ConnectionError[] swigValues = { ConnNoError, ConnStreamError, ConnStreamVersionError, ConnStreamClosed, ConnProxyAuthRequired, ConnProxyAuthFailed, ConnProxyNoSupportedAuth, ConnIoError, ConnParseError, ConnConnectionRefused, ConnDnsError, ConnOutOfMemory, ConnNoSupportedAuth, ConnTlsFailed, ConnTlsNotAvailable, ConnCompressionFailed, ConnAuthenticationFailed, ConnUserDisconnected, ConnNotConnected };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}
