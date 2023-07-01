package com.shuen.splan;

import com.google.common.io.Files;
import com.mojang.brigadier.CommandDispatcher;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.LanServerPingThread;
import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.BanCommand;
import net.minecraft.command.impl.BanIpCommand;
import net.minecraft.command.impl.BanListCommand;
import net.minecraft.command.impl.DeOpCommand;
import net.minecraft.command.impl.KickCommand;
import net.minecraft.command.impl.OpCommand;
import net.minecraft.command.impl.PardonCommand;
import net.minecraft.command.impl.PardonIpCommand;
import net.minecraft.command.impl.SaveAllCommand;
import net.minecraft.command.impl.SaveOffCommand;
import net.minecraft.command.impl.SaveOnCommand;
import net.minecraft.command.impl.SetIdleTimeoutCommand;
import net.minecraft.command.impl.StopCommand;
import net.minecraft.command.impl.WhitelistCommand;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
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
    private int port = 0;
    private boolean bisNetherEnabled=true;
    private boolean bisCommandBlockEnabled=true;
    private boolean brepliesToStatus=true;
    /** Property manager */
    private PropertyManagerClient ServerProperties = null;
    /** Log4j logger */
    private static final Logger LOGGER = LogManager.getLogger();
    /** Global variable */
    private IntegratedServer server;
    /** Only first player joined game send server info */
    private boolean sent = false;
    /** If the server started with resource pack, close the first ConfirmScreen */
    private boolean GuiEventDisabled = true;
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
    public boolean stop_LAN() {
        try {
            server.getConnection().stop();
            getField(IntegratedServer.class,"publishedPort","field_195580_l").set(server,-1);
            Field f=getField(IntegratedServer.class,"lanPinger","field_71345_q");
            LanServerPingThread lsp=(LanServerPingThread) f.get(server);
            lsp.interrupt();
            f.set(server,null);
            server.getPlayerList().setOverrideGameMode(null);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error stopping LAN");
            LOGGER.error("",e);
            return false;
        }
    }
    /** Send server status to first player joined game */
    @SubscribeEvent
    public void SendMessageToPlayer(EntityJoinWorldEvent event) {
        if (server==null)
            return;
        if (event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity&&!sent) {
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("Server Status: "),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("online-mode = " + server.usesAuthentication()),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("pvp = " + server.isPvpAllowed()),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("allow-flight = " + server.isFlightAllowed()),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("player-idle-timeout = " + server.getPlayerIdleTimeout()),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-build-height = " + server.getMaxBuildHeight()),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("motd = " + server.getMotd()),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("max-players = " + server.getMaxPlayers()),event.getEntity().getUUID());
            if (!server.getResourcePack().isEmpty())
                event.getEntity().sendMessage((ITextComponent)new StringTextComponent("resource-pack = " + server.getResourcePack()),event.getEntity().getUUID()); 
            if (!server.getResourcePackHash().isEmpty())
                event.getEntity().sendMessage((ITextComponent)new StringTextComponent("resource-pack-sha1 = " + server.getResourcePackHash()),event.getEntity().getUUID()); 
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("allow-nether = " + bisNetherEnabled),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("enable-command-block = " + bisCommandBlockEnabled),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("enable-status = " + brepliesToStatus),event.getEntity().getUUID());
            if (port>0 && port<=65535)
                event.getEntity().sendMessage((ITextComponent)new StringTextComponent("port = " + port),event.getEntity().getUUID());
            else
                event.getEntity().sendMessage((ITextComponent)new StringTextComponent("port = random"),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("use /whitelist command control whitelist"),event.getEntity().getUUID());
            event.getEntity().sendMessage((ITextComponent)new StringTextComponent("^ require allow-cheat on"),event.getEntity().getUUID());
            sent = true;
        }
    }
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent event) {
        if (server==null||GuiEventDisabled)
            return;
        if (event.getGui() instanceof net.minecraft.client.gui.screen.ConfirmScreen) {
            /** should be the resource pack dialog */
            LOGGER.info("closing ConfirmScreen");
            GuiEventDisabled=true;
            event.getGui().onClose();
        }
    }
    /** reset */
    @SubscribeEvent
    public void onServerStopped(FMLServerStoppedEvent event){
        port = 0;
        ServerProperties = null;
        server = null;
        sent = false;
        GuiEventDisabled=true;
        bisNetherEnabled=true;
        bisCommandBlockEnabled=true;
        brepliesToStatus=true;
    }
    
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        boolean firstRun = false;
        server = (IntegratedServer)event.getServer();
        String worldrootdir = server.getWorldPath(new FolderName("")).toString() + File.separator;
        File local = new File(worldrootdir + "server.properties");
        @SuppressWarnings("resource")
        File global = new File((Minecraft.getInstance()).gameDirectory + File.separator + "config" + File.separator + "serverGlobalConfig.properties");
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
                LOGGER.warn("",e);
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
        server.setUsesAuthentication(ServerProperties.getBooleanProperty("online-mode", true));
        server.setPvpAllowed(ServerProperties.getBooleanProperty("pvp", true));
        server.setFlightAllowed(ServerProperties.getBooleanProperty("allow-flight", false));
        server.setResourcePack(ServerProperties.getStringProperty("resource-pack", ""), loadResourcePackSHA());
        server.setMotd(ServerProperties.getStringProperty("motd", "<! " + server.getSingleplayerName() + "'s " + server.getServerDirectory() + " ON LAN !>"));
        server.setPlayerIdleTimeout(ServerProperties.getIntProperty("player-idle-timeout", 0));
        server.setMaxBuildHeight(ServerProperties.getIntProperty("max-build-height", 256));
        sent=!ServerProperties.getBooleanProperty("send-server-status", true);
        bisNetherEnabled=ServerProperties.getBooleanProperty("allow-nether", true);
        bisCommandBlockEnabled=ServerProperties.getBooleanProperty("enable-command-block", true);
        brepliesToStatus=ServerProperties.getBooleanProperty("enable-status", true);
        /** Debug info */
        LOGGER.info("Server Status:");
        LOGGER.info("online-mode = " + server.usesAuthentication());
        LOGGER.info("pvp = " + server.isPvpAllowed());
        LOGGER.info("allow-flight = " + server.isFlightAllowed());
        LOGGER.info("player-idle-timeout = " + server.getPlayerIdleTimeout());
        LOGGER.info("max-build-height = " + server.getMaxBuildHeight());
        LOGGER.info("resource-pack = " + server.getResourcePack());
        LOGGER.info("resource-pack-sha1 = " + server.getResourcePackHash());
        LOGGER.info("motd = " + server.getMotd());
        LOGGER.info("allow-nether = " + bisNetherEnabled);
        LOGGER.info("enable-command-block = " + bisCommandBlockEnabled);
        LOGGER.info("enable-status = " + brepliesToStatus);
        if (!server.getResourcePack().isEmpty())
            GuiEventDisabled=false;
        /** Process special data */
        PlayerList customPlayerList = server.getPlayerList();
        try {
            /** Max Players */
            getField(PlayerList.class,"maxPlayers","field_72405_c").set(customPlayerList, Integer.valueOf(ServerProperties.getIntProperty("max-players", 10)));
            LOGGER.info("Max Players = " + customPlayerList.getMaxPlayers());
        } catch (Exception E1) {
            /** Something went wrong */
            LOGGER.error("Unknown Error:");
            LOGGER.error("",E1);
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
                LOGGER.error("",e);
            }
    }
    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        /** useful command*/
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
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
        KickCommand.register(dispatcher);
        StopLANCommand.register(dispatcher);
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
