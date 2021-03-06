package nil.nadph.qnotified.hook;

import android.app.Application;
import de.robv.android.xposed.XC_MethodHook;
import nil.nadph.qnotified.SyncUtils;
import nil.nadph.qnotified.record.ConfigManager;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static nil.nadph.qnotified.util.Initiator.load;
import static nil.nadph.qnotified.util.Utils.*;

public class ReplyNoAtHook extends BaseDelayableHook {

    private static final ReplyNoAtHook self = new ReplyNoAtHook();
    private boolean inited = false;

    private ReplyNoAtHook() {
    }

    public static ReplyNoAtHook get() {
        return self;
    }

    /**
     * 813 1246 k
     * 815 1258 l
     * 818 1276 l
     * 820 1296 l
     * 826 1320 m
     */
    @Override
    public boolean init() {
        if (inited) return true;
        try {
            String method = null;
            int ver = getHostVersionCode();
            if (ver > 1296) {
                method = "m";
            } else if (ver > 1246) {
                method = "l";
            } else if (ver >= 1246) {
                method = "k";
            }
            if (method == null) return false;
            findAndHookMethod(load("com/tencent/mobileqq/activity/BaseChatPie"), method, boolean.class, new XC_MethodHook(49) {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (!isEnabled()) return;
                    boolean p0 = (boolean) param.args[0];
                    if (!p0) param.setResult(null);
                }
            });
            inited = true;
            return true;
        } catch (Throwable e) {
            log(e);
            return false;
        }
    }

    @Override
    public int[] getPreconditions() {
        return new int[0];
    }

    @Override
    public int getEffectiveProc() {
        return SyncUtils.PROC_MAIN;
    }

    @Override
    public boolean isInited() {
        return inited;
    }

    @Override
    public boolean isEnabled() {
        try {
            Application app = getApplication();
            if (app != null && isTim(app)) return false;
            return ConfigManager.getDefaultConfig().getBooleanOrFalse(qn_disable_auto_at);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }
}