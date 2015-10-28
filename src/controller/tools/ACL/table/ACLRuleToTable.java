package controller.tools.ACL.table;

import model.tools.ACL.ACLRule;

public class ACLRuleToTable {
	
	public static String[][] getACLRuleTableFormat(ACLRule aclrule) {
		
		String[][] rule = { { "RuleID", aclrule.getRuleID() },
				{ "nw-proto", aclrule.getNw_proto() },
				{ "src-ip", aclrule.getSrc_ip() },
				{ "dst-ip", aclrule.getDst_ip() },
				{ "tp-dst", String.valueOf(aclrule.getTp_dst()) },
				{ "action", aclrule.getAction() } };

		return rule;
	}
	
	public static String[][] getNewACLRuleTableFormat() {
		
		ACLRule aclrule = new ACLRule();
		
		String[][] rule = { { "RuleID", aclrule.getRuleID() },
				{ "nw-proto", aclrule.getNw_proto() },
				{ "src-ip", aclrule.getSrc_ip() },
				{ "dst-ip", aclrule.getDst_ip() },
				{ "tp-dst", String.valueOf(aclrule.getTp_dst()) },
				{ "action", aclrule.getAction() } };

		return rule;
	}
	
}
