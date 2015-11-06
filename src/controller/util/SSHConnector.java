package controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSHConnector {
	private static String hostname = "192.168.1.124";
	private static String username = "root";
	private static String password = "123456";
	private static  Session sess;
	private static  Connection conn;
	
	public static String exec(String command){
		
		StringBuilder sb = new StringBuilder();
		try{
			/* Create a connection instance */
	
			conn = new Connection(hostname);
	
			/* Now connect */
	
			conn.connect();
	
			/*
			 * Authenticate. If you get an IOException saying something like
			 * "Authentication method password not supported by the server at this stage."
			 * then please check the FAQ.
			 */
	
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
	
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");
	
			/* Create a session */
			
			sess = conn.openSession();
			sess.execCommand(command);
	
			/*
			 * This basic example does not handle stderr, which is sometimes
			 * dangerous (please read the FAQ).
			 */
			
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				sb.append(line);
				sb.append("\n");
			}
	
			/* Show exit status, if available (otherwise "null") */
			//System.out.println("ExitCode: " + sess.getExitStatus());
			
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}finally{
			/* Close this session */
			if(sess != null)
				sess.close();
	
			/* Close the connection */
			if(conn != null)
				conn.close();
		}
		return sb.toString();
	}
}
