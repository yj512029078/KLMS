package com.neekle.kunlunandroid.xmpp.common;

import org.xmpp.myWRAP.Message;
import org.xmpp.myWRAP.klcppwrapJNI;
import org.xmpp.myWRAP.Message.MessageType;
import org.xmpp.myWRAP.Presence.PresenceType;

public class XmppConstants {

	public final static class XmpPresenceType {
		public final static int Available = PresenceType.Available.swigValue();
		public final static int Chatv = PresenceType.Chat.swigValue();
		public final static int Away = PresenceType.Away.swigValue();
		public final static int DND = PresenceType.DND.swigValue();
		public final static int XA = PresenceType.XA.swigValue();
		public final static int Unavailable = PresenceType.Unavailable
				.swigValue();
		public final static int Probe = PresenceType.Probe.swigValue();
		public final static int Error = PresenceType.Error.swigValue();
		public final static int Invalid = PresenceType.Invalid.swigValue();
	}

	public final static class MessageType {
		public final static int Chat = Message.MessageType.Chat.swigValue();
		public final static int Error = Message.MessageType.Error.swigValue();
		public final static int Groupchat = Message.MessageType.Groupchat
				.swigValue();
		public final static int Headline = Message.MessageType.Headline
				.swigValue();
		public final static int Normal = Message.MessageType.Normal.swigValue();
		public final static int Invalid = Message.MessageType.Invalid
				.swigValue();
	}
}
