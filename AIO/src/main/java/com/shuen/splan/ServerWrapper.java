package com.shuen.splan;

import net.minecraft.command.Commands;

public class ServerWrapper {
	private Object obj;
	public ServerWrapper(Object obj) {
		this.obj=obj;
	}
	public String getFolderName() {
		//1.13 - 1.15
		return ((net.minecraft.server.integrated.IntegratedServer)obj).getFolderName();
	}
	public void setCanSpawnAnimals(boolean booleanProperty) {
		//1.13 - 1.15
		((net.minecraft.server.integrated.IntegratedServer)obj).setCanSpawnAnimals(booleanProperty);
	}
	public void setCanSpawnNPCs(boolean booleanProperty) {
		//1.13 - 1.15
		((net.minecraft.server.integrated.IntegratedServer)obj).setCanSpawnNPCs(booleanProperty);
	}
	public boolean getCanSpawnAnimals() {
		//1.13 - 1.15
		return ((net.minecraft.server.integrated.IntegratedServer)obj).getCanSpawnAnimals();
	}
	public boolean getCanSpawnNPCs() {
		//1.13 - 1.15
		return ((net.minecraft.server.integrated.IntegratedServer)obj).getCanSpawnNPCs();
	}
	public void setBuildLimit(int intProperty) {
		//1.13 - 1.16
		((net.minecraft.server.integrated.IntegratedServer)obj).setBuildLimit(intProperty);
	}
	public int getBuildLimit() {
		//1.13 - 1.16
		return ((net.minecraft.server.integrated.IntegratedServer)obj).getBuildLimit();
	}
	public Commands getCommandManager() {
		//1.13 - 1.16
		return ((net.minecraft.server.integrated.IntegratedServer)obj).getCommandManager();
	}
	public net.minecraft.commands.Commands getCommandManager1() {
		//1.17 - 1.18
		return ((net.minecraft.client.server.IntegratedServer)obj).m_129892_();
	}
	public void setOnlineMode(boolean booleanProperty) {
		try {//1.13 - 1.16
			((net.minecraft.server.integrated.IntegratedServer)obj).setOnlineMode(booleanProperty);
		} catch (Error E1) {
			try {// 1.17 - 1.18
				((net.minecraft.client.server.IntegratedServer)obj).m_129985_(booleanProperty);
			} catch (Error E2) {
				splan.LOGGER.error("Error Setting Online Mode");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
	}
	public void setAllowPvp(boolean booleanProperty) {
		try {//1.13 - 1.16
			((net.minecraft.server.integrated.IntegratedServer)obj).setAllowPvp(booleanProperty);
		} catch (Error E1) {
			try {// 1.17 - 1.18
				((net.minecraft.client.server.IntegratedServer)obj).m_129997_(booleanProperty);
			} catch (Error E2) {
				splan.LOGGER.error("Error Setting Allow PvP");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
	}
	public void setAllowFlight(boolean booleanProperty) {
		try {//1.13 - 1.16
			((net.minecraft.server.integrated.IntegratedServer)obj).setAllowFlight(booleanProperty);
		} catch (Error E1) {
			try {// 1.17 - 1.18
				((net.minecraft.client.server.IntegratedServer)obj).m_129999_(booleanProperty);
			} catch (Error E2) {
				splan.LOGGER.error("Error Setting Allow Flight");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
	}
	public void setResourcePack(String stringProperty, String loadResourcePackSHA) {
		try {//1.13 - 1.16
			((net.minecraft.server.integrated.IntegratedServer)obj).setResourcePack(stringProperty, loadResourcePackSHA);
		} catch (Error E1) {
			try {// 1.17 - 1.18
				((net.minecraft.client.server.IntegratedServer)obj).m_129853_(stringProperty,loadResourcePackSHA);
			} catch (Error E2) {
				splan.LOGGER.error("Error Setting Resource Pack");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
	}
	public String getServerOwner() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getServerOwner();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129791_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Server Owner");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return null;
	}
	public String getWorldName() {
		try {//1.13-1.15
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getWorldName();
		} catch (Error E1) {
			try {//1.16
				return ((net.minecraft.server.integrated.IntegratedServer)obj).func_71214_G();
			} catch (Error E2) {
				try {
					//1.17 - 1.18
					return ((net.minecraft.client.server.IntegratedServer)obj).m_129910_().m_5462_();
				} catch (Error E3) {
					splan.LOGGER.error("Error Getting World Name");
					splan.LOGGER.error("Error 1:",E1);
					splan.LOGGER.error("Error 2:",E2);
					splan.LOGGER.error("Error 3:",E3);
				}
			}
		}
		return null;
	}
	public void setMOTD(String stringProperty) {
		try {//1.13 - 1.16
			((net.minecraft.server.integrated.IntegratedServer)obj).setMOTD(stringProperty);
		} catch (Error E1) {
			try {// 1.17 - 1.18
				((net.minecraft.client.server.IntegratedServer)obj).m_129989_(stringProperty);
			} catch (Error E2) {
				splan.LOGGER.error("Error Setting MOTD");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
	}
	public void setPlayerIdleTimeout(int intProperty) {
		try {//1.13 - 1.16
			((net.minecraft.server.integrated.IntegratedServer)obj).setPlayerIdleTimeout(intProperty);
		} catch (Error E1) {
			try {// 1.17 - 1.18
				((net.minecraft.client.server.IntegratedServer)obj).m_7196_(intProperty);
			} catch (Error E2) {
				splan.LOGGER.error("Error Setting Player Idle Timeout");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
	}
	public boolean isServerInOnlineMode() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).isServerInOnlineMode();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129797_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Online Mode");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return true;
	}
	public boolean isPVPEnabled() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).isPVPEnabled();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129799_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Allow PvP");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return true;
	}
	public boolean isFlightAllowed() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).isFlightAllowed();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129915_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Allow Flight");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return false;
	}
	public int getMaxPlayerIdleMinutes() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getMaxPlayerIdleMinutes();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129924_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Player Idle Timeout");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return 0;
	}
	public String getResourcePackUrl() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getResourcePackUrl();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129795_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Resource Pack URL");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return null;
	}
	public String getResourcePackHash() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getResourcePackHash();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129796_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Resource Pack Hash");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return null;
	}
	public String getMOTD() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getMOTD();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_129916_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting MOTD");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return null;
	}
	public Object getPlayerList() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getPlayerList();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_6846_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting PlayerList");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return null;
	}
	public int getMaxPlayers() {
		try {//1.13 - 1.16
			return ((net.minecraft.server.integrated.IntegratedServer)obj).getMaxPlayers();
		} catch (Error E1) {
			try {// 1.17 - 1.18
				return ((net.minecraft.client.server.IntegratedServer)obj).m_7418_();
			} catch (Error E2) {
				splan.LOGGER.error("Error Getting Command Manager");
				splan.LOGGER.error("Error 1:",E1);
				splan.LOGGER.error("Error 2:",E2);
			}
		}
		return 20;
	}
	public Object getObj() {
		return obj;
	}
}
