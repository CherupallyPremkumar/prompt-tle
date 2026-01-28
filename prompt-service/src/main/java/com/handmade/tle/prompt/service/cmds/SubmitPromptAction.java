package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.SubmitPromptPayload;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class SubmitPromptAction extends AbstractSTMTransitionAction<Prompt, SubmitPromptPayload> {
    @Override
    public void transitionTo(Prompt prompt, SubmitPromptPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            prompt.userId = authentication.getName();
        } else {
            prompt.userId = payload.userId;
        }
        System.out.println("Prompt submitted by " + prompt.userId + ": " + prompt.title);
    }
}
