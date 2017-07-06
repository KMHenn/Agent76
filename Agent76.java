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
	private Bid lastPartnerBid;
	static double MIN_BID_UTILITY;
	private static double alpha = .30769;
	private static double utilDec = .025;
	
	OpponentTracker opponent;
	
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
		opponent = new OpponentTracker();
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
		
		try{
			if ((validActs.size() == 2) && (validActs.contains(Offer.class)))
				action = new Offer(getPartyId(), generateBid(threshold));
		
			else if (validActs.contains(Accept.class)){
				double currentOppUtility = getUtilitySpace().getUtility(lastPartnerBid);
			
				if (isFair(currentOppUtility, (1 - currentOppUtility)))
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
	 * Use Jain's fairness index to decide whether or not the bid
	 * is fair for Agent76.
	 * 
	 * @return true if fair, false otherwise.
	 */

	
	/**
	 * Receive the opponent's action as a message.
	 * 
	 * @param sender
	 * @param action
	 */
	@Override
	public void receiveMessage(AgentID sender, Action action){
		super.receiveMessage(sender, action);
		
		if(action instanceof Offer){
			lastPartnerBid = ((Offer) action).getBid(); //TODO problem here?
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
		//TODO want to prevent just handing the opponent the win with a bad bid.
		Bid bid = utilitySpace.getMinUtilityBid();
		double desiredUtility = getUtility(utilitySpace.getMaxUtilityBid());
		
		while(!isFair(getUtility(bid), (1 - getUtility(bid)))){
			bid = generateRandomBid();
			desiredUtility -= utilDec;
		}

		utilDec += .025;
		System.out.println("-----\nMy Util: " + getUtility(bid) + "\n-----");
		
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
	 * Check whether or not the offer is fair for me.
	 * 
	 * @param myUtil
	 * @param oppUtil
	 * @return true if fair, false otherwise
	 */
	public boolean isFair(double myUtil, double oppUtil){
		double comparison = myUtil / oppUtil;
		
		if (comparison >= 1)
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