package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.ImpressionPayload;

public class RecordImpressionAction extends AbstractSTMTransitionAction<Prompt, ImpressionPayload> {
    @Override
    public void transitionTo(Prompt prompt, ImpressionPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.viewCount++;
        System.out.println("Impression recorded for prompt: " + prompt.title);
    }
}
