package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;

@CommandParameters(description = "Deops a player", usage = "/<command> <player>", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_deop
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

        FUtil.adminAction(sender.getName(), "De-opping " + player.getName(), true);
        player.setOp(false);
        player.sendMessage(TextFormat.YELLOW + "You are no longer op!");
        return true;
    }
}
