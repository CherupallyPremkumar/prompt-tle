package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.ReopenPromptPayload;

public class ReopenPromptAction extends AbstractSTMTransitionAction<Prompt, ReopenPromptPayload> {
    @Override
    public void transitionTo(Prompt prompt, ReopenPromptPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.closedAt = null;
        prompt.closeReason = null;
        prompt.closedByUserId = null;
        System.out.println("Prompt reopened: " + prompt.title);
    }
}
