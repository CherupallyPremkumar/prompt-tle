package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.RemoveTagPayload;

public class RemoveTagAction extends AbstractSTMTransitionAction<Prompt, RemoveTagPayload> {
    @Override
    public void transitionTo(Prompt prompt, RemoveTagPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.tags.remove(payload.tagName);
    }
}
