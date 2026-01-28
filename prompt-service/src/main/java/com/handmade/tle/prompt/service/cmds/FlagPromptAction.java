package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.FlagPromptPayload;

public class FlagPromptAction extends AbstractSTMTransitionAction<Prompt, FlagPromptPayload> {
    @Override
    public void transitionTo(Prompt prompt, FlagPromptPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        System.out.println("Flag recorded for prompt: " + prompt.title + " Reason: " + payload.reason);
    }
}
