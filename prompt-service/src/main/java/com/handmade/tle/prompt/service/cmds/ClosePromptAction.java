package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.ClosePromptPayload;
import java.util.Date;

public class ClosePromptAction extends AbstractSTMTransitionAction<Prompt, ClosePromptPayload> {
    @Override
    public void transitionTo(Prompt prompt, ClosePromptPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.closeReason = payload.reason;
        prompt.closedAt = new Date();
        prompt.closedByUserId = payload.userId;
        prompt.duplicateOfPromptId = payload.duplicateOfPromptId;
        System.out.println("Prompt closed: " + prompt.title + " for reason: " + payload.reason);
    }
}
