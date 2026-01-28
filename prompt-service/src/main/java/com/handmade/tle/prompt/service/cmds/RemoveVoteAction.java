package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.RemoveVotePayload;

public class RemoveVoteAction extends AbstractSTMTransitionAction<Prompt, RemoveVotePayload> {
    @Override
    public void transitionTo(Prompt prompt, RemoveVotePayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if ("UP".equalsIgnoreCase(payload.voteType))
            prompt.score--;
        else if ("DOWN".equalsIgnoreCase(payload.voteType))
            prompt.score++;
        System.out.println("Vote removed for prompt: " + prompt.title);
    }
}
