package me.focusvity.totalfreedommod.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.PlayerData;
import me.focusvity.totalfreedommod.TotalFreedomMod;
import me.focusvity.totalfreedommod.admin.AdminList;
import me.focusvity.totalfreedommod.banning.Ban;
import me.focusvity.totalfreedommod.banning.BanManager;
import me.focusvity.totalfreedommod.rank.Displayable;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.rank.Title;
import me.focusvity.totalfreedommod.util.FUtil;

public class PlayerListener implements Listener
{

    private final TotalFreedomMod plugin;
    private Server server;

    public PlayerListener(TotalFreedomMod plugin)
    {
        this.plugin = plugin;
        this.server = plugin.getServer();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(PlayerAsyncPreLoginEvent event)
    {
        Ban ban = BanManager.getBan(event.getName(), false);

        if (ban == null)
        {
            ban = BanManager.getBan(event.getAddress(), true);
        }

        if (ban != null && !ban.isExpired())
        {
            if (AdminList.isAdmin(event.getPlayer()))
            {
                event.allow();
                return;
            }

            event.disAllow(ban.getKickMessage());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        boolean isAdmin = AdminList.isAdmin(player);

        if (isAdmin)
        {
            if (!AdminList.getAdmin(player).getIps().contains(player.getAddress()))
            {
                AdminList.getImposters().add(player.getName());
                server.broadcastMessage(TextFormat.RED + player.getName() + " has been flagged as an imposter!");
                player.getInventory().clearAll();
                player.setGamemode(0);
                player.sendMessage(TextFormat.RED + "You have been marked as an imposter, please verify yourself.");
                return;
            }

            server.broadcastMessage(TextFormat.AQUA + player.getName() + " is " + getDisplay(player).getLoginMessage());
        }

        PlayerData.getData(player).setTag(getDisplay(player).getTag());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event)
    {
        if (AdminList.isImposter(event.getPlayer()))
        {
            AdminList.getImposters().remove(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (PlayerData.getData(player).isMuted())
        {
            player.sendMessage(TextFormat.RED + "You may not be able to chat while muted!");
            event.setCancelled(true);
            return;
        }

        String tag = PlayerData.getData(player).getTag() != null ? PlayerData.getData(player).getTag() : "";
        String name = player.getDisplayName();
        event.setFormat(tag + TextFormat.WHITE + " <" + name + "> " + event.getMessage());
    }

    /*@EventHandler(priority = EventPriority.LOW)
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if (PlayerData.getData(player).isFrozen())
        {
            player.teleport(event.getFrom());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();

        if (PlayerData.getData(player).isFrozen())
        {
            player.teleport(event.getFrom());
            event.setCancelled(true);
        }
    }*/

    //TODO master builder
    private Displayable getDisplay(Player player)
    {
        if (AdminList.isImposter(player))
        {
            return Rank.IMPOSTER;
        }

        if (FUtil.DEVELOPERS.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }

        if (TotalFreedomMod.plugin.config.getList("server.executives").contains(player.getName()) && AdminList.isAdmin(player))
        {
            return Title.EXECUTIVE;
        }

        if (TotalFreedomMod.plugin.config.getList("server.owners").contains(player.getName()))
        {
            return Title.OWNER;
        }

        return Rank.getRank(player);
    }
}
