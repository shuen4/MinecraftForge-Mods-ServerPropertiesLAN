package com.shuen.splan;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntityJoinWorldEvent1 {
    public static void onEvent(EntityJoinWorldEvent event,ServerWrapper server) {
        try {//1.13
            if (event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer && !splan.instance.sent) {
                event.getEntity().sendMessage(new TextComponentString("Server Status:"));
                event.getEntity().sendMessage(new TextComponentString("online-mode = " + server.isServerInOnlineMode()));
                event.getEntity().sendMessage(new TextComponentString("spawn-animals = " + server.getCanSpawnAnimals()));
                event.getEntity().sendMessage(new TextComponentString("spawn-npcs = " + server.getCanSpawnNPCs()));
                event.getEntity().sendMessage(new TextComponentString("pvp = " + server.isPVPEnabled()));
                event.getEntity().sendMessage(new TextComponentString("allow-flight = " + server.isFlightAllowed()));
                event.getEntity().sendMessage(new TextComponentString("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()));
                event.getEntity().sendMessage(new TextComponentString("max-build-height = " + server.getBuildLimit()));
                event.getEntity().sendMessage(new TextComponentString("motd = " + server.getMOTD()));
                event.getEntity().sendMessage(new TextComponentString("max-players = " + server.getMaxPlayers()));
                if (!server.getResourcePackUrl().isEmpty())
                    event.getEntity().sendMessage(new TextComponentString("resource-pack = " + server.getResourcePackUrl())); 
                if (!server.getResourcePackHash().isEmpty())
                    event.getEntity().sendMessage(new TextComponentString("resource-pack-sha1 = " + server.getResourcePackHash())); 
                event.getEntity().sendMessage(new TextComponentString("allow-nether = " + splan.instance.bisNetherEnabled));
                event.getEntity().sendMessage(new TextComponentString("enable-command-block = " + splan.instance.bisCommandBlockEnabled));
                if (splan.instance.port>0 && splan.instance.port<=65535)
                    event.getEntity().sendMessage(new TextComponentString("port = " + splan.instance.port));
                else
                    event.getEntity().sendMessage(new TextComponentString("port = random"));
                event.getEntity().sendMessage(new TextComponentString("use /whitelist command control whitelist"));
                event.getEntity().sendMessage(new TextComponentString("^ require allow-cheat on"));
                splan.instance.sent = true;
            }
        } catch (Error E1) {
            try {//1.14 - 1.15
                if (event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity && !splan.instance.sent) {
                    event.getEntity().sendMessage(new StringTextComponent("Server Status:"));
                    event.getEntity().sendMessage(new StringTextComponent("online-mode = " + server.isServerInOnlineMode()));
                    event.getEntity().sendMessage(new StringTextComponent("spawn-animals = " + server.getCanSpawnAnimals()));
                    event.getEntity().sendMessage(new StringTextComponent("spawn-npcs = " + server.getCanSpawnNPCs()));
                    event.getEntity().sendMessage(new StringTextComponent("pvp = " + server.isPVPEnabled()));
                    event.getEntity().sendMessage(new StringTextComponent("allow-flight = " + server.isFlightAllowed()));
                    event.getEntity().sendMessage(new StringTextComponent("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()));
                    event.getEntity().sendMessage(new StringTextComponent("max-build-height = " + server.getBuildLimit()));
                    event.getEntity().sendMessage(new StringTextComponent("motd = " + server.getMOTD()));
                    event.getEntity().sendMessage(new StringTextComponent("max-players = " + server.getMaxPlayers()));
                if (!server.getResourcePackUrl().isEmpty())
                    event.getEntity().sendMessage(new StringTextComponent("resource-pack = " + server.getResourcePackUrl())); 
                if (!server.getResourcePackHash().isEmpty())
                    event.getEntity().sendMessage(new StringTextComponent("resource-pack-sha1 = " + server.getResourcePackHash())); 
                event.getEntity().sendMessage(new StringTextComponent("allow-nether = " + splan.instance.bisNetherEnabled));
                event.getEntity().sendMessage(new StringTextComponent("enable-command-block = " + splan.instance.bisCommandBlockEnabled));
                if (splan.instance.port>0 && splan.instance.port<=65535)
                     event.getEntity().sendMessage(new StringTextComponent("port = " + splan.instance.port));
                else
                     event.getEntity().sendMessage(new StringTextComponent("port = random"));
                event.getEntity().sendMessage(new StringTextComponent("use /whitelist command control whitelist"));
                event.getEntity().sendMessage(new StringTextComponent("^ require allow-cheat on"));
                splan.instance.sent = true;
                }
            } catch (Error E2) {
                try {//1.16
                    if (event.getEntity() instanceof net.minecraft.entity.player.PlayerEntity&&!splan.instance.sent) {
                        event.getEntity().func_145747_a(new StringTextComponent("Server Status: "),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("online-mode = " + server.isServerInOnlineMode()),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("pvp = " + server.isPVPEnabled()),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("allow-flight = " + server.isFlightAllowed()),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("max-build-height = " + server.getBuildLimit()),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("motd = " + server.getMOTD()),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("max-players = " + server.getMaxPlayers()),event.getEntity().getUniqueID());
                        if (!server.getResourcePackUrl().isEmpty())
                            event.getEntity().func_145747_a(new StringTextComponent("resource-pack = " + server.getResourcePackUrl()),event.getEntity().getUniqueID()); 
                        if (!server.getResourcePackHash().isEmpty())
                            event.getEntity().func_145747_a(new StringTextComponent("resource-pack-sha1 = " + server.getResourcePackHash()),event.getEntity().getUniqueID()); 
                        event.getEntity().func_145747_a(new StringTextComponent("allow-nether = " + splan.instance.bisNetherEnabled),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("enable-command-block = " + splan.instance.bisCommandBlockEnabled),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("enable-status = " + splan.instance.brepliesToStatus),event.getEntity().getUniqueID());
                        if (splan.instance.port>0 && splan.instance.port<=65535)
                            event.getEntity().func_145747_a(new StringTextComponent("port = " + splan.instance.port),event.getEntity().getUniqueID());
                        else
                            event.getEntity().func_145747_a(new StringTextComponent("port = random"),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("use /whitelist command control whitelist"),event.getEntity().getUniqueID());
                        event.getEntity().func_145747_a(new StringTextComponent("^ require allow-cheat on"),event.getEntity().getUniqueID());
                        splan.instance.sent = true;
                    }
                } catch (Error E3) {
                    splan.LOGGER.error("Error Send Message1");
                    splan.LOGGER.error("Error 1:",E1);
                    splan.LOGGER.error("Error 2:",E2);
                    splan.LOGGER.error("Error 3:",E3);
                }
            }
        }
    } 
}
