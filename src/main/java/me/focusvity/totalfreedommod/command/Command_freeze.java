package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.PlayerData;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;

@CommandParameters(description = "Freeze a player", usage = "/<command> <player>", aliases = "fr", rank = Rank.SUPER_ADMIN, source = SourceType.PLAYER)
public class Command_freeze
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        Player player = Server.getInstance().getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(TextFormat.RED + "That player does not exist!");
            return true;
        }

        boolean freeze = PlayerData.getData(player).isFrozen();
        FUtil.adminAction(sender.getName(), (freeze ? "Unfreezing " : "Freezing ") + player.getName(), false);
        PlayerData.getData(player).setFrozen(!freeze);
        return true;
    }
}
