package me.focusvity.totalfreedommod.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.banning.BanManager;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;

@CommandParameters(description = "Unbans a player", usage = "/<command> <player>", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_unban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (BanManager.getBan(args[0], false) == null)
        {
            sender.sendMessage(TextFormat.RED + "That player doesn't seem to be banned!");
            return true;
        }

        FUtil.adminAction(sender.getName(), "Unbanning " + args[0], true);
        BanManager.removeBan(BanManager.getBan(args[0], false));
        return true;
    }
}
