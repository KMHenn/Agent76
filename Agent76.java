package agent76;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.issue.Issue;
import negotiator.issue.IssueDiscrete;
import negotiator.issue.IssueInteger;
import negotiator.issue.IssueReal;
import negotiator.issue.Value;
import negotiator.issue.ValueInteger;
import negotiator.issue.ValueReal;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;

/**
 * 
 * First attempt at an automated negotiation agent.
 * @author Kaitlyn
 *
 */
public class Agent76 extends AbstractNegotiationParty{
	
	private Bid lastReceivedBid = null;
	private Bid lastOfferedBid = null;
	private static final double MIN_BID_UTIL = 0.00;
	
	@Override
	public void init(NegotiationInfo info){
		super.init(info);
		
		System.out.println("Discount Factor: " + info.getUtilitySpace().getDiscountFactor());
		System.out.println("Reservation Value: " + info.getUtilitySpace().getReservationValueUndiscounted());
		
		//initialize any variables here.
	}
	
	/**
	 * Called each round, asking you to accept or create an offer.
	 * The first party of the first round is a bit different, since 
	 * the agent must propose.
	 * 
	 * @param validActions
	 * @return chosen action
	 * 
	 */
	@Override
	public Action chooseAction(List<Class<? extends Action>> validActions) {
		try{
			if ((lastReceivedBid == null) || (!validActions.contains(Accept.class))){ //Agent76 must bid.
				Bid newBid = createBid();
				return new Offer(getPartyId(), newBid);
			}
			else{
				double time = timeline.getTime();
				boolean bidQuality = judgeOffer(getUtility(lastReceivedBid), getUtility(lastOfferedBid), time);
				if (bidQuality == true)
					return new Accept(getPartyId(), lastReceivedBid);
				else
					return new Offer(getPartyId(), createBid()); 
			}
		}
		catch (Exception e){
			System.out.println("An error has occured.");
			e.printStackTrace();
			return new Offer(getPartyId(), lastReceivedBid);
		}
	}
	
	/**
	 * Receive all offers from the other parties.
	 * Used to predict the utility.
	 * 
	 * @param sender
	 * @param action
	 * 
	 */
	@Override
	public void receiveMessage(AgentID sender, Action action){
		super.receiveMessage(sender, action);
		if (action instanceof Offer){
			lastReceivedBid = ((Offer) action).getBid();
		}
	}

	/**
	 * Determine whether or not to accept the offer made.
	 * 
	 * @param bid
	 * @return true for acceptable, false for rejected.
	 *
	 */
	public boolean judgeOffer(double oppUtility, double myUtility, double time) throws Exception{
		double paccept = Paccept(oppUtility, time);
		
		if (paccept >= 0.75)
			return true;
		
		return false;
	}
	
	/**
	 * Create the new bid to be offered.
	 * 
	 * @return new bid
	 * 
	 */
	public Bid createBid() throws Exception{
		HashMap<Integer, Value> values = new HashMap<>();
		List<Issue> issues = utilitySpace.getDomain().getIssues();
		Random randomGen = new Random();
		Bid newBid = null;
		
		while (getUtility(newBid) < MIN_BID_UTIL){
			for (Issue lIssue : issues){
				switch (lIssue.getType()){
				
				case DISCRETE:
					IssueDiscrete lIssueDisc = (IssueDiscrete) lIssue;
					int optionIndDisc = randomGen.nextInt(lIssueDisc.getNumberOfValues());
					values.put(lIssue.getNumber(), lIssueDisc.getValue(optionIndDisc));
					break;
				
				case REAL:
					IssueReal lIssueReal = (IssueReal) lIssue;
					int optionIndReal = randomGen.nextInt(lIssueReal.getNumberOfDiscretizationSteps() - 1);
					values.put((lIssueReal.getNumber()),
								new ValueReal(lIssueReal.getLowerBound()
								+ (lIssueReal.getUpperBound() - lIssueReal.getLowerBound()) * (double) (optionIndReal)
								/ (double) (lIssueReal.getNumberOfDiscretizationSteps())));
					break;
				
				case INTEGER:
					IssueInteger lIssueInt = (IssueInteger) lIssue;
					int optionIndInt = lIssueInt.getLowerBound() + randomGen.nextInt(lIssueInt.getUpperBound()
								- lIssueInt.getLowerBound());
					values.put(lIssueInt.getNumber(), new ValueInteger(optionIndInt));
					break;
				
				default:
					throw new Exception("Issue Type: " + lIssue.getType() + "\nCurrently not supported by Agent76.");
				}
			}
			newBid = new Bid(utilitySpace.getDomain(), values);
		}
		return newBid;
	}
	
	/**
	 * Code taken from ExampleAgent.java
	 * 
	 * This function determines the accept probability for an offer. At t=0 it
	 * will prefer high-utility offers. As t gets closer to 1, it will accept
	 * lower utility offers with increasing probability. it will never accept
	 * offers with utility 0.
	 * 
	 * @param u
	 *            is the utility
	 * @param t
	 *            is the time as fraction of the total available time (t=0 at
	 *            start, and t=1 at end time)
	 * @return the probability of an accept at time t
	 * @throws Exception
	 *             if you use wrong values for u or t.
	 * 
	 */
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
	
	/**
	 * Get the description of the agent.
	 * 
	 * @return description of the agent. 
	 * 
	 */
	@Override
	public String getDescription() {
		return "Agent76";
	}
}