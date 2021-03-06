package me.timlampen.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import me.timlampen.commands.CommandHandler;
import me.timlampen.commands.Rankup;
import me.timlampen.commands.RankupCommand;
import me.timlampen.currency.BackPack;
import me.timlampen.currency.PackPlayer;
import me.timlampen.currency.RankDis;
import me.timlampen.currency.ScratchCards;
import me.timlampen.currency.ScratchInv;
import me.timlampen.currency.Tokens;
import me.timlampen.currency.UpgradeInv;
import me.timlampen.dcscoreboard.BlockListener;
import me.timlampen.dcscoreboard.DropParty;
import me.timlampen.dcscoreboard.SBVoteListener;
import me.timlampen.dcscoreboard.ScoreBoardListener;
import me.timlampen.explosions.ExtraExplosions;
import me.timlampen.explosions.GrenadeListener;
import me.timlampen.explosions.MineBow;
import me.timlampen.explosions.RocketListener;
import me.timlampen.extras.ColorPick;
import me.timlampen.extras.EnragedMining;
import me.timlampen.extras.Goodies;
import me.timlampen.extras.Hologram;
import me.timlampen.extras.NameInfo;
import me.timlampen.extras.Prestige;
import me.timlampen.extras.SellAll;
import me.timlampen.extras.WarpInv;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Main extends JavaPlugin{
	private Prestige pre;
	private NameInfo ni;
	private UpgradeInv ui;
	private SellAll sa;
	private BackPack bp;
	private RankDis rd;
	private CommandHandler ch;
	private WarpInv wi;
	private ColorPick cp;
	private ScratchInv si;
	private ScratchCards sc;
	private Parse pa;
	private Tokens t;
	private EnragedMining enm;
	private MineBow mb;
	private BlockListener bl;
	private ScoreBoardListener sbl;
	private GrenadeListener gl;
	private SBVoteListener vl;
	private RocketListener rl;
	private DropParty dp;
	private Explosions e;
	private Rankup r;
	private Lang l;
	private Goodies g;
	private Hologram h;
	private ExtraExplosions ee;
	Logger log = Bukkit.getLogger();
    public static Permission perms = null;
    public static Economy eco = null;
    public static int vote;
   // public EffectLib lib;
    //public EffectManager em;
	public Map<UUID, PackPlayer> playerInfos;
    private static Plugin plugin;
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	public ArrayList<UUID> message = new ArrayList<UUID>();
    public static Plugin getPlugin(){
    	return plugin;
    }
    public PackPlayer getPlayerInfo(UUID name) {
        PackPlayer pi = playerInfos.get(name);
        if (pi != null) {
           return pi;
        }
        pi = new PackPlayer();
        playerInfos.put(name, pi);
        return pi;
    }
    public String getPrefix(){
    	
    	return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix", ChatColor.BOLD + "["+ChatColor.RED+ChatColor.BOLD+"D"+ChatColor.GRAY+ChatColor.BOLD+"C"+ChatColor.WHITE+ChatColor.BOLD+"]"+ChatColor.RESET + " "));
    }
	@Override
	public void onEnable(){
		console.sendMessage(getPrefix() + "Starting Load...");
		playerInfos = new HashMap<UUID, PackPlayer>();
		plugin = this;
		//lib = EffectLib.instance();
		//em = new EffectManager(lib);
		console.sendMessage(getPrefix() + "Loading Config...");
		if(getConfig().getInt("CurrentVotes")==0){
			vote = getConfig().getInt("MaxVotes");
		}
		else{
			vote = getConfig().getInt("CurrentVotes");	
		}
		saveDefaultConfig();
		setupEconomy();
		setupPermissions();
		console.sendMessage(getPrefix() + "Success! Registering Events...");
		e = new Explosions();
		pre = new Prestige(this);
		cp = new ColorPick(this);
		enm = new EnragedMining(this);
		bl = new BlockListener(this);
		h = new Hologram(this);
		sc = new ScratchCards(this);
		l = new Lang(this);
		pa = new Parse(l);
		sa = new SellAll(this, pa);
		r = new Rankup(l);
		si = new ScratchInv(sc, this, l);
		ee = new ExtraExplosions(this, l);
		rd = new RankDis(this, r);
		mb = new MineBow(this, e);
		gl = new GrenadeListener(this, e);
		rl = new RocketListener(this, e);
		dp = new DropParty(this, gl, rl);
		vl = new SBVoteListener(this, dp);
		t = new Tokens(this, l, pa);
		ni = new NameInfo(this, t, pre);
		bp = new BackPack(this, t, l);
		wi = new WarpInv(this, l, r);
		g = new Goodies(this, l, gl, rl);
		sbl = new ScoreBoardListener(this, bl, r);
		ui = new UpgradeInv(this,  t);
		ch = new CommandHandler(gl, dp, bl, rl, this, l, sbl, mb, g, t, sc, rd, bp, sa, ui, pre);
		Bukkit.getPluginManager().registerEvents(bl, this);
		Bukkit.getPluginManager().registerEvents(sbl, this);
		Bukkit.getPluginManager().registerEvents(gl, this);
		Bukkit.getPluginManager().registerEvents(rl, this);
		Bukkit.getPluginManager().registerEvents(gl, this);
		Bukkit.getPluginManager().registerEvents(rl, this);
		Bukkit.getPluginManager().registerEvents(g, this);
		Bukkit.getPluginManager().registerEvents(h, this);
		Bukkit.getPluginManager().registerEvents(ee, this);
		Bukkit.getPluginManager().registerEvents(mb, this);
		Bukkit.getPluginManager().registerEvents(t, this);
		Bukkit.getPluginManager().registerEvents(enm, this);
		Bukkit.getPluginManager().registerEvents(sc, this);
		Bukkit.getPluginManager().registerEvents(si, this);
		Bukkit.getPluginManager().registerEvents(cp, this);
		Bukkit.getPluginManager().registerEvents(wi, this);
		Bukkit.getPluginManager().registerEvents(bp, this);
		Bukkit.getPluginManager().registerEvents(ui, this);
		Bukkit.getPluginManager().registerEvents(ni, this);
		getCommand("prestige").setExecutor(ch);
		getCommand("crystalinv").setExecutor(ch);
		getCommand("sall").setExecutor(ch);
		getCommand("sellall").setExecutor(ch);
		getCommand("bag").setExecutor(ch);
		getCommand("backpack").setExecutor(ch);
		getCommand("dc").setExecutor(ch);
		getCommand("darknesscraft").setExecutor(ch);
		getCommand("casino").setExecutor(ch);
		getCommand("warps").setExecutor(ch);
		getCommand("crystals").setExecutor(ch);
		getCommand("crystal").setExecutor(ch);
		getCommand("ranks").setExecutor(ch);
		getCommand("rankup").setExecutor(new RankupCommand(this));
		getCommand("withdraw").setExecutor(ch);
		getCommand("scoreboard").setExecutor(ch);
		console.sendMessage(getPrefix() + "Success! Implementing Scoreboard...");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()){
					player.setScoreboard(sbl.setupScoreboard(player));
					ui.checkEffects(player);
				}
				
			}}, 0, 20*5);
		console.sendMessage(getPrefix() + "Success! Cleaning up...");
		for(String s: getConfig().getStringList("SafeRanks")){
			sbl.safe.add(s);
		}
		for(String s: getConfig().getStringList("MineRegions")){
			regions.add(s);
		}
		for(String s: getConfig().getStringList("DropParty.Commands")){
			dp.cmd.add(s);
		}
		for(String s: getConfig().getStringList("Goodie.Commands")){
			g.cmd.add(s);
		}
		bp.loadItems();
		ni.loadItems();
		pre.loadPresitge();
		console.sendMessage(getPrefix() + "Success! Plugin Startup Complete!");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable(){
		console.sendMessage(getPrefix() + "Saving Files...");
		//em.dispose();
		getConfig().set("CurrentVotes", vote);
		saveDefaultConfig();
		t.saveTokens();
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getScoreboard()!=null){
				player.getScoreboard().resetScores(player);
			}
		}
		for(UUID s : playerInfos.keySet()){
				bp.saveItems(s);
		}
		for(UUID s : ni.blks.keySet()){
			ni.saveItems(s);
		}
		for(UUID s : pre.rank.keySet()){
			pre.savePrestige(s);
		}
		log.info("Config saving complete!");
	}
	
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            eco = economyProvider.getProvider();
        }

        return (eco != null);
    }
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	Bukkit.broadcastMessage("Its Null!");
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	private ArrayList<String> regions = new ArrayList<String>();
	public boolean isInRegion(Player player){
		RegionManager rm = getWorldGuard().getRegionManager(player.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(player.getLocation());
		LinkedList< String > parentNames = new LinkedList< String >();
		LinkedList< String > regions = new LinkedList< String >();
		for(ProtectedRegion region : set){
			String id = region.getId();
			regions.add(id);
			ProtectedRegion parent = region.getParent();
			while(parent !=null){
				parentNames.add(parent.getId());
				parent = parent.getParent();
			}
		}
		for(String name: parentNames){
			regions.remove(name);
		}
		if(regions.size()<1){
			return true;
		} 
		for(String s: getConfig().getStringList("MineRegions")){
			if(regions.contains(s)){
				return true;
			}
		}
		parentNames.clear();
		regions.clear();
		return false;
	}
	public boolean isInRegion(Block e){
		RegionManager rm = getWorldGuard().getRegionManager(e.getWorld());
		ApplicableRegionSet set = rm.getApplicableRegions(e.getLocation());
		LinkedList< String > parentNames = new LinkedList< String >();
		LinkedList< String > regions = new LinkedList< String >();
		for(ProtectedRegion region : set){
			String id = region.getId();
			regions.add(id);
			ProtectedRegion parent = region.getParent();
			while(parent !=null){
				parentNames.add(parent.getId());
				parent = parent.getParent();
			}
		}
		for(String name: parentNames){
			regions.remove(name);
		}
		if(regions.size()<1){
			return false;
		} 
		for(String s: getConfig().getStringList("MineRegions")){
			if(regions.contains(s)){
				return true;
			}
		}
		parentNames.clear();
		regions.clear();
		return false;
	}
}
