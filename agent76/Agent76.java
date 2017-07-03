package agent76;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import negotiator.Agent;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.EndNegotiation;
import negotiator.actions.Offer;
import negotiator.issue.Issue;
import negotiator.issue.IssueDiscrete;
import negotiator.issue.IssueInteger;
import negotiator.issue.IssueReal;
import negotiator.issue.Value;
import negotiator.issue.ValueInteger;
import negotiator.issue.ValueReal;

/**
 * 
 * First attempt at an automated negotiation agent.
 * @author Kaitlyn
 *
 */
public class Agent76 extends Agent{
	private Action partnerAction = null;
	private Bid lastPartnerBid;
	private static double MIN_BID_UTILITY = 0.00;
	
	@Override
	public void init(){
		MIN_BID_UTILITY = utilitySpace.getReservationValueUndiscounted();
	}
	
	@Override
	public String getVersion(){
		return "0.1";
	}
	
	@Override
	public String getName(){
		return "Agent76";
	}
	
	@Override
	public void ReceiveMessage(Action opponentAction){
	}
	
	@Override
	public Action chooseAction(){
	}

	private boolean isAcceptable(double oppUtil, double myUtil, double time) throws Exception{
	}
	
	private Action chooseRandomBidAction(){
	}
	
	private Bid getRandomBid() throws Exception{
		return bid;
	}
}