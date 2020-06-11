package me.focusvity.totalfreedommod.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;
import org.apache.commons.lang3.StringUtils;

@CommandParameters(name = "say", description = "Another fancy way to chat", usage = "/<command> <message>", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_say extends FreedomCommand
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        String message = StringUtils.join(args, " ");
        FUtil.bcastMsg(String.format("[Server:%s] %s", sender.getName(), message), TextFormat.LIGHT_PURPLE);
        return true;
    }
}
