package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.AddBountyPayload;

public class AddBountyAction extends AbstractSTMTransitionAction<Prompt, AddBountyPayload> {
    @Override
    public void transitionTo(Prompt prompt, AddBountyPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        System.out.println("Bounty added to prompt: " + prompt.title + " amount: " + payload.amount);
    }
}
