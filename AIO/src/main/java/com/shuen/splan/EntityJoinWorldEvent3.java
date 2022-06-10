package com.shuen.splan;

import java.lang.reflect.Field;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntityJoinWorldEvent3 {
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
			entity.m_213846_(Component.m_237113_("Server Status: "));
			entity.m_213846_(Component.m_237113_("online-mode = " + server.isServerInOnlineMode()));
			entity.m_213846_(Component.m_237113_("pvp = " + server.isPVPEnabled()));
			entity.m_213846_(Component.m_237113_("allow-flight = " + server.isFlightAllowed()));
			entity.m_213846_(Component.m_237113_("player-idle-timeout = " + server.getMaxPlayerIdleMinutes()));
			entity.m_213846_(Component.m_237113_("motd = " + server.getMOTD()));
			entity.m_213846_(Component.m_237113_("max-players = " + server.getMaxPlayers()));
			if (!server.getResourcePackUrl().isEmpty())
				entity.m_213846_(Component.m_237113_("resource-pack = " + server.getResourcePackUrl()));
			if (!server.getResourcePackHash().isEmpty())
				entity.m_213846_(Component.m_237113_("resource-pack-sha1 = " + server.getResourcePackHash()));
			entity.m_213846_(Component.m_237113_("allow-nether = " + splan.instance.bisNetherEnabled));
			entity.m_213846_(Component.m_237113_("enable-command-block = " + splan.instance.bisCommandBlockEnabled));
			entity.m_213846_(Component.m_237113_("enable-status = " + splan.instance.brepliesToStatus));
			entity.m_213846_(Component.m_237113_("require-resource-pack = " + splan.instance.bisResourcePackRequired));
			if (splan.ClassExist("net.minecraftforge.event.server.ServerStoppedEvent")) //1.18
				entity.m_213846_(Component.m_237113_("hide-online-players = " + splan.instance.bhidesOnlinePlayers));
			if (splan.instance.port>0 && splan.instance.port<=65535)
				entity.m_213846_(Component.m_237113_("port = " + splan.instance.port));
			else
				entity.m_213846_(Component.m_237113_("port = random"));
			entity.m_213846_(Component.m_237113_("use /whitelist command control whitelist"));
			entity.m_213846_(Component.m_237113_("^ require allow-cheat on"));
			splan.instance.sent = true;
		}
	} 
}
