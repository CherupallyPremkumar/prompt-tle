package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.RemoveFavoritePayload;

public class RemoveFavoriteAction extends AbstractSTMTransitionAction<Prompt, RemoveFavoritePayload> {
    @Override
    public void transitionTo(Prompt prompt, RemoveFavoritePayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (prompt.favoriteCount > 0)
            prompt.favoriteCount--;
        System.out.println("Favorite removed for prompt: " + prompt.title);
    }
}
