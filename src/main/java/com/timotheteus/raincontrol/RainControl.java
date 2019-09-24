package com.timotheteus.raincontrol;

import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.proxy.ClientProxy;
import com.timotheteus.raincontrol.proxy.CommonProxy;
import com.timotheteus.raincontrol.proxy.ServerProxy;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//@Mod(modid = ModUtil.MOD_ID,
//        name = ModUtil.NAME,
//        version = ModUtil.VERSION,
//        acceptedMinecraftVersions = "[1.12, 1.13)"
//)
//@Mod(value = ModUtil.MOD_ID)
@Mod(value = ModUtil.MOD_ID)
public class RainControl {

    public static RainControl instance;

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public RainControl() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        proxy.commonSetup(event);
    }

    public void clientSetup(FMLClientSetupEvent event){
        proxy.clientSetup(event);
    }

//    @SubscribeEvent
//    public static void entityJoined(PlayerEvent.PlayerLoggedInEvent event) {
//        Entity entity = event.player;
//        if (entity instanceof EntityPlayerMP) {
//            new PacketConfig().sendTo((EntityPlayerMP) entity);
//        }
//    }

}