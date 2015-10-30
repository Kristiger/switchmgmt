package view.tools.Qos;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.List;

import model.overview.Port;
import model.overview.Switch;
import model.tools.Qos.QosPolicy;
import model.tools.Qos.QosQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import view.util.DisplayMessage;
import controller.floodlightprovider.FloodlightProvider;

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
	private int currSwitchIndex;
	private int currPortIndex;
	private static boolean unsavedprogress, newqueue, newpolicy;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			AddQos shell = new AddQos(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

		// trees clear
		treePort.removeAll();
		treeQueues.removeAll();

		// text port clear
		textPortMax.setText("");
		textPortMin.setText("");
		textQueueName.setText("");

		// text queue clear
		textQueueID.setText("");
		textMax.setText("");
		textMin.setText("");

		currPortIndex = index;
		try {
			currPort = currSwitch.getPorts().get(index);
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (qospolicy != null
				&& qospolicy.getSwitchdpid().equals(currSwitch.getDpid())
				&& qospolicy.getQueuePort().equals(currPort.getPortNumber())) {

			textQueueName.setText(qospolicy.getQosName());
			textPortMax.setText(String.valueOf(qospolicy.getMaxRate()));
			textPortMin.setText(String.valueOf(qospolicy.getMinRate()));

			populateQueueTree();
		}
	}

	private void populateQueueTree() {

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

	public void setupNewPolicy() {

		if (currSwitch == null || currPort == null) {
			DisplayMessage.displayError(this, "Please choose a switch port.");
			return;
		} else if (unsavedprogress) {
			
		} else if (qospolicy == null
				|| !(qospolicy.getSwitchdpid().equals(currSwitch.getDpid()) && qospolicy
						.getQueuePort().equals(currPort.getPortNumber()))) {
			qospolicy = new QosPolicy();
			qospolicy.setSwitchdpid(currSwitch.getDpid());
			qospolicy.setQueuePort(currPort.getPortNumber());
			newpolicy = false;
		}
	}

	public void setupNewQueues() {
		if (qospolicy != null) {
			queue = new QosQueue();
			newqueue = false;
		} else {
			DisplayMessage
					.displayError(this, "Please create new policy first.");
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public AddQos(Display display) {
		super(display, SWT.SHELL_TRIM);

		createContents();
		populateSwitchTree();
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
		treeSwitches.setHeaderVisible(true);
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
		treeSwitches.setBounds(10, 10, 154, 141);

		treePort = new Tree(this, SWT.BORDER);
		treePort.setHeaderVisible(true);
		treePort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] port_selection = treePort.getSelection();
				if (port_selection.length != 0) {
					populatePortView(treePort.indexOf(port_selection[0]));
				}
			}
		});
		treePort.setBounds(10, 157, 154, 159);

		Composite composite = new Composite(this, SWT.BORDER);
		composite.setBounds(293, 102, 429, 170);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(10, 50, 55, 15);
		lblNewLabel.setText("ID");

		textQueueID = new Text(composite, SWT.BORDER);
		textQueueID.setBounds(71, 47, 73, 21);

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
		composite_1.setBounds(170, 10, 552, 86);

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
		composite_2.setBounds(170, 102, 117, 214);

		treeQueues = new Tree(composite_2, SWT.BORDER);
		treeQueues.setHeaderVisible(true);
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
		treeQueues.setBounds(0, 0, 117, 214);

		Button btnAdd = new Button(this, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAdd.setBounds(514, 278, 75, 38);
		btnAdd.setText("ADD");

		Button btnClear = new Button(this, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnClear.setBounds(595, 278, 75, 38);
		btnClear.setText("ClearInput");

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
