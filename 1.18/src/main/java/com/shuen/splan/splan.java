package com.shuen.splan;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.regex.Pattern;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.BanPlayerCommands;
import net.minecraft.server.commands.BanIpCommands;
import net.minecraft.server.commands.BanListCommands;
import net.minecraft.server.commands.DeOpCommands;
import net.minecraft.server.commands.OpCommand;
import net.minecraft.server.commands.PardonCommand;
import net.minecraft.server.commands.PardonIpCommand;
import net.minecraft.server.commands.SaveAllCommand;
import net.minecraft.server.commands.SaveOffCommand;
import net.minecraft.server.commands.SaveOnCommand;
import net.minecraft.server.commands.SetPlayerIdleTimeoutCommand;
import net.minecraft.server.commands.StopCommand;
import net.minecraft.server.commands.WhitelistCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("splan")
public class splan {
	/** server port */
	private static int port = 0;
	/** Property manager */
	private PropertyManagerClient ServerProperties = null;
	/** Log4j logger */
	public static final Logger LOGGER = LogManager.getLogger();
	/** Global variable */
	private IntegratedServer server;
	/** Only first player joined game send server info */
	private boolean sent = false;
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
		if (server==null)
			return;
		if (event.getEntity() instanceof Player&&!sent) {
			event.getEntity().sendMessage((Component)new TextComponent("Server Status: "),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("online-mode = " + server.usesAuthentication()),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("pvp = " + server.isPvpAllowed()),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("allow-flight = " + server.isFlightAllowed()),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("player-idle-timeout = " + server.getPlayerIdleTimeout()),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("motd = " + server.getMotd()),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("max-players = " + server.getMaxPlayers()),event.getEntity().getUUID());
			if (!server.getResourcePack().isEmpty())
				event.getEntity().sendMessage((Component)new TextComponent("resource-pack = " + server.getResourcePack()),event.getEntity().getUUID()); 
			if (!server.getResourcePackHash().isEmpty())
				event.getEntity().sendMessage((Component)new TextComponent("resource-pack-sha1 = " + server.getResourcePackHash()),event.getEntity().getUUID()); 
			int i = ServerProperties.getIntProperty("max-view-distance", 0);
			if (i > 0)
				event.getEntity().sendMessage((Component)new TextComponent("max-view-distance = " + i),event.getEntity().getUUID());
			else
				event.getEntity().sendMessage((Component)new TextComponent("max-view-distance = default"),event.getEntity().getUUID());
			if (port>0 && port<=65535)
				event.getEntity().sendMessage((Component)new TextComponent("port = " + port),event.getEntity().getUUID());
			else
				event.getEntity().sendMessage((Component)new TextComponent("port = random"),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("use /whitelist command control whitelist"),event.getEntity().getUUID());
			event.getEntity().sendMessage((Component)new TextComponent("^ require allow-cheat on"),event.getEntity().getUUID());
			sent = true;
		}
	}
	/** reset */
	@SubscribeEvent
	public void onServerStopped(ServerStoppedEvent event){
		port = 0;
		ServerProperties = null;
		server = null;
		sent = false;
	}
	
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		boolean firstRun = false;
		server = (IntegratedServer)event.getServer();
		String worldrootdir = "";
		/** server.levelsave.getWorldDir().toString() = world directory full path */
		Field f;
		LevelStorageAccess ls;
		try {
			f = getField(MinecraftServer.class,"f_129744_","storageSource");
			ls = (LevelStorageAccess) f.get(server);
			worldrootdir = ls.getWorldDir().toString() + File.separator;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			LOGGER.error("Error getting world directory",e1);
		}
		File local = new File(worldrootdir + "server.properties");
		@SuppressWarnings("resource")
		File global = new File((Minecraft.getInstance()).gameDirectory + File.separator + "config" + File.separator + "serverGlobalConfig.properties");
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
				LOGGER.warn("",e);
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
		server.setUsesAuthentication(ServerProperties.getBooleanProperty("online-mode", true));
		server.setPvpAllowed(ServerProperties.getBooleanProperty("pvp", true));
		server.setFlightAllowed(ServerProperties.getBooleanProperty("allow-flight", false));
		server.setResourcePack(ServerProperties.getStringProperty("resource-pack", ""), loadResourcePackSHA());
		server.setMotd(ServerProperties.getStringProperty("motd", "<! " + server.getSingleplayerName() + "'s " + server.getWorldData().getLevelName() + " ON LAN !>"));
		server.setPlayerIdleTimeout(ServerProperties.getIntProperty("player-idle-timeout", 0));
		/** Debug info */
		LOGGER.debug("Server Status:");
		LOGGER.debug("online-mode = " + server.usesAuthentication());
		LOGGER.debug("pvp = " + server.isPvpAllowed());
		LOGGER.debug("allow-flight = " + server.isFlightAllowed());
		LOGGER.debug("player-idle-timeout = " + server.getPlayerIdleTimeout());
		LOGGER.debug("resource-pack-sha1 = " + server.getResourcePackHash());
		LOGGER.debug("motd = " + server.getMotd());
		/** Process special data */
		PlayerList customPlayerList = server.getPlayerList();
		try {
			/** Max Players */
			Field field = getField(PlayerList.class,"f_11193_","maxPlayers");
			field.set(customPlayerList, Integer.valueOf(ServerProperties.getIntProperty("max-players", 10)));
			LOGGER.debug("Max Players = " + customPlayerList.getMaxPlayers());
		} catch (Exception E1) {
			/** Something went wrong */
			LOGGER.error("Unknown Error:");
			LOGGER.error("",E1);
		}
		/** useful command*/
		CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
		BanIpCommands.register(dispatcher);
		BanListCommands.register(dispatcher);
		BanPlayerCommands.register(dispatcher);
		DeOpCommands.register(dispatcher);
		OpCommand.register(dispatcher);
		PardonCommand.register(dispatcher);
		PardonIpCommand.register(dispatcher);
		SaveAllCommand.register(dispatcher);
		SaveOffCommand.register(dispatcher);
		SaveOnCommand.register(dispatcher);
		SetPlayerIdleTimeoutCommand.register(dispatcher);
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
				LOGGER.error("",e);
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
	private static Field getField(Class<?> c,String... fn) throws NoSuchFieldException {
		for (String s:fn) {
			try {
				Field f;
				f = c.getDeclaredField(s);
				f.setAccessible(true);
				return f;
			} catch (NoSuchFieldException | SecurityException e) {}
		}
		throw new NoSuchFieldException();
	}
}