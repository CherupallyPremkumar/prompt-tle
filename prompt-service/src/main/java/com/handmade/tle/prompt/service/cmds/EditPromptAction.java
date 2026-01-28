package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.EditPromptPayload;

public class EditPromptAction extends AbstractSTMTransitionAction<Prompt, EditPromptPayload> {
    @Override
    public void transitionTo(Prompt prompt, EditPromptPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload.newTitle != null)
            prompt.title = payload.newTitle;
        if (payload.newBody != null)
            prompt.body = payload.newBody;
        if (payload.newTags != null)
            prompt.tags = payload.newTags;
        prompt.revisionNumber++;
        System.out.println("Prompt edited: " + prompt.title);
    }
}
