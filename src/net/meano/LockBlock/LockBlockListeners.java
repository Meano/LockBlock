package net.meano.LockBlock;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class LockBlockListeners implements Listener{
	LockBlockMain LBM;
	public LockBlockListeners(LockBlockMain GetPlugin){
		LBM=GetPlugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		//获得被破坏的方块
		Block block = event.getBlock();
		Block blockUp = block.getRelative(BlockFace.UP);
		Player player = event.getPlayer();
		BlockBreakDeal(block,event,true,player);
		BlockBreakDeal(blockUp,event,false,player);
		BlockBreakDeal(block.getRelative(BlockFace.EAST).getRelative(BlockFace.UP),event,false,player);
		BlockBreakDeal(block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP),event,false,player);
		BlockBreakDeal(block.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP),event,false,player);
		BlockBreakDeal(block.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP),event,false,player);
	}
	public void BlockBreakDeal(Block block,BlockBreakEvent event,boolean isLockBlock,Player player){
		if(block.getType().equals(Material.WALL_SIGN)){
			Sign sign = (Sign) block.getState();
			if(sign.getLine(0).toLowerCase().equals("private")){
				sign.setLine(0, "[Private]");
				sign.update(true);
				if(isLockBlock)
					event.setCancelled(true);
			}
		}else if(LBM.isChest(block)){
			//LBM.getLogger().info("检测到牌子");
			List<Sign> SignsGet = LBM.BlockFindSign(block, true);
			//LBM.getLogger().info("牌子准备遍历");
			for(Sign sign : SignsGet){
				//LBM.getLogger().info("牌子遍历取得");
				if(sign.getLine(0).toLowerCase().equals("private")){
					//LBM.getLogger().info("牌子判断完成");
					sign.setLine(0, "[Private]");
					sign.update(true);
					if(isLockBlock)
						event.setCancelled(true);
					else if(!sign.getLine(1).toLowerCase().equals(player.getName().toLowerCase()))
						event.setCancelled(true);
				}else if(sign.getLine(0).toLowerCase().equals("[private]")){
					if(!sign.getLine(1).toLowerCase().equals(player.getName().toLowerCase()))
						if(!player.isOp())
							event.setCancelled(true);
				}
			}
		}else if(LBM.isSingleLockBlock(block)){
			List<Sign> SignsGet = LBM.BlockFindSign(block, false);
			for(Sign sign : SignsGet){
				if(sign.getLine(0).toLowerCase().equals("private")){
					sign.setLine(0, "[Private]");
					sign.update(true);
					if(isLockBlock)
						event.setCancelled(true);
					else if(!sign.getLine(1).toLowerCase().equals(player.getName().toLowerCase()))
						event.setCancelled(true);
				}else if(sign.getLine(0).toLowerCase().equals("[private]")){
					if(!sign.getLine(1).toLowerCase().equals(player.getName().toLowerCase()))
						if(!player.isOp())
							event.setCancelled(true);
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlaceEvent(BlockPlaceEvent event){
		//获得被放置的方块
		Block block = event.getBlock();
		//检查正在放置的牌子
		if(block.getType().equals(Material.WALL_SIGN)){
			//LBM.getLogger().info("牌子Data"+block.getData());
			switch(block.getData()){
				case 2:
					PlaceEventDeal(block,BlockFace.SOUTH,event);
					break;
				case 3:
					PlaceEventDeal(block,BlockFace.NORTH,event);
					break;
				case 4:
					PlaceEventDeal(block,BlockFace.EAST,event);
					break;
				case 5:
					PlaceEventDeal(block,BlockFace.WEST,event);
					break;
			}
		}
		else{
			PlaceEventDeal(block,BlockFace.UP,event);
			PlaceEventDeal(block.getRelative(BlockFace.EAST),BlockFace.UP,event);
			PlaceEventDeal(block.getRelative(BlockFace.SOUTH),BlockFace.UP,event);
			PlaceEventDeal(block.getRelative(BlockFace.NORTH),BlockFace.UP,event);
			PlaceEventDeal(block.getRelative(BlockFace.WEST),BlockFace.UP,event);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event){
		//不是与方块交互则退出
		if(!event.hasBlock())return;
		Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(action.equals(Action.LEFT_CLICK_BLOCK)||action.equals(Action.RIGHT_CLICK_BLOCK)){
			if(block.getType().equals(Material.WALL_SIGN)){
				Sign sign = (Sign) block.getState();
				if(sign.getLine(0).toLowerCase().equals("private")){
					sign.setLine(0, "[Private]");
					sign.update(true);
					event.setUseInteractedBlock(Result.DENY);
					event.setUseItemInHand(Result.DENY);
					player.sendMessage("牌子转换完成");
				}
			}else if(LBM.isChest(block)){
				//LBM.getLogger().info("检测到牌子");
				List<Sign> SignsGet = LBM.BlockFindSign(block, true);
				//LBM.getLogger().info("牌子准备遍历");
				for(Sign sign : SignsGet){
					//LBM.getLogger().info("牌子遍历取得");
					if(sign.getLine(0).toLowerCase().equals("private")){
						//LBM.getLogger().info("牌子判断完成");
						sign.setLine(0, "[Private]");
						sign.update(true);
						event.setUseInteractedBlock(Result.DENY);
						event.setUseItemInHand(Result.DENY);
						player.sendMessage("牌子转换完成");
					}
				}
			}else if(LBM.isSingleLockBlock(block)){
				List<Sign> SignsGet = LBM.BlockFindSign(block, false);
				for(Sign sign : SignsGet){
					if(sign.getLine(0).toLowerCase().equals("private")){
						sign.setLine(0, "[Private]");
						sign.update(true);
						event.setCancelled(true);
						player.sendMessage("牌子转换完成");
					}
				}
			}
		}
	}
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event){
		if(event.getVehicle().getType().equals(EntityType.MINECART_HOPPER)){
			if(!event.getTo().getBlock().getType().equals(Material.RAILS))
				if(!event.getTo().getBlock().getType().equals(Material.POWERED_RAIL))
					if(!event.getTo().getBlock().getType().equals(Material.ACTIVATOR_RAIL))
						if(!event.getTo().getBlock().getType().equals(Material.DETECTOR_RAIL))
							event.getVehicle().teleport(event.getFrom());
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event){
		List<Block> BlockWillDrops = event.blockList();
		Iterator<Block> DropsIterator = BlockWillDrops.iterator();
		//LBM.getLogger().info("获得列表。");
		while(DropsIterator.hasNext()){
			Block block = DropsIterator.next();
			if(block.getType().equals(Material.WALL_SIGN)){
				Sign sign = (Sign) block.getState();
				if(sign.getLine(0).toLowerCase().equals("private")){
					sign.setLine(0, "[Private]");
					sign.update(true);
					DropsIterator.remove();
				}
			}else if(LBM.isChest(block)){
				//LBM.getLogger().info("检测到牌子");
				List<Sign> SignsGet = LBM.BlockFindSign(block, true);
				//LBM.getLogger().info("牌子准备遍历");
				for(Sign sign : SignsGet){
					//LBM.getLogger().info("牌子遍历取得");
					if(sign.getLine(0).toLowerCase().equals("private")){
						//LBM.getLogger().info("牌子判断完成");
						sign.setLine(0, "[Private]");
						sign.update(true);
						DropsIterator.remove();
					}
				}
			}else if(LBM.isSingleLockBlock(block)){
				List<Sign> SignsGet = LBM.BlockFindSign(block, false);
				for(Sign sign : SignsGet){
					if(sign.getLine(0).toLowerCase().equals("private")){
						sign.setLine(0, "[Private]");
						sign.update(true);
						DropsIterator.remove();
					}
				}
			}else if(block.getType().equals(Material.BEACON)){
				DropsIterator.remove();
			}
		}
	}
	public void PlaceEventDeal(Block block,BlockFace SignFace,BlockPlaceEvent event){
		//LBM.getLogger().info("方块类型"+block.getRelative(SignFace).getType().toString());
		Block CheckBlock = block.getRelative(SignFace);
		if(LBM.isChest(CheckBlock)){
			List<Sign> SignsGet = LBM.BlockFindSign(CheckBlock, true);
			for(Sign sign : SignsGet){
				if(sign.getLine(0).toLowerCase().equals("private")){
					sign.setLine(0, "[Private]");
					sign.update(true);
					block.setType(Material.AIR);
					event.setCancelled(true);
				}else if(sign.getLine(0).toLowerCase().equals("[private]")){
					if(!sign.getLine(1).toLowerCase().equals(event.getPlayer().getName().toLowerCase()))
						if(!sign.getLine(2).toLowerCase().equals(event.getPlayer().getName().toLowerCase()))
							if(!sign.getLine(3).toLowerCase().equals(event.getPlayer().getName().toLowerCase()))
								if(!event.getPlayer().isOp())
									event.setCancelled(true);
				}
			}
		}else if(LBM.isSingleLockBlock(CheckBlock)){
			List<Sign> SignsGet = LBM.BlockFindSign(CheckBlock, false);
			for(Sign sign : SignsGet){
				if(sign.getLine(0).toLowerCase().equals("private")){
					sign.setLine(0, "[Private]");
					sign.update(true);
					block.setType(Material.AIR);
					event.setCancelled(true);
				}else if(sign.getLine(0).toLowerCase().equals("[private]")){
					if(!sign.getLine(1).toLowerCase().equals(event.getPlayer().getName().toLowerCase()))
						if(!sign.getLine(2).toLowerCase().equals(event.getPlayer().getName().toLowerCase()))
							if(!sign.getLine(3).toLowerCase().equals(event.getPlayer().getName().toLowerCase()))
								if(!event.getPlayer().isOp())
									event.setCancelled(true);
				}
			}
		}
	}
}
