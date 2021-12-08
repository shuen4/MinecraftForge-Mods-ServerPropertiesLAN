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
			int i = splan.instance.ServerProperties.getIntProperty("max-view-distance", 0);
			if (i > 0)
				entity.m_6352_((Component)new TextComponent("max-view-distance = " + i),entity.m_142081_());
			else
				entity.m_6352_((Component)new TextComponent("max-view-distance = default"),entity.m_142081_());
			if (splan.port>0 && splan.port<=65535)
				entity.m_6352_((Component)new TextComponent("port = " + splan.port),entity.m_142081_());
			else
				entity.m_6352_((Component)new TextComponent("port = random"),entity.m_142081_());
			entity.m_6352_((Component)new TextComponent("use /whitelist command control whitelist"),entity.m_142081_());
			entity.m_6352_((Component)new TextComponent("^ require allow-cheat on"),entity.m_142081_());
			splan.instance.sent = true;
		}
	} 
}
