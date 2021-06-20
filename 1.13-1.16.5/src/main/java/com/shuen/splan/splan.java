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
import net.minecraft.command.impl.BanCommand;
import net.minecraft.command.impl.BanIpCommand;
import net.minecraft.command.impl.BanListCommand;
import net.minecraft.command.impl.DeOpCommand;
import net.minecraft.command.impl.OpCommand;
import net.minecraft.command.impl.PardonCommand;
import net.minecraft.command.impl.PardonIpCommand;
import net.minecraft.command.impl.SaveAllCommand;
import net.minecraft.command.impl.SaveOffCommand;
import net.minecraft.command.impl.SaveOnCommand;
import net.minecraft.command.impl.SetIdleTimeoutCommand;
import net.minecraft.command.impl.StopCommand;
import net.minecraft.command.impl.WhitelistCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.SaveFormat.LevelSave;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("splan")
public class splan {
	/** server port */
	private static int port = 0;
	private static boolean firstRun = false;
	/** Property manager */
	private PropertyManagerClient ServerProperties = null;
	/** Log4j logger */
	private static final Logger LOGGER = LogManager.getLogger();
	/** Global variable */
	private IntegratedServer server;
	/** Only first player joined game send server info */
	private boolean sent = false;
	/** Prevent send message to player if joined server not IntegratedServer by this client */
	private boolean serverstarted = false;
	/** null if server running this mod */
	public static splan instance = null;
	/** mod initialization */
	public splan() {
		try {
			Minecraft.getInstance();
		} catch (RuntimeException e) {
			LOGGER.error("Refuse to work on server");
			return;
		}
		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}
	/** Send server status to first player joined game */
	@SubscribeEvent
	public void SendMessageToPlayer(EntityJoinWorldEvent event) {
		if (!serverstarted)
			return;
		try {//1.13
			if (event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer && !sent) {
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("Server Status:"));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("online-mode = " + server.isServerInOnlineMode()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("spawn-animals = " + server.getCanSpawnAnimals()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("spawn-npcs = " + server.getCanSpawnNPCs()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("pvp = " + server.isPVPEnabled()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("allow-flight = " + server.isFlightAllowed()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("max-build-height = " + server.getBuildLimit()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("motd = " + server.getMOTD()));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("max-players = " + server.getMaxPlayers()));
				if (!server.getResourcePackUrl().isEmpty())
					event.getEntity().sendMessage((ITextComponent)new TextComponentString("resource-pack = " + server.getResourcePackUrl())); 
				if (!server.getResourcePackHash().isEmpty())
					event.getEntity().sendMessage((ITextComponent)new TextComponentString("resource-pack-sha1 = " + server.getResourcePackHash())); 
				int i = ServerProperties.getIntProperty("max-view-distance", 0);
				if (i > 0)
					event.getEntity().sendMessage((ITextComponent)new TextComponentString("max-view-distance = " + i));
				else
					event.getEntity().sendMessage((ITextComponent)new TextComponentString("max-view-distance = default"));
				if (port>0 && port<=65535)
					event.getEntity().sendMessage((ITextComponent)new TextComponentString("port = " + port));
				else
					event.getEntity().sendMessage((ITextComponent)new TextComponentString("port = random"));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("use /whitelist command control whitelist"));
				event.getEntity().sendMessage((ITextComponent)new TextComponentString("^ require allow-cheat on"));
				sent = true;
			}
		} catch (Error E1) {
			try {//1.14 - 1.15
				if (event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity && !sent) {
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("Server Status:"));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("online-mode = " + server.isServerInOnlineMode()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("spawn-animals = " + server.getCanSpawnAnimals()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("spawn-npcs = " + server.getCanSpawnNPCs()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("pvp = " + server.isPVPEnabled()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("allow-flight = " + server.isFlightAllowed()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-build-height = " + server.getBuildLimit()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("motd = " + server.getMOTD()));
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-players = " + server.getMaxPlayers()));
				if (!server.getResourcePackUrl().isEmpty())
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("resource-pack = " + server.getResourcePackUrl())); 
				if (!server.getResourcePackHash().isEmpty())
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("resource-pack-sha1 = " + server.getResourcePackHash())); 
				int i = ServerProperties.getIntProperty("max-view-distance", 0);
				if (i > 0)
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-view-distance = " + i));
				else
					event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-view-distance = default"));
				if (port>0 && port<=65535)
					 event.getEntity().sendMessage((ITextComponent)new StringTextComponent("port = " + port));
				else
					 event.getEntity().sendMessage((ITextComponent)new StringTextComponent("port = random"));
				event.getEntity().sendMessage((ITextComponent)new StringTextComponent("use /whitelist command control whitelist"));
				event.getEntity().sendMessage((ITextComponent)new StringTextComponent("^ require allow-cheat on"));
				sent = true;
				}
			} catch (Error E2) {
				try {//1.16
					if (event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity&&!sent) {
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("Server Status: "),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("online-mode = " + server.isServerInOnlineMode()),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("pvp = " + server.isPVPEnabled()),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("allow-flight = " + server.isFlightAllowed()),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("max-build-height = " + server.getBuildLimit()),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("motd = " + server.getMOTD()),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("max-players = " + server.getMaxPlayers()),event.getEntity().getUniqueID());
						if (!server.getResourcePackUrl().isEmpty())
							event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("resource-pack = " + server.getResourcePackUrl()),event.getEntity().getUniqueID()); 
						if (!server.getResourcePackHash().isEmpty())
							event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("resource-pack-sha1 = " + server.getResourcePackHash()),event.getEntity().getUniqueID()); 
						int i = ServerProperties.getIntProperty("max-view-distance", 0);
						if (i > 0)
							event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("max-view-distance = " + i),event.getEntity().getUniqueID());
						else
							event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("max-view-distance = default"),event.getEntity().getUniqueID());
						if (port>0 && port<=65535)
							event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("port = " + port),event.getEntity().getUniqueID());
						else
							event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("port = random"),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("use /whitelist command control whitelist"),event.getEntity().getUniqueID());
						event.getEntity().func_145747_a((ITextComponent)new StringTextComponent("^ require allow-cheat on"),event.getEntity().getUniqueID());
						sent = true;
					}
				} catch (Error E3) {
					/** Something went wrong */
					LOGGER.error("Error send message to player:");
					E1.printStackTrace();
					LOGGER.error("Error 2:");
					E2.printStackTrace();
					LOGGER.error("Error 3:");
					E3.printStackTrace();
				}
			}
		}
	}
	/** reset */
	@SubscribeEvent
	public void onServerStopped(FMLServerStoppedEvent event){
		port = 0;
		firstRun = false;
		ServerProperties = null;
		server = null;
		sent = false;
		serverstarted = false;
	}
	
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		serverstarted = true;
		server = (IntegratedServer)event.getServer();
		String worldrootdir = "";
		try {//1.13 - 1.15
			/** half hardcoded */
			worldrootdir = (Minecraft.getInstance()).gameDir + File.separator + "saves" + File.separator + server.getFolderName() + File.separator;
		} catch (Error E1) {
			try {//1.16
				/** server.levelsave.getWorldDir().toString() = world directory full path */
				Field f = MinecraftServer.class.getDeclaredField("field_71310_m");
				f.setAccessible(true);
				LevelSave ls=(LevelSave) f.get(server);
				worldrootdir = ls.getWorldDir().toString() + File.separator;
			} catch (Exception E2) {
				/** Something went wrong */
				LOGGER.error("Error get world directory:");
				E1.printStackTrace();
				LOGGER.error("Error 2:");
				E2.printStackTrace();
			}
		}
		File local = new File(worldrootdir + "server.properties");
		File global = new File((Minecraft.getInstance()).gameDir + File.separator + "config" + File.separator + "serverGlobalConfig.properties");
		LOGGER.debug("Integrated Server Starting");
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
				LOGGER.debug("Using Global Server Properties !");
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
				e.printStackTrace();
				/** use global file */
				ServerProperties = new PropertyManagerClient(global);
			}
		}
		LOGGER.info("Using file : " + (ServerProperties.getBooleanProperty("overrideGlobalDefaults", true) ? local.getPath() : global.getPath()));
		server = (IntegratedServer)event.getServer();
		ServerProperties.comment = "Minecraft Server Properties for LAN." + System.getProperty("line.separator") 
								 + "For default behaviour :-" + System.getProperty("line.separator") 
								 + "set max-view-distance=0" + System.getProperty("line.separator") 
								 + "set port=0" + System.getProperty("line.separator") 
								 + "You can also delete this(or any properties) file to get it regenerated with default values.";
		/** process data */
		port = ServerProperties.getIntProperty("port", 0);
		server.setOnlineMode(ServerProperties.getBooleanProperty("online-mode", true));
		try {//1.13 - 1.15
			server.setCanSpawnAnimals(ServerProperties.getBooleanProperty("spawn-animals", true));
			server.setCanSpawnNPCs(ServerProperties.getBooleanProperty("spawn-npcs", true));
		} catch (Error E1){}
		server.setAllowPvp(ServerProperties.getBooleanProperty("pvp", true));
		server.setAllowFlight(ServerProperties.getBooleanProperty("allow-flight", false));
		server.setResourcePack(ServerProperties.getStringProperty("resource-pack-sha1", ""), loadResourcePackSHA());
		try {//1.13 - 1.15
			server.setMOTD(ServerProperties.getStringProperty("motd", "<! " + server.getServerOwner() + "'s " + server.getWorldName() + " ON LAN !>"));
		} catch (Error E1) {
			try {//1.16
				server.setMOTD(ServerProperties.getStringProperty("motd", "<! " + server.getServerOwner() + "'s " + server.func_71214_G() + " ON LAN !>"));
			} catch (Error E2) {
				/** Something went wrong */
				LOGGER.error("Error setting server MOTD:");
				E1.printStackTrace();
				LOGGER.error("Error 2:");
				E2.printStackTrace();
			}
		}
		server.setPlayerIdleTimeout(ServerProperties.getIntProperty("player-idle-timeout", 0));
		server.setBuildLimit(ServerProperties.getIntProperty("max-build-height", 256));
		/** Debug info */
		LOGGER.debug("Server Status:");
		LOGGER.debug("online-mode = " + server.isServerInOnlineMode());
		try {//1.13 - 1.15
			LOGGER.debug("spawn-animals = " + server.getCanSpawnAnimals());
			LOGGER.debug("spawn-npcs = " + server.getCanSpawnNPCs());
		} catch (Error E1) {}
		LOGGER.debug("pvp = " + server.isPVPEnabled());
		LOGGER.debug("allow-flight = " + server.isFlightAllowed());
		LOGGER.debug("player-idle-timeout = " + server.getMaxPlayerIdleMinutes());
		LOGGER.debug("max-build-height = " + server.getBuildLimit());
		LOGGER.debug("resource-pack-sha1 = " + server.getResourcePackHash());
		LOGGER.debug("motd = " + server.getMOTD());
		/** Process special data */
		PlayerList customPlayerList = server.getPlayerList();
		try {
			/** Max Players */
			Field field = PlayerList.class.getDeclaredField("field_72405_c");
			field.setAccessible(true);
			field.set(customPlayerList, Integer.valueOf(ServerProperties.getIntProperty("max-players", 10)));
			LOGGER.debug("Max Players = " + customPlayerList.getMaxPlayers());
			/** view distance */
			Field dist = PlayerList.class.getDeclaredField("field_72402_d");
			dist.setAccessible(true);
			int d = ServerProperties.getIntProperty("max-view-distance", 0);
			if (d > 0) {
				dist.set(customPlayerList, Integer.valueOf(d));
				LOGGER.debug("Max view distance = " + d);
			} else
				LOGGER.debug("max-view-distance is set <= 0. Using default view distance algorithm.");
			server.setPlayerList(customPlayerList);
		} catch (Exception E1) {
			/** Something went wrong */
			LOGGER.error("Unknown Error:");
			E1.printStackTrace();
		}
		/** useful command*/
		CommandDispatcher<CommandSource> dispatcher = server.getCommandManager().getDispatcher();
		BanIpCommand.register(dispatcher);
		BanListCommand.register(dispatcher);
		BanCommand.register(dispatcher);
		DeOpCommand.register(dispatcher);
		OpCommand.register(dispatcher);
		PardonCommand.register(dispatcher);
		PardonIpCommand.register(dispatcher);
		SaveAllCommand.register(dispatcher);
		SaveOffCommand.register(dispatcher);
		SaveOnCommand.register(dispatcher);
		SetIdleTimeoutCommand.register(dispatcher);
		StopCommand.register(dispatcher);
		WhitelistCommand.register(dispatcher);
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
				e.printStackTrace();
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
	/** ASM Handler */
	public static int getPort() {
		/** if not server running mod and port is valid */
		if (instance != null && port > 0 && port <= 65535)
			return port;
		/** if server running mod or port is invalid*/
		else
			/** act like normal client */
			try (ServerSocket serversocket = new ServerSocket(0)){
				return serversocket.getLocalPort();
			} catch (IOException e) {
				return 25564;
			}
	}
}
