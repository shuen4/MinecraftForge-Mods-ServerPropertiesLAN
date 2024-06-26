package com.shuen.splan;

public class ServerWrapper {
    private Object obj;
    public ServerWrapper(Object obj) {
        this.obj=obj;
    }
    public String getFolderName() {
        // 1.13 - 1.15
        return ((net.minecraft.server.integrated.IntegratedServer)obj).getFolderName();
    }
    public void setCanSpawnAnimals(boolean booleanProperty) {
        // 1.13 - 1.15
        ((net.minecraft.server.integrated.IntegratedServer)obj).setCanSpawnAnimals(booleanProperty);
    }
    public void setCanSpawnNPCs(boolean booleanProperty) {
        // 1.13 - 1.15
        ((net.minecraft.server.integrated.IntegratedServer)obj).setCanSpawnNPCs(booleanProperty);
    }
    public boolean getCanSpawnAnimals() {
        // 1.13 - 1.15
        return ((net.minecraft.server.integrated.IntegratedServer)obj).getCanSpawnAnimals();
    }
    public boolean getCanSpawnNPCs() {
        // 1.13 - 1.15
        return ((net.minecraft.server.integrated.IntegratedServer)obj).getCanSpawnNPCs();
    }
    public void setBuildLimit(int intProperty) {
        // 1.13 - 1.16
        ((net.minecraft.server.integrated.IntegratedServer)obj).setBuildLimit(intProperty);
    }
    public int getBuildLimit() {
        // 1.13 - 1.16
        return ((net.minecraft.server.integrated.IntegratedServer)obj).getBuildLimit();
    }
    public net.minecraft.command.Commands getCommandManager() {
        // 1.13 - 1.16
        return ((net.minecraft.server.integrated.IntegratedServer)obj).getCommandManager();
    }
    public net.minecraft.commands.Commands getCommandManager1() {
        // 1.17 - 1.18
        return ((net.minecraft.client.server.IntegratedServer)obj).m_129892_();
    }
    public void setOnlineMode(boolean booleanProperty) {
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
    public void setResourcePack(String url, String hash) {
        try {// 1.13 - 1.16
            ((net.minecraft.server.integrated.IntegratedServer)obj).setResourcePack(url, hash);
        } catch (Error E1) {
            try {// 1.17 - 1.18
                ((net.minecraft.client.server.IntegratedServer)obj).m_129853_(url,hash);
            } catch (Error E2) {
                if (splan.ClassExist("net.minecraft.server.MinecraftServer$ServerResourcePackInfo")) {// 1.19 - 1.20
                    splan.instance.ResourcePackUrl=url;
                    splan.instance.ResourcePackHash=hash;
                }
                else {
                    splan.LOGGER.error("Error Setting Resource Pack");
                    splan.LOGGER.error("Error 1:",E1);
                    splan.LOGGER.error("Error 2:",E2);
                }
            }
        }
    }
    public String getServerOwner() {
        try {// 1.13 - 1.16
            return ((net.minecraft.server.integrated.IntegratedServer)obj).getServerOwner();
        } catch (Error E1) {
            try {// 1.17 - 1.18
                return ((net.minecraft.client.server.IntegratedServer)obj).m_129791_();
            } catch (Error E2) {
                try {// 1.19 - 1.20
                    return ((net.minecraft.client.server.IntegratedServer)obj).m_236731_().getName();
                } catch (Error E3) {
                    splan.LOGGER.error("Error Getting Server Owner");
                    splan.LOGGER.error("Error 1:",E1);
                    splan.LOGGER.error("Error 2:",E2);
                    splan.LOGGER.error("Error 3:",E3);
                }
            }
        }
        return null;
    }
    public String getWorldName() {
        try {// 1.13-1.15
            return ((net.minecraft.server.integrated.IntegratedServer)obj).getWorldName();
        } catch (Error E1) {
            try {// 1.16
                return ((net.minecraft.server.integrated.IntegratedServer)obj).func_71214_G();
            } catch (Error E2) {
                try {
                    // 1.17 - 1.18
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
            return ((net.minecraft.server.integrated.IntegratedServer)obj).getResourcePackUrl();
        } catch (Error E1) {
            try {// 1.17 - 1.18
                return ((net.minecraft.client.server.IntegratedServer)obj).m_129795_();
            } catch (Error E2) {
                if (splan.ClassExist("net.minecraft.server.MinecraftServer$ServerResourcePackInfo"))// 1.19 - 1.20
                    return splan.instance.ResourcePackUrl;
                else {
                    splan.LOGGER.error("Error Getting Resource Pack URL");
                    splan.LOGGER.error("Error 1:",E1);
                    splan.LOGGER.error("Error 2:",E2);
                }
            }
        }
        return null;
    }
    public String getResourcePackHash() {
        try {// 1.13 - 1.16
            return ((net.minecraft.server.integrated.IntegratedServer)obj).getResourcePackHash();
        } catch (Error E1) {
            try {// 1.17 - 1.18
                return ((net.minecraft.client.server.IntegratedServer)obj).m_129796_();
            } catch (Error E2) {
                if (splan.ClassExist("net.minecraft.server.MinecraftServer$ServerResourcePackInfo"))// 1.19 - 1.20
                    return splan.instance.ResourcePackHash;
                else {
                    splan.LOGGER.error("Error Getting Resource Pack Hash");
                    splan.LOGGER.error("Error 1:",E1);
                    splan.LOGGER.error("Error 2:",E2);
                }
            }
        }
        return null;
    }
    public String getMOTD() {
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
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
        try {// 1.13 - 1.16
            return ((net.minecraft.server.integrated.IntegratedServer)obj).getMaxPlayers();
        } catch (Error E1) {
            try {// 1.17 - 1.18
                return ((net.minecraft.client.server.IntegratedServer)obj).m_7418_();
            } catch (Error E2) {
                splan.LOGGER.error("Error Getting Max Players");
                splan.LOGGER.error("Error 1:",E1);
                splan.LOGGER.error("Error 2:",E2);
            }
        }
        return 20;
    }
    public Object getObj() {
        return obj;
    }
    public java.nio.file.Path getWorldPath(String s) {
        try {// 1.16
            return ((net.minecraft.server.integrated.IntegratedServer)obj).func_240776_a_(new net.minecraft.world.storage.FolderName(s));
        } catch (Error E1) {
            try {// 1.17 - 1.20
                return ((net.minecraft.client.server.IntegratedServer)obj).m_129843_(new net.minecraft.world.level.storage.LevelResource(s));
            } catch (Error E2) {
                splan.LOGGER.error("Error Getting World Path");
                splan.LOGGER.error("Error 1:",E1);
                splan.LOGGER.error("Error 2:",E2);
            }
        }
        return null;
    }
    public boolean terminateNetworkSystemEndpoints() {
        try {//1.13 - 1.16
            ((net.minecraft.server.integrated.IntegratedServer)obj).getNetworkSystem().terminateEndpoints();
        } catch (Error E1) {
            try {// 1.17 - 1.20
                ((net.minecraft.client.server.IntegratedServer)obj).m_129919_().m_9718_();
            } catch (Error E2) {
                splan.LOGGER.error("Error Stopping LAN");
                splan.LOGGER.error("Error 1:",E1);
                splan.LOGGER.error("Error 2:",E2);
                return false;
            }
        }
        return true;
    }
    public boolean interruptLanServerPing() {
        try {// 1.13
            java.lang.reflect.Field f=splan.getField(net.minecraft.server.integrated.IntegratedServer.class,"lanPinger","field_71345_q");
            net.minecraft.client.multiplayer.ThreadLanServerPing lsp=(net.minecraft.client.multiplayer.ThreadLanServerPing) f.get(obj);
            lsp.interrupt();
            f.set(obj,null);
        } catch (Exception|Error E1) {
            try {// 1.14 - 1.16
                java.lang.reflect.Field f=splan.getField(net.minecraft.server.integrated.IntegratedServer.class,"lanPinger","field_71345_q");
                net.minecraft.client.multiplayer.LanServerPingThread lsp=(net.minecraft.client.multiplayer.LanServerPingThread) f.get(obj);
                lsp.interrupt();
                f.set(obj,null);
            } catch (Exception|Error E2) {
                try {// 1.17 - 1.20
                    java.lang.reflect.Field f=splan.getField(net.minecraft.client.server.IntegratedServer.class,"lanPinger","f_120018_");
                    net.minecraft.client.server.LanServerPinger lsp=(net.minecraft.client.server.LanServerPinger) f.get(obj);
                    lsp.interrupt();
                    f.set(obj,null);
                } catch (Exception|Error E3) {
                    splan.LOGGER.error("Error Stopping LAN");
                    splan.LOGGER.error("Error 1:",E1);
                    splan.LOGGER.error("Error 2:",E2);
                    splan.LOGGER.error("Error 3:",E3);
                    return false;
                }
            }
        }
        return true;
    }
    public boolean setPublishedPort(int i) {
        try {// 1.13 - 1.16
            splan.getField(net.minecraft.server.integrated.IntegratedServer.class,"publishedPort","field_195580_l").set(obj,i);
        } catch (Exception|Error E1) {
            try {
                splan.getField(net.minecraft.client.server.IntegratedServer.class,"publishedPort","f_120017_").set(obj,i);
            } catch (Exception|Error E2) {
                splan.LOGGER.error("Error Stopping LAN");
                splan.LOGGER.error("Error 1:",E1);
                splan.LOGGER.error("Error 2:",E2);
                return false;
            }
        }
        return true;
    }
}
