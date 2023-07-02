package net.minecraft.server;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import java.io.File;
import java.net.Proxy;
import javax.annotation.Nullable;
import net.minecraft.command.Commands;
import net.minecraft.command.ICommandSource;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.IThreadListener;

public abstract class MinecraftServer implements IThreadListener, ISnooperInfo, ICommandSource, Runnable {
   public MinecraftServer(@Nullable File anvilFileIn, Proxy serverProxyIn, DataFixer dataFixerIn, Commands commandManagerIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
	   
   }

public boolean isServerInOnlineMode() {
	// TODO Auto-generated method stub
	return false;
}

public boolean getCanSpawnAnimals() {
	// TODO Auto-generated method stub
	return false;
}

public boolean getCanSpawnNPCs() {
	// TODO Auto-generated method stub
	return false;
}

public boolean isPVPEnabled() {
	// TODO Auto-generated method stub
	return false;
}

public boolean isFlightAllowed() {
	// TODO Auto-generated method stub
	return false;
}

public int getMaxPlayerIdleMinutes() {
	// TODO Auto-generated method stub
	return 0;
}

public int getBuildLimit() {
	// TODO Auto-generated method stub
	return 0;
}

public String getMOTD() {
	// TODO Auto-generated method stub
	return null;
}

public int getMaxPlayers() {
	// TODO Auto-generated method stub
	return 0;
}

public String getResourcePackUrl() {
	// TODO Auto-generated method stub
	return null;
}

public String getResourcePackHash() {
	// TODO Auto-generated method stub
	return null;
}

public String getFolderName() {
	// TODO Auto-generated method stub
	return null;
}

public void setOnlineMode(boolean booleanProperty) {
	// TODO Auto-generated method stub
	
}

public void setCanSpawnAnimals(boolean booleanProperty) {
	// TODO Auto-generated method stub
	
}

public void setCanSpawnNPCs(boolean booleanProperty) {
	// TODO Auto-generated method stub
	
}

public void setAllowPvp(boolean booleanProperty) {
	// TODO Auto-generated method stub
	
}

public void setAllowFlight(boolean booleanProperty) {
	// TODO Auto-generated method stub
	
}

public void setResourcePack(String stringProperty, String loadResourcePackSHA) {
	// TODO Auto-generated method stub
	
}

public String getServerOwner() {
	// TODO Auto-generated method stub
	return null;
}

public String getWorldName() {
	// TODO Auto-generated method stub
	return null;
}

public String func_71214_G() {
	// TODO Auto-generated method stub
	return null;
}

public void setPlayerIdleTimeout(int intProperty) {
	// TODO Auto-generated method stub
	
}

public void setMOTD(String stringProperty) {
	// TODO Auto-generated method stub
	
}

public void setBuildLimit(int intProperty) {
	// TODO Auto-generated method stub
	
}

public PlayerList getPlayerList() {
	// TODO Auto-generated method stub
	return null;
}

public Commands getCommandManager() {
	// TODO Auto-generated method stub
	return null;
}
public Commands m_129892_() {
	// TODO Auto-generated method stub
	return null;
}

public void setPlayerList(PlayerList customPlayerList) {
	// TODO Auto-generated method stub
	
}
public static class ServerResourcePackInfo {
	public ServerResourcePackInfo(String url, String hash, boolean isRequired, @Nullable net.minecraft.network.chat.Component prompt) {
		
	}

	public String f_236743_() {
		// TODO Auto-generated method stub
		return null;
	}

	public String f_236744_() {
		// TODO Auto-generated method stub
		return null;
	}
}
public abstract boolean getPublic();

public boolean m_6992_() {
	// TODO Auto-generated method stub
	return false;
}
}
