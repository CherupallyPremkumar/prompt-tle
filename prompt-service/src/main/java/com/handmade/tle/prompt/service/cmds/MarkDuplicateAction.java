package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.MarkDuplicatePayload;

public class MarkDuplicateAction extends AbstractSTMTransitionAction<Prompt, MarkDuplicatePayload> {
    @Override
    public void transitionTo(Prompt prompt, MarkDuplicatePayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.duplicateOfPromptId = payload.duplicateOfPromptId;
        System.out.println("Prompt marked as duplicate: " + prompt.title);
    }
}
