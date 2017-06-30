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
		partnerAction = opponentAction;
		
		if (partnerAction instanceof Offer)
			lastPartnerBid = ((Offer) partnerAction).getBid();
	}
	
	@Override
	public Action chooseAction(){
		Action action = null;
		
		try{
			if (partnerAction == null)
				action = chooseRandomBidAction();
			
			else if (partnerAction instanceof Offer){
				double offeredOppUtil = getUtility(lastPartnerBid);
				double time = timeline.getTime();
				action = chooseRandomBidAction();
				
				Bid myBid = ((Offer) action).getBid();
				double myOfferUtil = getUtility(myBid);
				
				if (isAcceptable(offeredOppUtil, myOfferUtil, time))
					action = new Accept (getAgentID(), lastPartnerBid);
			}
		}
		catch (Exception e){
			System.out.println("Exception in chooseAction():" + e.getMessage());
			if (lastPartnerBid != null)
				action = new Accept(getAgentID(), lastPartnerBid);
			else
				action = new EndNegotiation(getAgentID());
		}
		return action;
	}

	private boolean isAcceptable(double oppUtil, double myUtil, double time) throws Exception{
		double paccept = Paccept(oppUtil, time);
		if (paccept > Math.random())
			return true;
		return false;
	}
	
	private Action chooseRandomBidAction(){
		Bid nextBid = null;
		try{
			nextBid = getRandomBid();
		}
		catch (Exception e){
			System.out.println("Problem with received bid:" + e.getMessage() + ".\nCancelling bidding.");
		}
		if (nextBid == null)
			return (new Accept(getAgentID(), lastPartnerBid));
		return (new Offer(getAgentID(), nextBid));
	}
	
	private Bid getRandomBid() throws Exception{
		HashMap<Integer, Value> vals = new HashMap<>();
		List<Issue> issues = utilitySpace.getDomain().getIssues();
		Random randomGen = new Random();
		Bid bid = null;
		
		do{
			for (Issue lIssue: issues){
				switch (lIssue.getType()){
				
				case DISCRETE:
					IssueDiscrete lIssueDiscrete = (IssueDiscrete) lIssue;
					int opIndDisc = randomGen.nextInt(lIssueDiscrete.getNumberOfValues());
					vals.put(lIssue.getNumber(), lIssueDiscrete.getValue(opIndDisc));
					break;
					
				case REAL:
					IssueReal lIssueReal = (IssueReal) lIssue;
					int opIndReal = randomGen.nextInt(lIssueReal.getNumberOfDiscretizationSteps() - 1);
					vals.put(lIssueReal.getNumber(),
							new ValueReal (lIssueReal.getLowerBound()
									+ (lIssueReal.getUpperBound() - lIssueReal.getLowerBound()) * (double)(opIndReal)
									/ (double)(lIssueReal.getNumberOfDiscretizationSteps())));
					break;
				
				case INTEGER:
					IssueInteger lIssueInteger = (IssueInteger) lIssue;
					int opIndInt = lIssueInteger.getLowerBound()
								+ randomGen.nextInt(lIssueInteger.getUpperBound() - lIssueInteger.getLowerBound());
					vals.put(lIssueInteger.getNumber(), new ValueInteger(opIndInt));
					break;
					
				default:
					throw new Exception("Issue type " + lIssue.getType() + " is not supported.");
				}
			}
			bid = new Bid(utilitySpace.getDomain(), vals);
		}while (getUtility(bid) < MIN_BID_UTILITY);
		
		return bid;
	}
	
	double Paccept(double u, double t1) throws Exception {
		double t = t1 * t1 * t1; // steeper increase when deadline approaches.
		if (u < 0 || u > 1.05)
			throw new Exception("utility " + u + " outside [0,1]");
		// normalization may be slightly off, therefore we have a broad boundary
		// up to 1.05
		if (t < 0 || t > 1)
			throw new Exception("time " + t + " outside [0,1]");
		if (u > 1.)
			u = 1;
		if (t == 0.5)
			return u;
		return (u - 2. * u * t + 2. * (-1. + t + Math.sqrt(sq(-1. + t) + u * (-1. + 2 * t)))) / (-1. + 2 * t);
	}

	double sq(double x) {
		return x * x;
	}
	
	
}