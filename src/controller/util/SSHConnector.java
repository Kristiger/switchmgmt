package controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSHConnector {
	private String hostname = "192.168.1.124";
	private String username = "root";
	private String password = "123456";
	private Session sess;
	private Connection conn;

	public List<String> exec(String command) {

		//StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		InputStream stdout = null;
		BufferedReader br = null;
		try {
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

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));

			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				list.add(line);
				//sb.append(line);
				//sb.append("\n");
			}

			/* Show exit status, if available (otherwise "null") */
			// System.out.println("ExitCode: " + sess.getExitStatus());

		} catch (IOException e) {
			e.printStackTrace(System.err);
			/* Close this session */
			try {
				stdout.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}

			try {
				br.close();
			} catch (Exception e3) {
				// TODO: handle exception
				e3.printStackTrace();
			}

			/* Close the connection */
			try {
				conn.close();
			} catch (Exception e4) {
				// TODO: handle exception
				e4.printStackTrace();
			}
			System.exit(2);
		}

		return list;
	}
}
