package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.rank.Rank;

@CommandParameters(description = "Set your gamemode to creative", aliases = "gmc", rank = Rank.OP, source = SourceType.PLAYER)
public class Command_creative
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 0)
        {
            return false;
        }

        ((Player) sender).setGamemode(1);
        sender.sendMessage(TextFormat.GOLD + "Your gamemode have been set to creative!");
        return true;
    }
}
