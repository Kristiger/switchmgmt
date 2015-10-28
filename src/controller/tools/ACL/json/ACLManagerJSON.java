package controller.tools.ACL.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import model.tools.ACL.ACLRule;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.Deserializer;
import controller.util.JSONArray;
import controller.util.JSONException;
import controller.util.JSONObject;

public class ACLManagerJSON {
	private static String IP = FloodlightProvider.getIP();
	private static String PORT = FloodlightProvider.getPort();
	private static JSONObject obj, jsonobj;
	private static JSONArray json;
	private static Future<Object> future;
	
	public static List<ACLRule> getACLRules() throws IOException, JSONException{
		List<ACLRule> list = new ArrayList<ACLRule>();
		
		future = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "wm/acl/rules/json");
		try {
			obj = (JSONObject)future.get(5, TimeUnit.SECONDS);
			json = (JSONArray)future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		//need explicit rule to analysis
		
		return null;
	}

}
