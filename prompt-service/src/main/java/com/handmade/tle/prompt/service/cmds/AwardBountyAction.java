package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.AwardBountyPayload;

public class AwardBountyAction extends AbstractSTMTransitionAction<Prompt, AwardBountyPayload> {
    @Override
    public void transitionTo(Prompt prompt, AwardBountyPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        System.out.println("Bounty awarded on prompt: " + prompt.title + " to answer: " + payload.answerId);
    }
}
