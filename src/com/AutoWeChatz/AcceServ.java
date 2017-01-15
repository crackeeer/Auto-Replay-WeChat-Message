package com.AutoWeChatz;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class AcceServ extends AccessibilityService{
	
	static final String TAG = "AutoWeChat";//调试LOG的标签
	static final String WECHAT_PACKAGENAME = "com.tencent.mm";//微信程序包名称
	static final String TEXT_KEY = "Yao Qin";//准备捕获的关键字
	Handler handler = new Handler();

	protected void onServiceConnected()
	{
		super.onServiceConnected();
		// TODO 自动生成的方法存根
		Toast.makeText(this, "onServiceConnected()", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {
		// TODO 自动生成的方法存根
		Toast.makeText(this, "onAccessibilityEvent()", Toast.LENGTH_SHORT).show();
		
		final int eventType = arg0.getEventType();//获取所捕获的事件种类
		Log.d(TAG, "事件---->" + arg0);//打印出事件种类
		
		//通知栏事件
        if(eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = arg0.getText();
            if(!texts.isEmpty()) {
                for(CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if(text.contains(TEXT_KEY)) {
                    	Toast.makeText(this, "Yao Qin来信", Toast.LENGTH_SHORT).show();
                        openNotify(arg0);//打开通知栏
                        
                        break;
                    }
                }
            }
        } else if(eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            openHongBao(arg0);
        }
	}
	 
	/** openNotify(arg0)打开通知栏*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotify(AccessibilityEvent event) {
        if(event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
      //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openHongBao(AccessibilityEvent event) {
        if("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            //点中了红包，下一步就是去拆红包
            checkKey1();
        } else if("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            //拆完红包后看详细的纪录界面
            //nonething
        } else if("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            //在聊天界面,去点中红包
            checkKey2();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey1() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
        for(AccessibilityNodeInfo n : list) {
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey2() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if(list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(TEXT_KEY);
            for(AccessibilityNodeInfo n : list) {
                Log.i(TAG, "-->微信红包:" + n);
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        } else {
            //最新的红包领起
            for(int i = list.size() - 1; i >= 0; i --) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                Log.i(TAG, "-->领取红包:" + parent);
                if(parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }



    
	@Override
	public void onInterrupt() {
		// TODO 自动生成的方法存根
		Toast.makeText(this, "onInterrupt()", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressLint("NewApi")
	private void findAndPerformAction(String text)
	{
		// 查找当前窗口中包含“安装”文字的按钮
		if(getRootInActiveWindow()==null)
			return;
		//通过文字找到当前的节点
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
		for (int i = 0; i < nodes.size(); i++)
		{
			AccessibilityNodeInfo node = nodes.get(i);
			// 执行按钮点击行为
			if (node.getClassName().equals("android.widget.Button")&&node.isEnabled())
			{
				node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}
	}

}
