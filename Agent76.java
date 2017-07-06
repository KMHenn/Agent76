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
 * 
 * @author Kaitlyn
 * @version 7.6.2017
 *
 */
public class Agent76 extends AbstractNegotiationParty{
	private Bid lastPartnerBid; // Track the last bid made by the opponent.
	static double MIN_BID_UTILITY; // Hold the minimum bid utility.
	private static double alpha = .30769; // Arbitrary alpha value used for calculating threshold.
	
	/**
	 * Called at the beginning of negotiations.
	 * Initiates all values needed for the negotiation.
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
		Action action = null; // To hold the chosen action.
		double time = timeline.getTime(); // Get the current normalized time of the negotiation.
		double threshold = getThreshold(time); // Calculate the threshold based on time.
		
		try{
			if ((validActs.size() == 2) && (validActs.contains(Offer.class))) // Check if an initial offer needs to be made.  Size 2 because initial valid options are Offer and EndNegotiations.
				action = new Offer(getPartyId(), generateBid(threshold));
		
			else if (validActs.contains(Accept.class)){ // Decide how to behave based on previous bids.
				double currentOppUtility = getUtilitySpace().getUtility(lastPartnerBid);
			
				if (isFair(currentOppUtility, (1 - currentOppUtility))) // Check if the bid is fair for Agent76.
					action = new Accept(getPartyId(), lastPartnerBid);
			
				else if (threshold >= MIN_BID_UTILITY){ // Bids are still within what we've determined to be a valid range.
					action = new Offer(getPartyId(), generateBid(threshold));
				}
			}
				
			else // No point in bidding further
				action = new EndNegotiation(getPartyId());
		}
		catch (Exception e){
			System.out.println("Error encountered.\n" + e + "\nEnding negotiations");
			action = new EndNegotiation(getPartyId());
		}
		
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
		
		if(action instanceof Offer){ // Check if the opponent just made an offer.
			lastPartnerBid = ((Offer) action).getBid();
		}
	}
	
	/**
	 * Create a bid, making sure it is reasonable for us.
	 * 
	 * @param threshold
	 * @return
	 * @throws Exception 
	 */
	private Bid generateBid(double threshold) throws Exception{
		Bid bid = utilitySpace.getMinUtilityBid(); // Start the bid using the minimum utility.
		
		while(!isFair(getUtility(bid), (1 - getUtility(bid)))) // Check if this bid is fair for Agent76.
			bid = generateRandomBid();

		System.out.println("-----\nMy Util: " + getUtility(bid) + "\n-----"); // For debug.
		return bid;
	}
	
	/**
	 * Determine the current threshold based on the current time relative to
	 * the negotiation round.
	 * 
	 * @param time
	 * @return threshold, based on current time.
	 */
	public double getThreshold(double time){
		return (1.0 - Math.pow(time,  (1/alpha)));
	}
	
	/**
	 * Check whether or not the offer is fair for me.
	 * 
	 * @param myUtil
	 * @param oppUtil
	 * @return true if fair, false otherwise
	 */
	public boolean isFair(double myUtil, double oppUtil){
		double comparison = myUtil / oppUtil; // Ratio of Agent76's utility to the opponents.
		
		if (comparison >= 1) // Utility split is at least even, if not better for Agent76.
			return true;
		
		return false;
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
