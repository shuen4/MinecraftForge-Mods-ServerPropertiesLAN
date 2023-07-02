package net.minecraftforge.fmlserverevents;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class FMLServerStartingEvent extends ServerLifecycleEvent{

	public FMLServerStartingEvent(MinecraftServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	public MinecraftServer getServer() {
		// TODO Auto-generated method stub
		return null;
	}

}
