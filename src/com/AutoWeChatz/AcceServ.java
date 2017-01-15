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
	
	static final String TAG = "AutoWeChat";//����LOG�ı�ǩ
	static final String WECHAT_PACKAGENAME = "com.tencent.mm";//΢�ų��������
	static final String TEXT_KEY = "Yao Qin";//׼������Ĺؼ���
	Handler handler = new Handler();

	protected void onServiceConnected()
	{
		super.onServiceConnected();
		// TODO �Զ����ɵķ������
		Toast.makeText(this, "onServiceConnected()", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {
		// TODO �Զ����ɵķ������
		Toast.makeText(this, "onAccessibilityEvent()", Toast.LENGTH_SHORT).show();
		
		final int eventType = arg0.getEventType();//��ȡ��������¼�����
		Log.d(TAG, "�¼�---->" + arg0);//��ӡ���¼�����
		
		//֪ͨ���¼�
        if(eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = arg0.getText();
            if(!texts.isEmpty()) {
                for(CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if(text.contains(TEXT_KEY)) {
                    	Toast.makeText(this, "Yao Qin����", Toast.LENGTH_SHORT).show();
                        openNotify(arg0);//��֪ͨ��
                        
                        break;
                    }
                }
            }
        } else if(eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            openHongBao(arg0);
        }
	}
	 
	/** openNotify(arg0)��֪ͨ��*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotify(AccessibilityEvent event) {
        if(event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
      //�����Ǿ�������΢�ŵ�֪ͨ����Ϣ��
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
            //�����˺������һ������ȥ����
            checkKey1();
        } else if("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(event.getClassName())) {
            //����������ϸ�ļ�¼����
            //nonething
        } else if("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            //���������,ȥ���к��
            checkKey2();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey1() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            Log.w(TAG, "rootWindowΪ��");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("����");
        for(AccessibilityNodeInfo n : list) {
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey2() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            Log.w(TAG, "rootWindowΪ��");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("��ȡ���");
        if(list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(TEXT_KEY);
            for(AccessibilityNodeInfo n : list) {
                Log.i(TAG, "-->΢�ź��:" + n);
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        } else {
            //���µĺ������
            for(int i = list.size() - 1; i >= 0; i --) {
                AccessibilityNodeInfo parent = list.get(i).getParent();
                Log.i(TAG, "-->��ȡ���:" + parent);
                if(parent != null) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }



    
	@Override
	public void onInterrupt() {
		// TODO �Զ����ɵķ������
		Toast.makeText(this, "onInterrupt()", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressLint("NewApi")
	private void findAndPerformAction(String text)
	{
		// ���ҵ�ǰ�����а�������װ�����ֵİ�ť
		if(getRootInActiveWindow()==null)
			return;
		//ͨ�������ҵ���ǰ�Ľڵ�
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
		for (int i = 0; i < nodes.size(); i++)
		{
			AccessibilityNodeInfo node = nodes.get(i);
			// ִ�а�ť�����Ϊ
			if (node.getClassName().equals("android.widget.Button")&&node.isEnabled())
			{
				node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}
	}

}
