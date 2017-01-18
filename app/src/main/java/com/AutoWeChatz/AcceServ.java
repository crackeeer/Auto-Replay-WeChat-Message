package com.AutoWeChatz;

import java.util.List;

import com.xrobot.di.utils.HttpUtils;


import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class AcceServ extends AccessibilityService{
	
	int i = 100;
	public boolean a = true;
	public String pubres = "";/////********
	public String pubsendername = "";/////********
	public String HongBao_KEY = "΢�ź��";/////********
	public String text = "";
	
	  public String e = "";
	  public boolean f = false;
	  public boolean g = false;
	  public boolean h = false;
	  
	static final String TAG = "AutoWeChat";//����LOG�ı�ǩ
	static final String WECHAT_PACKAGENAME = "com.tencent.mm";//΢�ų��������
	 public String [] namelists = new String [20];//���׼������Ĺؼ���

	
	Handler handler = new Handler();

	protected void onServiceConnected()
	{

		super.onServiceConnected();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onAccessibilityEvent(AccessibilityEvent arg0) {

		namelists[0]=Parameters.name1; 
		namelists[1]=Parameters.name2;
		namelists[2]=Parameters.name3;
		namelists[3]=Parameters.name4;
		namelists[4]=Parameters.name5;
		
		final int eventType = arg0.getEventType();//��ȡ��������¼�����
		//Log.d(TAG, "�¼�---->" + arg0);//��ӡ���¼�����
		//֪ͨ���¼�
        if(eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

            List<CharSequence> texts = arg0.getText();
            if(!texts.isEmpty()) {
                for(CharSequence t : texts) {
                text = String.valueOf(t);
                    
                    Log.d(TAG, "�ı���Ϣ---->" + text);//log���Է���������Ϣ
           
                    if(text.contains(HongBao_KEY)){
                    	openNotify(arg0);
                    }
                    else{	
                    //����ǰ���Ԥ�����õĹؼ���
                    if(text.contains(namelists[0])|| text.contains(namelists[1])||
                    		text.contains(namelists[2])||text.contains(namelists[3])
                    		||text.contains(namelists[4])) 
                    {
                    	//�ָ�����ġ���ȥ���ˡ��ǳƣ�����һ�Σ�
                    	final String text2 =  text.substring(text.lastIndexOf(':')+1);
                    	
                    	//��ȡ�����ߵ�����
                    	pubsendername =  text.substring(0,text.lastIndexOf(':'));
                    	//Log.d(TAG, "sendername---->" + pubsendername);//��ӡ���¼�����
                    	
                    	//�ָ�����ĺ󣬽��������͡��ı����Ϊtrue
                    	 f=true;
                    	 
                    	 //�¿�һ���̣߳����Է��û������������ķ�������������ˡ�������ûظ�
                    	new Thread(new Runnable(){
                			
                			public void run(){
                				pubres = HttpUtils.doGet(text2);
                				//����ظ�����
                				pubres=pubres.substring(pubres.lastIndexOf(':')+2,pubres.lastIndexOf('}')-1);
                				Log.d("TAG",pubres );//��ӡ���ظ�����  
                			}
                		}).start();
                    	
                    	//Ȼ���֪ͨ����׼���ظ����Է�
                        openNotify(arg0);                        
                        break;
                    }
                }
                }
            }


        } 
        	//���TYPE_NOTIFICATION_STATE_û��CHANGED����ô�������Ƿ�仯
        else if(eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {      	
        	//represents the event of opening a PopupWindow, Menu, Dialog, etc.
        	
        	Log.w(TAG, "autorpl");
        	autorpl(arg0);

        }
	}
	 
	/** openNotify(arg0)��֪ͨ��*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotify(AccessibilityEvent event) {
        if(event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    
    //�Զ��ظ�
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void autorpl(AccessibilityEvent event) {
    	
    	if("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            //�����˺������һ������ȥ����
            checkKey1();
        } else
    if("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            //�������������棬��ô��
    		if(text.contains(HongBao_KEY)){
    			checkKey4();
    			text = "";//��ʱ��չؼ���
    		}
        	checkKey2();//�����Ŀ���û����������
        	
        	//Ȼ���ȡ��ǰ���ڵĸ��ڵ�
        	AccessibilityNodeInfo nodeInfox = getRootInActiveWindow();
        	checkKey3(nodeInfox);
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
    
    
    //�����Ϣ�����С���text_key���ĵط�
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey2() {
    	
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {      
        	Log.w(TAG, "rootWindowΪ��");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(pubsendername);
        
        if(list!=null){
        //**���Լ�һ���жϣ���ȻlistΪ�յ�ʱ��ᱨ��**//
            for(AccessibilityNodeInfo n : list) {
            	n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            } }     
    	} 
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    
    private void checkKey4() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            Log.w(TAG, "rootWindowΪ��");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("��ȡ���");
        if(list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(HongBao_KEY);
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
    
    
	
    @TargetApi(21)
    private void checkKey3(AccessibilityNodeInfo nodeInfo3) {

        if(nodeInfo3 == null) {      
        	Log.w(TAG, "rootWindowΪ��");
            return;
            
        }
        //������ڵ�
        if ("android.widget.EditText".equals(nodeInfo3.getClassName())){
        	Log.w(TAG, "android.widget.EditText");
        	SystemClock.sleep(1000);
        	Bundle localBundle = new Bundle();
  		  	localBundle.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE",pubres);
  		  	nodeInfo3.performAction(2097152, localBundle);
  		    Log.w(TAG, "nodeInfo3�ǣ�"+nodeInfo3);
 
  		  AccessibilityNodeInfo localInfo1 = getRootInActiveWindow();
  		  List localList = localInfo1.findAccessibilityNodeInfosByText("����");//�ҵ����Ͱ�ť

  		   ((AccessibilityNodeInfo)localList.get(0)).performAction(16);//����ð�ť

  		 pubres="";
  		 f = false;
  		 //��λص������桭
         performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);//����������
            return;
        }
    
        int j = nodeInfo3.getChildCount();
        if(j==0){return;}
        AccessibilityNodeInfo localInfo;
        for(int i=0; i<j;i++){
        	localInfo = nodeInfo3.getChild(i);
        	if (f==false)break;//��һ���жϣ�����ѭ���Խ�ʡ��Դ
        	checkKey3(localInfo);
          }
            		}

 

    
    @Override
	public void onInterrupt(){
		// TODO �Զ����ɵķ������
		Toast.makeText(this, "onInterrupt()", Toast.LENGTH_SHORT).show();
	}
	
	
}
