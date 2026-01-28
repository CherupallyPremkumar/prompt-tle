package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.model.Answer;
import com.handmade.tle.shared.dto.AddAnswerPayload;

public class AddAnswerAction extends AbstractSTMTransitionAction<Prompt, AddAnswerPayload> {
    @Override
    public void transitionTo(Prompt prompt, AddAnswerPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        Answer answer = new Answer();
        answer.promptId = prompt.id;
        answer.userId = payload.userId;
        answer.authorUsername = payload.authorUsername;
        answer.body = payload.body;
        prompt.answers.add(answer);
        prompt.answerCount++;
        System.out.println("Answer added to prompt: " + prompt.title);
    }
}
