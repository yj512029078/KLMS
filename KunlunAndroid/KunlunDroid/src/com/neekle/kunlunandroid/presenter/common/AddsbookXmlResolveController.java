package com.neekle.kunlunandroid.presenter.common;

import java.util.List;

public class AddsbookXmlResolveController {

	public static class ResolveType {
		public static final int DEPTH_LEVEL_RESOLVE = 0;
		public static final int CONTENT_RESOLVE = 1;
	}

	public static List<?> parse(String xml, int xmlType, String rootIdName,
			String rootIdValue, int resolveType) throws Exception {
		List<?> list = null;

		switch (resolveType) {
		case ResolveType.DEPTH_LEVEL_RESOLVE: {
			list = AddsbookXmlLevelResolver.parse(xml, xmlType, rootIdName,
					rootIdValue);
			break;
		}
		case ResolveType.CONTENT_RESOLVE: {
			list = AddsbookXmlContentResolver.parse(xml, xmlType);
			break;
		}
		default:
			break;
		}

		return list;
	}
}
