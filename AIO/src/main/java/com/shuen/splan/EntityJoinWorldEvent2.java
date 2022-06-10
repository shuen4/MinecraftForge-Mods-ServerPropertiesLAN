package com.shuen.splan;

import java.lang.reflect.Field;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntityJoinWorldEvent2 {
    public static void onEvent(EntityJoinWorldEvent event,ServerWrapper server) {
        Entity entity;
        Field f;
        Object obj;
        try {
            f=net.minecraftforge.event.entity.EntityEvent.class.getDeclaredField("entity");
            f.setAccessible(true);
            obj=f.get(event);
            entity=(Entity)obj;
        } catch (Exception | Error E) {
            splan.LOGGER.error("Error Send Message2:",E);
            return;
        }
        if (entity instanceof Player&&!splan.instance.sent) {
            entity.m_6352_((Component)new TextComponent("Server Status: "),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("online-mode = " + server.isServerInOnlineMode()),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("pvp = " + server.isPVPEnabled()),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("allow-flight = " + server.isFlightAllowed()),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("motd = " + server.getMOTD()),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("max-players = " + server.getMaxPlayers()),entity.m_142081_());
            if (!server.getResourcePackUrl().isEmpty())
                entity.m_6352_((Component)new TextComponent("resource-pack = " + server.getResourcePackUrl()),entity.m_142081_()); 
            if (!server.getResourcePackHash().isEmpty())
                entity.m_6352_((Component)new TextComponent("resource-pack-sha1 = " + server.getResourcePackHash()),entity.m_142081_()); 
            entity.m_6352_((Component)new TextComponent("allow-nether = " + splan.instance.bisNetherEnabled),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("enable-command-block = " + splan.instance.bisCommandBlockEnabled),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("enable-status = " + splan.instance.brepliesToStatus),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("require-resource-pack = " + splan.instance.bisResourcePackRequired),entity.m_142081_());
            if (splan.ClassExist("net.minecraftforge.event.server.ServerStoppedEvent")) //1.18
                entity.m_6352_((Component)new TextComponent("hide-online-players = " + splan.instance.bhidesOnlinePlayers),entity.m_142081_());
            if (splan.instance.port>0 && splan.instance.port<=65535)
                entity.m_6352_((Component)new TextComponent("port = " + splan.instance.port),entity.m_142081_());
            else
                entity.m_6352_((Component)new TextComponent("port = random"),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("use /whitelist command control whitelist"),entity.m_142081_());
            entity.m_6352_((Component)new TextComponent("^ require allow-cheat on"),entity.m_142081_());
            splan.instance.sent = true;
        }
    } 
}
