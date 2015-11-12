package view;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.overview.Port;
import model.overview.QosPolicy;
import model.overview.QosQueue;
import model.overview.Switch;
import model.overview.VMData;

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

public class Qos {
	protected Shell shell;
	private static final int winWidth = 748;
	private static final int winHeight = 364;

	private Text textPortMax;
	private Text textPortMin;
	private Text textQueueID_0;
	private Text textQueueMax_0;
	private Text textQueueMin_0;
	private Text textQueueID_1;
	private Text textQueueMax_1;
	private Text textQueueMin_1;
	private Text textQueueID_2;
	private Text textQueueMax_2;
	private Text textQueueMin_2;
	private Text textQueueID_3;
	private Text textQueueMax_3;
	private Text textQueueMin_3;
	private Text textQueueID_4;
	private Text textQueueMax_4;
	private Text textQueueMin_4;

	private Tree treeSwitches;
	private Tree treePort;

	private List<VMData> vms;
	private List<QosPolicy> qospolicies;
	private QosPolicy qospolicy = null;
	private static Port currPort = null;
	private static Switch currSwitch = null;
	private int currSwitchIndex = -1, currPortIndex = -1;
	private Button btnCheckButton_0;
	private Button btnCheckButton_4;
	private Button btnCheckButton_1;
	private Button btnCheckButton_2;
	private Button btnCheckButton_3;
	private static boolean unsavedprogress;

	public Qos() {
		open();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		populateSwitchTree();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void populateSwitchTree() {

		// clear trees
		treeSwitches.removeAll();
		treePort.removeAll();

		// clear port
		textPortMax.setText("");
		textPortMin.setText("");

		// clear queue
		clearQueueTexts();

		qospolicy = null;
		currSwitch = null;
		currPort = null;

		if (!FloodlightProvider.getSwitches(true).isEmpty()) {
			for (Switch sw : FloodlightProvider.getSwitches(false)) {
				new TreeItem(treeSwitches, SWT.NONE).setText(sw.getDpid());
			}
		} else {
			new TreeItem(treeSwitches, SWT.NONE).setText("None");
		}

		if (!FloodlightProvider.getQospolicys().isEmpty()) {
			qospolicies = FloodlightProvider.getQospolicys();
		}

		if (!FloodlightProvider.getVms(false).isEmpty()) {
			vms = FloodlightProvider.getVms(false);
		}
	}

	private void populatePortTree(int index) {

		if (unsavedprogress)
			if (abandonUnsaved())
				populatePortTree(index);

		// trees clear
		treePort.removeAll();

		// queue text clear
		clearQueueTexts();

		// port clear
		textPortMax.setText("");
		textPortMin.setText("");

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
		if (unsavedprogress)
			if (abandonUnsaved())
				populatePortView(index);

		// port clear
		textPortMax.setText("");
		textPortMin.setText("");

		// queue clear
		clearQueueTexts();

		currPortIndex = index;
		try {
			currPort = currSwitch.getPorts().get(currPortIndex);
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		treePort.select(treePort.getItem(currPortIndex));

		if (qospolicyExist()) {
			textPortMax.setText(String.valueOf(qospolicy.getMaxRate()));
			textPortMin.setText(String.valueOf(qospolicy.getMinRate()));
			populateQueueView(currPortIndex);
		}
	}

	private boolean qospolicyExist() {
		// TODO Auto-generated method stub
		if (qospolicies == null) {
			qospolicies = FloodlightProvider.getQospolicys();
			if (qospolicies == null)
				return false;
		}

		Iterator<QosPolicy> it = qospolicies.iterator();
		VMData vm = null;
		while (it.hasNext()) {
			qospolicy = it.next();
			if (qospolicy.getSwitchdpid().equals(currSwitch.getDpid())) {
				vm = FloodlightProvider.getVM(currPort.getPortNumber());
				if (vm != null
						&& vm.getVmOvsPort().equals(qospolicy.getQueuePort()))
					return true;
			}
		}
		qospolicy = null;
		return false;
	}

	private void populateQueueView(int index) {
		if (qospolicyExist()) {
			Iterator<QosQueue> it = qospolicy.getQueues().iterator();
			QosQueue queue = null;
			while (it.hasNext()) {
				queue = it.next();
				switch (queue.getQueueID()) {
				case 0:
					textQueueID_0.setText(String.valueOf(queue.getQueueID()));
					textQueueMax_0.setText(String.valueOf(queue.getMaxRate()));
					textQueueMin_0.setText(String.valueOf(queue.getMinRate()));
					btnCheckButton_0.setSelection(true);
					break;
				case 1:
					textQueueID_1.setText(String.valueOf(queue.getQueueID()));
					textQueueMax_1.setText(String.valueOf(queue.getMaxRate()));
					textQueueMin_1.setText(String.valueOf(queue.getMinRate()));
					btnCheckButton_1.setSelection(true);
					break;
				case 2:
					textQueueID_2.setText(String.valueOf(queue.getQueueID()));
					textQueueMax_2.setText(String.valueOf(queue.getMaxRate()));
					textQueueMin_2.setText(String.valueOf(queue.getMinRate()));
					btnCheckButton_2.setSelection(true);
					break;
				case 3:
					textQueueID_3.setText(String.valueOf(queue.getQueueID()));
					textQueueMax_3.setText(String.valueOf(queue.getMaxRate()));
					textQueueMin_3.setText(String.valueOf(queue.getMinRate()));
					btnCheckButton_3.setSelection(true);
					break;
				case 4:
					textQueueID_4.setText(String.valueOf(queue.getQueueID()));
					textQueueMax_4.setText(String.valueOf(queue.getMaxRate()));
					textQueueMin_4.setText(String.valueOf(queue.getMinRate()));
					btnCheckButton_4.setSelection(true);
					break;
				default:
					break;
				}
			}
		}
	}

	private void setupNewPolicy() {
		if (currSwitch == null || currPort == null) {
			DisplayMessage.displayError(shell, "Please choose a switch port.");
			return;
		} else if (unsavedprogress) {
			if (abandonUnsaved()) {
				setupNewPolicy();
			}
		} else if (qospolicyExist()) {
			DisplayMessage
					.displayError(shell, "Policy exists, just add queues");
		} else {
			if (vms == null || vms.size() == 0) {
				// should be exist
				DisplayMessage.displayError(shell, "havent get vms");
				vms = FloodlightProvider.getVms(false);
				return;
			}

			qospolicy = new QosPolicy();
			qospolicy.setSwitchdpid(currSwitch.getDpid());

			// set qospolicy ovs port
			Iterator<VMData> it = vms.iterator();
			VMData vm = null;
			while (it.hasNext()) {
				vm = it.next();
				if (vm.getVmSwPort().equals(currPort.getPortNumber())) {
					if (!vm.getVmOvsPort().equals("none"))
						qospolicy.setQueuePort(vm.getVmOvsPort());
					else {
						DisplayMessage
								.displayError(shell,
										"Havent get the port attached to physicalswitch");
					}
					return;
				}
			}
			unsavedprogress = true;
		}
	}

	private boolean abandonUnsaved() {
		// TODO Auto-generated method stub
		int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setText("Are you sure?!");
		messageBox
				.setMessage("Are you sure you wish to exit the flow manager? "
						+ "Any unsaved changes will not be pushed.");
		if (messageBox.open() == SWT.YES) {
			unsavedprogress = false;
			return true;
		}
		return false;
	}

	private void addToRemote() {
		// TODO Auto-generated method stub
		if (qospolicy == null) {
			qospolicy = new QosPolicy();
		}
		// get the data inputed
		if (!fullfillQospolicy() || qospolicy.getQueues().size() == 0)
			return;

		String clearcmd = "ovs-vsctl clear port " + qospolicy.getQueuePort()
				+ " qos";
		String command = clearcmd + " && " + qospolicy.serialize();
		String command1 = "ovs-vsctl clear port vif52.0 && ovs-vsctl -- set Port vif52.0 qos=@newqos "
				+ "-- --id=@newqos create QoS type=linux-htb other-config:max-rate=111111 other-config:min-rate=11111 "
				+ "queues=0=@q0,1=@q1 "
				+ "-- --id=@q0 create queue other-config:max-rate=1111 other-config:min-rate=111 "
				+ "-- --id=@q1 create queue other-config:max-rate=100 other-config:min-rate=50";

		String result = SSHConnector.exec(command);
		if (!result.equals("")) {
			String[] uuids = result.split("\n");
			qospolicy.setUuid(uuids[0]);
			int i = 1;
			Iterator<QosQueue> it = qospolicy.getQueues().iterator();
			QosQueue queue = null;
			while (it.hasNext()) {
				queue = it.next();
				queue.setUuid(uuids[i++]);
			}
			QosPolicy other = null;
			if ((other = FloodlightProvider.getQospolicy(
					qospolicy.getSwitchdpid(), qospolicy.getQueuePort())) != null)
				other = qospolicy;
			else {
				FloodlightProvider.addQospolicy(qospolicy);
				qospolicies.add(qospolicy);
				DisplayMessage.displayStatus(shell, "Success");
				populatePortView(currPortIndex);
			}
		} else {
			DisplayMessage.displayError(shell, "No uuids received.");
		}
	}

	private boolean fullfillQospolicy() {
		// TODO Auto-generated method stub
		List<QosQueue> queues = new ArrayList<QosQueue>();
		VMData vm = null;
		qospolicy.setSwitchdpid(currSwitch.getDpid());
		if ((vm = FloodlightProvider.getVM(currPort.getPortNumber())) != null)
			qospolicy.setQueuePort(vm.getVmOvsPort());
		else {
			DisplayMessage.displayError(shell, "Can't get ovs port");
			return false;
		}
		try {
			qospolicy.setMaxRate(Integer.valueOf(textPortMax.getText()));
			qospolicy.setMinRate(Integer.valueOf(textPortMin.getText()));
		} catch (NumberFormatException e) {
			// TODO: handle exception
			e.printStackTrace();
			DisplayMessage.displayError(shell, "Please input number: port");
			return false;
		}
		queues = qospolicy.getQueues();
		queues.clear();
		if (btnCheckButton_0.getSelection()) {
			if (!textQueueMax_0.getText().equals("")
					&& !textQueueMin_0.getText().equals("")) {
				try {
					queues.add(new QosQueue(Integer.valueOf(textQueueID_0
							.getText()), Integer.valueOf(textQueueMin_0
							.getText()), Integer.valueOf(textQueueMax_0
							.getText())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
					DisplayMessage.displayError(shell,
							"Please input number: queue 0");
					return false;
				}
			}
		}
		if (btnCheckButton_1.getSelection()) {
			if (!textQueueMax_1.getText().equals("")
					&& !textQueueMin_1.getText().equals("")) {
				try {
					queues.add(new QosQueue(Integer.valueOf(textQueueID_1
							.getText()), Integer.valueOf(textQueueMin_1
							.getText()), Integer.valueOf(textQueueMax_1
							.getText())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
					DisplayMessage.displayError(shell,
							"Please input number: queue 1");
					return false;
				}
			}
		}
		if (btnCheckButton_2.getSelection()) {
			if (!textQueueMax_2.getText().equals("")
					&& !textQueueMin_2.getText().equals("")) {
				try {
					queues.add(new QosQueue(Integer.valueOf(textQueueID_2
							.getText()), Integer.valueOf(textQueueMin_2
							.getText()), Integer.valueOf(textQueueMax_2
							.getText())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
					DisplayMessage.displayError(shell,
							"Please input number: queue 2");
					return false;
				}
			}
		}
		if (btnCheckButton_3.getSelection()) {
			if (!textQueueMax_3.getText().equals("")
					&& !textQueueMin_3.getText().equals("")) {
				try {
					queues.add(new QosQueue(Integer.valueOf(textQueueID_3
							.getText()), Integer.valueOf(textQueueMin_3
							.getText()), Integer.valueOf(textQueueMax_3
							.getText())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
					DisplayMessage.displayError(shell,
							"Please input number: queue 3");
					return false;
				}
			}
		}
		if (btnCheckButton_4.getSelection()) {
			if (!textQueueMax_4.getText().equals("")
					&& !textQueueMin_4.getText().equals("")) {
				try {
					queues.add(new QosQueue(Integer.valueOf(textQueueID_4
							.getText()), Integer.valueOf(textQueueMin_4
							.getText()), Integer.valueOf(textQueueMax_4
							.getText())));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
					DisplayMessage.displayError(shell,
							"Please input number: queue 4");
					return false;
				}
			}
		}
		return true;
	}

	private void clearQueueTexts() {
		textQueueID_0.setText("0");
		textQueueID_1.setText("1");
		textQueueID_2.setText("2");
		textQueueID_3.setText("3");
		textQueueID_4.setText("4");

		textQueueMax_0.setText("");
		textQueueMax_1.setText("");
		textQueueMax_2.setText("");
		textQueueMax_3.setText("");
		textQueueMax_4.setText("");

		textQueueMin_0.setText("");
		textQueueMin_1.setText("");
		textQueueMin_2.setText("");
		textQueueMin_3.setText("");
		textQueueMin_4.setText("");

		btnCheckButton_0.setSelection(false);
		btnCheckButton_1.setSelection(false);
		btnCheckButton_2.setSelection(false);
		btnCheckButton_3.setSelection(false);
		btnCheckButton_4.setSelection(false);
	}

	private void clearAllQueues() {
		String command = "ovs-vsctl clear port " + qospolicy.getQueuePort()
				+ " qos && ovs-vsctl --all destroy qos --all destroy queue";
		String result = SSHConnector.exec(command);
		DisplayMessage.displayStatus(shell, result);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setText("NewQos");
		shell.setSize(686, 364);
		shell.setLocation(new Point(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2
						- winWidth / 2, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 2 - winHeight / 2));

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(204, 88, 452, 178);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(64, 13, 28, 15);
		lblNewLabel.setText("ID");

		textQueueID_0 = new Text(composite, SWT.BORDER);
		textQueueID_0.setBounds(45, 34, 92, 21);
		textQueueID_0.setEditable(false);

		textQueueMax_0 = new Text(composite, SWT.BORDER);
		textQueueMax_0.setBounds(175, 34, 92, 21);

		Label lblMaxrate = new Label(composite, SWT.NONE);
		lblMaxrate.setText("MaxRate");
		lblMaxrate.setAlignment(SWT.RIGHT);
		lblMaxrate.setBounds(180, 13, 55, 15);

		textQueueMin_0 = new Text(composite, SWT.BORDER);
		textQueueMin_0.setBounds(301, 34, 92, 21);

		Label lblMinrate = new Label(composite, SWT.NONE);
		lblMinrate.setText("MinRate");
		lblMinrate.setAlignment(SWT.RIGHT);
		lblMinrate.setBounds(309, 13, 55, 15);

		textQueueID_1 = new Text(composite, SWT.BORDER);
		textQueueID_1.setText("");
		textQueueID_1.setEditable(false);
		textQueueID_1.setBounds(45, 61, 92, 21);

		textQueueMax_1 = new Text(composite, SWT.BORDER);
		textQueueMax_1.setText("");
		textQueueMax_1.setBounds(175, 61, 92, 21);

		textQueueMin_1 = new Text(composite, SWT.BORDER);
		textQueueMin_1.setText("");
		textQueueMin_1.setBounds(301, 61, 92, 21);

		textQueueID_2 = new Text(composite, SWT.BORDER);
		textQueueID_2.setText("");
		textQueueID_2.setEditable(false);
		textQueueID_2.setBounds(45, 88, 92, 21);

		textQueueMax_2 = new Text(composite, SWT.BORDER);
		textQueueMax_2.setText("");
		textQueueMax_2.setBounds(175, 88, 92, 21);

		textQueueMin_2 = new Text(composite, SWT.BORDER);
		textQueueMin_2.setText("");
		textQueueMin_2.setBounds(301, 88, 92, 21);

		textQueueID_3 = new Text(composite, SWT.BORDER);
		textQueueID_3.setText("");
		textQueueID_3.setEditable(false);
		textQueueID_3.setBounds(45, 115, 92, 21);

		textQueueMax_3 = new Text(composite, SWT.BORDER);
		textQueueMax_3.setText("");
		textQueueMax_3.setBounds(175, 115, 92, 21);

		textQueueMin_3 = new Text(composite, SWT.BORDER);
		textQueueMin_3.setText("");
		textQueueMin_3.setBounds(301, 115, 92, 21);

		textQueueID_4 = new Text(composite, SWT.BORDER);
		textQueueID_4.setText("");
		textQueueID_4.setEditable(false);
		textQueueID_4.setBounds(45, 142, 92, 21);

		textQueueMax_4 = new Text(composite, SWT.BORDER);
		textQueueMax_4.setText("");
		textQueueMax_4.setBounds(175, 142, 92, 21);

		textQueueMin_4 = new Text(composite, SWT.BORDER);
		textQueueMin_4.setText("");
		textQueueMin_4.setBounds(301, 142, 92, 21);

		btnCheckButton_0 = new Button(composite, SWT.CHECK);
		btnCheckButton_0.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton_0.setBounds(26, 39, 13, 16);
		btnCheckButton_0.setText("Check Button");

		btnCheckButton_4 = new Button(composite, SWT.CHECK);
		btnCheckButton_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton_4.setText("Check Button");
		btnCheckButton_4.setBounds(26, 147, 13, 16);

		btnCheckButton_1 = new Button(composite, SWT.CHECK);
		btnCheckButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton_1.setText("Check Button");
		btnCheckButton_1.setBounds(26, 66, 13, 16);

		btnCheckButton_2 = new Button(composite, SWT.CHECK);
		btnCheckButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton_2.setText("Check Button");
		btnCheckButton_2.setBounds(26, 93, 13, 16);

		btnCheckButton_3 = new Button(composite, SWT.CHECK);
		btnCheckButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton_3.setText("Check Button");
		btnCheckButton_3.setBounds(26, 120, 13, 16);

		Composite composite_1 = new Composite(shell, SWT.BORDER);
		composite_1.setBounds(204, 10, 452, 72);

		textPortMax = new Text(composite_1, SWT.BORDER);
		textPortMax.setToolTipText("1-1000000");
		textPortMax.setBounds(42, 31, 133, 21);

		Label lblPortmaxrate = new Label(composite_1, SWT.NONE);
		lblPortmaxrate.setText("PortMaxRate");
		lblPortmaxrate.setBounds(75, 10, 67, 15);

		textPortMin = new Text(composite_1, SWT.BORDER);
		textPortMin.setToolTipText("1-1000000");
		textPortMin.setBounds(259, 31, 133, 21);

		Label lblPortminrate = new Label(composite_1, SWT.NONE);
		lblPortminrate.setText("PortMinRate");
		lblPortminrate.setBounds(289, 10, 67, 15);

		Button btnPush = new Button(shell, SWT.NONE);
		btnPush.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addToRemote();
			}
		});
		btnPush.setBounds(399, 278, 75, 38);
		btnPush.setText("Save");

		Button btnNewPolicy = new Button(shell, SWT.NONE);
		btnNewPolicy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupNewPolicy();
			}
		});
		btnNewPolicy.setBounds(269, 278, 75, 38);
		btnNewPolicy.setText("NewPolicy");

		Button btnClearall = new Button(shell, SWT.NONE);
		btnClearall.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		btnClearall.setBounds(525, 278, 75, 38);
		btnClearall.setText("ClearAll");

		Composite composite_2 = new Composite(shell, SWT.BORDER);
		composite_2.setBounds(10, 10, 177, 306);
		treeSwitches = new Tree(composite_2, SWT.BORDER);
		treeSwitches.setLocation(-1, 26);
		treeSwitches.setSize(177, 119);

		treePort = new Tree(composite_2, SWT.BORDER);
		treePort.setLocation(-1, 165);
		treePort.setSize(177, 138);

		Label lblSwitches = new Label(composite_2, SWT.NONE);
		lblSwitches.setBounds(10, 10, 55, 15);
		lblSwitches.setText("Switches");

		Label lblPorts = new Label(composite_2, SWT.NONE);
		lblPorts.setBounds(10, 153, 55, 15);
		lblPorts.setText("Ports");
		treePort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] port_selection = treePort.getSelection();
				if (port_selection.length != 0) {
					populatePortView(treePort.indexOf(port_selection[0]));
				}
			}
		});
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

	}

}
