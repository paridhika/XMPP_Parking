/**
 * $RCSfile$
 * $Revision: 1583 $
 * $Date: 2005-07-03 17:55:39 -0300 (Sun, 03 Jul 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.openfire.spi;

import org.jivesoftware.openfire.ConnectionManager;
import org.jivesoftware.openfire.ServerPort;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.XMPPServerInfo;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Version;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Implements the server info for a basic server. Optimization opportunities
 * in reusing this object the data is relatively static.
 *
 * @author Iain Shigeoka
 */
public class XMPPServerInfoImpl implements XMPPServerInfo {

    private Date startDate;
    private String xmppDomain;
    private String hostname;
    private Version ver;
    private ConnectionManager connectionManager;

    /**
     * Simple constructor
     *
     * @param xmppDomain the server's XMPP domain name (e.g. example.org).
     * @param hostname the server's host name (e.g. server1.example.org).
     * @param version the server's version number.
     * @param startDate the server's last start time (can be null indicating
     *      it hasn't been started).
     */
    public XMPPServerInfoImpl(String xmppDomain, String hostname, Version version, Date startDate) {
        this.xmppDomain = xmppDomain;
        this.hostname = hostname;
        this.ver = version;
        this.startDate = startDate;
    }

    @Override
    public Version getVersion() {
        return ver;
    }

    @Override
    public String getHostname()
	{
		return hostname;
	}

	@Override
    public String getXMPPDomain()
	{
		return xmppDomain;
	}

	@Override
    public void setXMPPDomain(String domainName)
	{
        this.xmppDomain = domainName;
        if (domainName == null) { 
            JiveGlobals.deleteProperty("xmpp.domain");
        }
        else {
            JiveGlobals.setProperty("xmpp.domain", domainName);
        }
    }

    @Override
    public Date getLastStarted() {
        return startDate;
    }

    @Override
    public Collection<ServerPort> getServerPorts() {
        if (connectionManager == null) {
        	connectionManager = XMPPServer.getInstance().getConnectionManager();
        }
        return connectionManager == null ?
        		Collections.<ServerPort>emptyList() :
        		connectionManager.getPorts();
    }
}