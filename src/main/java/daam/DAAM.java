package daam;

import daam.client.RegionHandler;
import daam.common.items.ItemRegister;
import daam.common.network.NetworkHandler;
import daam.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.discovery.ContainerType;
import net.minecraftforge.fml.common.discovery.ModCandidate;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@Mod(modid = DAAM.MODID, name = DAAM.NAME, version = DAAM.VERSION)
public class DAAM {

    public static final String MODID = "daam";
    public static final String NAME = "Dynamic Ambience and Music";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final NetworkHandler NETWORK = new NetworkHandler();
    /**
     * Set it to false if you want to run it in your environment, but if you want to run it on a production client, set it to true
     */
    public static final boolean remap = false;
    public static final CreativeTabs TAB = new CreativeTabs("daam") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemRegister.regionEditor);
        }
    };
    public static File DAAM_DIR;
    public static File SOUNDS_DIR;
    public static File SOUND_JSON;
    // ########## READ IT !!!!!!!!!!!!!!!!!!!!!
    @SidedProxy(clientSide = "daam.proxy.ClientProxy", serverSide = "daam.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static Logger logger;

    public static String getGameFolder() {
        return ((File) (FMLInjectionData.data()[6])).getAbsolutePath();
    }

    @EventHandler
    public void constructionEvent(FMLConstructionEvent event) {
        DAAM_DIR = new File(getGameFolder(), "DynamicAmbienceAndMusic");
        SOUNDS_DIR = new File(Paths.get(DAAM_DIR.getPath(), "assets/daam/sounds").toUri());
        SOUND_JSON = new File(Paths.get(DAAM_DIR.getPath(), "assets/daam/sounds.json").toUri());

        if (!DAAM_DIR.exists()) {
            DAAM_DIR.mkdir();
            try {
                SOUNDS_DIR.mkdirs();
                SOUND_JSON.createNewFile();

                String jsonContent = "{\n\n}";
                Files.write(SOUND_JSON.toPath(), jsonContent.getBytes());

                LOGGER.info("The file was successfully created and completed: " + SOUND_JSON);
            } catch (IOException e) {
                LOGGER.info("An error has occurred: " + e.getMessage());
                e.printStackTrace();
            }
            LOGGER.error("Created DynamicAmbienceAndMusic folder, this is necessary to load sounds.");
        }

        if (FMLCommonHandler.instance().getSide().isClient()) {
            try {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("modid", MODID);
                map.put("name", "DynamicAmbienceAndMusic");
                map.put("version", "1");
                FMLModContainer container = new FMLModContainer("daam.DAAM", new ModCandidate(DAAM_DIR, DAAM_DIR, ContainerType.DIR), map);
                container.bindMetadata(MetadataCollection.from(null, ""));
                FMLClientHandler.instance().addModAsResource(container);
                FMLClientHandler.instance().refreshResources();
                LOGGER.info("######### Dynamic Ambience And Music successfully loaded #########");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info(e.getMessage());
            }
            RegionHandler.loadSounds();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        NETWORK.registry();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("         ,.  '             ,.-·.          ,. -  .,                              ,.,   '          ,-·-.          ,'´¨;    ");
        logger.info("       /   ';\\            /    ;'\\'      ,' ,. -  .,  `' ·,                     ;´   '· .,         ';   ';\\      ,'´  ,':\\'  ");
        logger.info("     ,'   ,'::'\\          ;    ;:::\\     '; '·~;:::::'`,   ';\\                .´  .-,    ';\\        ;   ';:\\   .'   ,'´::'\\' ");
        logger.info("    ,'    ;:::';'        ';    ;::::;'     ;   ,':\\::;:´  .·´::\\'             /   /:\\:';   ;:'\\'      '\\   ';::;'´  ,'´::::;'  ");
        logger.info("    ';   ,':::;'          ;   ;::::;      ;  ·'-·'´,.-·'´:::::::';          ,'  ,'::::'\\';  ;::';        \\  '·:'  ,'´:::::;' '  ");
        logger.info("    ;  ,':::;' '         ';  ;'::::;     ;´    ':,´:::::::::::·´'       ,.-·'  '·~^*'´¨,  ';::;         '·,   ,'::::::;'´    ");
        logger.info("   ,'  ,'::;'            ;  ';:::';       ';  ,    `·:;:-·'´            ':,  ,·:²*´¨¯'`;  ;::';          ,'  /::::::;'  '    ");
        logger.info("   ;  ';_:,.-·´';\\‘     ';  ;::::;'      ; ,':\\'`:·.,  ` ·.,           ,'  / \\::::::::';  ;::';        ,´  ';\\::::;'  '      ");
        logger.info("   ',   _,.-·'´:\\:\\‘     \\*´\\:::;‘      \\·-;::\\:::::'`:·-.,';        ,' ,'::::\\·²*'´¨¯':,'\\:;         \\`*ª'´\\\\::/‘         ");
        logger.info("    \\¨:::::::::::\\';      '\\::\\:;'        \\::\\:;'` ·:;:::::\\::\\'      \\`¨\\:::/          \\::\\'          '\\:::::\\';  '        ");
        proxy.init(event);
        proxy.server();
    }

}
