package agent76;

import java.util.List;

import negotiator.AgentID;
import negotiator.Bid;
import negotiator.Deadline;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;
import negotiator.persistent.PersistentDataContainer;
import negotiator.utility.AbstractUtilitySpace;

/**
 * 
 * First attempt at an automated negotiation agent.
 * @author Kaitlyn
 *
 */
public class Agent76 extends AbstractNegotiationParty{
	
	private Bid lastReceivedBid = null;
	private Bid lastOfferedBid = null;
	
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
		// TODO 
		if ((lastReceivedBid == null) || (!validActions.contains(Accept.class))){ //Agent76 must bid.
			Bid newBid = createBid();
			return new Offer(getPartyId(), newBid); //TODO need to generate an offer
		}
		else{
			//return new Accept(getPartyId(), lastReceivedBid);
			boolean bidQuality = judgeOffer(lastReceivedBid);
			if (bidQuality == true)
				return new Accept(getPartyId(), lastReceivedBid);
			else
				return new Offer(getPartyId(), lastReceivedBid); //TODO not really using lastReceivedBid here.
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
	public boolean judgeOffer(Bid bid){
		return false;
	}
	
	/**
	 * Create the new bid to be offered.
	 * 
	 * @return new bid
	 * 
	 */
	public Bid createBid(){
		//TODO
		double lastReceiveUtil = getUtility(lastReceivedBid);
		double lastOfferedUtil = getUtility(lastOfferedBid);
		Math.abs(lastReceiveUtil - lastOfferedUtil);
		return null;
	}
	
	/**
	 * Get the description of the agent.
	 * 
	 * @return description of the agent. 
	 * 
	 */
	@Override
	public String getDescription() {
		return "Party Agent76";
	}
}
