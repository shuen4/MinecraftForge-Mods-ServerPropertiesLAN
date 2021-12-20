package com.shuen.splan;

import com.google.common.io.Files;
import com.mojang.brigadier.CommandDispatcher;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveFormat.LevelSave;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("splan")
public class splan {
	/** server port */
	public int port = 0;
	public boolean bisNetherEnabled=true;
	public boolean bisCommandBlockEnabled=true;
	public boolean brepliesToStatus=true;
	public boolean bisResourcePackRequired=false;
	public boolean bhidesOnlinePlayers=false;
	/** Property manager */
	public PropertyManagerClient ServerProperties = null;
	/** Log4j logger */
	public static final Logger LOGGER = LogManager.getLogger();
	/** Global variable */
	public ServerWrapper server;
	/** Only first player joined game send server info */
	public boolean sent = false;
	/** If the server started with resource pack, close the first GuiYesNo (1.13) / ConfirmScreen (1.14 - 1.16) */
	private boolean GuiEventDisabled = true;
	/** null if server running this mod */
	public static splan instance = null;
	/** mod initialization */
	public splan() {
		if (!ClassExist("net.minecraft.client.Minecraft")) {
			LOGGER.error("Refuse to work on server");
			return;
		}
		instance = this;
		MinecraftForge.EVENT_BUS.addListener(this::SendMessageToPlayer);
		if (ClassExist("net.minecraftforge.fml.event.server.FMLServerStartingEvent"))//1.13 - 1.16
			MinecraftForge.EVENT_BUS.addListener(this::onServerStarting1);
		else if (ClassExist("net.minecraftforge.fmlserverevents.FMLServerStartingEvent"))//1.17
			MinecraftForge.EVENT_BUS.addListener(this::onServerStarting2);
		else if (ClassExist("net.minecraftforge.event.server.ServerStartingEvent"))//1.18
			MinecraftForge.EVENT_BUS.addListener(this::onServerStarting3);
		else
			LOGGER.error("Register Server Starting Event failed");
		if (ClassExist("net.minecraftforge.fml.event.server.FMLServerStoppedEvent"))//1.13 - 1.16
			MinecraftForge.EVENT_BUS.addListener(this::onServerStopped1);
		else if (ClassExist("net.minecraftforge.fmlserverevents.FMLServerStoppedEvent"))//1.17
			MinecraftForge.EVENT_BUS.addListener(this::onServerStopped2);
		else if (ClassExist("net.minecraftforge.event.server.ServerStoppedEvent"))//1.18
			MinecraftForge.EVENT_BUS.addListener(this::onServerStopped3);
		else
			LOGGER.error("Register Server Stoped Event failed");
		/** net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent = 1.13 - 1.17 */
		if(ClassExist("net.minecraftforge.fml.event.server.FMLServerStartingEvent"))//1.13 - 1.16
			MinecraftForge.EVENT_BUS.addListener(this::onGuiInit);
	}
	
	public void onGuiInit(GuiScreenEvent event) {
		if (server==null||GuiEventDisabled)
			return;
		if (ClassExist("net.minecraft.client.gui.GuiYesNo")) {//1.13
			try {
				net.minecraft.client.gui.GuiScreen gui=(net.minecraft.client.gui.GuiScreen) getField(GuiScreenEvent.class,"gui").get(event);
				/** should be the resource pack dialog */
				if (gui instanceof net.minecraft.client.gui.GuiYesNo) {
					LOGGER.info("closing GuiYesNo");
					GuiEventDisabled=true;
					gui.close();
				}
			} catch (Exception e) {
				LOGGER.error("Error parse Gui",e);
			}
		}
		else {//1.14 - 1.16
			try {
				net.minecraft.client.gui.screen.Screen gui=(net.minecraft.client.gui.screen.Screen) getField(GuiScreenEvent.class,"gui").get(event);
				/** should be the resource pack dialog */
				if (gui instanceof net.minecraft.client.gui.screen.ConfirmScreen) {
					LOGGER.info("closing ConfirmScreen");
					GuiEventDisabled=true;
					try {//1.14 - 1.15
						gui.onClose();
					} catch (Error E1) {
						try{//1.16
							gui.func_231175_as__();
						} catch (Error E2) {
							LOGGER.error("Error Closing ConfirmScreen");
							LOGGER.error("",E1);
							LOGGER.error("",E2);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error parse Gui",e);
			}
		}
	}
	/** Send server status to first player joined game */
	public void SendMessageToPlayer(EntityJoinWorldEvent event) {
		if (server==null)
			return;
		if (sent)
			return;
		if (ClassExist("net.minecraft.util.text.ITextComponent"))//1.13 - 1.16
			EntityJoinWorldEvent1.onEvent(event,server);
		else//1.17 - 1.18
			EntityJoinWorldEvent2.onEvent(event,server);
	}
	//1.13 - 1.16
	public void onServerStopped1(net.minecraftforge.fml.event.server.FMLServerStoppedEvent event) {
		onServerStopped(event);
	}
	// 1.17
	public void onServerStopped2(net.minecraftforge.fmlserverevents.FMLServerStoppedEvent event) {
		onServerStopped(event);
	}
	// 1.18
	public void onServerStopped3(net.minecraftforge.event.server.ServerStoppedEvent event) {
		onServerStopped(event);
	}
	/** reset */
	public void onServerStopped(Object obj){
		port = 0;
		ServerProperties = null;
		server = null;
		sent = false;
		GuiEventDisabled=true;
		bisNetherEnabled=true;
		bisCommandBlockEnabled=true;
		brepliesToStatus=true;
		bisResourcePackRequired=false;
		bhidesOnlinePlayers=false;
	}
	//1.13 - 1.16
	public void onServerStarting1(net.minecraftforge.fml.event.server.FMLServerStartingEvent event) {
		onServerStarting(event);
	}
	// 1.17
	public void onServerStarting2(net.minecraftforge.fmlserverevents.FMLServerStartingEvent event) {
		onServerStarting(event);
	}
	// 1.18
	public void onServerStarting3(net.minecraftforge.event.server.ServerStartingEvent event) {
		onServerStarting(event);
	}
	
	public void onServerStarting(Object obj) {
		boolean firstRun=false;
		String gameDir="";
		try {
			gameDir=""+(Minecraft.getInstance()).gameDir;
		} catch (Error E1) {
			try {
				gameDir=""+(Minecraft.m_91087_()).f_91069_;
			} catch (Error E2) {
				LOGGER.error("Error getting gameDir");
			}
		}
		try { //1.13 - 1.16
			server = new ServerWrapper(((net.minecraftforge.fml.event.server.FMLServerStartingEvent)obj).getServer());
		} catch (Error E1) {
			try {//1.17
				server = new ServerWrapper(((net.minecraftforge.fmlserverevents.FMLServerStartingEvent)obj).getServer());	
			} catch (Error E2) {
				try {//1.18
					server = new ServerWrapper(((net.minecraftforge.event.server.ServerStartingEvent)obj).getServer());	
				} catch (Error E3) {
					LOGGER.error("Error parse ServerEvent");
					LOGGER.error("Error 1:",E1);
					LOGGER.error("Error 2:",E2);
					LOGGER.error("Error 3:",E3);
				}
			}
		}
		String worldrootdir = "";
		try {//1.13 - 1.15
			/** half hardcoded */
			worldrootdir = gameDir + File.separator + "saves" + File.separator + server.getFolderName() + File.separator;
		} catch (Error E1) {
			try {//1.16
				/** server.levelsave.getWorldDir().toString() = world directory full path */
				Field f = getField(MinecraftServer.class,"field_71310_m","f_129744_","storageSource");
				LevelSave ls=(LevelSave) f.get(server.getObj());
				worldrootdir = ls.getWorldDir().toString() + File.separator;
			} catch (Exception | Error E2) {
				try {//1.17 - 1.18
					/** server.storageSource.getWorldDir().toString() = world directory full path */
					Field f = getField(MinecraftServer.class,"f_129744_","storageSource");
					LevelStorageSource.LevelStorageAccess ls=(LevelStorageSource.LevelStorageAccess) f.get(server.getObj());
					worldrootdir = ls.getWorldDir().toString() + File.separator;
				} catch (Exception | Error E3) {
					/** Something went wrong */
					LOGGER.error("Error get world directory:");
					LOGGER.error("Error 1:",E1);
					LOGGER.error("Error 2:",E2);
					LOGGER.error("Error 3:",E3);
				}
			}
		}
		File local = new File(worldrootdir + "server.properties");
		File global = new File(gameDir + File.separator + "config" + File.separator + "serverGlobalConfig.properties");
		LOGGER.info("Integrated Server Starting");
		if (!global.exists()) {
			/** create new file */
			firstRun = true;
			ServerProperties = new PropertyManagerClient(global);
		} else if (local.exists()) {
			/** load file */
			ServerProperties = new PropertyManagerClient(local);
			if (!ServerProperties.getBooleanProperty("overrideGlobalDefaults", true)) {
				/** use global */
				ServerProperties.setPropertiesFile(global);
				LOGGER.info("Using Global Server Properties !");
			}
		} else {
			try {
				/** copy global file to world directory*/
				Files.copy(global, local);
				ServerProperties = new PropertyManagerClient(local);
				ServerProperties.comment += System.getProperty("line.separator") 
										  + "overrideGlobalDefaults :" + System.getProperty("line.separator") 
										  + "\tspecify weather to use this file to override the global settings in the file \"" 
										  + global.getAbsolutePath() + "\"";
				/** Generate property "overrideGlobalDefaults" */
				ServerProperties.getBooleanProperty("overrideGlobalDefaults", true);
				ServerProperties.saveProperties();
			} catch (Exception e) {
				/** Something went wrong */
				LOGGER.warn("Could not create local server config file. Using the global one.");
				LOGGER.error("Error:",e);
				/** use global file */
				ServerProperties = new PropertyManagerClient(global);
			}
		}
		LOGGER.info("Using file : " + (ServerProperties.getBooleanProperty("overrideGlobalDefaults", true) ? local.getPath() : global.getPath()));
		ServerProperties.comment = "Minecraft Server Properties for LAN." + System.getProperty("line.separator") 
								 + "For default behaviour :-" + System.getProperty("line.separator") 
								 + "set max-view-distance=0" + System.getProperty("line.separator") 
								 + "set port=0" + System.getProperty("line.separator") 
								 + "You can also delete this(or any properties) file to get it regenerated with default values.";
		/** process data */
		port = ServerProperties.getIntProperty("port", 0);
		server.setOnlineMode(ServerProperties.getBooleanProperty("online-mode", true));
		try {//1.13 - 1.16
			server.setBuildLimit(ServerProperties.getIntProperty("max-build-height", 256));
			//1.13 - 1.15
			server.setCanSpawnAnimals(ServerProperties.getBooleanProperty("spawn-animals", true));
			server.setCanSpawnNPCs(ServerProperties.getBooleanProperty("spawn-npcs", true));
		} catch (Error E1){}
		server.setAllowPvp(ServerProperties.getBooleanProperty("pvp", true));
		server.setAllowFlight(ServerProperties.getBooleanProperty("allow-flight", false));
		server.setResourcePack(ServerProperties.getStringProperty("resource-pack", ""), loadResourcePackSHA());
		server.setMOTD(ServerProperties.getStringProperty("motd", "<! " + server.getServerOwner() + "'s " + server.getWorldName() + " ON LAN !>"));
		server.setPlayerIdleTimeout(ServerProperties.getIntProperty("player-idle-timeout", 0));
		sent=!ServerProperties.getBooleanProperty("send-server-status", true);
		bisNetherEnabled=ServerProperties.getBooleanProperty("allow-nether", true);
		bisCommandBlockEnabled=ServerProperties.getBooleanProperty("enable-command-block", true);
		brepliesToStatus=ServerProperties.getBooleanProperty("enable-status", true);
		bisResourcePackRequired=ServerProperties.getBooleanProperty("require-resource-pack", false);
		bhidesOnlinePlayers=ServerProperties.getBooleanProperty("hide-online-players", false);
		/** Debug info */
		LOGGER.info("Server Status:");
		LOGGER.info("online-mode = " + server.isServerInOnlineMode());
		try {//1.13 - 1.16
			LOGGER.info("max-build-height = " + server.getBuildLimit());
			//1.13 - 1.15
			LOGGER.info("spawn-animals = " + server.getCanSpawnAnimals());
			LOGGER.info("spawn-npcs = " + server.getCanSpawnNPCs());
		} catch (Error E1) {}
		LOGGER.info("pvp = " + server.isPVPEnabled());
		LOGGER.info("allow-flight = " + server.isFlightAllowed());
		LOGGER.info("player-idle-timeout = " + server.getMaxPlayerIdleMinutes());
		LOGGER.info("resource-pack = " + server.getResourcePackUrl());
		LOGGER.info("resource-pack-sha1 = " + server.getResourcePackHash());
		LOGGER.info("motd = " + server.getMOTD());
		LOGGER.info("allow-nether = " + bisNetherEnabled);
		LOGGER.info("enable-command-block = " + bisCommandBlockEnabled);
		LOGGER.info("enable-status = " + brepliesToStatus);
		LOGGER.info("require-resource-pack = " + bisResourcePackRequired);
		LOGGER.info("hide-online-players = " + bhidesOnlinePlayers);
		if (!server.getResourcePackUrl().isEmpty())
			GuiEventDisabled=false;
		/** Process special data */
		PlayerListWrapper customPlayerList = new PlayerListWrapper(server.getPlayerList());
		try {
			/** Max Players */
			customPlayerList.setMaxPlayers(ServerProperties.getIntProperty("max-players", 10));
			LOGGER.info("Max Players = " + customPlayerList.getMaxPlayers());
		} catch (Exception E1) {
			/** Something went wrong */
			LOGGER.error("Unknown Error:");
			LOGGER.error("Error 1:",E1);
		}
		/** useful command*/
		try {//1.13 - 1.16
			CommandDispatcher<CommandSource> dispatcher = server.getCommandManager().getDispatcher();
			net.minecraft.command.impl.BanIpCommand.register(dispatcher);
			net.minecraft.command.impl.BanListCommand.register(dispatcher);
			net.minecraft.command.impl.BanCommand.register(dispatcher);
			net.minecraft.command.impl.DeOpCommand.register(dispatcher);
			net.minecraft.command.impl.OpCommand.register(dispatcher);
			net.minecraft.command.impl.PardonCommand.register(dispatcher);
			net.minecraft.command.impl.PardonIpCommand.register(dispatcher);
			net.minecraft.command.impl.SaveAllCommand.register(dispatcher);
			net.minecraft.command.impl.SaveOffCommand.register(dispatcher);
			net.minecraft.command.impl.SaveOnCommand.register(dispatcher);
			net.minecraft.command.impl.SetIdleTimeoutCommand.register(dispatcher);
			net.minecraft.command.impl.StopCommand.register(dispatcher);
			net.minecraft.command.impl.WhitelistCommand.register(dispatcher);
		} catch (Error E) {//1.17 - 1.18
			CommandDispatcher<CommandSourceStack> dispatcher = server.getCommandManager1().m_82094_();
			net.minecraft.server.commands.BanIpCommands.m_136527_(dispatcher);
			net.minecraft.server.commands.BanListCommands.m_136543_(dispatcher);
			net.minecraft.server.commands.BanPlayerCommands.m_136558_(dispatcher);
			net.minecraft.server.commands.DeOpCommands.m_136888_(dispatcher);
			net.minecraft.server.commands.OpCommand.m_138079_(dispatcher);
			net.minecraft.server.commands.PardonCommand.m_138093_(dispatcher);
			net.minecraft.server.commands.PardonIpCommand.m_138108_(dispatcher);
			net.minecraft.server.commands.SaveAllCommand.m_138271_(dispatcher);
			net.minecraft.server.commands.SaveOffCommand.m_138284_(dispatcher);
			net.minecraft.server.commands.SaveOnCommand.m_138292_(dispatcher);
			net.minecraft.server.commands.SetPlayerIdleTimeoutCommand.m_138634_(dispatcher);
			net.minecraft.server.commands.StopCommand.m_138785_(dispatcher);
			net.minecraft.server.commands.WhitelistCommand.m_139201_(dispatcher);
		}
		if (firstRun)
			try {
				/** copy global file to world directory */
				Files.copy(global, local);
				ServerProperties.setPropertiesFile(local);
				ServerProperties.comment += System.getProperty("line.separator") 
										  + "overrideGlobalDefaults :" + System.getProperty("line.separator") 
										  + "\tspecify weather to use this file to override the global settings in the file \"" 
										  + global.getAbsolutePath() + "\"";
				/** Generate property "overrideGlobalDefaults" */
				ServerProperties.getBooleanProperty("overrideGlobalDefaults", true);
				ServerProperties.saveProperties();
			} catch (Exception e) {
				/** Something went wrong */
				LOGGER.error("Oops..! Couldn't copy to local server config file. Please manually copy the global server config file to your world save directory.");
				LOGGER.error("Error:",e);
			}
	}
	/** copied from net.minecraft.server.dedicated.DedicatedServer#loadResourcePackSHA */
	private String loadResourcePackSHA() {
		if (ServerProperties.hasProperty("resource-pack-hash"))
			if (ServerProperties.hasProperty("resource-pack-sha1")) {
				LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
			} else {
				LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
				ServerProperties.getStringProperty("resource-pack-sha1", ServerProperties.getStringProperty("resource-pack-hash", ""));
				ServerProperties.removeProperty("resource-pack-hash");
			}
		String s = ServerProperties.getStringProperty("resource-pack-sha1", "");
		if (!s.isEmpty() && !Pattern.compile("^[a-fA-F0-9]{40}$").matcher(s).matches())
			LOGGER.warn("Invalid sha1 for ressource-pack-sha1"); 
		if (!ServerProperties.getStringProperty("resource-pack", "").isEmpty() && s.isEmpty())
			LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack."); 
		return s;
	}
	public static boolean ClassExist(String classname) {
		try {
			Class.forName(classname);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
	/** ASM Handler */
	public static int getPort() {
		/** if not server running mod and port is valid */
		if (instance != null && instance.port > 0 && instance.port <= 65535)
			return instance.port;
		/** if server running mod or port is invalid*/
		else
			/** act like normal client */
			try (ServerSocket serversocket = new ServerSocket(0)){
				return serversocket.getLocalPort();
			} catch (IOException e) {
				return 25564;
			}
	}
	public static boolean isNetherEnabled() {
		return instance.bisNetherEnabled;
	}
	public static boolean isCommandBlockEnabled() {
		return instance.bisCommandBlockEnabled;
	}
	public static boolean repliesToStatus() {
		return instance.brepliesToStatus;
	}
	public static boolean isResourcePackRequired() {
		return instance.bisResourcePackRequired;
	}
	public static boolean hidesOnlinePlayers() {
		return instance.bhidesOnlinePlayers;
	}
	public static Field getField(Class<?> c,String... fn) throws Exception {
		for (String s:fn) {
			try {
				Field f;
				f = c.getDeclaredField(s);
				f.setAccessible(true);
				return f;
			} catch (NoSuchFieldException | SecurityException e) {}
		}
		c.getDeclaredField(fn[fn.length-1]).setAccessible(true);
		return null;
	}
}
