package net.meano.LockBlock;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LockBlockMain extends JavaPlugin {
	public void onEnable(){
		//Log开始记录
		getLogger().info("LockBlock已载入");
		PluginManager PM = Bukkit.getServer().getPluginManager();
		PM.registerEvents(new LockBlockListeners(this), this);
	}
	public void onDisable(){
		
	}
	@SuppressWarnings("deprecation")
	public List<Sign> BlockFindSign(Block SourceBlock,boolean iterate){
		List<Sign> SignsGet = new CopyOnWriteArrayList<Sign>();
		Block BlockGet;
		Material SourceType = SourceBlock.getType();
		if(isChest(SourceBlock)||isSingleLockBlock(SourceBlock)){
			//检查方块东边的方块
			BlockGet = SourceBlock.getRelative(BlockFace.EAST);
			if(BlockGet.getType()==Material.WALL_SIGN){
				//确保紧贴
				//getLogger().info("牌子在东边");
				//getLogger().info("牌子Data"+BlockGet.getData());
				if(BlockGet.getData() == 5){
					SignsGet.add((Sign)BlockGet.getState());
				}
			}else if(BlockGet.getType()==SourceType&&iterate){
				SignsGet.addAll(BlockFindSign(BlockGet,false));
			}
			//检查方块西边的方块
			BlockGet = SourceBlock.getRelative(BlockFace.WEST);
			if(BlockGet.getType()==Material.WALL_SIGN){
				//确保紧贴
				//getLogger().info("牌子在西边");
				//getLogger().info("牌子Data"+BlockGet.getData());
				if(BlockGet.getData() == 4){
					SignsGet.add((Sign)BlockGet.getState());
				}
			}else if(BlockGet.getType()==SourceType&&iterate){
				SignsGet.addAll(BlockFindSign(BlockGet,false));
			}
			//检查方块北边的方块
			BlockGet = SourceBlock.getRelative(BlockFace.NORTH);
			if(BlockGet.getType()==Material.WALL_SIGN){
				//确保紧贴
				//getLogger().info("牌子在北边");
				//getLogger().info("牌子Data"+BlockGet.getData());
				if(BlockGet.getData() == 2){
					SignsGet.add((Sign)BlockGet.getState());
				}
			}else if(BlockGet.getType()==SourceType&&iterate){
				SignsGet.addAll(BlockFindSign(BlockGet,false));
			}
			//检查方块南边的方块
			BlockGet = SourceBlock.getRelative(BlockFace.SOUTH);
			if(BlockGet.getType()==Material.WALL_SIGN){
				//确保紧贴
				//getLogger().info("牌子在南边");
				//getLogger().info("牌子Data"+BlockGet.getData());
				if(BlockGet.getData() == 3){
					SignsGet.add((Sign)BlockGet.getState());
				}
			}else if(BlockGet.getType()==SourceType&&iterate){
				SignsGet.addAll(BlockFindSign(BlockGet,false));
			}
		}
		return SignsGet;
	}
	public boolean isChest(Block block){
		if(block.getType().equals(Material.CHEST)){
			return true;
		}else if(block.getType().equals(Material.TRAPPED_CHEST)){
			return true;
		}else{
			return false;
		}
	}
	public boolean isSingleLockBlock(Block block){
		if(block.getType().equals(Material.HOPPER)){
			return true;
		}else if(block.getType().equals(Material.FURNACE)){
			return true;
		}else if(block.getType().equals(Material.BURNING_FURNACE)){
			return true;
		}else if(block.getType().equals(Material.DISPENSER)){
			return true;
		}else{
			return false;
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return true;	
	}
}
