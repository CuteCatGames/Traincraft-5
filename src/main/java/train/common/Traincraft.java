package train.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import train.common.api.LiquidManager;
import train.common.blocks.TCBlocks;
import train.common.core.CommonProxy;
import train.common.core.CreativeTabTraincraft;
import train.common.core.TrainModCore;
import train.common.core.handlers.*;
import train.common.generation.ComponentVillageTrainstation;
import train.common.generation.WorldGenWorld;
import train.common.items.TCItems;
import train.common.library.Info;
import train.common.recipes.AssemblyTableRecipes;

import java.io.File;

@Mod(modid = Info.modID, name = Info.modName, version = Info.modVersion)
public class Traincraft {

	/* TrainCraft instance */
	@Mod.Instance(Info.modID)
	public static Traincraft instance;

	/* TrainCraft proxy files */
	@SidedProxy(clientSide = "train.client.core.ClientProxy", serverSide = "train.common.core.CommonProxy")
	public static CommonProxy proxy;

	/* TrainCraft Logger */
	public static Logger tcLog = LogManager.getLogger(Info.modName);

	/** Network Channel to send packets on */
	public static SimpleNetworkWrapper modChannel;
	public static SimpleNetworkWrapper keyChannel;
	public static SimpleNetworkWrapper rotationChannel;


	public static SimpleNetworkWrapper slotschannel;
	public static SimpleNetworkWrapper ignitionChannel;
	public static SimpleNetworkWrapper brakeChannel;
	public static SimpleNetworkWrapper lockChannel;
	public static SimpleNetworkWrapper builderChannel;
	public static SimpleNetworkWrapper updateTrainIDChannel = NetworkRegistry.INSTANCE.newSimpleChannel("TrainIDChannel");
    public static SimpleNetworkWrapper updateDestinationChannel = NetworkRegistry.INSTANCE.newSimpleChannel("updateDestnChannel");


	public static final SimpleNetworkWrapper itaChannel = NetworkRegistry.INSTANCE.newSimpleChannel("TransmitterAspect");
	public static  SimpleNetworkWrapper itsChannel = NetworkRegistry.INSTANCE.newSimpleChannel("TransmitterSpeed");
	//public static  SimpleNetworkWrapper mtcsChannel = NetworkRegistry.INSTANCE.newSimpleChannel("MTCSysSetSpeed");
	public static  SimpleNetworkWrapper itnsChannel = NetworkRegistry.INSTANCE.newSimpleChannel("TransmitterNextSpeed");
	public static final SimpleNetworkWrapper mtlChannel = NetworkRegistry.INSTANCE.newSimpleChannel("MTCLevelUpdater");
	public static final SimpleNetworkWrapper msChannel = NetworkRegistry.INSTANCE.newSimpleChannel("MTCStatus");
	public static final SimpleNetworkWrapper mscChannel = NetworkRegistry.INSTANCE.newSimpleChannel("MTCStatusToClient");
	public static final SimpleNetworkWrapper atoChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ATOPacket");
	public static final SimpleNetworkWrapper atoDoSlowDownChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ATODoSlowDown");
	public static final SimpleNetworkWrapper atoDoAccelChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ATODoAccel");
	public static final SimpleNetworkWrapper atoSetStopPoint = NetworkRegistry.INSTANCE.newSimpleChannel("ATOSetStopPoint");
	public static final SimpleNetworkWrapper NCSlowDownChannel = NetworkRegistry.INSTANCE.newSimpleChannel("NCDoSlowDown");
	//public static final SimpleNetworkWrapper ctChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ctmChannel");
	public static final SimpleNetworkWrapper gsfsChannel = NetworkRegistry.INSTANCE.newSimpleChannel("gsfsChannel");
	public static final SimpleNetworkWrapper gsfsrChannel = NetworkRegistry.INSTANCE.newSimpleChannel("gsfsReturnChannel");
	public static final SimpleNetworkWrapper playSoundOnClientChannel  = NetworkRegistry.INSTANCE.newSimpleChannel(" SoundOnCChannel");


	public static File configDirectory;

	/* Creative tab for Traincraft */
	public static CreativeTabs tcTab;

	public ArmorMaterial armor = EnumHelper.addArmorMaterial("Armor","", 5, new int[] { 1, 2, 2, 1 }, 25);
	public ArmorMaterial armorCloth = EnumHelper.addArmorMaterial("TCcloth","", 5, new int[] {1, 2, 2, 1}, 25);
	public ArmorMaterial armorCompositeSuit = EnumHelper.addArmorMaterial("TCsuit","", 70, new int[] {2, 6, 5, 2}, 50);
	public static int trainArmor;
	public static int trainCloth;
	public static int trainCompositeSuit;

	
	public static WorldGenWorld worldGen;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		tcLog.info("Starting Traincraft " + Info.modVersion + "!");
		/* Config handler */
		configDirectory= event.getModConfigurationDirectory();
		ConfigHandler.init(new File(event.getModConfigurationDirectory(), Info.modName + ".cfg"));

		/* Register the KeyBinding Handler */
		proxy.registerKeyBindingHandler();

		/* Register Items, Blocks, ... */
		tcLog.info("Initialize Blocks, Items, ...");
		tcTab = new CreativeTabTraincraft(CreativeTabs.getNextID(), "Traincraft");
		trainArmor = proxy.addArmor("armor");
		trainCloth = proxy.addArmor("Paintable");
		trainCompositeSuit = proxy.addArmor("CompositeSuit");

		tcLog.info("Finished PreInitialization");
	}


	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		tcLog.info("Start Initialization");

		TCBlocks.init(event.getSide());
		TCItems.init(event.getSide());
		EntityHandler.init();
		proxy.registerTileEntities();
		proxy.registerSounds();
		proxy.setHook(); // Moved file needed to run JLayer, we need to set a hook in order to retrieve it

		GameRegistry.registerFuelHandler(new FuelHandler());
		AchievementHandler.load();
		AchievementPage.registerAchievementPage(AchievementHandler.tmPage);
		GameRegistry.registerWorldGenerator(worldGen = new WorldGenWorld(),5);

		//Retrogen Handling
		RetrogenHandler retroGen = new RetrogenHandler();
		MinecraftForge.EVENT_BUS.register(retroGen);
		FMLCommonHandler.instance().bus().register(retroGen);

		MapGenStructureIO.registerStructureComponent(ComponentVillageTrainstation.class, "Trainstation");

		if (Loader.isModLoaded("ComputerCraft")) {
			try {
				proxy.registerComputerCraftPeripherals();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		/* Other Proxy init */
		tcLog.info("Initialize Renderer and Events");
		proxy.registerRenderInformation();
		proxy.registerEvents(event);

		/* Networking and Packet initialisation */
		PacketHandler.init();

		//proxy.getCape();

		/* GUI handler initiation */
		tcLog.info("Initialize Gui");
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		FMLCommonHandler.instance().bus().register(new CraftingHandler());

		/* Ore dictionary */
		OreHandler.registerOres();

		/* Recipes */
		tcLog.info("Initialize Recipes");
		RecipeHandler.initBlockRecipes();
		RecipeHandler.initItemRecipes();
		RecipeHandler.initSmeltingRecipes();
		AssemblyTableRecipes.recipes();

		/* Register the liquids */
		tcLog.info("Initialize Fluids");
		LiquidManager.getInstance().registerLiquids();

		/* Liquid FX */
		proxy.registerTextureFX();

		/*Trainman Villager*/
		tcLog.info("Initialize Station Chief Villager");
		VillagerRegistry.instance().registerVillagerId(ConfigHandler.TRAINCRAFT_VILLAGER_ID);
		VillagerRegistry.IVillageCreationHandler villageHandler = new VillagerTraincraftHandler();
		VillagerRegistry.instance().registerVillageCreationHandler(villageHandler);
		proxy.registerVillagerSkin(ConfigHandler.TRAINCRAFT_VILLAGER_ID, "station_chief.png");
		//completley unnecessary now?
		//VillagerRegistry.instance().registerVillageTradeHandler(ConfigHandler.TRAINCRAFT_VILLAGER_ID, villageHandler);


		proxy.registerBookHandler();

		
		tcLog.info("Finished Initialization");


	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		tcLog.info("Start to PostInitialize");
		tcLog.info("Register ChunkHandler");

		tcLog.info("Activation Mod Compatibility");
		TrainModCore.ModsLoaded();
		LiquidManager.getLiquidsFromDictionnary();
		if (Loader.isModLoaded("OpenComputers")) {
			tcLog.info("OpenComputers integration successfully activated!");
		}
		tcLog.info("Finished PostInitialization");
	}

	@Mod.EventHandler
	public void serverStop(FMLServerStoppedEvent event) {
		proxy.killAllStreams();
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new tcAdminPerm());
	}


	public class tcAdminPerm extends CommandBase {
		public String getCommandName() {return "tc.admin";}
		public String getCommandUsage(ICommandSender CommandSender) {return "/tcadmin";}
		public int getRequiredPermissionLevel() {return 2;}

		public void processCommand(ICommandSender CommandSender, String[] par2ArrayOfStr) {
			try {
				getCommandSenderAsPlayer(CommandSender).addChatMessage(
						new ChatComponentText(
								"this command exists as a placeholder to allow admin permissions in TC via plugins and mds such as GroupManager and Forge Essentials"));
			} catch (PlayerNotFoundException e) {
				System.out.println("Attempted to get player that didn't exist");
				e.printStackTrace();
			}

		}
	}


}
