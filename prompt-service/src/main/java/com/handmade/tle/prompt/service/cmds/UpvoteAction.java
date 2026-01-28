package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.UpvotePayload;

public class UpvoteAction extends AbstractSTMTransitionAction<Prompt, UpvotePayload> {
    @Override
    public void transitionTo(Prompt prompt, UpvotePayload payload, 
                             State startState, String eventId, State endState, 
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.score++;
        System.out.println("Upvoted: " + prompt.title);
    }
}
