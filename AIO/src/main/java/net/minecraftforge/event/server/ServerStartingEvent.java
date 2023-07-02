package net.minecraftforge.event.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class ServerStartingEvent extends ServerLifecycleEvent{

	public ServerStartingEvent(MinecraftServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	public MinecraftServer getServer() {
		// TODO Auto-generated method stub
		return null;
	}

}
