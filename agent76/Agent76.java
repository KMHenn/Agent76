package agent76;

import java.util.List;

import negotiator.Agent;
import negotiator.AgentID;
import negotiator.Bid;
import negotiator.Deadline;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;
import negotiator.persistent.PersistentDataContainer;
import negotiator.timeline.Timeline;
import negotiator.utility.AbstractUtilitySpace;
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
	
	/**
	 * Return the action the agent chooses to make next.
	 * 
	 * @return
	 */
	@Override
	public Action chooseAction() {
		// TODO
		Action newAction = null;
		try{
			if (lastOpponentAction == null) // First turn of first round, must bid.
				//TODO make some kind of bid
			if (lastOpponentAction instanceof Offer){ // Action must be decided based on circumstances.
				Bid opponentBid = ((Offer) lastOpponentAction).getBid();
				double offeredOpponentUtil = getUtility(opponentBid);
				// get current time
				double time = timeline.getTime();
				//TODO make some kind of bid
				
				Bid newBid = ((Offer) newAction).getBid();
				double myOfferedUtil = getUtility(newBid);
				
				//accept under certain circumstances
				if (isAcceptable(offeredOpponentUtil, myOfferedUtil, time))
					newAction = new Accept(getAgentID(), newBid);
			}
		} catch (Exception e){
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
	 * Informs the agent that a new negotiation session has begun.
	 */
	@Override
	public void init(){
		super.init();
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
		double probableAcceptance = ((opponentOffer - (2*opponentOffer*time)
				+ 2*(time - 1 + Math.sqrt((time - 1)*(time - 1) + opponentOffer*((2*time) - 1))))
				/ ((2*time) - 1));
		if (probableAcceptance >= 0.75)
			return true;
		
		return false;
	}
	
	/**
	 * Return the name of the agent.
	 * 
	 * @return
	 */
	@Override
	public String getName(){
		return "Agent 76";
	}
}