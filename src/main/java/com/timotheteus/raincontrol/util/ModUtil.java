package com.timotheteus.raincontrol.util;

import java.util.logging.Logger;

public final class ModUtil {
	
	public static final String MOD_ID = "raincontrol";
    public static final String VERSION = "1.3";
    public static final String NAME = "Rain Control";
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private static final String PROXY_BASE = "com.timotheteus.raincontrol.proxy.";
    public static final String ClIENT_PROXY = PROXY_BASE + "ClientProxy";
    public static final String COMMON_PROXY = PROXY_BASE + "CommonProxy";
    public static final String SERVER_PROXY = PROXY_BASE + "ServerProxy";

}
