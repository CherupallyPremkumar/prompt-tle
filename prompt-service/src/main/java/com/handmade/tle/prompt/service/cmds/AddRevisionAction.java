package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.model.PromptRevision;
import com.handmade.tle.shared.dto.AddRevisionPayload;

public class AddRevisionAction extends AbstractSTMTransitionAction<Prompt, AddRevisionPayload> {
    @Override
    public void transitionTo(Prompt prompt, AddRevisionPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        PromptRevision revision = new PromptRevision();
        revision.promptId = prompt.id;
        revision.userId = payload.userId;
        revision.body = payload.newContent;
        revision.title = prompt.title;
        revision.tags = prompt.tags;
        revision.revisionNumber = prompt.revisionNumber++;
        revision.changeComment = payload.changeComment;
        prompt.revisions.add(revision);
        System.out.println("Revision added for prompt: " + prompt.title);
    }
}
