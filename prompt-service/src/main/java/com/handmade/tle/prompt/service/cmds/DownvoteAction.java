package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.DownvotePayload;

public class DownvoteAction extends AbstractSTMTransitionAction<Prompt, DownvotePayload> {
    @Override
    public void transitionTo(Prompt prompt, DownvotePayload payload, 
                             State startState, String eventId, State endState, 
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.score--;
        System.out.println("Downvoted: " + prompt.title);
    }
}
