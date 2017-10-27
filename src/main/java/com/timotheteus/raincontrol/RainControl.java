package com.timotheteus.raincontrol;

import com.timotheteus.raincontrol.proxy.CommonProxy;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModUtil.MOD_ID,
        name = ModUtil.NAME,
        version = ModUtil.VERSION,
        acceptedMinecraftVersions = "[1.12, 1.13)",
        dependencies = "required-after:redstoneflux;" +
                "after:tesla;"
)
@Mod.EventBusSubscriber
public class RainControl {

	@Mod.Instance(ModUtil.MOD_ID)
    public static RainControl instance;

    @SidedProxy(clientSide = ModUtil.ClIENT_PROXY, serverSide = ModUtil.SERVER_PROXY)
    private static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);//NB! Needed for models to be registered
        proxy.preInit(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }


//    @SubscribeEvent
//    public static void entityJoined(PlayerEvent.PlayerLoggedInEvent event) {
//        Entity entity = event.player;
//        if (entity instanceof EntityPlayerMP) {
//            new PacketConfig().sendTo((EntityPlayerMP) entity);
//        }
//    }

}
