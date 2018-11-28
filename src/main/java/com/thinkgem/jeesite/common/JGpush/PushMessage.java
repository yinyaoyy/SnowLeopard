package com.thinkgem.jeesite.common.JGpush;

import java.util.Map;

import com.google.gson.JsonObject;


public class PushMessage {    
 /*   private static final String TITLE = "title";
    private static final String MSG_CONTENT = "msg_content";
    private static final String CONTENT_TYPE = "content_type";
    private static final String EXTRAS = "extras";
    */
    private  String title;
    private  String msgContent;
    private  String contentType;
    private  Map<String, String> extras;

    public PushMessage(){}
    public PushMessage(String title, String msgContent, String contentType, 
    		Map<String, String> extras, 
    		Map<String, Number> numberExtras,
    		Map<String, Boolean> booleanExtras,
    		Map<String, JsonObject> jsonExtras) {
        this.title = title;
        this.msgContent = msgContent;
        this.contentType = contentType;
        this.extras = extras;
      
    }
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Map<String, String> getExtras() {
		return extras;
	}
	public void setExtras(Map<String, String> extras) {
		this.extras = extras;
	}


    
  
    
   /* @Override
    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if (null != title) {
            json.add(TITLE, new JsonPrimitive(title));
        }
        if (null != msgContent) {
            json.add(MSG_CONTENT, new JsonPrimitive(msgContent));
        }
        if (null != contentType) {
            json.add(CONTENT_TYPE, new JsonPrimitive(contentType));
        }
        
        JsonObject extrasObject = null;
        if (null != extras || null != numberExtras || null != booleanExtras) {
            extrasObject = new JsonObject();
        }
        
        if (null != extras) {
            for (String key : extras.keySet()) {
                if (extras.get(key) != null) {
                    extrasObject.add(key, new JsonPrimitive(extras.get(key)));
                } else {
                    extrasObject.add(key, JsonNull.INSTANCE);
                }
            }
        }
        if (null != numberExtras) {
            for (String key : numberExtras.keySet()) {
                extrasObject.add(key, new JsonPrimitive(numberExtras.get(key)));
            }
        }
        if (null != booleanExtras) {
            for (String key : booleanExtras.keySet()) {
                extrasObject.add(key, new JsonPrimitive(booleanExtras.get(key)));
            }
        }
        if (null != jsonExtras) {
            for (String key : jsonExtras.keySet()) {
                extrasObject.add(key, jsonExtras.get(key));
            }
        }

        if (null != extras || null != numberExtras || null != booleanExtras) {
            json.add(EXTRAS, extrasObject);
        }
        
        return json;
    }
*/
   }
