package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.overview.VmData;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controller.overview.vms.VmDataGetter;

public class RateConfig {

	protected Shell shell;
	private Text textMax;
	private Text textMin;
	private List<VmData> vms = VmDataGetter.getVmDatas(false);
	private VmData vm = null;
	private Label label;
	private Label label_1;
	private Label label_2;
	private boolean initFromVm = false;

	/**
	 * @wbp.parser.constructor
	 */
	public RateConfig() {
		open();
	}

	public RateConfig(int index) {
		open();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(621, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tabItemBandwidth = new CTabItem(tabFolder, SWT.NONE);
		tabItemBandwidth.setText("  \u5E26\u5BBD\u8BBE\u7F6E  ");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItemBandwidth.setControl(composite_1);

		label = new Label(composite_1, SWT.NONE);
		label.setLocation(10, 65);
		label.setSize(55, 15);
		label.setText("IP:");

		textMax = new Text(composite_1, SWT.BORDER);
		textMax.setLocation(10, 139);
		textMax.setSize(124, 21);

		label_1 = new Label(composite_1, SWT.NONE);
		label_1.setLocation(180, 65);
		label_1.setSize(55, 15);
		label_1.setText("Mac");

		textMin = new Text(composite_1, SWT.BORDER);
		textMin.setLocation(276, 139);
		textMin.setSize(124, 21);

		label_2 = new Label(composite_1, SWT.NONE);
		label_2.setLocation(368, 65);
		label_2.setSize(55, 15);
		label_2.setText("Port");

		Combo combo = new Combo(composite_1, SWT.NONE);
		combo.setSize(134, 23);
		combo.setItems(getVmComboData());
		
		Label lblMax = new Label(composite_1, SWT.NONE);
		lblMax.setBounds(10, 120, 55, 15);
		lblMax.setText("Max");

		Label lblMin = new Label(composite_1, SWT.NONE);
		lblMin.setText("Min");
		lblMin.setBounds(276, 120, 55, 15);

		CTabItem tabItemQueue = new CTabItem(tabFolder, SWT.NONE);
		tabItemQueue.setText("  \u961F\u5217\u8BBE\u7F6E  ");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabItemQueue.setControl(composite_2);

		CTabItem tabItemVlan = new CTabItem(tabFolder, SWT.NONE);
		tabItemVlan.setText("  VLAN\u8BBE\u7F6E  ");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tabItemVlan.setControl(composite_3);

		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("  \u4E1A\u52A1\u4FDD\u8BC1  ");

		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_4);

		CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("New Item");

		if(initFromVm){
			label.setText("IP : " + vm.getVmIpAddr());
			label_1.setText("Mac : " + vm.getVmMacAddr());
			label_2.setText("Port : " + vm.getVmSwitchPort());
		}
	}

	private String[] getVmComboData() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		Iterator<VmData> it = vms.iterator();
		while(it.hasNext()){
			list.add(it.next().getVmIpAddr());
		}
		return list.toArray(new String[list.size()]);
	}
}
