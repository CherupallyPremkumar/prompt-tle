package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.AcceptAnswerPayload;

public class AcceptAnswerAction extends AbstractSTMTransitionAction<Prompt, AcceptAnswerPayload> {
    @Override
    public void transitionTo(Prompt prompt, AcceptAnswerPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.acceptedAnswerId = payload.answerId;
        prompt.answers.forEach(a -> {
            if (a.id.equals(payload.answerId))
                a.isAccepted = true;
            else
                a.isAccepted = false;
        });
        System.out.println("Answer accepted for prompt: " + prompt.title);
    }
}
