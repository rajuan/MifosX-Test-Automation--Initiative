// ========================================================================
// $Id: ErrorPageHandler.java,v 1.9 2005/03/15 10:03:44 gregwilkins Exp $
// Copyright 1999-2004 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at 
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.browsermob.proxy.jetty.http.handler;

import org.browsermob.proxy.jetty.http.HttpException;
import org.browsermob.proxy.jetty.http.HttpFields;
import org.browsermob.proxy.jetty.http.HttpRequest;
import org.browsermob.proxy.jetty.http.HttpResponse;
import org.browsermob.proxy.jetty.util.ByteArrayISO8859Writer;
import org.browsermob.proxy.jetty.util.StringUtil;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;

// TODO: Auto-generated Javadoc
/* ------------------------------------------------------------ */
/**
 * Handler for Error pages A handler that is registered at the
 * org.mortbay.http.ErrorHandler context attributed and called by the
 * HttpResponse.sendError method to write a error page.
 * 
 * @version $Id: ErrorPageHandler.java,v 1.9 2005/03/15 10:03:44 gregwilkins Exp
 *          $
 * @author Greg Wilkins (gregw)
 */
public class ErrorPageHandler extends AbstractHttpHandler {
	/* ------------------------------------------------------------ */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.browsermob.proxy.jetty.http.HttpHandler#handle(java.lang.String,
	 * java.lang.String, org.browsermob.proxy.jetty.http.HttpRequest,
	 * org.browsermob.proxy.jetty.http.HttpResponse)
	 */
	public void handle(String pathInContext, String pathParams,
			HttpRequest request, HttpResponse response) throws HttpException,
			IOException {
		response.setContentType(HttpFields.__TextHtml);
		ByteArrayISO8859Writer writer = new ByteArrayISO8859Writer(2048);
		writeErrorPage(request, writer, response.getStatus(),
				response.getReason());
		writer.flush();
		response.setContentLength(writer.size());
		writer.writeTo(response.getOutputStream());
		writer.destroy();
	}

	/* ------------------------------------------------------------ */
	/**
	 * Write error page.
	 * 
	 * @param request
	 *            the request
	 * @param writer
	 *            the writer
	 * @param code
	 *            the code
	 * @param message
	 *            the message
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void writeErrorPage(HttpRequest request, Writer writer, int code,
			String message) throws IOException {
		if (message != null) {
			message = URLDecoder.decode(message, "UTF-8");
			message = StringUtil.replace(message, "<", "&lt;");
			message = StringUtil.replace(message, ">", "&gt;");
		}
		String uri = request.getPath();
		uri = StringUtil.replace(uri, "<", "&lt;");
		uri = StringUtil.replace(uri, ">", "&gt;");
		writer.write("<html>\n<head>\n<title>Error ");
		writer.write(Integer.toString(code));
		writer.write(' ');
		writer.write(message);
		writer.write("</title>\n</head>\n<body>\n<h2>HTTP ERROR: ");
		writer.write(Integer.toString(code));
		writer.write("</h2><pre>");
		writer.write(message);
		writer.write("</pre>\n");
		writer.write("<p>RequestURI=");
		writer.write(uri);
		writer.write("</p>\n<p><i><small><a href=\"http://jetty.jetty.org\">Powered by Jetty://</a></small></i></p>");
		for (int i = 0; i < 20; i++)
			writer.write("\n                                                ");
		writer.write("\n</body>\n</html>\n");
	}
}
