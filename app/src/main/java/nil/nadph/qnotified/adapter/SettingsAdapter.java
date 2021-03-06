package nil.nadph.qnotified.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import nil.nadph.qnotified.hook.*;
import nil.nadph.qnotified.record.ConfigManager;
import nil.nadph.qnotified.ui.ResUtils;
import nil.nadph.qnotified.ui.SimpleBgDrawable;
import nil.nadph.qnotified.util.NewsHelper;
import nil.nadph.qnotified.util.UpdateCheck;
import nil.nadph.qnotified.util.Utils;

import java.io.File;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static nil.nadph.qnotified.adapter.ActProxyMgr.*;
import static nil.nadph.qnotified.ui.ViewBuilder.*;
import static nil.nadph.qnotified.util.Initiator.load;
import static nil.nadph.qnotified.util.SendBatchMsg.clickToBatchMsg;
import static nil.nadph.qnotified.util.Utils.*;

public class SettingsAdapter implements ActivityAdapter, View.OnClickListener {

    private static final int R_ID_BTN_FILE_RECV = 0x300AFF91;

    private Activity self;
    private TextView __tv_muted_atall, __tv_muted_redpacket, __tv_fake_bat_status, __recv_status, __recv_desc;

    public SettingsAdapter(Activity activity) {
        self = activity;
    }

    @Override
    public void doOnPostCreate(Bundle savedInstanceState) throws Throwable {
        LinearLayout ll = new LinearLayout(self);
        ll.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams mmlp = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        LinearLayout __ll = new LinearLayout(self);
        __ll.setOrientation(LinearLayout.VERTICAL);
        ViewGroup bounceScrollView = (ViewGroup) new_instance(load("com/tencent/mobileqq/widget/BounceScrollView"), self, null, Context.class, AttributeSet.class);
        //invoke_virtual(bounceScrollView,"a",true,500,500,boolean.class,int.class,int.class);
        bounceScrollView.setLayoutParams(mmlp);
        bounceScrollView.addView(ll, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        //bounceScrollView.setBackgroundDrawable(ResUtils.qq_setting_item_bg_nor);
        //invoke_virtual(bounceScrollView,"setNeedHorizontalGesture",true,boolean.class);
        LinearLayout.LayoutParams fixlp = new LinearLayout.LayoutParams(MATCH_PARENT, dip2px(self, 48));
        RelativeLayout.LayoutParams __lp_l = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        int mar = (int) (dip2px(self, 12) + 0.5f);
        __lp_l.setMargins(mar, 0, mar, 0);
        __lp_l.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        __lp_l.addRule(RelativeLayout.CENTER_VERTICAL);
        RelativeLayout.LayoutParams __lp_r = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        __lp_r.setMargins(mar, 0, mar, 0);
        __lp_r.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        __lp_r.addRule(RelativeLayout.CENTER_VERTICAL);
        ColorStateList hiColor = ColorStateList.valueOf(Color.argb(255, 242, 140, 72));
        RelativeLayout _t;
        try {
            LinearLayout nnn = subtitle(self, "");
            TextView t = (TextView) nnn.getChildAt(0);
            NewsHelper.getCachedNews(t);
            ll.addView(nnn);
            NewsHelper.asyncFetchNewsIfNeeded(t);
        } catch (Throwable e) {
            log(e);
        }
        ll.addView(subtitle(self, "遗留功能"));//群发已不再维护
        ll.addView(newListItemButton(self, "群发文本消息", "年少不知号贵-理性使用以免永冻", null, clickToBatchMsg()));
        ll.addView(subtitle(self, "基本功能"));
        ll.addView(_t = newListItemButton(self, "自定义电量", "[QQ>=8.2.6]在线模式为我的电量时生效", "N/A", clickToProxyActAction(ACTION_FAKE_BAT_CONFIG_ACTIVITY)));
        __tv_fake_bat_status = (TextView) _t.findViewById(R_ID_VALUE);
        ll.addView(newListItemSwitchConfigInit(self, "语音转发", "长按语音消息", qn_enable_ptt_forward, false, PttForwardHook.get()));
        ll.addView(newListItemSwitchConfigInit(self, "发送卡片消息", "长按发送ArkAppMsg(json)+StructMsg(xml)", qn_send_card_msg, false, CardMsgHook.get()));
        ll.addView(newListItemSwitchConfigInit(self, "复读机", "+1", bug_repeater, false, RepeaterHook.get()));
        ll.addView(subtitle(self, "净化设置"));
        if (!Utils.isTim(self)) {
            ll.addView(newListItemSwitchConfigNext(self, "隐藏小程序入口", "隐藏消息列表下拉出现的小程序列表", qn_hide_msg_list_miniapp, false));
            ll.addView(newListItemSwitchConfigInit(self, "隐藏送礼动画", null, qn_hide_gift_animation, false, HideGiftAnim.get()));
            ll.addView(newListItemSwitchConfigInit(self, "禁止自动@", "[>=8.1.3]去除回复消息时自动@特性", qn_disable_auto_at, false, ReplyNoAtHook.get()));
            ll.addView(newListItemSwitchConfigInit(self, "禁用$打开送礼界面", "禁止聊天时输入$自动弹出[选择赠送对象]窗口", qn_disable_$end_gift, false, $endGiftHook.get()));
        }
        ll.addView(newListItemSwitchConfigInit(self, "签到文本化", null, qn_sign_in_as_text, false, SimpleCheckInHook.get()));
        ll.addView(subtitle(self, "消息通知设置(不影响接收消息)屏蔽后可能仍有[橙字],但通知栏不会有通知,赞说说不提醒仅屏蔽通知栏的通知"));
        ll.addView(subtitle(self, "    注:屏蔽后可能仍有[橙字],但不会有通知"));
        ll.addView(_t = newListItemButton(self, "屏蔽指定群@全体成员通知", Html.fromHtml("<font color='" + get_RGB(hiColor.getDefaultColor()) + "'>[@全体成员]</font>就这点破事"), "%d个群", clickToProxyActAction(ACTION_MUTE_AT_ALL)));
        __tv_muted_atall = (TextView) _t.findViewById(R_ID_VALUE);
        ll.addView(_t = newListItemButton(self, "屏蔽指定群的红包通知", Html.fromHtml("<font color='" + get_RGB(hiColor.getDefaultColor()) + "'>[QQ红包][有红包]</font>恭喜发财"), "%d个群", clickToProxyActAction(ACTION_MUTE_RED_PACKET)));
        __tv_muted_redpacket = (TextView) _t.findViewById(R_ID_VALUE);
        ll.addView(newListItemSwitchConfigInit(self, "赞说说不提醒", "不影响评论,转发或击掌的通知", qn_mute_thumb_up, false, MuteQZoneThumbsUp.get()));
        ll.addView(subtitle(self, "图片相关"));
        ll.addView(newListItemSwitchConfigInit(self, "以图片方式打开闪照", null, qn_flash_as_pic, false, FlashPicHook.get()));
        if (!Utils.isTim(self)) {
            ll.addView(newListItemSwitchConfigInit(self, "以图片方式打开表情", null, qn_sticker_as_pic, false, EmoPicHook.get()));
        }
        //ll.addView(newListItemSwitchConfigInit(self, "聊天图片背景透明", null, qn_gallery_bg, false, GalleryBgHook.get()));
        ll.addView(subtitle(self, "实验性功能(未必有效)"));
        ll.addView(newListItemSwitchConfigInit(self, "收藏更多表情", "保存在本地", qqhelper_fav_more_emo, false, FavMoreEmo.get()));
        ll.addView(newListItemSwitchConfigInit(self, "防撤回", "来自旧版QX,稳定性不如最新版QX", qn_anti_revoke_msg, false, RevokeMsgHook.get()));
        ll.addView(newListItemSwitchConfigInit(self, "屏蔽更新提醒", null, qh_pre_upgrade, false, PreUpgradeHook.get()));
        if (!Utils.isTim(self)) {
            ll.addView(newListItemSwitchConfigInit(self, "自定义猜拳骰子", null, qh_random_cheat, false, CheatHook.get()));
            ll.addView(newListItemSwitchConfigInit(self, "简洁模式圆头像", "From Rikka", qn_round_avatar, false, RoundAvatarHook.get()));
        }
        ll.addView(subtitle(self, "好友列表"));
        ll.addView(newListItemButton(self, "历史好友", null, null, clickToProxyActAction(ACTION_EXFRIEND_LIST)));
        ll.addView(newListItemButton(self, "导出历史好友列表", "支持csv/json格式", null, clickToProxyActAction(ACTION_FRIENDLIST_EXPORT_ACTIVITY)));
        if (!Utils.isTim(self)) {
            ll.addView(newListItemSwitchConfigNext(self, "隐藏分组下方入口", "隐藏分组列表最下方的历史好友按钮", qn_hide_ex_entry_group, false));
        }
        ll.addView(_t = newListItemButton(self, "重定向文件下载目录", "N/A", "N/A", this));
        _t.setId(R_ID_BTN_FILE_RECV);
        __recv_desc = (TextView) _t.findViewById(R_ID_DESCRIPTION);
        __recv_status = (TextView) _t.findViewById(R_ID_VALUE);
        ll.addView(subtitle(self, "还没完成的功能(咕咕咕)"));
        ll.addView(newListItemSwitchConfigStub(self, "屏蔽回执消息的通知", null, qn_mute_talk_back, false));
        ll.addView(newListItemSwitchConfigStub(self, "禁用QQ热补丁", "一般无需开启", qn_disable_qq_hot_patch, false));
        ll.addView(subtitle(self, "参数设定"));
        ll.addView(newListItemButton(self, "小尾巴", "请勿在多个模块同时开启小尾巴", "[无]", clickTheComing()));
        ll.addView(newListItemButton(self, "DelFriendReq.delType", "只能为1或2", "[不改动]", clickTheComing()));
        ll.addView(newListItemButton(self, "AddFriendReq.sourceID", "改错可能导致无法添加好友", "[不改动]", clickTheComing()));
        ll.addView(subtitle(self, "关于"));
        PackageInfo pi = Utils.getHostInfo(self);
        ll.addView(newListItemDummy(self, pi.applicationInfo.loadLabel(self.getPackageManager()), null, pi.versionName));
        ll.addView(newListItemDummy(self, "模块版本", null, Utils.QN_VERSION_NAME));
        UpdateCheck uc = new UpdateCheck();
        ll.addView(_t = newListItemButton(self, "检查更新", null, "点击检查", uc));
        ll.addView(newListItemButton(self, "关于模块", null, null, clickToProxyActAction(ACTION_ABOUT)));
        ll.addView(newListItemButton(self, "故障排查", null, null, clickToProxyActAction(ACTION_TROUBLESHOOT_ACTIVITY)));
        uc.setVersionTip(_t);
        ll.addView(subtitle(self, "调试"));
        ll.addView(newListItemButton(self, "Shell.exec", "正常情况下无需使用此功能", null, clickTheComing()));
        ll.addView(subtitle(self, "作者"));
        ll.addView(newListItemButton(self, "捐赠", "请选择扶贫方式", null, clickToProxyActAction(ACTION_DONATE_ACTIVITY)));
        if (isNiceUser())
            ll.addView(newListItemButton(self, "QQ", "点击私信反馈(bug,建议等等)", "1041703712", clickToChat()));
        ll.addView(newListItemButton(self, "Mail", null, "xenonhydride@gmail.com", null));
        ll.addView(newListItemButton(self, "Github", "Bug -> Issue (star)", "cinit/QNotified", clickToUrl("https://github.com/cinit/QNotified")));
        ll.addView(newListItemButton(self, "Telegram", null, "Auride", clickToUrl("https://t.me/Auride")));
        ll.addView(subtitle(self, "本软件为免费软件,请尊重个人劳动成果,严禁倒卖"));
        //bounceScrollView.setFocusable(true);
        //bounceScrollView.setFocusableInTouchMode(true);
        __ll.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        self.setContentView(bounceScrollView);
        LinearLayout.LayoutParams _lp_fat = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        _lp_fat.weight = 1;
        //__ll.addView(bounceScrollView,_lp_fat);
        ActProxyMgr.setContentBackgroundDrawable(self, ResUtils.skin_background);
        invoke_virtual(self, "setTitle", "高级", CharSequence.class);
        invoke_virtual(self, "enableLeftBtn", true, boolean.class);
        invoke_virtual(self, "setImmersiveStatus");
        //TextView rightBtn=(TextView)invoke_virtual(self,"getRightTextView");
        //log("Title:"+invoke_virtual(self,"getTextTitle"));
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void doOnPostResume() throws Throwable {
        ConfigManager cfg = ConfigManager.getDefaultConfig();
        String str = cfg.getString(qn_muted_at_all);
        int n = 0;
        if (str != null && str.length() > 4) n = str.split(",").length;
        __tv_muted_atall.setText(n + "个群");
        str = cfg.getString(qn_muted_red_packet);
        n = 0;
        if (str != null && str.length() > 4) n = str.split(",").length;
        __tv_muted_redpacket.setText(n + "个群");
        if (__tv_fake_bat_status != null) {
            FakeBatteryHook bat = FakeBatteryHook.get();
            if (bat.isEnabled()) {
                int cap = bat.getFakeBatteryCapacity();
                boolean c = bat.isFakeBatteryCharging();
                __tv_fake_bat_status.setText(cap + (c ? "%+ " : "% "));
            } else {
                __tv_fake_bat_status.setText("[系统电量]");
            }
        }
        updateRecvRedirectStatus();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R_ID_BTN_FILE_RECV) {
            if (FileRecvRedirect.get().checkPreconditions()) {
                showChangeRecvPathDialog();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doSetupForPrecondition(self, FileRecvRedirect.get());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showChangeRecvPathDialog();
                            }
                        });
                    }
                }).start();
            }

        }
    }

    private void showChangeRecvPathDialog() {
        final FileRecvRedirect recv = FileRecvRedirect.get();
        String currPath = recv.getRedirectPath();
        if (currPath == null) currPath = recv.getDefaultPath();
        final EditText editText = new EditText(self);
        editText.setText(currPath);
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(16);
        int _5 = dip2px(self, 5);
        editText.setPadding(_5, _5, _5, _5);
        editText.setBackgroundDrawable(new SimpleBgDrawable(0, 0x80000000, 1));
        LinearLayout linearLayout = new LinearLayout(self);
        linearLayout.addView(editText, newLinearLayoutParams(MATCH_PARENT, WRAP_CONTENT, _5 * 2));
        final AlertDialog alertDialog = new AlertDialog.Builder(self, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle("输入重定向文件夹路径")
                .setView(linearLayout)
                .setPositiveButton("确认并激活", null)
                .setNegativeButton("取消", null)
                .setNeutralButton("使用默认值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recv.setEnabled(false);
                        updateRecvRedirectStatus();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = editText.getText().toString();
                if (path.equals("")) {
                    showToast(self, TOAST_TYPE_ERROR, "请输入路径", Toast.LENGTH_SHORT);
                    return;
                }
                if (!path.startsWith("/")) {
                    showToast(self, TOAST_TYPE_ERROR, "请输入完整路径(以\"/\"开头)", Toast.LENGTH_SHORT);
                    return;
                }
                File f = new File(path);
                if (!f.exists() || !f.isDirectory()) {
                    showToast(self, TOAST_TYPE_ERROR, "文件夹不存在", Toast.LENGTH_SHORT);
                    return;
                }
                if (!f.canWrite()) {
                    showToast(self, TOAST_TYPE_ERROR, "文件夹无访问权限", Toast.LENGTH_SHORT);
                    return;
                }
                if (!path.endsWith("/")) path += "/";
                recv.setRedirectPathAndEnable(path);
                updateRecvRedirectStatus();
                alertDialog.dismiss();
            }
        });
    }

    private void updateRecvRedirectStatus() {
        FileRecvRedirect recv = FileRecvRedirect.get();
        if (recv.isEnabled()) {
            __recv_status.setText("[已启用]");
            __recv_desc.setText(recv.getRedirectPath());
        } else {
            __recv_status.setText("[禁用]");
            __recv_desc.setText(recv.getDefaultPath());
        }
    }

    @Override
    public void doOnPostPause() throws Throwable {

    }

    @Override
    public void doOnPostDestory() throws Throwable {

    }

    @Override
    public boolean isWrapContent() throws Throwable {
        return true;
    }

    @Override
    public void doOnPostActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
