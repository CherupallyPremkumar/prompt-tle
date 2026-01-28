package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.model.Comment;
import com.handmade.tle.shared.dto.AddCommentPayload;

public class AddCommentAction extends AbstractSTMTransitionAction<Prompt, AddCommentPayload> {

    @Override
    public void transitionTo(Prompt prompt, AddCommentPayload payload,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        Comment comment = new Comment();
        comment.author = payload.author;
        comment.content = payload.content;
        comment.promptId = prompt.id;

        prompt.comments.add(comment);
        prompt.commentCount++;

        System.out.println("Added comment to prompt: " + prompt.title + " by author: " + payload.author);
    }
}
