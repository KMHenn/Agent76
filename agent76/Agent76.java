package agent76;

import java.util.List;

import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.EndNegotiation;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;

/**
 * 
 * First attempt at an automated negotiation agent.
 * @author Kaitlyn
 *
 */
public class Agent76 extends AbstractNegotiationParty{
	private Bid lastPartnerBid;
	static double MIN_BID_UTILITY;
	private static double alpha = .3;
	
	/**
	 * Called at the beginning of negotiations.
	 * 
	 * @param info
	 */
	@Override
	public void init(NegotiationInfo info){
		super.init(info);
		MIN_BID_UTILITY = utilitySpace.getReservationValueUndiscounted();
		lastPartnerBid = null;
	}
	
	/**
	 * How the agent decides what action to take.
	 * 
	 * @param validActs
	 */
	@Override
	public Action chooseAction(List<Class<? extends Action>> validActs) {
		Action action = null;
		double time = timeline.getTime();
		double threshold = getThreshold(time);
		Bid maxBid = new Bid(utilitySpace.getDomain());
		
		if (lastPartnerBid == null)
			action = new Offer(getPartyId(), maxBid);
		
		else if (validActs.contains(Accept.class)){
			double currentOppUtility = getUtilitySpace().getUtility(lastPartnerBid);
			
			if (threshold <= currentOppUtility) // The bid meets current minimum requirements.
				action = new Accept(getPartyId(), lastPartnerBid);
			
			else if (threshold >= MIN_BID_UTILITY) // Bids are still within what we've determined to be a valid range.
				action = new Offer(getPartyId(), generateBid(threshold));
		}
				
		else // No point in bidding further
			action = new EndNegotiation(getPartyId());
		
		
		return action;
	}
	
	/**
	 * Receive the opponent's action as a message.
	 * 
	 * @param sender
	 * @param action
	 */
	@Override
	public void receiveMessage(AgentID sender, Action action){
		super.receiveMessage(sender, action);
		
		if(action instanceof Offer)
			lastPartnerBid = ((Offer) action).getBid();
	}
	
	/**
	 * Create a bid, making sure it is reasonable for us.
	 * 
	 * @param threshold
	 * @return
	 */
	private Bid generateBid(double threshold){
		Bid bid = null;
		
		while(getUtility(bid) < getUtility(lastPartnerBid))
			bid = generateRandomBid();
		
		return bid;
	}
	
	/**
	 * Determine the current threshold based on the current time relative to
	 * the negotiation round.
	 * 
	 * @param time
	 * @return
	 */
	public double getThreshold(double time){
		return (1.0 - Math.pow(time,  (1/alpha)));
	}


	/**
	 * Return the description of the agent.
	 * 
	 * @return name of the agent (Agent76)
	 */
	@Override
	public String getDescription() {
		return "Agent76";
	}
}