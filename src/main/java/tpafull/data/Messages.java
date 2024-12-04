package tpafull.data;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public final class Messages {
    private Messages() {}

    public static final Function<String, Text> TPA_REQUEST_SENT =
            name -> Text.literal("Tpa request sent to ")
                    .append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA)));


    public static final Function<String, Text> TPAHERE_REQUEST_SENT =
            name -> Text.literal("Tpahere request sent to ")
                    .append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA)));


    public static final Text NO_DEFAULT_TARGET =
            Text.literal("No default tpa target specified\nUse ")
            .styled(style -> style.withColor(Formatting.RED))
            .append(Text.literal("/tpadefault set [player]")
                    .styled(style -> style
                            .withColor(Formatting.AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpadefault set "))
                    )
            )
            .append(Text.literal(" to set it")
                    .styled(style -> style
                            .withColor(Formatting.RED)));

    public static final Function<String, Text> DEFAULT_TARGET_SET =
            name -> Text.literal("Default tpa target set to ")
                    .append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA)));

    public static final Function<String, Text> DEFAULT_TARGET =
            name -> Text.literal("Default tpa target is ").
                    append(Text.literal(name).styled(style -> style
                            .withColor(Formatting.AQUA)));

    public static final Text DEFAULT_TARGET_REMOVED =
            Text.literal("Default tpa target removed");

    public static final Text REQUEST_ACCEPTED =
            Text.literal("Request accepted")
                    .styled(style -> style
                            .withColor(Formatting.GREEN));

    public static final Text REQUEST_REJECTED =
            Text.literal("Request rejected")
                    .styled(style -> style
                            .withColor(Formatting.GREEN));

    public static final Text NO_PENDING_REQUESTS =
            Text.literal("No pending requests")
                    .styled(style -> style
                            .withColor(Formatting.RED));

    public static final Text TELEPORTED_HOME =
            Text.literal("Teleported home")
                    .styled(style -> style
                            .withColor(Formatting.GREEN));

    public static final Text NO_HOME =
            Text.literal("No home specified\nUse ")
                    .styled(style -> style.withColor(Formatting.RED))
                    .append(Text.literal("/tpahome set")
                            .styled(style -> style
                                    .withColor(Formatting.AQUA)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpahome set"))
                            )
                    )
                    .append(Text.literal(" to set it")
                            .styled(style -> style
                                    .withColor(Formatting.RED)));

    public static final Function<BlockPos, Text> HOME_SET =
            home -> Text.literal("Home set at ")
                    .append(Text.literal(home.getX() + " " + home.getY() + " " + home.getX()).styled(style -> style
                            .withColor(Formatting.AQUA)));

    public static final Function<BlockPos, Text> HOME =
            home -> Text.literal("Home is at ").
                    append(Text.literal(home.getX() + " " + home.getY() + " " + home.getX()).styled(style -> style
                            .withColor(Formatting.AQUA)));

    public static final Text HOME_REMOVED =
            Text.literal("Home removed");
}
