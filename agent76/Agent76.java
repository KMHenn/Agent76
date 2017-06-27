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
		return null;
	}
	
	/**
	 * Receive all offers from the other parties.
	 * Used to predict the utility.
	 * 
	 * @param sender
	 * @param action
	 */
	@Override
	public void receiveMessage(AgentID sender, Action action){
		super.receiveMessage(sender, action);
		if (action instanceof Offer){
			lastReceivedBid = ((Offer) action).getBid();
		}
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
