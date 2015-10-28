package controller.tools.ACL.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.swt.widgets.TableItem;

import controller.floodlightprovider.FloodlightProvider;
import model.tools.ACL.ACLRule;
import controller.util.ErrorCheck;
import controller.util.JSONException;
import controller.util.JSONObject;

public class ACLManagerPusher {
	private static String IP = FloodlightProvider.getIP();
	private static String PORT = FloodlightProvider.getPort();

	public static String push(ACLRule rule) throws IOException, JSONException {
		String jsonResponse = "";

		URL url = new URL("http://" + IP + ":" + PORT + "/wm/acl/rules/json");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(rule.serialize());
		wr.flush();

		BufferedReader read = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = read.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}

		wr.close();
		read.close();
		
		// need to now jsonobj's detail
		JSONObject json = new JSONObject(jsonResponse);
		if (json.get("status").equals("Rule added")){
			for(ACLRule ar: FloodlightProvider.getACLRules()){
				System.out.println(ar.serialize());
				System.out.println(rule.serialize());
				if(ar.equals(rule)){
					return "Success.";
				}
			}
			
			return "Rule was not pushed, see the controller's log.";
		}else{
			return json.getString("status");
		}
	}

	public static String remove(ACLRule rule) throws IOException, JSONException {
		String str = "{\"ruleid\":\"" + rule.getRuleID() + "\"}";
		String jsonResponse = "";

		URL url = new URL("http://" + IP + ":" + PORT + "/wm/acl/rules/json");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(str);
		wr.flush();

		BufferedReader read = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = read.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}

		wr.close();
		read.close();

		JSONObject json = new JSONObject(jsonResponse);
		
		return json.getString("status");
	}

	public static String clear() throws IOException, JSONException {
		String jsonResponse = "";

		URL url = new URL("http://" + IP + ":" + PORT + "/wm/acl/clear/json");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write("");
		wr.flush();

		BufferedReader read = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = read.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}

		wr.close();
		read.close();
		
		JSONObject json = new JSONObject(jsonResponse);
		
		return json.getString("status");
	}
	
	public static ACLRule parseTableChanges(ACLRule rule, TableItem[] items){
		
        if (!items[2].getText(1).isEmpty())
            rule.setSrc_ip(items[2].getText(1));
        if (!items[3].getText(1).isEmpty())
            rule.setDst_ip(items[3].getText(1));
        if (!items[4].getText(1).isEmpty())
            rule.setNw_proto(items[4].getText(1));
        if (!items[5].getText(1).isEmpty())
            rule.setTp_dst(Integer.parseInt(items[5].getText(1)));
        if (!items[6].getText(1).isEmpty())
        	rule.setAction(items[6].getText());
        
		return rule;		
	}
	
	public static boolean errorCheckPassed(TableItem[] items){
		if (!items[2].getText(1).isEmpty()){
			if (!ErrorCheck.isIP(items[2].getText(1))){
				//DisplayMessage.displayError(ACL.getShell(),"Src IP must be a valid IP address!");
				return false;
			}
		}
		
		if (!items[3].getText(1).isEmpty()){
			if (!ErrorCheck.isIP(items[3].getText(1))){
				return false;
			}
		}
		
		if (!items[4].getText(1).isEmpty()){
			String proto = items[4].getText(1);
			if ( !(proto.equalsIgnoreCase("TCP") || proto.equalsIgnoreCase("UDP") 
					|| proto.equalsIgnoreCase("ICMP"))){
				return false;
			}
		}
		
		if (!items[5].getText(1).isEmpty()){
			if (!ErrorCheck.isNumeric(items[5].getText(1))){
				return false;
			}
		}
		
		if (!items[6].getText(1).isEmpty()){
			String action = items[6].getText(1);
			if ( !(action.equalsIgnoreCase("ALLOW") || action.equalsIgnoreCase("DENY"))){
				return false;
			}
		}
		
		return true;		
	}
}
