package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.AddFavoritePayload;

public class AddFavoriteAction extends AbstractSTMTransitionAction<Prompt, AddFavoritePayload> {
    @Override
    public void transitionTo(Prompt prompt, AddFavoritePayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        prompt.favoriteCount++;
        System.out.println("Prompt favorited: " + prompt.title);
    }
}
