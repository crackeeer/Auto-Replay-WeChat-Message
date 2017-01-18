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
	public String HongBao_KEY = "微信红包";/////********
	public String text = "";
	
	  public String e = "";
	  public boolean f = false;
	  public boolean g = false;
	  public boolean h = false;
	  
	static final String TAG = "AutoWeChat";//调试LOG的标签
	static final String WECHAT_PACKAGENAME = "com.tencent.mm";//微信程序包名称
	 public String [] namelists = new String [20];//存放准备捕获的关键字

	
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
		
		final int eventType = arg0.getEventType();//获取所捕获的事件种类
		//Log.d(TAG, "事件---->" + arg0);//打印出事件种类
		//通知栏事件
        if(eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

            List<CharSequence> texts = arg0.getText();
            if(!texts.isEmpty()) {
                for(CharSequence t : texts) {
                text = String.valueOf(t);
                    
                    Log.d(TAG, "文本信息---->" + text);//log出对方发来的信息
           
                    if(text.contains(HongBao_KEY)){
                    	openNotify(arg0);
                    }
                    else{	
                    //如果是包含预先设置的关键字
                    if(text.contains(namelists[0])|| text.contains(namelists[1])||
                    		text.contains(namelists[2])||text.contains(namelists[3])
                    		||text.contains(namelists[4])) 
                    {
                    	//分割出正文。（去掉了“昵称：”这一段）
                    	final String text2 =  text.substring(text.lastIndexOf(':')+1);
                    	
                    	//获取发送者的名字
                    	pubsendername =  text.substring(0,text.lastIndexOf(':'));
                    	//Log.d(TAG, "sendername---->" + pubsendername);//打印出事件种类
                    	
                    	//分割出正文后，将“允许发送”的标记设为true
                    	 f=true;
                    	 
                    	 //新开一个线程，将对方用户发过来的正文发给“聊天机器人”，并获得回复
                    	new Thread(new Runnable(){
                			
                			public void run(){
                				pubres = HttpUtils.doGet(text2);
                				//处理回复内容
                				pubres=pubres.substring(pubres.lastIndexOf(':')+2,pubres.lastIndexOf('}')-1);
                				Log.d("TAG",pubres );//打印出回复内容  
                			}
                		}).start();
                    	
                    	//然后打开通知栏，准备回复给对方
                        openNotify(arg0);                        
                        break;
                    }
                }
                }
            }


        } 
        	//如果TYPE_NOTIFICATION_STATE_没有CHANGED，那么看窗口是否变化
        else if(eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {      	
        	//represents the event of opening a PopupWindow, Menu, Dialog, etc.
        	
        	Log.w(TAG, "autorpl");
        	autorpl(arg0);

        }
	}
	 
	/** openNotify(arg0)打开通知栏*/
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
    
    //自动回复
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void autorpl(AccessibilityEvent event) {
    	
    	if("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI".equals(event.getClassName())) {
            //点中了红包，下一步就是去拆红包
            checkKey1();
        } else
    if("com.tencent.mm.ui.LauncherUI".equals(event.getClassName())) {
            //如果进入聊天界面，那么：
    		if(text.contains(HongBao_KEY)){
    			checkKey4();
    			text = "";//及时清空关键字
    		}
        	checkKey2();//点进与目标用户的聊天界面
        	
        	//然后获取当前窗口的根节点
        	AccessibilityNodeInfo nodeInfox = getRootInActiveWindow();
        	checkKey3(nodeInfox);
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
    
    
    //点击消息界面中“带text_key”的地方
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey2() {
    	
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {      
        	Log.w(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(pubsendername);
        
        if(list!=null){
        //**可以加一个判断，不然list为空的时候会报错**//
            for(AccessibilityNodeInfo n : list) {
            	n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            } }     
    	} 
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    
    private void checkKey4() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if(list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByText(HongBao_KEY);
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
    
    
	
    @TargetApi(21)
    private void checkKey3(AccessibilityNodeInfo nodeInfo3) {

        if(nodeInfo3 == null) {      
        	Log.w(TAG, "rootWindow为空");
            return;
            
        }
        //如果根节点
        if ("android.widget.EditText".equals(nodeInfo3.getClassName())){
        	Log.w(TAG, "android.widget.EditText");
        	SystemClock.sleep(1000);
        	Bundle localBundle = new Bundle();
  		  	localBundle.putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE",pubres);
  		  	nodeInfo3.performAction(2097152, localBundle);
  		    Log.w(TAG, "nodeInfo3是："+nodeInfo3);
 
  		  AccessibilityNodeInfo localInfo1 = getRootInActiveWindow();
  		  List localList = localInfo1.findAccessibilityNodeInfosByText("发送");//找到发送按钮

  		   ((AccessibilityNodeInfo)localList.get(0)).performAction(16);//点击该按钮

  		 pubres="";
  		 f = false;
  		 //如何回到主界面…
         performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);//返回主界面
            return;
        }
    
        int j = nodeInfo3.getChildCount();
        if(j==0){return;}
        AccessibilityNodeInfo localInfo;
        for(int i=0; i<j;i++){
        	localInfo = nodeInfo3.getChild(i);
        	if (f==false)break;//加一个判断，跳出循环以节省资源
        	checkKey3(localInfo);
          }
            		}

 

    
    @Override
	public void onInterrupt(){
		// TODO 自动生成的方法存根
		Toast.makeText(this, "onInterrupt()", Toast.LENGTH_SHORT).show();
	}
	
	
}
