package view;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.basic.BasicTreeUI.TreeHomeAction;

import model.overview.Port;
import model.overview.QosPolicy;
import model.overview.QosQueue;
import model.overview.Switch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import view.util.DisplayMessage;
import controller.floodlightprovider.FloodlightProvider;
import controller.util.SSHConnector;

public class AddQos extends Shell {
	private static final int winWidth = 748;
	private static final int winHeight = 364;
	private Text textQueueName;
	private Text textPortMax;
	private Text textPortMin;
	private Text textQueueID;
	private Text textMax;
	private Text textMin;

	private Tree treeSwitches;
	private Tree treeQueues;
	private Tree treePort;

	private QosPolicy qospolicy;
	private QosQueue queue;
	private static Port currPort;
	private static Switch currSwitch;
	private int currSwitchIndex, currPortIndex;
	private static boolean unsavedprogress, newqueue, policyset;
	private static int queueNumber = 0;

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public AddQos(Display display) {
		super(display);
		createContents();
		populateSwitchTree();
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	/*
	 * public static void main(String args[]) { try { Display display =
	 * Display.getDefault(); AddQos shell = new AddQos(display); shell.open();
	 * shell.layout(); while (!shell.isDisposed()) { if
	 * (!display.readAndDispatch()) { display.sleep(); } } } catch (Exception e)
	 * { e.printStackTrace(); } }
	 */

	/*
	 * public void open(){ try{ Display display = Display.getDefault(); AddQos
	 * shell = new AddQos(display); shell.open(); shell.layout(); while
	 * (!shell.isDisposed()) { if (!display.readAndDispatch()) {
	 * display.sleep(); } } } catch (Exception e) { e.printStackTrace(); } }
	 */

	private void populateSwitchTree() {

		// clear trees
		treePort.removeAll();
		treeQueues.removeAll();
		treeSwitches.removeAll();

		// clear port
		textQueueName.setText("");
		textPortMax.setText("");
		textPortMin.setText("");

		// clear queue
		textMax.setText("");
		textMin.setText("");
		textQueueID.setText("");

		qospolicy = null;
		queue = null;
		currSwitch = null;
		currPort = null;

		if (!FloodlightProvider.getSwitches(true).isEmpty()) {
			for (Switch sw : FloodlightProvider.getSwitches(false)) {
				new TreeItem(treeSwitches, SWT.NONE).setText(sw.getDpid());
			}
		} else {
			new TreeItem(treeSwitches, SWT.NONE).setText("None");
		}
	}

	private void populatePortTree(int index) {

		// trees clear
		treePort.removeAll();
		treeQueues.removeAll();

		// queue text clear
		textQueueID.setText("");
		textMax.setText("");
		textMin.setText("");

		// port text clear
		textPortMax.setText("");
		textPortMin.setText("");
		textQueueName.setText("");

		currSwitchIndex = index;
		try {
			currSwitch = FloodlightProvider.getSwitches(false).get(
					currSwitchIndex);
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// invoke event?
		treeSwitches.select(treeSwitches.getItem(index));

		for (Port port : currSwitch.getPorts()) {
			new TreeItem(treePort, SWT.NONE).setText(port.getPortNumber());
		}
	}

	private void populatePortView(int index) {

		// text port clear
		textPortMax.setText("");
		textPortMin.setText("");
		textQueueName.setText("");

		// trees clear
		treeQueues.removeAll();

		// text queue clear
		textQueueID.setText("");
		textMax.setText("");
		textMin.setText("");

		currPortIndex = index;
		try {
			currPort = currSwitch.getPorts().get(currPortIndex);
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		treePort.select(treePort.getItem(currPortIndex));

		if (qospolicy != null
				&& qospolicy.getSwitchdpid().equals(currSwitch.getDpid())
				&& qospolicy.getQueuePort().equals(currPort.getPortNumber())) {

			textQueueName.setText(qospolicy.getQosName());
			textPortMax.setText(String.valueOf(qospolicy.getMaxRate()));
			textPortMin.setText(String.valueOf(qospolicy.getMinRate()));
			// means the policy data haven't been totally set
			policyset = false;
			populateQueueTree(index);
		} else if (qospolicy == null) {
			return;
		} else {

		}
	}

	private void populateQueueTree(int index) {

		List<QosQueue> queues = qospolicy.getQueues();
		Iterator<QosQueue> it = queues.iterator();
		QosQueue queueitem;

		if (queues.size() == 0) {
			// if there is no queues set, set "none"
			new TreeItem(treeQueues, SWT.NONE).setText("None");
		} else {
			// get all queues in qospolicy to tree view
			while (it.hasNext()) {
				queueitem = it.next();
				new TreeItem(treeQueues, SWT.NONE).setText(String
						.valueOf(queueitem.getQueueID()));
			}
		}
	}

	private void populateQueueView(int index) {

		textQueueID.setText("");
		textMax.setText("");
		textMin.setText("");

		queue = qospolicy.getQueue(index);
		if (queue != null) {
			textQueueID.setText(String.valueOf(queue.getQueueID()));
			textMax.setText(String.valueOf(queue.getMaxRate()));
			textMin.setText(String.valueOf(queue.getMinRate()));
		}
	}

	private void setupNewPolicy() {

		if (currSwitch == null || currPort == null) {
			DisplayMessage.displayError(this, "Please choose a switch port.");
			return;
		} else if (unsavedprogress) {
			int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
			MessageBox messageBox = new MessageBox(this, style);
			messageBox.setText("Are you sure?!");
			messageBox
					.setMessage("Are you sure you wish to exit the flow manager? "
							+ "Any unsaved changes will not be pushed.");
			if (messageBox.open() == SWT.YES) {
				unsavedprogress = false;
				setupNewPolicy();
			}
		} else if (qospolicy == null
				|| !(qospolicy.getSwitchdpid().equals(currSwitch.getDpid()) && qospolicy
						.getQueuePort().equals(currPort.getPortNumber()))) {
			qospolicy = new QosPolicy();
			qospolicy.setSwitchdpid(currSwitch.getDpid());
			qospolicy.setQueuePort(currPort.getPortNumber());
			unsavedprogress = true;
		}
	}

	private void setupNewQueues() {

		// set policy name and max & min rate
		if (qospolicy != null && policyset == false) {
			qospolicy.setQosName(textQueueName.getText());
			qospolicy.setMaxRate(Integer.valueOf(textPortMax.getText()));
			qospolicy.setMinRate(Integer.valueOf(textPortMin.getText()));
			policyset = true;
		}

		// newqueue true means there has been a new queue, no need to create
		if (qospolicy != null && newqueue == false) {
			queue = new QosQueue();
			textMax.setText("");
			textMin.setText("");
			textQueueID.setText(String.valueOf(queueNumber));
			newqueue = true;
		} else if (newqueue == true) {
			int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
			MessageBox messageBox = new MessageBox(this, style);
			messageBox.setText("Are you sure?!");
			messageBox
					.setMessage("Are you sure you wish to exit the flow manager? "
							+ "Any unsaved changes will not be pushed.");
			if (messageBox.open() == SWT.YES) {
				newqueue = false;
				setupNewQueues();
			}
		}
	}

	private void addToPolicy() {
		// TODO Auto-generated method stub
		// there is a new queue
		if (newqueue == true) {
			try {
				queue.setMaxRate(Integer.valueOf(textMax.getText()));
				queue.setMinRate(Integer.valueOf(textMin.getText()));
				queue.setQueueID(Integer.valueOf(textQueueID.getText()));
			} catch (NumberFormatException e) {
				// TODO: handle exception
				DisplayMessage.displayError(this, "Rate must be number.");
				e.printStackTrace();
				return;
			}
			qospolicy.addQueue(queue);
			queueNumber++;
			newqueue = false;
		} else {
			DisplayMessage.displayError(this, "you have saved this queue.");
		}
		populateQueueTree(currPortIndex);
	}

	private void addToRemote() {
		// TODO Auto-generated method stub

		if (qospolicy == null) {
			return;
		} else if (qospolicy.getQueues().size() == 0) {
			return;
		}

		String command = qospolicy.serialize();
		String command1 = "ovs-vsctl -- set Port vif52.0 qos=@newqos "
				+ "-- --id=@newqos create QoS type=linux-htb other-config:max-rate=111111 other-config:min-rate=11111 "
				+ "queues=0=@q0,1=@q1 "
				+ "-- --id=@q0 create queue other-config:max-rate=1111 other-config:min-rate=111 "
				+ "-- --id=@q1 create queue other-config:max-rate=100 other-config:min-rate=50";

		String result = SSHConnector.exec(command);
		if (!result.equals("")) {
			String[] uuids = result.split("\n");
			qospolicy.setUuid(uuids[0]);
			System.out.println(uuids[0]);
			for (int i = 1; i < uuids.length; i++) {
				qospolicy.getQueue(i - 1).setUuid(uuids[i]);
				System.out.println(uuids[i]);
			}
			DisplayMessage.displayError(this, "uuid received");
		} else {
			DisplayMessage.displayError(this, "No uuids received.");
		}
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("NewQos");
		this.setSize(winWidth, winHeight);
		this.setLocation(new Point(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2
						- winWidth / 2, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 2 - winHeight / 2));
		treeSwitches = new Tree(this, SWT.BORDER);
		// treeSwitches.setHeaderVisible(true);
		treeSwitches.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] sw_selection = treeSwitches.getSelection();
				if (sw_selection.length != 0) {
					if (sw_selection[0].getText().equals("None")) {
						return;
					}
					populatePortTree(treeSwitches.indexOf(sw_selection[0]));
				}
			}
		});
		treeSwitches.setBounds(10, 10, 188, 141);

		treePort = new Tree(this, SWT.BORDER);
		// treePort.setHeaderVisible(true);
		treePort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] port_selection = treePort.getSelection();
				if (port_selection.length != 0) {
					populatePortView(treePort.indexOf(port_selection[0]));
				}
			}
		});
		treePort.setBounds(10, 157, 188, 159);

		Composite composite = new Composite(this, SWT.BORDER);
		composite.setBounds(338, 102, 384, 170);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 50, 55, 15);
		lblNewLabel.setText("ID");

		textQueueID = new Text(composite, SWT.BORDER);
		textQueueID.setBounds(71, 47, 73, 21);
		textQueueID.setEditable(false);

		textMax = new Text(composite, SWT.BORDER);
		textMax.setBounds(71, 95, 73, 21);

		Label lblMaxrate = new Label(composite, SWT.NONE);
		lblMaxrate.setText("MaxRate");
		lblMaxrate.setAlignment(SWT.RIGHT);
		lblMaxrate.setBounds(10, 98, 55, 15);

		textMin = new Text(composite, SWT.BORDER);
		textMin.setBounds(248, 95, 73, 21);

		Label lblMinrate = new Label(composite, SWT.NONE);
		lblMinrate.setText("MinRate");
		lblMinrate.setAlignment(SWT.RIGHT);
		lblMinrate.setBounds(187, 98, 55, 15);

		Composite composite_1 = new Composite(this, SWT.BORDER);
		composite_1.setBounds(204, 10, 518, 86);

		Label lblQueuename = new Label(composite_1, SWT.NONE);
		lblQueuename.setBounds(10, 37, 67, 15);
		lblQueuename.setText("QueueName");

		textQueueName = new Text(composite_1, SWT.BORDER);
		textQueueName.setBounds(83, 33, 140, 21);

		textPortMax = new Text(composite_1, SWT.BORDER);
		textPortMax.setToolTipText("1-1000000");
		textPortMax.setBounds(329, 10, 133, 21);

		Label lblPortmaxrate = new Label(composite_1, SWT.NONE);
		lblPortmaxrate.setText("PortMaxRate");
		lblPortmaxrate.setBounds(256, 14, 67, 15);

		textPortMin = new Text(composite_1, SWT.BORDER);
		textPortMin.setToolTipText("1-1000000");
		textPortMin.setBounds(329, 55, 133, 21);

		Label lblPortminrate = new Label(composite_1, SWT.NONE);
		lblPortminrate.setText("PortMinRate");
		lblPortminrate.setBounds(256, 59, 67, 15);

		Composite composite_2 = new Composite(this, SWT.NONE);
		composite_2.setBounds(203, 102, 129, 214);

		treeQueues = new Tree(composite_2, SWT.BORDER);
		// treeQueues.setHeaderVisible(true);
		treeQueues.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] queue_selection = treeQueues.getSelection();
				if (queue_selection.length != 0) {
					if (queue_selection[0].getText().equals("None"))
						return;
					populateQueueView(treeQueues.indexOf(queue_selection[0]));
				}
			}
		});
		treeQueues.setBounds(0, 0, 129, 214);

		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addToPolicy();
			}
		});
		btnAdd.setBounds(514, 278, 75, 38);
		btnAdd.setText("ADD");

		Button btnFinish = new Button(this, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addToRemote();
			}
		});
		btnFinish.setBounds(595, 278, 75, 38);
		btnFinish.setText("Finish");

		Button btnNewQueue = new Button(this, SWT.NONE);
		btnNewQueue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupNewQueues();
			}
		});
		btnNewQueue.setBounds(427, 278, 75, 38);
		btnNewQueue.setText("NewQueue");

		Button btnNewPolicy = new Button(this, SWT.NONE);
		btnNewPolicy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupNewPolicy();
			}
		});
		btnNewPolicy.setBounds(346, 278, 75, 38);
		btnNewPolicy.setText("NewPolicy");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
