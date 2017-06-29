package agent76;

import java.util.ArrayList;
import negotiator.Agent;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.timeline.Timeline;
import negotiator.utility.UtilitySpace;

/**
 * 
 * First attempt at an automated negotiation agent.
 * @author Kaitlyn
 *
 */
public class Agent76 extends Agent{
	
	private UtilitySpace utilitySpace;
	private Timeline timeline;
	private Action lastOpponentAction = null;
	private ArrayList <Offer> previousOpponentOffers;
	private static double MINIMUM_UTILITY = 0.00;
	
	
	/**
	 * Informs the agent that a new negotiation session has begun.
	 */
	@Override
	public void init(){
		super.init();
		//TODO
		MINIMUM_UTILITY = utilitySpace.getReservationValue();
	}
	
	/**
	 * Return the action the agent chooses to make next.
	 * 
	 * @return
	 */
	@Override
	public Action chooseAction() {
		// TODO
		Action newAction = null; //TODO always null as of now
		try{
			if (lastOpponentAction == null) // First turn of first round, must bid.
				//TODO make some kind of bid
				return null;
			
			if (lastOpponentAction instanceof Offer){ // Action must be decided based on circumstances.
				previousOpponentOffers.add((Offer) lastOpponentAction);
				Bid opponentBid = ((Offer) lastOpponentAction).getBid();
				double offeredOpponentUtil = getUtility(opponentBid);
				
				// get current time
				double time = timeline.getTime();
				
				//TODO make some kind of bid
				
				Bid newBid = ((Offer) newAction).getBid();
				double myOfferedUtil = getUtility(newBid);
				
				if (isAcceptable(offeredOpponentUtil, myOfferedUtil, time)) // Check if offer should be accepted.
					newAction = new Accept(getAgentID(), newBid);
			}
		}
		catch (Exception e){
			e.printStackTrace();
			newAction = new Accept(getAgentID(), null); //TODO probably shouldn't be null.
		}
		return newAction;
	}
	
	/**
	 * Inform the agent which action the opponent chose. 
	 * 
	 * @param opponentAction
	 */
	@Override
	public void ReceiveMessage(Action opponentAction){
		lastOpponentAction = opponentAction;
		//TODO
	}
	
	/**
	 * Convenience method, used to get the utility of a bid by
	 * taking into account the discount factor.
	 * 
	 * @param bid
	 * @return the utility value of the bid.
	 */
	public double getUtility(Bid bid){
		//TODO
		return 0.00;
	}
	
	/**
	 * Check whether or not the offer is acceptable by Agent76's standards.
	 * 
	 * @param opponentOffer
	 * @param myOffer
	 * @param time
	 * @return whether or not the offer is okay.
	 */
	public boolean isAcceptable(double opponentOffer, double myOffer, double time){
		//TODO
		double paccept = ((opponentOffer - (2*opponentOffer*time)
				+ 2*(time - 1 + Math.sqrt((time - 1)*(time - 1) + opponentOffer*((2*time) - 1))))
				/ ((2*time) - 1));
		if (paccept >= 0.75)
			return true;
		
		return false;
	}
	
	/**
	 * Calculate the average utility of the opponent's past bids.
	 * 
	 * @return average opponent utility.
	 */
	private double averageOpponentUtility(){
		double numerator = 0;
		for (int i = 0; i < previousOpponentOffers.size(); i ++)
			numerator += getUtility(previousOpponentOffers.get(i).getBid()); 
		
		return (numerator / previousOpponentOffers.size());
	}
	
	/**
	 * Return the name of the agent.
	 * 
	 * @return name of the agent.
	 */
	@Override
	public String getName(){
		return "Agent 76";
	}
}