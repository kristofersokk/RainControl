package com.timotheteus.raincontrol;

import com.timotheteus.raincontrol.config.Config;
import com.timotheteus.raincontrol.packets.PacketConfig;
import com.timotheteus.raincontrol.packets.PacketTypes;
import com.timotheteus.raincontrol.proxy.CommonProxy;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod(modid = ModUtil.MOD_ID,
        name = ModUtil.NAME,
        version = ModUtil.VERSION,
        acceptedMinecraftVersions = "[1.12, 1.13)",
        dependencies = "required-after:redstoneflux;" +
                "required-after:tesla;"
)
@Mod.EventBusSubscriber
public class RainControl {

    //TODO is config syncing really working? --WIP
	
	@Mod.Instance(ModUtil.MOD_ID)
    public static RainControl instance;

    public static final PacketTypes.CONFIG[] configPackets = new PacketTypes.CONFIG[]{
            PacketTypes.CONFIG.GENERATOR_GENERATION
    };
    @SidedProxy(clientSide = ModUtil.ClIENT_PROXY, serverSide = ModUtil.SERVER_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit(event);
    }

    @SubscribeEvent
    public void entityJoined(PlayerEvent.PlayerLoggedInEvent event) {
        Entity entity = event.player;
        if (entity instanceof EntityPlayerMP) {
            new PacketConfig(configPackets, Config.generatorProduce).sendTo((EntityPlayerMP) entity);
        }
    }

}
