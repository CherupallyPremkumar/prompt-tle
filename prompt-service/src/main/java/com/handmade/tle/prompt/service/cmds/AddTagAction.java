package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.AddTagPayload;

public class AddTagAction extends AbstractSTMTransitionAction<Prompt, AddTagPayload> {
    @Override
    public void transitionTo(Prompt prompt, AddTagPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (!prompt.tags.contains(payload.tagName)) {
            prompt.tags.add(payload.tagName);
        }
    }
}
