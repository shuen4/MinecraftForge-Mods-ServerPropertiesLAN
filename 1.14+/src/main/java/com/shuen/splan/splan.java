package com.shuen.splan;

import com.google.common.io.Files;
import com.mojang.brigadier.CommandDispatcher;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.LanServerPingThread;
//import net.minecraft.client.multiplayer.ThreadLanServerPing;
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
//import net.minecraft.server.management.UserListWhitelist;
import net.minecraft.server.management.WhiteList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
/*import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;*/
import net.minecraft.util.text.TranslationTextComponent;
/*import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.IServerWorldInfo;*/
import net.minecraft.world.storage.SaveFormat.LevelSave;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
//import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("splan")
public class splan {
  public int port = 0;
  
  private static boolean firstRun = false;
  
  private PropertyManagerClient ServerProperties = null;
  
  private static final Logger LOGGER = LogManager.getLogger();
  
  private IntegratedServer server;
  
  private boolean sent = false;
  
  public static splan instance;
  
  public splan() {
    instance = this;
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  @SubscribeEvent
  public void test(EntityJoinWorldEvent event) {
      if (event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity&&!sent) {
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("Server Status: "),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("online-mode = " + this.server.isServerInOnlineMode()),event.getEntity().getUniqueID());
        /*event.getEntity().sendMessage((ITextComponent)new StringTextComponent("spawn-animals = " + this.server.getCanSpawnAnimals()),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("spawn-npcs = " + this.server.getCanSpawnNPCs()),event.getEntity().getUniqueID());*/
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("pvp = " + this.server.isPVPEnabled()),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("allow-flight = " + this.server.isFlightAllowed()),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("player-idle-timeout = " + this.server.getMaxPlayerIdleMinutes()),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-build-height = " + this.server.getBuildLimit()),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("motd = " + this.server.getMOTD()),event.getEntity().getUniqueID());
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-players = " + this.server.getMaxPlayers()),event.getEntity().getUniqueID());
        if (!this.server.getResourcePackUrl().isEmpty())
          event.getEntity().sendMessage((ITextComponent)new StringTextComponent("resource-pack = " + this.server.getResourcePackUrl()),event.getEntity().getUniqueID()); 
        if (!this.server.getResourcePackHash().isEmpty())
          event.getEntity().sendMessage((ITextComponent)new StringTextComponent("resource-pack-sha1 = " + this.server.getResourcePackHash()),event.getEntity().getUniqueID()); 
        int i = this.ServerProperties.getIntProperty("max-view-distance", 0);
        if (i > 0) {
          event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-view-distance = " + i),event.getEntity().getUniqueID());
        } else {
          event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-view-distance = default"),event.getEntity().getUniqueID());
        } 
        event.getEntity().sendMessage((ITextComponent)new StringTextComponent("white-list = " + this.ServerProperties.getBooleanProperty("white-list", false)),event.getEntity().getUniqueID());
        try {
          this.server.getNetworkSystem().addEndpoint((InetAddress)null, this.port);
          LOGGER.info("Started serving on {}", Integer.valueOf(this.port));
          Field field = IntegratedServer.class.getDeclaredField("field_195580_l");
          field.setAccessible(true);
          field.set(this.server, Integer.valueOf(this.port));
          field = IntegratedServer.class.getDeclaredField("field_71345_q");
          field.setAccessible(true);
          LanServerPingThread tlsp = new LanServerPingThread(this.server.getMOTD(), this.port + "");
          tlsp.start();
          field.set(this.server, tlsp);
          this.server.getPlayerList().setGameType(this.server.getGameType());
          this.server.getPlayerList().setCommandsAllowedForAll(false);
          event.getEntity().sendMessage((ITextComponent)new TranslationTextComponent("commands.publish.success", new Object[] { Integer.valueOf(this.port) }),event.getEntity().getUniqueID());
        } catch (Exception e1) {
          e1.printStackTrace();
        } 
        this.sent = true;
      } 
  }
  
  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {
    this.sent = false;
    this.server = (IntegratedServer)event.getServer();
    MinecraftServer s=(MinecraftServer)server;
    String worldrootdir = "";
	try {
		Field f = MinecraftServer.class.getDeclaredField("field_71310_m");
	    f.setAccessible(true);
	    LevelSave ls=(LevelSave) f.get(server);
	    LOGGER.info(this.server.getServerConfiguration().getWorldName());
	    LOGGER.info(ls.getWorldDir());
	    LOGGER.info(ls.getSaveName());
	    LOGGER.info(ls.getWorldDir().toString());
	    worldrootdir = ls.getWorldDir().toString() + File.separator;
	} catch (Exception e) {
		e.printStackTrace();
	}
    File local = new File(worldrootdir + "server.properties");
    File global = new File(Minecraft.getInstance().gameDir + File.separator + "config" + File.separator + "serverGlobalConfig.properties");
    LOGGER.debug("Integrated Server Starting");
    if (!global.exists()) {
      firstRun = true;
      this.ServerProperties = new PropertyManagerClient(global);
    } else if (local.exists()) {
      this.ServerProperties = new PropertyManagerClient(local);
      if (!this.ServerProperties.getBooleanProperty("overrideGlobalDefaults", true)) {
        this.ServerProperties.setPropertiesFile(global);
        LOGGER.debug("Using Global Server Properties !");
      } 
    } else {
      try {
        Files.copy(global, local);
        this.ServerProperties = new PropertyManagerClient(local);
        this.ServerProperties.comment += System.getProperty("line.separator") + "overrideGlobalDefaults :" + System.getProperty("line.separator") + "\tspecify weather to use this file to override the global settings in the file \"" + global.getAbsolutePath() + "\"";
        this.ServerProperties.getBooleanProperty("overrideGlobalDefaults", false);
        this.ServerProperties.saveProperties();
      } catch (IOException e) {
        LOGGER.warn("Could not create local server config file. Using the global one.");
        e.printStackTrace();
        this.ServerProperties = new PropertyManagerClient(global);
      } 
    } 
    LOGGER.info("Using file : " + (this.ServerProperties.getBooleanProperty("overrideGlobalDefaults", true) ? local.getPath() : global.getPath()));
    this.server = (IntegratedServer)event.getServer();
    this.ServerProperties.comment = "Minecraft Server Properties for LAN." + System.getProperty("line.separator") + "For default behaviour :-" + System.getProperty("line.separator") + "set max-view-distance=0" + System.getProperty("line.separator") + "set port=0" + System.getProperty("line.separator") + "You can also delete this(or any properties) file to get it regenerated with default values.";
    this.port = this.ServerProperties.getIntProperty("port", 0);
    this.server.setOnlineMode(this.ServerProperties.getBooleanProperty("online-mode", true));
    /*this.server.setCanSpawnAnimals(this.ServerProperties.getBooleanProperty("spawn-animals", true));
    this.server.setCanSpawnNPCs(this.ServerProperties.getBooleanProperty("spawn-npcs", true));*/
    this.server.setAllowPvp(this.ServerProperties.getBooleanProperty("pvp", true));
    this.server.setAllowFlight(this.ServerProperties.getBooleanProperty("allow-flight", false));
    this.server.setResourcePack(this.ServerProperties.getStringProperty("resource-pack-sha1", ""), loadResourcePackSHA());
    this.server.setMOTD(this.ServerProperties.getStringProperty("motd", "<! " + this.server.getServerOwner() + "'s " + this.server.getName() + " ON LAN !>"));
    this.server.setPlayerIdleTimeout(this.ServerProperties.getIntProperty("player-idle-timeout", 0));
    this.server.setBuildLimit(this.ServerProperties.getIntProperty("max-build-height", 256));
    LOGGER.debug("Server Data :- ");
    LOGGER.debug("online-mode = " + this.server.isServerInOnlineMode());
    /*LOGGER.debug("spawn-animals = " + this.server.getCanSpawnAnimals());
    LOGGER.debug("spawn-npcs = " + this.server.getCanSpawnNPCs());*/
    LOGGER.debug("pvp = " + this.server.isPVPEnabled());
    LOGGER.debug("allow-flight = " + this.server.isFlightAllowed());
    LOGGER.debug("player-idle-timeout = " + this.server.getMaxPlayerIdleMinutes());
    LOGGER.debug("max-build-height = " + this.server.getBuildLimit());
    LOGGER.debug("resource-pack-sha1 = " + this.server.getResourcePackHash());
    LOGGER.debug("motd = " + this.server.getMOTD());
    PlayerList customPlayerList = this.server.getPlayerList();
    try {
      Field field = PlayerList.class.getDeclaredField("field_72405_c");
      field.setAccessible(true);
      field.set(customPlayerList, Integer.valueOf(this.ServerProperties.getIntProperty("max-players", 10)));
      LOGGER.debug("Max Players = " + customPlayerList.getMaxPlayers());
      Field dist = PlayerList.class.getDeclaredField("field_72402_d");
      dist.setAccessible(true);
      int d = this.ServerProperties.getIntProperty("max-view-distance", 0);
      if (d > 0) {
        dist.set(customPlayerList, Integer.valueOf(d));
        LOGGER.debug("Max view distance = " + d);
      } else {
        LOGGER.debug("max-view-distance is set <= 0. Using default view distance algorithm.");
      } 
      if (this.ServerProperties.getBooleanProperty("white-list", false)) {
        WhiteList whitelist;
        LOGGER.warn("Whitelist enabled. Make sure to include your game ID in the file.");
        File whitelistjson = new File(worldrootdir + "whitelist.json");
        if (!whitelistjson.exists()) {
          whitelistjson.createNewFile();
          whitelist = new WhiteList(whitelistjson);
          whitelist.writeChanges();
        } else {
          whitelist = new WhiteList(whitelistjson);
        } 
        field = PlayerList.class.getDeclaredField("field_72411_j");
        field.setAccessible(true);
        field.set(customPlayerList, whitelist);
        customPlayerList.setWhiteListEnabled(true);
      } 
      this.server.setPlayerList(customPlayerList);
      CommandDispatcher<CommandSource> dispatcher = this.server.getCommandManager().getDispatcher();
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
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (firstRun)
      try {
        Files.copy(global, local);
        this.ServerProperties.setPropertiesFile(local);
        this.ServerProperties.comment += System.getProperty("line.separator") + "overrideGlobalDefaults :" + System.getProperty("line.separator") + "\tspecify weather to use this file to override the global settings in the file \"" + global.getAbsolutePath() + "\"";
        this.ServerProperties.getBooleanProperty("overrideGlobalDefaults", false);
        this.ServerProperties.saveProperties();
      } catch (IOException e) {
        LOGGER.error("Oops..! Couldn't copy to local server config file. Please manually copy the global server config file to your world save directory.");
        e.printStackTrace();
      }  
  }
  
  private String loadResourcePackSHA() {
    if (this.ServerProperties.hasProperty("resource-pack-hash"))
      if (this.ServerProperties.hasProperty("resource-pack-sha1")) {
        LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
      } else {
        LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
        this.ServerProperties.getStringProperty("resource-pack-sha1", this.ServerProperties.getStringProperty("resource-pack-hash", ""));
        this.ServerProperties.removeProperty("resource-pack-hash");
      }  
    String s = this.ServerProperties.getStringProperty("resource-pack-sha1", "");
    if (!s.isEmpty() && !Pattern.compile("^[a-fA-F0-9]{40}$").matcher(s).matches())
      LOGGER.warn("Invalid sha1 for ressource-pack-sha1"); 
    if (!this.ServerProperties.getStringProperty("resource-pack", "").isEmpty() && s.isEmpty())
      LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack."); 
    return s;
  }
}
